package com.themuler.appender;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesisAsync;
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClientBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KinesisAsyncAdapter {

    private final AmazonKinesisAsync kinesisAsync;
    private int corePoolSize = 100;
    private int maxPoolSize = 300;
    private long ttl = 86000;
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    public KinesisAsyncAdapter(String accessKey, String secretKey) {
        this.kinesisAsync = AmazonKinesisAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withExecutorFactory(() -> new ThreadPoolExecutor(corePoolSize, maxPoolSize, ttl, timeUnit, new LinkedBlockingQueue<Runnable>())).build();
    }

    public AmazonKinesisAsync getKinesisAsync() {
        return kinesisAsync;
    }
}
