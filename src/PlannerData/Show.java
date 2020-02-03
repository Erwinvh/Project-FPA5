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

	public Show(int expectedPopularity, LocalTime beginTime, LocalTime endTime, Stage stage, ArrayList<Artist> artists){

	}

	public int getExpectedPopularity() {
		return expectedPopularity;
	}

	public LocalTime getBeginTime() {
		return beginTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public String getName() {
		return name;
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

	public LocalTime getDuration() {
		LocalTime duration = endTime.minusMinutes(beginTime.getMinute());
		duration = endTime.minusHours(beginTime.getHour());
		return duration;
	}

}