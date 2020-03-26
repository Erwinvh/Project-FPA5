package PlannerData;

import Enumerators.Genres;
import java.io.Serializable;

/**
 * A class that saves attributes concerning an Artist
 */
public class Artist implements Serializable {

	private String name;
	private Genres genre;
	private String description;

	/**
	 * The constructor of Artist
	 * @param name
	 * @param genre
	 * @param description
	 */
	public Artist(String name, Genres genre, String description) {
		this.name = name;
		this.genre = genre;
		this.description = description;
	}

	/**
	 * The getter for the artist name
	 * @return Artist name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The getter for the artist genre
	 * @return Artist genre
	 */
	public Genres getGenre() {
		return genre;
	}

	/**
	 * The getter for the artist description
	 * @return Artist description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * The setter for the artist name
	 * @param name New artist name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * the setter for the artist genre
	 * @param genre New artist genre
	 */
	public void setGenre(Genres genre) {
		this.genre = genre;
	}

	/**
	 * The setter for the artist description
	 * @param description New artist description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}