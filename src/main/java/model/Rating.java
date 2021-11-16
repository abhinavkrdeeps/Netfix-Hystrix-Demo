package model;

public class Rating {

	private String movieId;
	private Integer rating;
	
	
	
	public Rating() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Rating(String movieId, Integer rating) {
		super();
		this.movieId = movieId;
		this.rating = rating;
	}
	public String getMovieId() {
		return movieId;
	}
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	
	
}
