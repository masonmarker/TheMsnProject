import MsnLib.Msn;
import MsnStructures.MsnGraph;

class Main {

  public static void main(String[] args) {

    MsnGraph g = new MsnGraph();
        
    int[][] state1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, -1}};
    int[][] state2 = {{}, {}, {}};
    
    
    g.addEdge(Msn.toString2D(state1), Msn.toString2D(state1));
    g.addEdge("second", "third");
    g.addEdge("third", "fourth");
    g.addEdge("fourth", "first");

    g.visualize();
    
    
    
    
    
    
    
  }


}
