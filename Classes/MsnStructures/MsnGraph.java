package MsnStructures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.SwingWorker;
import Drawing.StdDraw;
import MsnLib.Msn;
import MsnStructures.MsnGraph.Edge;

/**
 * Analyzes sequences of edges.
 * 
 * @author Mason Marker
 * @version 1.0 - 10/05/2021
 */
public class MsnGraph implements Iterable<Edge> {

  ArrayList<Edge> edges;

  /**
   * Constructs an MsnGraph.
   */
  public MsnGraph() {
    edges = new ArrayList<>();
  }

  /**
   * Constructs an MsnGraph.
   * 
   * @param edges the initial edges
   */
  public MsnGraph(Edge... edges) {
    this.edges = new ArrayList<>(List.of(edges));
  }

  /**
   * Adds an Edge.
   * 
   * @param e the Edge to add
   */
  public void addEdge(Edge e) {
    edges.add(e);
    fix();
  }

  /**
   * Adds an Edge.
   * 
   * @param v1 the first Vertex
   * @param v2 the second Vertex
   */
  public void addEdge(Vertex v1, Vertex v2) {
    addEdge(new Edge(v1, v2));
  }

  /**
   * Adds an Edge.
   * 
   * @param first the name of the first Vertex
   * @param second the name of the second Vertex
   */
  public void addEdge(String first, String second) {
    addEdge(new Edge(new Vertex(first), new Vertex(second)));
  }

  /**
   * Adds an Edge.
   * 
   * @param first the name of the first Vertex
   * @param storage1 the storage of the first Vertex
   * @param second the name of the second Vertex
   * @param storage2 the storage of the second Vertex
   */
  public void addEdge(String first, Object[] storage1, String second, Object[] storage2) {
    addEdge(new Edge(new Vertex(first, storage1), new Vertex(second, storage2)));
  }

  /**
   * Adds a Vertex.htrsf
   * 
   * @param v the Vertex to add
   */
  public void addVertex(Vertex v) {
    addEdge(v.getName(), "");
  }

  /**
   * Removes an edge from this MsnGraph.
   * 
   * @param v1 the first Vertex
   * @param v2 the second Vertex
   */
  public void removeEdge(Vertex v1, Vertex v2) {
    edges.remove(new Edge(v1, v2));
  }

  /**
   * Removes a Vertex in this MsnGraph.
   * 
   * @param v the Vertex
   */
  public void removeVertex(Vertex v) {
    edges.forEach(edge -> {
      if (edge.getVertex1().equals(v)) {
        edge.setVertex1(new Vertex(""));
      } else if (edge.getVertex2().equals(v)) {
        edge.setVertex2(new Vertex(""));
      }
    });
    fix();
  }

  /**
   * (WIP) Removes a subdivision within this MsnGraph.
   * 
   * @param v the Vertex
   */
  public void removeSubdivision(Vertex v) {
    if (degreeOf(v) == 2) {
      Edge[] e = edgesFor(v);
      for (Edge edge : e) {
        // TODO
      }
    }
  }

  /**
   * Determines if the Vertices passed are a walk in the current MsnGraph.
   * 
   * @param vertices the vertices
   * @return whether the vertices passed create a walk
   */
  public boolean isWalk(Vertex... vertices) {
    if (vertices.length == 1)
      return false;
    for (int i = 0; i < vertices.length; i++) {
      try {
        if (!containsEdge(new Edge(vertices[i], vertices[i + 1])))
          return false;
      } catch (IndexOutOfBoundsException e) {
      }
    }
    return true;
  }

  /**
   * Determines if the Vertices passed are a path in the current MsnGraph.
   * 
   * @param vertices the vertices
   * @return whether the sequence of vertices is a path or not
   */
  public boolean isPath(Vertex... vertices) {
    if (isWalk(vertices)) {
      ArrayList<Vertex> v = new ArrayList<>(List.of(vertices));
      if (v.get(0).equals(v.get(v.size() - 1))) {
        v.remove(0);
        v.remove(v.size() - 1);
      }
      return !Msn.containsDups(v.toArray(Vertex[]::new));
    }
    return false;
  }

  /**
   * (WIP) Determines if the Vertices passed are a trail in the current MsnGraph.
   * 
   * @param vertices the vertices
   * @return whether the sequence of vertices is a trail or not
   */
  public boolean isTrail(Vertex... vertices) {
    if (isWalk(vertices))
      return !Msn.containsDups(vertices);
    return false;
  }

