package NPCLogic;

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

    private boolean isArtist = false;

    /**
     * A constructor of NPCLogic.Person
     *
     * @param position        the starting position of the NPCLogic.Person
     * @param genreChanceList a list for the probability of liking a Genre
     * @param speed           the movement speed of the NPCLogic.Person
     */
    public Person(Point2D position, ArrayList<Integer> genreChanceList, int speed, boolean isArtist) {
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

        if (isArtist) {
            //TODO: add artist sound effect.
            spriteSheetPath = "Artist.png";
            soundEffectPath = "ClassicLaugh.mp3";
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
                spriteSheetPath = "terminator.png";
                soundEffectPath = "Terminator.mp3";
                this.favoriteGenre = Genres.ELECTRONIC;

            } else {
                spriteSheetPath = "visitor.png";
                soundEffectPath = "VisitorHello.mp3";
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

    public PersonLogic getPersonLogic() {
        return personLogic;
    }

    /**
     * decides the behavior of the Person
     */
    public void update(ArrayList<Person> people) {

        this.personLogic.update();
        //colliding handler
        boolean collided = false;

        for (Person other : people) {
            if (other != this && this.personLogic.getNewPosition().distance(other.personLogic.getPosition()) < 32) {
                collided = true;
            }
        }

        if (!collided) {
            this.personLogic.setPosition(this.personLogic.getNewPosition());
        } else {
            this.personLogic.setTarget(PathCalculator.findRandomClosestWalkable(this.personLogic.getPosition(), this.personLogic.getDistanceMap()));
        }
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite, this.personLogic.getTransform(), null);
    }

    /**
     * Plays an soundEffect according to the genre
     */
    public void playSoundEffect() {
        if (this.favoriteGenre.equals("metal")) {
            this.mediaPlayer.setVolume(0.05);
        }

        this.mediaPlayer.setStartTime(Duration.millis(0));
        this.mediaPlayer.play();
        this.mediaPlayer.stop();
        this.mediaPlayer.setStartTime(Duration.millis(0));

    }

    public BufferedImage getSprite() {
        return sprite;
    }
}