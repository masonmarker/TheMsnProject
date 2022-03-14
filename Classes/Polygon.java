import MsnStructures.MsnGraph;
import MsnStructures.MsnGraph.Edge;
import MsnStructures.MsnGraph.Vertex;

/**
 * Gathers information of a polygon.
 * 
 * @author Mason Marker
 * @version 1.0 - 01/01/2022
 */
public class Polygon {

  private int sides;
  private double anglesum;
  private double eachangle;

  /**
   * Constructor.
   * 
   * @param angles the amount of angles in the Polygon
   */
  public Polygon(int sides) {
    if (sides < 3)
      throw new IllegalArgumentException("Polygon must have 3 or more sides, you said " + sides);
    this.sides = sides;
    anglesum = (sides - 2) * 180;
    eachangle = anglesum / sides;
  }

  public int sides() {
    return sides;
  }

  public double sumOfAngles() {
    return anglesum;
  }

  public double eachAngle() {
    return eachangle;
  }

  /**
   * Obtains an MsnGraph of this Polygon.
   * 
   * @return an MsnGraph
   */
  public MsnGraph graph() {
    MsnGraph g = new MsnGraph();
    Vertex[] v = new Vertex[sides];
    for (int i = 0; i < v.length; i++)
      v[i] = new Vertex("" + i);
    Edge[] edges = MsnGraph.convert(v);
    for (Edge e : edges)
      g.addEdge(e);
    g.addEdge(v[0], v[v.length - 1]);
    return g;
  }

  /**
   * Gets the name of this Polygon.
   * 
   * @return the proper name of this Polygon
   */
  public String name() {
    switch (sides) {
      case 3:
        return "triangle";
      case 4:
        return "square";
      case 5:
        return "pentagon";
      case 6:
        return "hexagon";
      case 7:
        return "heptagon";
      case 8:
        return "octagon";
      case 9:
        return "nonagon"; 
      case 10:
        return "decagon";
      case 11:
        return "hendecagon";
      case 12:
        return "dodecagon";
    }
    return sides + "-gon";
  }
}
