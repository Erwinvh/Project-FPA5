package GUILogic.SimulatorLogic.MapData;

public class WalkableMap {
    private boolean[][] walkableMap;

    /**
     * The constructor of the walkable map
     * @param walkableMap
     */
    public WalkableMap(boolean[][] walkableMap) {
        this.walkableMap = walkableMap;
    }

    /**
     * ???
     * @return
     */
    public boolean[][] getMap() {
        return walkableMap;
    }
}