import MsnStructures.MsnGraph;
import MsnStructures.MsnGraph.Edge;
import MsnStructures.MsnGraph.Vertex;

public class Main {

  public static void main(String[] args) {    
    MsnGraph graph = new MsnGraph();


    Vertex a = new Vertex("a");
    Vertex b = new Vertex("b");
    Vertex c = new Vertex("c");
    Vertex d = new Vertex("d");
    Vertex e = new Vertex("e");
    Vertex f = new Vertex("f");
    Vertex g = new Vertex("g");
    Vertex h = new Vertex("h");


    Edge e1 = new Edge(a, b);
    Edge e2 = new Edge(b, c);
    Edge e3 = new Edge(c, a);
 
    graph.addEdge(a, b);
    graph.addEdge(b, c);
    graph.addEdge(b, f);
    graph.addEdge(c, d);
    graph.addEdge(d, e);
    graph.addEdge(f, e);
   
    System.out.println(graph.isPath(c, d, e, f, b, c));
          
    graph.visualize();   
  }
}
