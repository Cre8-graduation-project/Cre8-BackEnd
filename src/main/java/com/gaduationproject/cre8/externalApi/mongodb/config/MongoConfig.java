package com.gaduationproject.cre8.externalApi.mongodb.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Value("${mongo.host}")
    private String host;

    @Value("${mongo.port}")
    private String port;

    @Value("${mongo.id}")
    private String id;

    @Value("${mongo.pw}")
    private String password;



    @Bean
    public MongoClient mongoClient() {
        // MongoDB 연결 정보 설정
        MongoCredential credential = MongoCredential.createCredential(id, "admin", password.toCharArray());
        ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port));


        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(serverAddress)))
                .credential(credential)
                .applyToSocketSettings(builder ->
                        builder.connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                                .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)

                )
                .applyToConnectionPoolSettings(builder ->
                        builder.maxSize(100)
                                .maxConnectionIdleTime(1, TimeUnit.SECONDS))
                .retryWrites(true)

                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        return mongoClient;
    }



}

