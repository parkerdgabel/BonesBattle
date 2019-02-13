import java.util.ArrayList;

/**
 * The game board for Bones Battle game
 */
public class Map {

    private final int ROWS;
    private final int COLUMNS;
    private final int VICTIMS;
    private final int NUMTERRITORIES;
    private final int OCCUPIED;
    private final int MAXDICE;
    private Territory[][] map;
    private Graph graph;
    private ArrayList<Player> players;

    /** Constructs the Territory map with appropriate #IDs
     * @return      An array of arrays containing territories
     */
    private Territory[][] buildTerritoryMap() {
        Territory[][] territories = new Territory[ROWS][COLUMNS];
        Territory territory;
        for (int i = 0; i < this.NUMTERRITORIES; i++) {
            territory = new Territory(this);
            territory.setIdNum(i);
            territories[territory.getRow()][territory.getCol()] = territory;
        }
        return territories;
    }

    /** Constructor for the map class
     * @param players       the players of the game
     * @param rows          the number of rows in the board
     * @param columns       the number of columns in the board
     * @param victims       the number of unavailable squares
     * @param maxDice       the maximum number of dice per square
     */
    public Map(ArrayList<Player> players, int rows, int columns, int victims, int maxDice) {
        this.players = players;
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.VICTIMS = victims;
        this.NUMTERRITORIES = rows * columns;
        this.OCCUPIED = NUMTERRITORIES - victims;
        this.MAXDICE = maxDice;
        this.map = this.buildTerritoryMap();
    }

    /** Getter for Rows
     * @return      the number of rows in the map
     */
    public int getROWS() {
        return ROWS;
    }

    /** Getter for Columns
     * @return      the number of columns in the map
     */
    public int getCOLUMNS() {
        return COLUMNS;
    }
}
