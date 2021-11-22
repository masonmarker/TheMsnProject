

import java.util.ArrayList;
import java.util.List;
import MsnStructures.MsnGraph;
import MsnStructures.MsnGraph.Edge;
import MsnStructures.MsnGraph.Vertex;

public class MasonGraph extends TileGraph {

  public MsnGraph graph;

  public MasonGraph(TileState[] allStates) {
    super(allStates);
    graph = new MsnGraph();


  }

  public void information() {
    graph.information();
  }

  public void visualize() {
    graph.visualize();
  }

  public List<Integer> shortest(int u, int v) {
    List<Integer> cycles = shortestPath(u, v);
    System.out.println("path found, trimming...");
    ArrayList<Vertex> vertices = new ArrayList<>();
    for (Integer i : cycles) {
      vertices.add(new Vertex(String.valueOf(i), vertexData(i)));
    }

    Edge[] edges = MsnGraph.convert(vertices.toArray(Vertex[]::new));

    for (Edge e : edges) {
      graph.addEdge(e);
    }

    graph.removeCycles();


    ArrayList<Integer> fixed = new ArrayList<>();
    for (Vertex vertex : graph.gatherVertices()) {
      fixed.add(Integer.valueOf(vertex.getName()));
    }
    return fixed;
  }

}
