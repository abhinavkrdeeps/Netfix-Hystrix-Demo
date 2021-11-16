package com.wissen.training.microservices.app;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import model.CatalogItem;
import model.Movie;
import model.Rating;
import model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//			Movie movie = wBuilder.build()
//					.get()  // HTTP Method
//					.uri("http://localhost:8082/movie/"+rating.getMovieId()) // Url to ca;;
//					.retrieve() // get Response
//					.bodyToMono(Movie.class) // Convert to Mono Object (Its a Kind of empty container , Whenever we get it
//					 // fill it with the movie object)
//					.block();  // Wait until complete.


@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;
	
	// Reactive (Asynchronous Way Of Doing Things)
	@Autowired
	private WebClient.Builder wBuilder;

	@Autowired
	private CatalogInfo catalogInfo;

	@Autowired
	private UserRatingsInfo userRatingsInfo;
	
	@RequestMapping(value = "/{userId}")
	@HystrixCommand(fallbackMethod = "getFallback", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000") ,// how log does it take to fail a request
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), // No of request to make a decision for breaking a circuit,
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "50"), // Percentage of failure after which circuit breaks
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "5000") // Time After Which resend request.
	})
	public List<CatalogItem> getAllCatalogs(@PathVariable("userId") String userId){
		// get all rated movieIds
		//ratings-data-service -> localhost:8083 (Discovery)
		UserRating ratings = userRatingsInfo.getUserRating(userId);
		// For each movie Id call movie info service and get details
		//movie-info-service -> localhost:8082
		List<CatalogItem> catalogItems = new ArrayList<>();
		for(Rating rating: ratings.getRating()) {
			catalogInfo.getCatalogItems(catalogItems, rating);
		}
		return catalogItems;
		
	}

	public List<CatalogItem> getFallback(String userId){
		return  Arrays.asList(
		new CatalogItem()
		);
	}


}

