public class MsnTesting {

  public static void main(String[] args) throws Exception {

    Network network = new Network(3, 2, 2, 1);
    
    double[][] data = {{3, 7, 4}, {2, 8, 9}};
    double[] targets = {.1, .73};
    
    network.bulkTrain(data, targets, 100000);
    
    System.out.println(network.getAnswer(new double[] {2, 8, 9}));
    
    
    
    

  }
}
