import java.util.Map;
import java.util.Scanner;
import javax.swing.SwingWorker;
import MsnLib.Msn;
import MsnStructures.MsnMap;

/**
 * Watch a Neural Network attempt to compute a letter after the one introduced.
 * 
 * @author Mason Marker
 * @version 1.0 - 12/02/2021
 */
public class AlphabeticalOrder {

  Network network = new Network(2, 3, 2, 1);

  public AlphabeticalOrder() {

    double[] possible = NetworkUtilities.getPossible(26);
    char[] alph = Msn.alphabetArray();

    MsnMap<Double, Character> map = new MsnMap<>();
    map.toHashMap();
    for (int i = 0; i < possible.length; i++)
      map.put(possible[i], alph[i]);

    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

      @Override
      protected Void doInBackground() throws Exception {
        while (true) {
          for (Map.Entry<Double, Character> entry : map.entrySet()) {
            try {
              network.train(new double[] {0, ((int) (char) entry.getValue())},
                  map.keyFor((char) (((int) (char)entry.getValue()) + 1)), 10000);
            } catch (Exception e) {

            }
          }
        }
      }
    };
    worker.execute();


    System.out.println(map);

    Scanner kb = new Scanner(System.in);
    char letter = '?';
    while (letter != 'x' && letter != 'X') {

      letter = ((String) Msn.prompt("type a letter: ", kb)).charAt(0);
      double answer = network.getAnswer(new double[] {0, (int) letter});
      System.out.println(answer);
      System.out.println("network says next letter is " + map.get(Msn.decFormat(answer, 4)));

    }


  }



  public static class Main {
    public static void main(String[] args) {
      new AlphabeticalOrder();
    }
  }
}


