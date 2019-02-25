import java.util.*;

/**
 * Graph object using an adjacency list for its representation
 */
public class Graph {
    // Use a matrix of booleans to represent the graph
    private List<List<Integer>> adjacencyList;

    /** Constructor for the graph
     * @param numVertices
     */
    public Graph(int numVertices) {
        this.adjacencyList = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            this.adjacencyList.add(new ArrayList<>());
        }
    }

    /** Returns a list of unused vertices in the graph
     * @return          A list of integers representing the ids of the unused vertices
     */
    public List<Integer> getUnusedVertices() {
        List<Integer> retList = new ArrayList<>();
        for (int i = 0; i < this.adjacencyList.size(); i++) {
            if (this.adjacencyList.get(i).isEmpty()) {
                retList.add(i);
            }
        }
        return retList;
    }

    /** Checks if source is adjacent to destination
     * @param source            The source vertex
     * @param destination       The destination vertex
     * @return                  True if source and destination are adjacent otherwise returns false
     */
    public boolean isEdge(int source, int destination) {
        for (Integer neighbor : this.adjacencyList.get(source)) {
            if (neighbor == destination) {
                return true;
            }
        }
        return false;
    }

    /** Adds an edge to graph joining source to destination
     * @param source            The source vertex
     * @param destination       The destination vertex
     */
    public void addEdge(int source, int destination) {
        this.adjacencyList.get(source).add(destination);
        this.adjacencyList.get(destination).add(source);
    }

    /** Removes an edge from source to destination
     * @param source            The source vertex
     * @param destination       The destination vertex
     */
    public void removeEdge(int source, int destination) {
        this.adjacencyList.get(source).remove(destination);
        this.adjacencyList.get(destination).remove(source);
    }

    /** Checks if a vertex is active in a graph
     * @param vertex            The vertex to check
     * @return                  True if the vertex is active otherwise false
     */
    public boolean isInGraph (int vertex) {
        return this.adjacencyList.get(vertex).size() != 0;
    }

    /** Returns a list of all vertices adjacent to the given vertex
     * @param vertex            The vertex to get the neighbors of
     * @return                  A list of all vertices adjacent to a given vertex
     */
    public List<Integer> getAdjacent(int vertex) {
        return this.adjacencyList.get(vertex);
    }

    /** Returns the degree of the given vertex
     * @param vertex            The vertex to get the degree of
     * @return                  The degree of the vertex
     */
    public int degree(int vertex) {
        return this.adjacencyList.get(vertex).size();
    }

    /** Breadth first search of the graph
     * @param queue             A queue to hold vertices to visit
     * @param discovered        A set of already visited discovering
     */
    private void breadthFirstSearch(Queue<Integer> queue, Set<Integer> discovered) {
        if (queue.isEmpty()) {
            return;
        } else {
            int vertex = queue.remove();
            for (int neighbor : this.adjacencyList.get(vertex)) {
                if (!discovered.contains(neighbor)) {
                    discovered.add(neighbor);
                    queue.add(vertex);
                }
            }
            breadthFirstSearch(queue, discovered);
        }
    }

    /** Checks if a graph is connected
     * @return          True if the graph is connected otherwise false
     */
    public boolean connected() {
        int vertex;
        for (vertex = 0; vertex < this.adjacencyList.size(); vertex++) {
            if (this.isInGraph(vertex)) {
                break;
            }
        }
        Set<Integer> discovered = new HashSet<>();
        discovered.add(vertex);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(vertex);
        breadthFirstSearch(queue, discovered);
        return discovered.size() == this.adjacencyList.size() - this.getUnusedVertices().size();
    }
}
