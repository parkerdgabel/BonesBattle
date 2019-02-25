import java.util.ArrayList;
import java.util.Random;

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

    /**
     * Constructs the Territory map with appropriate #IDs
     *
     * @return An array of arrays containing territories
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

    /**
     * Constructor for the map class
     *
     * @param players the players of the game
     * @param rows    the number of rows in the board
     * @param columns the number of columns in the board
     * @param victims the number of unavailable squares
     * @param maxDice the maximum number of dice per square
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

    /**
     * Getter for Rows
     *
     * @return the number of rows in the map
     */
    public int getROWS() {
        return ROWS;
    }

    /**
     * Getter for Columns
     *
     * @return the number of columns in the map
     */
    public int getCOLUMNS() {
        return COLUMNS;
    }

    /**
     * Getter for map
     *
     * @return the map of territories
     */
    public Territory[][] getMap() {
        return map;
    }

    /**
     * Getter for the graph
     *
     * @return the graph of the map
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Getter for a specific territory
     *
     * @param row    the row the territory is on
     * @param column the column the territory is on
     * @return the territory at (row, column)
     */
    public Territory getTerritory(int row, int column) {
        return this.map[row][column];
    }

    /**
     * Gets the id of the territory at (row, column)
     *
     * @param row    the row the territory is on
     * @param column the column the territory is on
     * @return the id number of the territory at (row, column)
     */
    public int getTerritoryId(int row, int column) {
        return this.map[row][column].getIdNum();
    }

    /**
     * Counts the number of territories owned by the player
     *
     * @param player the player to count
     * @return the number of territories owned by the player
     */
    public int countTerritory(Player player) {
        return getPropertyOf(player).size();
    }

    /**
     * Returns a list of references to territories owned by a player
     *
     * @param player the player to get the territories from
     * @return a list of references to territories owned by a player
     */
    public ArrayList<Territory> getPropertyOf(Player player) {
        ArrayList<Territory> arr = new ArrayList<Territory>();
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                if (this.getTerritory(i, j).getOwner().getId() == player.getId()) {
                    arr.add(this.getTerritory(i, j));
                }
            }
        }
        return arr;
    }

    /**
     * Checks if the territory at (row, column) is in the graph and returns it
     *
     * @param row    the row the territory is on
     * @param column the column the territory is on
     * @return The territory if it is in the graph: else null
     */
    private Territory getTerritoryInGraph(int row, int column) {
        Territory territory = null;
        try {
            territory = getTerritory(row, column);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        if (this.graph.isInGraph(territory.getIdNum())) {
            return territory;
        }
        return null;
    }

    /**
     * Adds a territory to the list if it is not null
     *
     * @param arr       The list to add the territory to
     * @param territory The territory to add
     */
    private void addIfNotNull(ArrayList<Territory> arr, Territory territory) {
        if (territory != null) {
            arr.add(territory);
        }
    }

    /**
     * Gets a list of all adjacent territories
     *
     * @param cell The territory to get the neighbors for
     * @return A list of all neighbors.
     */
    public ArrayList<Territory> getNeighbors(Territory cell) {
        ArrayList<Territory> arr = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getCol();
        Territory territory = getTerritory(row + 1, col);
        addIfNotNull(arr, territory);
        territory = getTerritory(row - 1, col);
        addIfNotNull(arr, territory);
        territory = getTerritory(row, col + 1);
        addIfNotNull(arr, territory);
        territory = getTerritory(row, col - 1);
        addIfNotNull(arr, territory);
        return arr;
    }

    public ArrayList<Territory> getEnemyNeighbors(Territory cell) {
        return null;
    }

    /**
     * Assigns territories to be inactive
     */
    private void assignInactiveTerritories() {
        Random rand = new Random();
        for (int i = 0; i < this.VICTIMS; i++) {
            Territory territory = this.getTerritory(rand.nextInt(this.ROWS), rand.nextInt(this.COLUMNS));
            // If the territory is already inactive just decrement i and continue
            if (territory.isInactive()) {
                i--;
            } else {
                // Set this territory to be inactive
                territory.setInactive(true);
            }
        }
    }

    private void assignTerritory(Random rand, int partitionSize, int numPlayers) {
        int randNum = rand.nextInt(numPlayers);
        Player player = this.players.get(randNum);
        int playerTerritoryCount = this.countTerritory(player);
        // If the player has enough territories then try again
        if (playerTerritoryCount >= partitionSize) {
            assignTerritory(rand, partitionSize, numPlayers);
        } else {
            Territory territory = this.getTerritory(rand.nextInt(this.ROWS), rand.nextInt(this.COLUMNS));
            // If the territory is inactive or already owned. Try again
            if (territory.isInactive() || territory.getOwner() != null) {
                assignTerritory(rand, partitionSize, numPlayers);
            } else {
                territory.setOwner(player);
            }
        }
    }

    /**
     * Partitions the active territories among the players at random. Some players may have more territories than others
     */
    private void partitionTerritories() {
        int numPlayers = this.players.size();
        int partitionSize = this.OCCUPIED / numPlayers;
        int modulo = this.OCCUPIED % numPlayers;
        Random rand = new Random();
        for (int i = 0; i < this.OCCUPIED; i++) {
            assignTerritory(rand, partitionSize, numPlayers);
        }
        // If occupied is not divisible by the number of players then some players get extra territories
        if (modulo != 0) {
            for (int i = 0; i < modulo; i++) {
                Territory territory = this.getTerritory(rand.nextInt(this.ROWS), rand.nextInt(this.COLUMNS));
                if (territory.isInactive() || territory.getOwner() != null) {
                    i--;
                } else {
                   Player player = this.players.get(rand.nextInt(numPlayers));
                   territory.setOwner(player);
                }
            }
        }
    }

    private void distributeDice() {

    }

    public int countConnected(Player player) {
        return 0;
    }

    public Graph constructGraph(int rows, int cols, int victims) {
        return null;
    }
}
