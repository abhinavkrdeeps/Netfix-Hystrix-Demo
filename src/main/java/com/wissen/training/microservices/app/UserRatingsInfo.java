package com.wissen.training.microservices.app;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import model.Rating;
import model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class UserRatingsInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getUserRatingsFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000") ,// how log does it take to fail a request
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), // No of request to make a decision for breaking a circuit,
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "50"), // Percentage of failure after which circuit breaks
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "5000") // Time After Which resend request.
    })
    public UserRating getUserRating(String userId) {
        return restTemplate.
                getForObject("http://ratings-data-service/ratingsData/users/"+ userId, UserRating.class);
    }

    public UserRating getUserRatingsFallback(){
        return new UserRating(Arrays.asList(
                new Rating("defualt MovieID",3)
        ));
    }



}
