import java.util.*;

/**
 * The game board for Bones Battle game
 */
public class Map {

    public final int ROWS;
    public final int COLUMNS;
    public final int VICTIMS;
    public final int NUMTERRITORIES;
    public final int OCCUPIED;
    public final int MAXDICE;
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
        this.graph = constructGraph(rows, columns, victims);
        this.partitionTerritories();
        this.distributeDice();
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
     * Getter for a specific territory. Returns null if the territory is not in the map
     *
     * @param row    the row the territory is on
     * @param column the column the territory is on
     * @return the territory at (row, column)
     */
    public Territory getTerritory(int row, int column) {
        if (row < this.ROWS && row >= 0 && column < this.COLUMNS && column >= 0)
            return this.map[row][column];
        return null;
    }

    /**
     * Gets the id of the territory at (row, column). Returns -1 if the territory is not in the map
     *
     * @param row    the row the territory is on
     * @param column the column the territory is on
     * @return the id number of the territory at (row, column)
     */
    public int getTerritoryId(int row, int column) {
        if (row < this.ROWS && row >= 0 && column < this.COLUMNS && column >= 0)
            return this.map[row][column].getIdNum();
        return -1;
    }

    /**
     * Counts the number of territories owned by the player
     *
     * @param player the player to count
     * @return the number of territories owned by the player
     */
    public int countTerritories(Player player) {
        return getPropertyOf(player).size();
    }

