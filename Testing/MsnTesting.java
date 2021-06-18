import java.awt.Color;

public class MsnTesting {

  public static void main(String[] args) {
        
    String[][] array = Msn.create2DStringArray(10, 10);
    
    Msn.pa(array);
    
    System.out.println();
    String[][] copy = Msn.toString(Msn.arraycopy(array));
    
    
  }

}
