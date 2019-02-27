import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.BLUE;
import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    private Map map;

    @BeforeEach
    void setUp() {
        Player player1 = new Player("Zapp", Color.RED);
        Player player2 = new Player("Fry", BLUE);
        Player player3 = new Player("Kip", Color.GREEN);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        this.map = new Map(players, 5, 5, 2, 6);
    }

    @Test
    void getTerritory() {
        assertEquals(this.map.getTerritory(0,0).getIdNum(), 0);
    }

    @Test
    void getTerritoryId() {
        assertEquals(this.map.getTerritoryId(0,0), 0);
    }

    @Test
    void countTerritory() {

    }

    @Test
    void getPropertyOf() {
    }

    @Test
    void getNeighbors() {
    }

    @Test
    void getEnemyNeighbors() {
    }

    @Test
    void countConnected() {
    }

    @Test
    void constructGraph() {
    }
}