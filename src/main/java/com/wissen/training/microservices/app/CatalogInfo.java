package com.wissen.training.microservices.app;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import model.CatalogItem;
import model.Movie;
import model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class CatalogInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItems", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000") ,// how long does it take to fail a request
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), // No of request to make a decision for breaking a circuit,
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "50"), // Percentage of failure after which circuit breaks
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "5000") // Time After Which resend request.
    })
    public void getCatalogItems(List<CatalogItem> catalogItems, Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movie/"+ rating.getMovieId(), Movie.class);


        CatalogItem catalogItem = new CatalogItem(movie.getMovieName(),"desc", rating.getRating());
        catalogItems.add(catalogItem);
    }

    public List<CatalogItem> getFallbackCatalogItems(){
        return Arrays.asList(
                new CatalogItem("default Movie","default desc",3)
        );

    }
}
