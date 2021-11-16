package com.wissen.training.microservices.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class MovieCatalogueServiceApp {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        // Applying Timeouts
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setConnectTimeout(3000);
        return new RestTemplate(httpComponentsClientHttpRequestFactory);
    }

    @Bean
    public WebClient.Builder getBuilder(){
        return WebClient.builder();
    }

    @Bean
    public CatalogInfo getCatalogInfo(){
        return new CatalogInfo();
    }

    @Bean
    public UserRatingsInfo getUserRatingsInfo(){
        return new UserRatingsInfo();
    }

    public static void main(String[] args) {

        SpringApplication.run(MovieCatalogueServiceApp.class,args);
    }
}
