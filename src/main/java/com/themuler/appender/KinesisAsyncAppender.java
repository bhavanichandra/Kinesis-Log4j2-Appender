package com.themuler.appender;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

@Plugin(name = "KinesisAsync", category = "Core", elementType = "appender", printObject = true)
public class KinesisAsyncAppender extends AbstractAppender {

    static KinesisAsyncAdapter kinesisAsyncAdapter;
    static String kinesisStreamName;

    public KinesisAsyncAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @PluginFactory
    public static KinesisAsyncAppender createAppender(@PluginAttribute("name") String name,
                                                 @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                 @PluginElement("Filter") final Filter filter,
                                                 @PluginAttribute("otherAttribute") String otherAttribute,
                                                 @PluginElement("properties") Property[] properties) {
        if (name == null) {
            LOGGER.error("Please enter a name for the kinesis appender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        LOGGER.info("Properties: {}", Arrays.toString(properties));
        String accessKey = "";
        String secretKey = "";
        for (Property prop : properties) {
            if (prop.getName().equals("aws_access_key")) {
                accessKey = prop.getValue();
            }
            if (prop.getName().equals("aws_secret_key")) {
                secretKey = prop.getValue();
            }
            if (prop.getName().equals("kinesis_stream")) {
                kinesisStreamName = prop.getValue();
            }
        }
        kinesisAsyncAdapter = new KinesisAsyncAdapter(accessKey, secretKey);
        return new KinesisAsyncAppender(name, filter, layout, true, properties);
    }

    @Override
    public void append(LogEvent event) {
        Layout<? extends Serializable> layout = getLayout();
        byte[] bytes = layout.toByteArray(event);
        AmazonKinesis kinesisClient = kinesisAsyncAdapter.getKinesisAsync();
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setData(ByteBuffer.wrap(bytes));
        putRecordRequest.setPartitionKey(UUID.randomUUID().toString());
        putRecordRequest.setStreamName(kinesisStreamName);
        LOGGER.info("Data being sent to AWS Kinesis Stream (Async) {}", new String(bytes, StandardCharsets.UTF_8));
        PutRecordResult putRecordResult = kinesisClient.putRecord(putRecordRequest);
        LOGGER.info("Data Sent to AWS Kinesis Stream (Async). AWS Result {}", putRecordResult);
    }
}
