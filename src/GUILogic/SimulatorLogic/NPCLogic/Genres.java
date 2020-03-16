package NPCLogic;

public enum Genres {
    ALTERNATIVE_ROCK("Alternative rock", "Metal"),
    BLUES("Blues","Classic"),
    BLUES_ROCK("Blues rock", "Metal"),
    CLASSICAL("Classical", "Classic"),
    COUNTRY("Country","Country"),
    DANCE("Dance", "Electro"),
    ELECTRONIC("Electronic", "Electro"),
    EMO_ROCK("Emo rock","Metal"),
    FOLK("Folk", "Country"),
    HIP_HOP("Hip hop", "Rap"),
    HOUSE("House", "Electro"),
    INDIE_POP("Indie pop", "Pop"),
    JAZZ("Jazz", "Classic"),
    K_POP("K-pop", "Pop"),
    METAL("Metal", "Metal"),
    NIGHTCORE("Nightcore", "Electro"),
    POP("Pop", "Pop"),
    PUNK_ROCK("Punk rock", "Metal"),
    RAP("Rap", "Rap"),
    REGGEA("Reggea", "Rap"),
    ROCK("Rock", "Metal"),
    SOUL("Soul", "Classic");

    private String fancyName;
    private String SuperGenre;

    Genres(String fancyName, String SuperGenre) {
        this.fancyName = fancyName;
        this.SuperGenre = SuperGenre;
    }

    public String getFancyName(){
        return this.fancyName;
    }

    public String getSuperGenre() {
        return SuperGenre;
    }

    public static Genres getGenre(String name){
        for (Genres genre : Genres.values()) {
            if(genre.getFancyName().equals(name))
                return genre;
        }

        return null;
    }
}