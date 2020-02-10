package Enumerators;

//public enum Constants{
//    YES("y"), NO("N")
//
//    // No changes
//
//    @Override
//    public String toString() {
//        return value;
//    }
//    }

public enum Genres {
    ALTERNATIVE_ROCK("Alternative rock"),
    BLUES("Blues"),
    BLUES_ROCK("Blues rock"),
    CLASSICAL("Classical"),
    COUNTRY("Country"),
    DANCE("Dance"),
    ELECTRONIC("Electronic"),
    EMO_ROCK("Emo rock"),
    FOLK("Folk"),
    HIP_HOP("Hip hop"),
    HOUSE("House"),
    INDIE_POP("Indie pop"),
    JAZZ("Jazz"),
    K_POP("K-pop"),
    METAL("Metal"),
    NIGHTCORE("Nightcore"),
    POP("Pop"),
    PUNK_ROCK("Punk rock"),
    RAP("Rap"),
    REGGEA("Reggea"),
    ROCK("Rock"),
    SOUL("Soul");

    private String fancyName;

    Genres(String fancyName) {
        this.fancyName = fancyName;
    }

    public String getFancyName(){
        return this.fancyName;
    }
}