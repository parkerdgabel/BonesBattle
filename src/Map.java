import java.util.ArrayList;

public class Map {

    private final int ROWS;
    private final int COLUMNS;


    public Map (ArrayList<Player> players, int rows, int columns, int victims, int maxDice) {
        ROWS = rows;
        COLUMNS = columns;
    }

    public int getROWS() {
        return ROWS;
    }

    public int getCOLUMNS() {
        return COLUMNS;
    }
}
