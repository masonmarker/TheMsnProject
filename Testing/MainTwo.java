import MsnLib.Msn;

public class MainTwo {



  public static void main(String[] args) {

    int[] array = Msn.createIntArray(10);
    
    Msn.pa(array);
    System.out.println(max(array));

  }


  public static int max(int[] array) {
    if (array.length > 0) {
      int max = array[0];
      for (int i = 0; i < array.length; i++)
        if (max < array[i])
          max = array[i];
      return max;
    }
    return Integer.MIN_VALUE;
  }



}
