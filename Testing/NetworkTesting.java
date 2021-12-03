import MsnLib.Msn;

public class NetworkTesting {

  public static void main(String[] args) {

    Network n = new Network(2, 2, 2, 1);

    double[] possible = NetworkUtilities.getPossible(15);
    
    Msn.pa(possible);

  }

}