  /**
   * Determines if the Vertices passed are a circuit in the current MsnGraph.
   * 
   * @param vertices the vertices
   * @return whether the sequence of vertices is a circuit or not
   */
  public boolean isCircuit(Vertex... vertices) {
    return isPath(vertices) && vertices[0].equals(vertices[vertices.length - 1]);
  }

  /**
   * Determines if the Vertices passed are a cycle in the current MsnGraph.
   * 
   * @param vertices the vertices
   * @return whether the sequence of vertices is a cycle or not
   */
  public boolean isCycle(Vertex... vertices) {
    return isCircuit(vertices) && !Msn.containsDups(vertices);
  }

  /**
   * Determines if the current MsnGraph is complete.
   * 
   * @return whether the graph is complete or not
   */
  public boolean isComplete() {
    Vertex[] vs = gatherVertices();
    for (Vertex v : vs)
      if (degreeOf(v) < vs.length - 1)
        return false;
    return true;
  }

  /**
   * (WIP) Determines whether this MsnGraph is connected.
   * 
   * Verifies that every Vertex has a degree of at least 2.
   * 
   * @return whether this MsnGraph is connected or not
   */
  public boolean isConnected() {
    for (Vertex v : gatherVertices())
      if (degreeOf(v) > 2)
        return false;
    return true;
  }

  /**
   * (WIP) Determines whether this MsnGraph can be drawn as planar.
   * 
   * @return whether this MsnGraph can be drawn as planar or not
   */
  public boolean isPlanar() {
    return false;
  }

  /**
   * Removes all cycles that exist in this MsnGraph.
   */
  public void removeCycles() {
    for (Vertex v : gatherVertices())
      removeCycle(v);
  }

  /**
   * Removes a cycle to and from the Vertex passed.
   * 
   * Does nothing if a cycle does not exist
   * 
   * @param startandfinish the starting and ending Vertex in this MsnGraph
   */
  public void removeCycle(Vertex startandfinish) {
    int start = -1;
    for (int i = 0; i < edges.size(); i++)
      if (edges.get(i).getVertex1().equals(startandfinish)) {
        start = i;
        break;
      }
    int finish = -1;
    if (start != -1)
      for (int i = start + 1; i < edges.size(); i++)
        if (edges.get(i).getVertex1().equals(startandfinish))
          finish = i;
    if (finish != -1)
      for (int i = edges.size() - 1; i >= 0; i--)
        if (i < finish && i >= start)
          removeEdge(edges.get(i).getVertex1(), edges.get(i).getVertex2());
  }

  /**
   * Calculates the degree of this MsnGraph.
   * 
   * @return the degree
   */
  public int degree() {
    return 2 * edges.size();
  }

  /**
   * (WIP) Determines if a path exists from the first Vertex to the second.
   * 
   * @param v1 the first Vertex
   * @param v2 the second Vertex
   * @return whether a path exists from the first Vertex to the second.
   */
  public boolean pathExistsFrom(Vertex v1, Vertex v2) {
    return false;
  }

  private boolean existshelper(Vertex[] vertices, Vertex v1, Vertex v2, Vertex current, int count) {
    if (current.equals(v2)) {
      return true;
    }
    if (count > vertices.length) {
      return false;
    }


    return false;

  }

  /**
   * (WIP) Finds the closest path from the first Vertex to the second.
   * 
   * @param v1 the first Vertex
   * @param v2 the second Vertex
   * @return the closest path (if any) from Vertex v1 to Vertex v2
   */
  public Edge[] closestPathFrom(Vertex v1, Vertex v2) {
    return null;
  }

  /**
   * (WIP) Gets the subgraphs that exist in this MsnGraph.
   * 
   * @return the subgraphs
   */
  public MsnGraph[] getComponents() {
    return null;
  }

  /**
   * Gets all Edges that incorporate the Vertex passed.
   * 
   * @param v the Vertex
   * @return the Edges
   */
  public Edge[] edgesFor(Vertex v) {
    ArrayList<Edge> edg = new ArrayList<>();
    edges.forEach(edge -> {
      if (edge.containsVertex(v))
        edg.add(edge);
    });
    return edg.toArray(Edge[]::new);
  }

  /**
   * Gets the Edges in this MsnGraph.
   * 
   * @return the Edges
   */
  public ArrayList<Edge> edges() {
    return edges;
  }

  /**
   * Collects existing vertices in this MsnGraph.
   * 
   * @return the vertices
   */
  public Vertex[] gatherVertices() {
    HashSet<Vertex> vertices = new HashSet<>();
    edges.forEach(e -> {
      if (!e.getVertex1().getName().equals(""))
        vertices.add(e.getVertex1());
      if (!e.getVertex2().getName().equals(""))
        vertices.add(e.getVertex2());
    });
    return vertices.toArray(Vertex[]::new);
  }

