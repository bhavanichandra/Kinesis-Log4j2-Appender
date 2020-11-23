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

@Plugin(name = "KinesisAppender", category = "Core", elementType = "appender", printObject = true)
public class KinesisAppender extends AbstractAppender {

    static KinesisAdapter kinesisAdapter;
    static String kinesisStreamName;

    public KinesisAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @PluginFactory
    public static KinesisAppender createAppender(@PluginAttribute("name") String name,
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

        kinesisAdapter = new KinesisAdapter(accessKey, secretKey);
        return new KinesisAppender(name, filter, layout, true, properties);
    }

    @Override
    public void append(LogEvent event) {

        Layout<? extends Serializable> layout = getLayout();
        byte[] bytes = layout.toByteArray(event);
        AmazonKinesis kinesisClient = kinesisAdapter.getKinesisClient();
        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setData(ByteBuffer.wrap(bytes));
        putRecordRequest.setPartitionKey(UUID.randomUUID().toString());
        putRecordRequest.setStreamName(kinesisStreamName);
        LOGGER.info("Data being sent to AWS Kinesis Stream {}", new String(bytes, StandardCharsets.UTF_8));
        PutRecordResult putRecordResult = kinesisClient.putRecord(putRecordRequest);
        LOGGER.info("Data Sent to AWS Kinesis Stream. AWS Result {}", putRecordResult);

    }
}
