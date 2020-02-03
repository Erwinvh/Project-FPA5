package PlannerData;

import Enumerators.Genres;

import java.time.LocalTime;
import java.util.ArrayList;

public class Show {

	private int expectedPopularity;
	private LocalTime beginTime;
	private LocalTime endTime;
	private String name;
	private String description;
	private Stage stage;
	private ArrayList<Genres> genre;
	private ArrayList<Artist> artists;

	public Show(LocalTime beginTime, LocalTime endTime, ArrayList<Artist> artists, String name, Stage stage, String description, ArrayList<Genres> genre, int expectedPopularity) {
		this.expectedPopularity = expectedPopularity;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.name = name;
		this.description = description;
		this.stage = stage;
		this.genre = genre;
		this.artists = artists;
	}

	public Show(LocalTime beginTime, LocalTime endTime, Stage stage, ArrayList<Artist> artists, int expectedPopularity){
		this.expectedPopularity = expectedPopularity;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.stage = stage;
		this.artists = artists;
		this.name = this.artists.get(0).getName();
		this.description = "";

		ArrayList<Genres> genres = new ArrayList<>();
		for(Artist artist : this.artists){
			if(!genres.contains(artist.getGenre()))
			genres.add(artist.getGenre());
		}
	}

	public int getExpectedPopularity() {
		return expectedPopularity;
	}

	public LocalTime getBeginTime() {
		return beginTime;
	}

	public void setGenre(ArrayList<Genres> genre) {
		this.genre = genre;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public Stage getStage() {
		return stage;
	}

	public ArrayList<Genres> getGenre() {
		return genre;
	}

	public ArrayList<Artist> getArtists() {
		return artists;
	}

	/**
	 * Calculates the duration of the show
	 * @return duration, a Local time of the duration of the show
	 */

	public LocalTime getDuration() {
		LocalTime duration = endTime.minusMinutes(beginTime.getMinute());
		duration = endTime.minusHours(beginTime.getHour());
		return duration;
	}

}