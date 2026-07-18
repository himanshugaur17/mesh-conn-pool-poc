package com.example.meshconnpoolpoc.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MainConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // connection would be closed if idle for 30s
        connectionManager.closeIdle(TimeValue.ofSeconds(30));
        connectionManager.setMaxTotal(50);// at max total 50 connections can be alive at a time

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                httpClient);
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(5000); // TIME TAKEN FOR TCP CONNECTION TO
                                                                                  // ESTABLISH
        httpComponentsClientHttpRequestFactory.setReadTimeout(10000); // MAX GAP BETWEEN 2 DATA PACKETS
        return new RestTemplate(httpComponentsClientHttpRequestFactory);
    }
}
