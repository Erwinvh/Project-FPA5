package NPCLogic;

import Enumerators.Genres;
import GUILogic.SimulatorLogic.NPCLogic.PathCalculator;
import GUILogic.SimulatorLogic.NPCLogic.PersonLogic;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

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
    private Media soundEffect;
    private MediaPlayer mediaPlayer;
    private PersonLogic personLogic;
    private String name;
    private boolean isArtist = false;

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
        this.personLogic = new PersonLogic(position, speed, this, isArtist);
    }

    /**
     * The second constructor for the artist and its name.
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
        this.personLogic = new PersonLogic(position, speed, this, isArtist);
    }

    /**
     * Assigns the corresponding image and sound according to the liked superGenre
     *
     * @param genreChance
     */
    public void genrePicker(ArrayList<Integer> genreChance) {

        String spriteSheetPath;
        String soundEffectPath;

        if (this.isArtist) {
            spriteSheetPath = "Artist.png";
            soundEffectPath = "ArtistSound.mp3";
        } else {
            int number = (int) (Math.random() * ((genreChance.get(6) - 1) + 1)) + 1;
            if (genreChance.get(0) >= number && number > 0) {
                spriteSheetPath = "metal.png";
                soundEffectPath = "MetalScream.mp3";
                this.favoriteGenre = Genres.METAL;

            } else if ((genreChance.get(0) + genreChance.get(1)) >= number && number > genreChance.get(0)) {
                spriteSheetPath = "classic.png";
                soundEffectPath = "ClassicLaugh.mp3";
                this.favoriteGenre = Genres.CLASSICAL;

            } else if ((genreChance.get(0) + genreChance.get(1) + genreChance.get(2)) >= number && number > (genreChance.get(0) + genreChance.get(1))) {
                spriteSheetPath = "country.png";
                soundEffectPath = "CountryAlabama.mp3";
                this.favoriteGenre = Genres.COUNTRY;

            } else if ((genreChance.get(6) - genreChance.get(5) - genreChance.get(4)) >= number && number > (genreChance.get(0) + genreChance.get(1) + genreChance.get(2))) {
                spriteSheetPath = "rap.png";
                soundEffectPath = "TingGoSkrraOriginal.mp3";
                this.favoriteGenre = Genres.RAP;

            } else if ((genreChance.get(6) - genreChance.get(5)) >= number && number > (genreChance.get(6) - genreChance.get(5) - genreChance.get(4))) {
                spriteSheetPath = "Pop.png";
                soundEffectPath = "AdoringFan.mp3";
                this.favoriteGenre = Genres.POP;

            } else if (genreChance.get(6) >= number && number > (genreChance.get(6) - genreChance.get(5))) {
                spriteSheetPath = "visitor.png";
                soundEffectPath = "VisitorHello.mp3";
                this.favoriteGenre = Genres.ELECTRONIC;

            } else {
                spriteSheetPath = "terminator.png";
                soundEffectPath = "Terminator.mp3";
                this.favoriteGenre = Genres.COUNTRY;
            }
        }

        try {
            this.sprite = ImageIO.read(getClass().getResource("/images/" + spriteSheetPath));
            this.soundEffect = new Media(getClass().getResource("/soundEffects/" + soundEffectPath).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mediaPlayer = new MediaPlayer(this.soundEffect);
    }

    /**
     * The getter for the npcs logic
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
            if (other != this && this.personLogic.getNewPosition().distance(other.personLogic.getPosition()) < 32 * this.personLogic.getTransform().getScaleX()) {
                collided = true;
            }
        }

        if (!collided) {
            this.personLogic.setPosition(this.personLogic.getNewPosition());
        } else {
            this.personLogic.setTarget(PathCalculator.findRandomClosestWalkable(this.personLogic.getPosition(), this.personLogic.getDistanceMap()));
        }
    }

    /**
     * The draw function of this npc
     * @param g The 2D graphics
     */
    public void draw(Graphics2D g) {
        g.drawImage(sprite, this.personLogic.getTransform(), null);
    }

    /**
     * Plays an soundEffect according to the genre
     */
    public void playSoundEffect() {
        if (this.favoriteGenre != null && this.favoriteGenre.equals(Genres.METAL)) {
            this.mediaPlayer.setVolume(0.05);
        }

        this.mediaPlayer.setStartTime(Duration.millis(0));
        this.mediaPlayer.play();
        this.mediaPlayer.stop();
        this.mediaPlayer.setStartTime(Duration.millis(0));
    }

    /**
     * The getter for the sprite
     * @return The sprite of the npc
     */
    public BufferedImage getSprite() {
        return sprite;
    }

    /**
     * The getter for the favorite genre of the npc
     * @return the favorite genre
     */
    public Genres getFavoriteGenre() {
        return favoriteGenre;
    }

    /**
     * The setter for the speed of the NPC
     * @param speed The given speed
     */
    public void setSpeed(double speed){
        this.personLogic.setSpeed(speed);
    }

    /**
     * The getter for the NPC name
     * @return The npc name
     */
    public String getName() {
        return name;
    }

    /**
     * The getter for the boolean of whether an npc is an artist or not
     * @return the true or false value of the npc
     */
    public boolean isArtist() {
        return isArtist;
    }
}