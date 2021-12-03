import java.util.TreeMap;
import MsnLib.Msn;

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
    
    TreeMap<Double, Character> map = new TreeMap<>();
    for (int i = 0; i < possible.length; i++)
      map.put(possible[i], alph[i]);
    

  }



  public static class Main {
    public static void main(String[] args) {
      new AlphabeticalOrder();
    }
  }
}


