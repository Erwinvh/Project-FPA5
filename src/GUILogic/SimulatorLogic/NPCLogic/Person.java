package GUILogic.SimulatorLogic.NPCLogic;

import Enumerators.Genres;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class to represent a visitor or artist
 */
public class Person {

    private BufferedImage sprite;
    private Genres favoriteGenre;
    private PersonLogic personLogic;
    private String name;
    private boolean isArtist;

    /**
     * A constructor of NPCLogic.Person
     *
     * @param position        The starting position of the NPCLogic.Person
     * @param genreChanceList A list for the probability of liking a Genre
     * @param speed           The movement speed of the NPCLogic.Person
     * @param isArtist        A Boolean that identifies the npc as artist or not
     */
    public Person(Point2D position, ArrayList<Integer> genreChanceList, double speed, boolean isArtist) {
        this.isArtist = isArtist;
        genrePicker(genreChanceList);
        this.personLogic = new PersonLogic(position, speed, this);
    }

    /**
     * The second constructor for the artist and its name.
     *
     * @param position        The starting position of the NPCLogic.Person
     * @param genreChanceList A list for the probability of liking a Genre
     * @param name            The artist name
     * @param speed           The movement speed of the NPCLogic.Person
     * @param isArtist        A Boolean that identifies the npc as artist or not
     */
    public Person(Point2D position, ArrayList<Integer> genreChanceList, String name, double speed, boolean isArtist) {
        this.name = name;
        this.isArtist = isArtist;
        genrePicker(genreChanceList);
        this.personLogic = new PersonLogic(position, speed, this);
    }

    /**
     * Assigns the corresponding image and sound according to the liked superGenre
     *
     * @param genreChance keeps track of how big the chance is to get a specific genre assigned
     */
    private void genrePicker(ArrayList<Integer> genreChance) {
        String spriteSheetPath;

        if (this.isArtist) {
            spriteSheetPath = "Artist.png";
        } else {
            int number = (int) (Math.random() * ((genreChance.get(6)) + 1));
            if (genreChance.get(0) >= number && number > 0) {
                spriteSheetPath = "metal.png";
                this.favoriteGenre = Genres.METAL;
            } else if ((genreChance.get(0) + genreChance.get(1)) >= number && number > genreChance.get(0)) {
                spriteSheetPath = "classic.png";
                this.favoriteGenre = Genres.CLASSICAL;
            } else if ((genreChance.get(0) + genreChance.get(1) + genreChance.get(2)) >= number) {
                spriteSheetPath = "country.png";
                this.favoriteGenre = Genres.COUNTRY;
            } else if ((genreChance.get(6) - genreChance.get(5) - genreChance.get(4)) >= number) {
                spriteSheetPath = "rap.png";
                this.favoriteGenre = Genres.RAP;
            } else if ((genreChance.get(6) - genreChance.get(5)) >= number) {
                spriteSheetPath = "Pop.png";
                this.favoriteGenre = Genres.POP;
            } else if (genreChance.get(6) >= number) {
                spriteSheetPath = "visitor.png";
                this.favoriteGenre = Genres.ELECTRONIC;
            } else {
                spriteSheetPath = "terminator.png";
                this.favoriteGenre = Genres.COUNTRY;
            }
        }
        try {
            this.sprite = ImageIO.read(getClass().getResource("/images/" + spriteSheetPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The getter for the npc's logic
     *
     * @return This Npc's logic
     */
    public PersonLogic getPersonLogic() {
        return personLogic;
    }

    /**
     * decides the behavior of the Person
     */
    public void update(ArrayList<Person> people) {

        this.personLogic.update();

        boolean collided = false;
        for (Person other : people) {
            if (other != this && this.personLogic.getNewPosition().distance(other.personLogic.getPosition()) < 16) {
                collided = true;
            }
        }

        if (!collided) {
            this.personLogic.setPosition(this.personLogic.getNewPosition());
        } else {
            this.personLogic.setTarget(PathCalculator.findRandomClosestWalkable(this.personLogic.getPosition()));
        }
    }

    /**
     * The draw function of this npc
     *
     * @param g The 2D graphics
     */
    public void draw(Graphics2D g) {
        g.drawImage(sprite, this.personLogic.getTransform(), null);
    }

    /**
     * The getter for the sprite
     *
     * @return The sprite of the npc
     */
    BufferedImage getSprite() {
        return sprite;
    }

    /**
     * The getter for the favorite genre of the npc
     *
     * @return the favorite genre
     */
    Genres getFavoriteGenre() {
        return favoriteGenre;
    }

    /**
     * The setter for the speed of the NPC
     *
     * @param speed The given speed
     */
    public void setSpeed(double speed) {
        this.personLogic.setSpeed(speed);
    }

    /**
     * The getter for the NPC name
     *
     * @return The npc name
     */
    public String getName() {
        return name;
    }

    /**
     * The getter for the boolean of whether an npc is an artist or not
     *
     * @return the true or false value of the npc
     */
    public boolean isArtist() {
        return isArtist;
    }
}