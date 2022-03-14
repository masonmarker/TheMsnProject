import java.util.Scanner;
import javax.swing.SwingWorker;
import MsnLib.Msn;

/**
 * (EXPERIMENTAL)
 * 
 * Attempts to teach Neural Networks how to convert binary strings to decimal.
 * 
 * @author Mason Marker
 * @version 1.0 - 01/04/2022
 */
public class LearnBinary {

  public static void main(String[] args) {

    MsnNetwork n = new MsnNetwork("binary knowledgable network", 3, new String[3]);
    Generation g = new Generation(3, 5, new String[3]);
    n.setLearningRate(0.9);
    n.setNeurons(2, 3);


    double[][] inputs = {doublearr("000"), doublearr("001"), doublearr("010"), doublearr("011"),
        doublearr("100"), doublearr("101"), doublearr("110"), doublearr("111")};

    double[] targets = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0};

    try {
      NetworkUtilities.findBest(inputs, targets, 10000000, true);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

      @Override
      protected Void doInBackground() throws Exception {
        while (true) {
          Timer t = new Timer();
          t.start();
          for (int i = 0; i < 8; i++) {
            String bin = Binary.formatBinary(Binary.binaryStringFrom(i), 3);
            int ans = (int) (Msn.round(n.getAnswerByValue(doublearr(bin)), 2) * 100);

            if (ans != new Binary(bin).toInteger()) {
              n.train(doublearr(Binary.formatBinary(Binary.binaryStringFrom(i), 3)),
                  (i * 1.0) / 100, 10);
            } else {

            }
          }


        }
      }

    };
    worker.execute();

    Scanner s = new Scanner(System.in);
    while (true) {
      String answer = Binary.formatBinary("" + Msn.prompt("type a 3 bit binary string: ", s), 3);
      int netans = (int) (Msn.round(n.getAnswerByValue(doublearr(answer)), 2) * 100);
      System.out.println("network says " + netans);
    }
  }


  public static double[] doublearr(String s) {
    double[] d = new double[s.length()];
    for (int i = 0; i < s.length(); i++)
      d[i] = Integer.parseInt("" + s.charAt(i));
    return d;
  }

}
