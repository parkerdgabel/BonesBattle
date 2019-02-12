import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    private Territory territory;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        Map map = new Map(new ArrayList<>(), 8, 8, 0, 1);
        this.territory = new Territory(map);
        this.territory.setIdNum(8);
    }

    @org.junit.jupiter.api.Test
    void getRow() {
        assertEquals(1, this.territory.getRow());
    }

    @org.junit.jupiter.api.Test
    void getCol() {
        assertEquals(0, this.territory.getCol());
    }
}