public class MsnTesting {

  public static void main(String[] args) {

    String[][] array = Msn.create2DStringArray(10, 10);
    
    Msn.pa(array);
    
    int[] coord = {5, 5};
    
    System.out.println(array[coord[0]][coord[1]]);
    System.out.println(Msn.directionalMulti(array, coord, "ne", 15, true));
    
  }

}
