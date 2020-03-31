package GUILogic.SimulatorLogic.MapData;

public class WalkableMap {
    private boolean[][] walkableMap;

    /**
     * The constructor of the walkable map
     *
     * @param walkableMap this is a 2D boolean array which holds the logic on what index NPC's can walk
     */
    WalkableMap(boolean[][] walkableMap) {
        this.walkableMap = walkableMap;
    }

    /**
     * @return the 2D boolean array
     */
    public boolean[][] getMap() {
        return walkableMap;
    }
}