package org.yordanoffnikolay.betterminer.config;

import org.aspectj.weaver.loadtime.definition.LightXMLParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public LightXMLParser lightXMLParser() {
        return new LightXMLParser();
    }
}
