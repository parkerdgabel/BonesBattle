import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    private Graph graph;

    @BeforeEach
    void setUp() {
        this.graph = new Graph(5);
        this.graph.addEdge(0, 1);
        this.graph.addEdge(0,2);
        this.graph.addEdge(1, 3);
    }

    @Test
    void getUnusedVertices() {
        List<Integer> expected = new ArrayList<>();
        expected.add(4);
        assertEquals(expected, this.graph.getUnusedVertices());
    }

    @Test
    void isEdge() {
        assertTrue(this.graph.isEdge(0,1));
        assertFalse(this.graph.isEdge(0, 4));
    }

    @Test
    void addEdge() {
        this.graph.addEdge(2, 3);
        assertTrue(this.graph.isEdge(2,3));
        assertTrue(this.graph.isEdge(3,2));
    }

    @Test
    void removeEdge() {
        this.graph.removeEdge(2,3);
        assertFalse(this.graph.isEdge(2,3));
        assertFalse(this.graph.isEdge(3,2));
    }

    @Test
    void isInGraph() {
        assertFalse(this.graph.isInGraph(4));
        assertTrue(this.graph.isInGraph(0));
    }

    @Test
    void removeVertex() {
        this.graph.addEdge(4, 3);
        this.graph.removeVertex(4);
        assertFalse(this.graph.isInGraph(4));
    }

    @Test
    void getAdjacent() {
        List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        assertEquals(expected, this.graph.getAdjacent(0));
    }

    @Test
    void degree() {
        assertEquals(2, this.graph.degree(0));
    }

    @Test
    void connected() {
        assertTrue(this.graph.connected());
        this.graph.removeEdge(0,1);
        assertFalse(this.graph.connected());
    }
}