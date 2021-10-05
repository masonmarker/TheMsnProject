import MsnLib.Msn;

public class SpeedTesting {


  public static void main(String[] args) {
    Timer t = new Timer();

    String[][] matrix = Msn.create2DStringArray(10, 10);

    Msn.pa(matrix);
    
    System.out.println();
    
    Msn.pa(Msn.arraycopy(matrix));

    t.printHistory();
  }

  public static void method1(Timer t, String[][] matrix) {

  }

  public static void method2(Timer t, String[][] matrix) {

  }

}
