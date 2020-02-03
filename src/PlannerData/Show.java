package PlannerData;

import Enumerators.Genres;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Show {

	private int expectedPopularity;
	private LocalDateTime beginTime;
	private LocalDateTime endTime;
	private String name;
	private String description;
	private Stage stage;
	private ArrayList<Genres> genre;
	private ArrayList<Artist> artists;

	public Show(int expectedPopularity, LocalDateTime beginTime, LocalDateTime endTime, String name, String description, Stage stage, ArrayList<Genres> genre, ArrayList<Artist> artists) {
		this.expectedPopularity = expectedPopularity;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.name = name;
		this.description = description;
		this.stage = stage;
		this.genre = genre;
		this.artists = artists;
	}

	public int getExpectedPopularity() {
		return expectedPopularity;
	}

	public LocalDateTime getBeginTime() {
		return beginTime;
	}

	public LocalDateTime getEndTime() {
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

	public LocalDateTime getDuration() {
		int minutes = this.endTime.getMinute() - this.beginTime.getMinute();
		if (minutes < 0){
			minutes += 60;
		}

		int hours = this.endTime.getHour() - this.beginTime.getHour();

		

		LocalDateTime duration =
	}

}