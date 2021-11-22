


import java.util.List;

/**
 * A basic graph interface. The parameterized type VertexDataT is the type of data stored at each
 * vertex.
 * 
 * @author John C. Bowers
 * @version Oct 6, 2021
 */
public interface IGraph<VertexDataT> {

  /**
   * @return The number of vertices in the graph.
   */
  public int vertexCount();

  /**
   * @return The number of edges in the graph.
   */
  public int edgeCount();

  /**
   * Returns the data stored at vertex u.
   */
  public VertexDataT vertexData(int u);

  /**
   * @param The data of some vertex.
   * @return The vertex index that contains the data.
   */
  public int indexOf(VertexDataT data);

  /**
   * @return true if there is an edge from u to v in the graph, false otherwise.
   */
  public boolean hasEdge(int u, int v);

  /**
   * Returns an Iterable over the neighbors of vertex u.
   */
  public Iterable<Integer> neighborsOf(int u);

  /**
   * Returns the number of connected components in the graph.
   */
  public int connectedComponents();

  /**
   * @return the shortest path from u to v in the graph or null if no path exists.
   */
  public List<Integer> shortestPath(int u, int v);
}
