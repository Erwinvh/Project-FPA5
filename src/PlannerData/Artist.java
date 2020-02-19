package PlannerData;

import Enumerators.Genres;
import javafx.scene.image.Image;

import java.io.Serializable;

public class Artist implements Serializable {

	private String name;
	private Genres genre;
	private String description;

	public Artist(String name, Genres genre, String description) {
		this.name = name;
		this.genre = genre;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public Genres getGenre() {
		return genre;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGenre(Genres genre) {
		this.genre = genre;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}