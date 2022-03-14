import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import MsnLib.Msn;

/**
 * Trains a Neural Network to add two numbers without any mathematical operations.
 * 
 * @author Mason Marker
 * @version 1.0 - 05/17/2021
 */
public class LearnToAdd {

  /**
   * @param args cmd args
   */
  public static void main(String[] args) {

    System.out.println("Running initial iterations...");
    System.out.println("(Average time to develop a successful Network: 500000 iterations)");

    Network network = new Network(2, 3, 2, 1);

    int iterations = 0;

    while (true) {
      double[] d = new double[2];
      for (int k = 0; k < 10000; k++) {
        double i = 0;
        double j = 0;
        while (i < .20) {
          j = 0;
          while (j < .20) {
            d[0] = Msn.decFormat(i, 2);
            d[1] = Msn.decFormat(j, 2);
            network.train(d, Msn.decFormat(i + j, 2));
            j += .01;
          }
          i += .01;
        }
      }

      iterations += 10000;

      HashMap<Map.Entry<Double, Double>, Double> map = new HashMap<>();
      double i = 0;
      double j = 0;
      i = 0;
      j = 0;
      double[] d2 = new double[2];
      while (i < .20) {
        j = 0;
        while (j < .20) {
          d2[0] = Msn.decFormat(i, 2);
          d2[1] = Msn.decFormat(j, 2);
          map.put(Map.entry(d2[0], d2[1]), network.getAnswer(d2));
          j += .01;
        }
        i += .01;
      }

      int count = 0;
      for (Entry<Entry<Double, Double>, Double> sum : map.entrySet()) {
        if (Msn.decFormat(sum.getKey().getKey() + sum.getKey().getValue(), 2) != Msn
            .decFormat(sum.getValue(), 2)) {
          count++;
          System.out.println(sum.getKey().getKey() + " + " + sum.getKey().getValue() + " = "
              + Msn.decFormat(sum.getValue(), 2) + ": " + count + " incorrect");
        }
      }
      System.out.println();
      System.out.println("Iterations: " + iterations);
      System.out.println("Trials: 400");
      double score = Msn.decFormat(((400.0 - count) / 400) * 100, 2);
      System.out.println("Network score: " + score + "%");
      if (score == 100)
        break;
      System.out.println();
      System.out.println("Running 10000 more iterations...");
    }
    System.out.println("Network successfully trained to add two numbers from 0.00 - 0.20!");
  }

}
