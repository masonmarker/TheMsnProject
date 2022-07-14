import MsnLib.Msn;

public class OtherTesting {

  public static void main(String[] args) {

    int[][] a1 = Msn.create2DIntArray(3, 3);
    int[][] a2 = Msn.create2DIntArray(3, 3);
    int[][] a3 = Msn.create2DIntArray(3, 3);

    System.out.println("---------");
    Msn.pa(a1);
    System.out.println("max wealth: " + maximumWealth(a1));
    System.out.println("---------");
    Msn.pa(a2);
    System.out.println("max wealth: " + maximumWealth(a2));
    System.out.println("---------");
    Msn.pa(a3);
    System.out.println("max wealth: " + maximumWealth(a3));
    System.out.println("---------");


  }

  public static int maximumWealth(int[][] accounts) {
    if (Msn.validCoord(accounts, new int[] {0, 0})) {
      int richest = accounts[0][0];
      for (int i = 0; i < accounts.length; i++) {
        int currentWealth = Msn.sum(accounts[i]);
        if (currentWealth > richest)
          richest = currentWealth;
      }
      return richest;
    }
    return Integer.MIN_VALUE;
  }


}
