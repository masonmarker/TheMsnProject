
public class NetworkTesting {

  public static void main(String[] args) {

    Network n = new Network(2, 2, 2, 1);

    double a = n.getAnswer(new double[] {0, 1});
    
    System.out.println(NetworkUtilities.interpret(a, 15));

  }

}
