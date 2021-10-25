import java.awt.Point;
import MsnLib.Msn;

public class MsnTesting {

  public static void main(String[] args) {

    String[][] arr = Msn.create2DStringArray(10, 10);
    
    Msn.pa(arr);
    Point rand = Msn.randomIndex(arr);
    System.out.println(rand);
    System.out.println(arr[(int) rand.getX()][(int) rand.getY()]);
    System.out.println("above:");
    Point[] above = Msn.pointsNw(arr, Msn.coord(rand));
    
    System.out.println(above.length);
    Msn.pa(above);
    
  }
 
}