  /**
   * Provides visualization of the current MsnGraph.
   */
  public void visualize() {
    StdDraw.setCanvasSize(600, 600);
    StdDraw.setXscale(0, 600);
    StdDraw.setYscale(0, 600);
    StdDraw.clear(Color.black);
    StdDraw.setPenRadius(0.004);
    StdDraw.setPenColor(Color.white);
    draw();
    StdDraw.show();
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        while (true)
          if (StdDraw.isMousePressed()) {
            draw();
            Thread.sleep(200);
          }
      };
    };
    worker.execute();
  }

  /**
   * Allows for visualization of any sequence of vertices, this function does not analyze or alter
   * the current MsnGraph.
   * 
   * @param vertices the vertices to visualize
   */
  public void visualize(Vertex... vertices) {
    new MsnGraph(convert(vertices)).visualize();
  }

  /**
   * Calculates the number of faces in this MsnGraph.
   * 
   * @return the number of faces
   */
  public int calculateFaces() {
    return edges.size() + 2 - gatherVertices().length;
  }

  /**
   * Draws this MsnGraph using StdDraw.
   */
  public void draw() {
    StdDraw.clear(Color.black);
    StdDraw.setPenColor(Color.white);
    HashMap<Vertex, Integer[]> positions = new HashMap<>();
    for (Vertex v : gatherVertices())
      positions.put(v, new Integer[] {Msn.randomInt(50, 550), Msn.randomInt(50, 550)});
    positions.entrySet().forEach(en -> {
      StdDraw.circle(en.getValue()[0], en.getValue()[1], 15);
      StdDraw.text(en.getValue()[0], en.getValue()[1] - 24, en.getKey().getName());
    });
    edges.forEach(e -> {
      Color c = Msn.randomColor();
      while (Msn.brightness(c) < 150)
        c = Msn.randomColor();
      StdDraw.setPenColor(c);
      if (!e.getVertex1().equals(e.getVertex2())) {
        try {
          StdDraw.line(positions.get(e.getVertex1())[0], positions.get(e.getVertex1())[1],
              positions.get(e.getVertex2())[0], positions.get(e.getVertex2())[1]);
        } catch (NullPointerException e1) {
        }
      } else {
        StdDraw.circle(positions.get(e.getVertex1())[0], positions.get(e.getVertex1())[1], 10);
      }
    });
  }

  /**
   * Determines if the current MsnGraph contains the Edge passed.
   * 
   * @param e the Edge
   * @return whether this MsnGraph contains the Edge passed or not
   */
  public boolean containsEdge(Edge e) {
    return edges.contains(e);
  }

  /**
   * Determines if the current MsnGraph contains the Vertex passed.
   * 
   * @param v the Vertex
   * @return whether this MsnGraph contains the Vertex passed or not
   */
  public boolean containsVertex(Vertex v) {
    for (Edge e : edges)
      if (e.containsVertex(v))
        return true;
    return false;
  }

  /**
   * Calculates the degree of the Vertex passed.
   * 
   * @param v the Vertex
   * @return the degree
   */
  public int degreeOf(Vertex v) {
    return vertexFreq(v);
  }

  /**
   * Counts the frequency of the Vertex passed.
   * 
   * @param v the Vertex to count
   * @return the number of times the Vertex passed exists
   */
  public int vertexFreq(Vertex v) {
    int count = 0;
    for (Edge e : edges)
      if (e.containsVertex(v))
        count++;
    return count;
  }

  /**
   * Converts this MsnGraph to a multimap of vertices.
   * 
   * @return a multimap representation
   */
  public MsnMultimap<Vertex, Vertex> toMultimap() {
    MsnMultimap<Vertex, Vertex> map = new MsnMultimap<>();
    edges.forEach(edge -> {
      map.put(edge.getVertex1(), edge.getVertex2());
      map.put(edge.getVertex2(), edge.getVertex1());
    });
    return map;
  }

  /**
   * (WIP) Converts this MsnGraph to its matrix representation.
   * 
   * @return a matrix
   */
  public int[][] toMatrix() {
    Vertex[] v = gatherVertices();
    int[][] mat = new int[v.length][v.length];
    return mat;
  }

  /**
   * Converts an array of Vertices to an array of Edges.
   * 
   * @param vertices the Vertices
   * @return an Edge array
   */
  public static Edge[] convert(Vertex... vertices) {
    ArrayList<Edge> edges = new ArrayList<>();
    for (int i = 0; i < vertices.length; i++)
      try {
        edges.add(new Edge(vertices[i], vertices[i + 1]));
      } catch (IndexOutOfBoundsException e) {
        edges.add(new Edge(vertices[i], new Vertex("")));
      }
    return edges.toArray(Edge[]::new);
  }

  /**
   * Iterates over the Edges in this MsnGraph.
   */
  @Override
  public Iterator<Edge> iterator() {
    return edges.iterator();
  }

  /**
   * Prints information on the current MsnGraph.
   */
  public void information() {
    String s = "vertices: " + gatherVertices().length + "\n";
    s += "edges: " + edges.size() + "\n";
    s += "degree: " + degree() + "\n";
    s += "is complete: " + isComplete();
    Msn.printboxed(s);
  }

  /**
   * String representation.
   */
  public String toString() {
    return edges.toString();
  }

  /**
   * Randomizes this MsnGraph.
   * 
   * @param vertices the amount of vertices
   */
  public void randomize(int vertices) {
    HashSet<Edge> randomized = new HashSet<>();
    Vertex[] vert = new Vertex[vertices];
    for (int i = 0; i < vertices; i++) {
      int rand = Msn.randomInt(0, 10000);
      while (hasName(String.valueOf(rand), vert))
        rand = Msn.randomInt(0, 10000);
      vert[i] = new Vertex(String.valueOf(rand));
      randomized.add(new Edge(vert[i], new Vertex("")));
    }
    for (int i = 0; i < Msn.randomInt(vertices, vertices + 11); i++)
      if (Msn.diceroll(vertices + 3))
        randomized.add(new Edge(Msn.randomElement(vert), new Vertex("")));
      else
        randomized.add(new Edge(Msn.randomElement(vert), Msn.randomElement(vert)));
    edges = new ArrayList<>(randomized);
    fix();
  }

  // -----------------------------------------------------------

  private boolean hasName(String name, Vertex... vertices) {
    for (int i = 0; i < vertices.length; i++)
      try {
        if (name.equals(vertices[i].getName()))
          return true;
      } catch (Exception e) {
        return false;
      }
    return false;
  }

  private boolean isSingle(Edge e) {
    return e.getVertex1().getName().equals("") || e.getVertex2().getName().equals("");
  }

  private void fix() {
    for (int i = edges.size() - 1; i >= 0; i--) {
      Edge e = edges.get(i);
      if (isSingle(e)) {
        Vertex single = null;
        if (e.getVertex1().getName().equals(""))
          single = e.getVertex2();
        else
          single = e.getVertex1();
        if (degreeOf(single) > 1)
          edges.remove(e);
      }
    }
  }

  /**
   * Contains two Vertices.
   * 
   * @author Mason Marker
   */
  public static class Edge {

    Vertex v1;
    Vertex v2;
    ArrayList<Object> storage;

    public Edge() {
      v1 = null;
      v2 = null;
    }

    public Edge(Vertex v1, Vertex v2) {
      this.v1 = v1;
      this.v2 = v2;
    }

    public Edge(Object... objects) {
      storage = new ArrayList<>(List.of(objects));
    }
    
    public ArrayList<Object> getStorage() {
      return storage;
    }
    
    public void setVertex1(Vertex v) {
      this.v1 = v;
    }

    public Vertex getVertex1() {
      return v1;
    }

    public void setVertex2(Vertex v) {
      this.v2 = v;
    }

    public Vertex getVertex2() {
      return v2;
    }

    public boolean containsVertex(Vertex v) {
      return v1.equals(v) || v2.equals(v);
    }

    public String toString() {
      return "{" + v1 + ", " + v2 + "}";
    }

    @Override
    public final int hashCode() {
      int res = 17;
      res = 31 * res + v1.hashCode();
      res = 31 * res + v2.hashCode();
      return res;
    }

    @Override
    public boolean equals(Object o) {
      Edge e = (Edge) o;
      return containsVertex(e.getVertex1()) && containsVertex(e.getVertex2());
    }
  }

  /**
   * Vertex in a MsnGraph.
   * 
   * @author Mason Marker
   */
  public static class Vertex {

    String name;
    ArrayList<Object> storage;

    public Vertex(String name) {
      this.name = name;
      storage = new ArrayList<>();
    }

    public Vertex(String name, Object... objects) {
      this.name = name;
      storage = new ArrayList<>(List.of(objects));
    }

    public ArrayList<Object> getStorage() {
      return storage;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public String toString() {
      if (storage.isEmpty())
        return name;
      return name + ":" + storage;
    }

    @Override
    public final int hashCode() {
      return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
      Vertex v = (Vertex) o;
      return name.equals(v.name) && Arrays.equals(v.getStorage().toArray(), storage.toArray());
    }
  }
}