    public int countDice(Player player) {
        int sum = 0;
        for (Territory territory : this.getPropertyOf(player)) {
            sum += territory.getDice();
        }
        return sum;
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
     * Checks if the territory at (row, column) is in the graph and returns it. Returns null if the the territory is not in the graph
     *
     * @param row    the row the territory is on
     * @param column the column the territory is on
     * @return The territory if it is in the graph: else null
     */
    private Territory getTerritoryInGraph(int row, int column) {
        Territory territory = getTerritory(row, column);
        if (territory == null) {
            return null;
        } else if (this.graph.isInGraph(territory.getIdNum())) {
            return territory;
        } else {
            return null;
        }
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

    /**
     * Returns a list of all adjacent territories not owned by the owner of the passed in territory
     *
     * @param cell The territory to get the enemies of
     * @return A list of the enemy territory
     */
    public ArrayList<Territory> getEnemyNeighbors(Territory cell) {
        ArrayList<Territory> enemies = new ArrayList<>();
        for (Territory neighbor : this.getNeighbors(cell)) {
            if (!(neighbor.getOwner().getId() == cell.getOwner().getId())) {
                enemies.add(neighbor);
            }
        }
        return enemies;
    }

    /**
     * Partitions the active territories among the players at random. Some players may have more territories than others
     */
    private void partitionTerritories() {
        int partitionSize = this.OCCUPIED / this.players.size();
        boolean done = false;
        Random rand = new Random();
        Territory territory;
        for (Player player : this.players) {
            done = false;
            while (!done) {
                territory = this.getTerritory(rand.nextInt(this.ROWS), rand.nextInt(this.COLUMNS));
                if (territory.getOwner() == null && this.graph.isInGraph(territory.getIdNum()) && this.getPropertyOf(player).size() <= partitionSize) {
                    territory.setOwner(player);
                } else if (this.getPropertyOf(player).size() == partitionSize) {
                    done = true;
                }
            }
        }
        int remainder = this.OCCUPIED % this.players.size();
        if (remainder != 0) {
            for (int i = 0; i < remainder; i++) {
                territory = this.getTerritory(rand.nextInt(this.ROWS), rand.nextInt(this.COLUMNS));
                if (territory.getOwner() == null && this.graph.isInGraph(territory.getIdNum())) {
                    Player player = this.players.get(rand.nextInt(this.players.size()));
                    territory.setOwner(player);
                } else {
                    i--;
                }
            }
        }
    }

    /**
     * Overloaded method to distribute dice randomly for one player
     *
     * @param player The player to distribute the dice
     */
    private void distributeDice(Player player) {
        List<Territory> owned = this.getPropertyOf(player);
        int totalDice = 3 * owned.size();
        int numDice;
        // Ensure all territories owned by the player have at least one dice
        for (Territory territory : owned) {
            numDice = territory.getDice();
            if (numDice < this.MAXDICE) {
                territory.setDice(numDice + 1);
                totalDice -= 1;
            }
        }
        Random rand = new Random();
        while (totalDice >= 0) {
            // Get a random territory owned by the player
            Territory territory = owned.get(rand.nextInt(owned.size()));
            numDice = territory.getDice();
            if (numDice < this.MAXDICE) {
                territory.setDice(numDice + 1);
                totalDice -= 1;
            }
        }
    }

    /**
     * Distributes the dice among all the players randomly
     */
    private void distributeDice() {
        for (Player player : this.players) {
            distributeDice(player);
        }
    }

    /** Returns the largest connected subgraph owned by the player
     * @param player            The player to use
     * @return                  The size of the largest connected subgraph owned by the player
     */
    public int countConnected(Player player) {
        Set<Integer> searched = new HashSet<>();
        Set<Integer> largestSubgraph = new HashSet<>();
        Set<Integer> temporary = new HashSet<>();
        List<Territory> owned = this.getPropertyOf(player);
        int vertex;
        for (Territory territory : owned) {
            vertex = territory.getIdNum();
            if (!searched.contains(vertex)) {
                temporary.clear();
                temporary.add(vertex);
                Deque<Integer> queue = new LinkedList<>();
                queue.add(vertex);
                breadthFirstSearch(queue, temporary);
                if (largestSubgraph.size() < temporary.size()) {
                    largestSubgraph = temporary;
                }
                searched.addAll(temporary);
                temporary.clear();
            }
        }
        return largestSubgraph.size();
    }

    private void breadthFirstSearch(Deque<Integer> queue, Set<Integer> discovered) {
        if (queue.isEmpty()) {
            return;
        } else {
            int vertex = queue.pop();
            int row = Math.floorDiv(vertex, this.ROWS);
            int col = Math.floorDiv(vertex, this.COLUMNS);
            Territory territory = this.getTerritory(row, col);
            for (Territory neighbor : this.getNeighbors(territory)) {
                if (neighbor.getOwner().getId() == territory.getOwner().getId()) {
                    int id = neighbor.getIdNum();
                    if (!discovered.contains(id)) {
                        discovered.add(id);
                        queue.add(id);
                    }
                }
            }
            breadthFirstSearch(queue, discovered);
        }

    }

    /**
     * Builds a lattice graph on every territory in the map. (see https://en.wikipedia.org/wiki/Lattice_graph)
     *
     * @param graph The graph to build the lattice for
     */
    private void fullyConnectGraph(Graph graph) {
        Territory territory;
        List<Territory> neighbors = new ArrayList<>();
        for (int i = 0; i < this.ROWS; i++) {
            neighbors.clear();
            for (int j = 0; j < this.COLUMNS; j++) {
                territory = this.getTerritory(i, j);
                neighbors = this.getNeighbors(territory);
                for (Territory neighbor : neighbors) {
                    graph.addEdge(territory.getIdNum(), neighbor.getIdNum());
                }
            }
        }
    }

    /**
     * Constructs a graph of the game board with the active territories scattered randomly. The graph is guaranteed to be connected
     *
     * @param rows    The rows of the board
     * @param cols    The columns of the board
     * @param victims The number of inactive territories on the board
     * @return A graph representing the active territories of the board
     */
    public Graph constructGraph(int rows, int cols, int victims) {
        Graph graph = new Graph(this.NUMTERRITORIES);
        fullyConnectGraph(graph);
        Random rand = new Random();
        int randNum;
        for (int i = 0; i < victims; i++) {
            randNum = rand.nextInt(this.NUMTERRITORIES);
            if (!graph.isInGraph(randNum)) {
                i--;
            } else {
                graph.removeVertex(randNum);
            }
        }
        if (!graph.connected()) {
            return constructGraph(rows, cols, victims);
        } else {
            return graph;
        }
    }
}
