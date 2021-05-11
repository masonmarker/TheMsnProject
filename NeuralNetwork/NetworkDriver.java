
public class NetworkDriver {

  public static void main(String[] args) {
    Network network = new Network(2, 2, 1);
    
    double[] inputs = {1, 1};
    double target = 1;
    
    double[] moreinputs = {1, 0};
    double othertarget = 0;
    
    
    for (int i = 0; i < 1000; i++) {
      network.forward(inputs).backward(target);
    }
    System.out.println(network.getNeurons()[4]);

    for (int i = 0; i < 10000; i++) {
      network.forward(moreinputs).backward(othertarget);
    }
    
    System.out.println(network.getNeurons()[4]);


    
  }

}
