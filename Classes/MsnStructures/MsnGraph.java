package MsnStructures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Stream;
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

  public MsnGraph() {
    edges = new ArrayList<>();
  }

  public MsnGraph(Edge... edges) {
    this.edges = new ArrayList<>(List.of(edges));
  }

  public void addEdge(Edge e) {
    edges.add(e);
    fix();
  }

  public void addEdge(Vertex v1, Vertex v2) {
    addEdge(new Edge(v1, v2));
  }

  public void addEdge(String first, String second) {
    addEdge(new Edge(new Vertex(first), new Vertex(second)));
  }

  public void addEdge(String first, Object[] storage1, String second, Object[] storage2) {
    addEdge(new Edge(new Vertex(first, storage1), new Vertex(second, storage2)));
  }

  public void addVertex(Vertex v) {
    addEdge(v.getName(), "");
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
   * Determines if the Vertices passed are a walk in the current MsnGraph.
   * 
   * @param vertices the vertices
   * @return whether the sequence of vertices is a path or not
   */
  public boolean isPath(Vertex... vertices) {
    if (isWalk(vertices)) {
      ArrayList<Vertex> v = new ArrayList<>(List.of(vertices));
      v.remove(0);
      v.remove(v.size() - 1);
      LinkedHashSet<Vertex> rem = new LinkedHashSet<>(v);
      return isWalk(rem.toArray(Vertex[]::new));
    }
    return false;
  }

  /**
   * Determines if the Vertices passed are a trail in the current MsnGraph.
   * 
   * @param vertices the vertices
   * @return whether the sequence of vertices is a trail or not
   */
  public boolean isTrail(Vertex... vertices) {
    if (isWalk(vertices)) {
      Edge[] edg = convert(vertices);
      return !Msn.containsDups(vertices);
    }
    return false;
  }

  /**
   * Determines if the Vertices passed are a circuit in the current MsnGraph.
   * 
   * @param vertices the vertices
   * @return whether the sequence of vertices is a circuit or not
   */
  public boolean isCircuit(Vertex... vertices) {
    return isWalk(vertices) && vertices[0].equals(vertices[vertices.length - 1]);
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

  public ArrayList<Edge> edges() {
    return edges;
  }

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

  public void visualize(Vertex... vertices) {
    MsnGraph g = new MsnGraph(convert(vertices));
    g.visualize();
  }

  public void draw() {
    StdDraw.clear(Color.black);
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

  public boolean containsEdge(Edge e) {
    return edges.contains(e);
  }

  public boolean containsVertex(Vertex v) {
    for (Edge e : edges)
      if (e.containsVertex(v))
        return true;
    return false;
  }

  public int degreeOf(Vertex v) {
    return vertexFreq(v);
  }

  public int vertexFreq(Vertex v) {
    int count = 0;
    for (Edge e : edges)
      if (e.containsVertex(v))
        count++;
    return count;
  }

  public void fix() {
    Edge torem = null;
    for (int i = 0; i < edges.size(); i++) {
      Edge e = edges.get(i);
      if (isSingle(e)) {
        Vertex single = null;
        if (e.getVertex1().getName().equals(""))
          single = e.getVertex2();
        else
          single = e.getVertex1();
        if (degreeOf(single) > 1)
          torem = e;
      }
    }
    edges.remove(torem);
  }

  public boolean isSingle(Edge e) {
    return e.getVertex1().getName().equals("") || e.getVertex2().getName().equals("");
  }

  @Override
  public Iterator<Edge> iterator() {
    return edges.iterator();
  }

  public String toString() {
    return edges.toString();
  }

  /**
   * Randomizes this graph.
   * 
   * @param vertices the amount of vertices
   */
  public void randomize(int vertices) {
    ArrayList<Edge> randomized = new ArrayList<>();
    Vertex[] vert = new Vertex[vertices];
    for (int i = 0; i < vertices; i++) {
      char rand = Msn.randomLetter();
      while (hasName(String.valueOf(rand), vert)) {
        rand = Msn.randomLetter();
      }

      vert[i] = new Vertex(String.valueOf(rand));
      randomized.add(new Edge(vert[i], new Vertex("")));
    }

    for (int i = 0; i < Msn.randomInt(vertices, vertices + 11); i++) {
      if (Msn.diceroll(10)) {
        randomized.add(new Edge(Msn.randomElement(vert), new Vertex("")));
      } else {
        randomized.add(new Edge(Msn.randomElement(vert), Msn.randomElement(vert)));
      }
    }



    edges = randomized;
  }

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

  /**
   * Converts a list of vertices to edges.
   * 
   * @param vertices the vertices
   * @return
   */
  private Edge[] convert(Vertex... vertices) {
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
   * Contains two Vertices.
   * 
   * @author Mason Marker
   */
  public static class Edge {

    Vertex v1;
    Vertex v2;

    public Edge() {
      v1 = null;
      v2 = null;
    }

    public Edge(Vertex v1, Vertex v2) {
      this.v1 = v1;
      this.v2 = v2;
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
