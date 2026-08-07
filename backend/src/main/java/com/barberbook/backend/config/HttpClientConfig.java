package com.barberbook.backend.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientConfig {

    @Bean
    RestClient brasilApiRestClient(
        RestClient.Builder builder,
        @Value("${app.brasil-api.base-url}") String baseUrl
    ) {
        var factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(5));

        return builder
            .baseUrl(baseUrl)
            .requestFactory(factory)
            .build();
    }
}
