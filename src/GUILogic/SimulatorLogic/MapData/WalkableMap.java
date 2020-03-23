package MapData;

public class WalkableMap {
    private boolean[][] walkableMap;

    public WalkableMap(boolean[][] walkableMap) {
        this.walkableMap = walkableMap;
    }

    public boolean[][] getMap() {
        return walkableMap;
    }
}