package com.example.concertsystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ConfigurationProperties
@Component
public class FaunaClient {

    private final Map<String, String> faunaConnections = new HashMap<>();
    private final Map<String, String> faunaSecrets = new HashMap<>();

    public com.faunadb.client.FaunaClient getFaunaClient(String region) throws MalformedURLException {

        String faunaUrl = faunaConnections.get(region);
        String faunaSecret = faunaSecrets.get(region);

        System.out.println(faunaUrl);
        System.out.println(faunaSecret);

        log.info("Creating Fauna Client for Region :{} with URL :{}",region, faunaUrl);

        return com.faunadb.client.FaunaClient.builder()
                .withEndpoint(faunaUrl)
                .withSecret(faunaSecret)
                .build();
    }

    public Map<String , String> getFaunaConnections() {
        return faunaConnections;
    }

    public Map<String , String> getFaunaSecrets() {
        return faunaSecrets;
    }
}
