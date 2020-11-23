# Kinesis-Log4j2-Appender
Custom Implementation of Kinesis Log4j2 Appender 

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Dependency Snippet
Add this snippet to pom file of any Java or Mule Application
```
<dependency>
  <groupId>com.themuler</groupId>
  <artifactId>kinesis-log4j2-appender</artifactId>
  <version>1.0.0</version>
</dependency>
```
## Configuration
Add the following xml configuration to `log4j2.xml` after adding above dependency
---
KinesisAsyncAppender Configuration
```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="info" packages="com.themuler.appender">
    <Appenders>
        <KinesisAsync name="test-appender">
            <property name="aws_access_key" value="AWS_ACCESS_KEY"/>
            <property name="aws_secret_key" value="AWS_SECRET_KEY"/>
            <property name="kinesis_stream" value="AWS_KINESIS_STREAM"/>
        </KinesisAsync>
        
        <Console name="STDOUT" target="SYSTEM_OUT">
            <PatternLayout pattern="%m%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <AsyncRoot level="info">
            <AppenderRef ref="test-appender"/>
        </AsyncRoot>
    </Loggers>
</Configuration>
```
---
Kinesis Appender Configuration
```
<Configuration status="info" packages="com.themuler.appender">
    <Appenders>
        <KinesisAppender name="test-appender">
            <property name="aws_access_key" value="AWS_ACCESS_KEY"/>
            <property name="aws_secret_key" value="AWS_SECRET_KEY"/>
            <property name="kinesis_stream" value="AWS_KINESIS_STREAM"/>
        </KinesisAppender>
        
        <Console name="STDOUT" target="SYSTEM_OUT">
            <PatternLayout pattern="%m%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <AsyncRoot level="info">
            <AppenderRef ref="test-appender"/>
        </AsyncRoot>
    </Loggers>
</Configuration>
```
