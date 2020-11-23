package com.themuler.appender;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;

public class KinesisAdapter {
    private final AmazonKinesis kinesisClient;

    public KinesisAdapter(String accessKey, String secretKey) {
        this.kinesisClient = AmazonKinesisClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    public AmazonKinesis getKinesisClient() {
        return kinesisClient;
    }
}
