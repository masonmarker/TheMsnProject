import java.awt.Point;
import java.util.Scanner;
import MsnLib.Msn;

class Main {
  public static void main(String[] args) {
    int count = 0;
    for (int i = 0; i < 10; i++) {
      if (i < 5) {
        count += i;
      }
    }
    System.out.println(count);
  }

  public static double distance(int[] from, int[] to) {
    return pyth(Math.max(Math.abs(from[0]), to[0]) - Math.min(from[0], to[0]),
        Math.max(Math.abs(from[1]), to[1]) - Math.min(from[1], to[1]));
  }

  public static double pyth(double a, double b) {
    return Math.sqrt(a * a + b * b);
  }
}

