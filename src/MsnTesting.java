public class MsnTesting {

  public static void main(String[] args) {

    Network n = new Network(4, 3, 3, 1);
    
    double[][] inputs = {{0, 0, 0, 0}, {0, 0, 1, 0}, {0, 1, 0, 0}, {1, 0, 0, 0}};
    double[] targets = {0, .3, .6, 1};
    
    n.train(inputs[0], targets[0]);
    n.train(inputs[0], targets[0]);

    n.train(inputs[0], targets[0]);

    n.train(inputs[1], targets[1]);

    
    System.out.println(n.getAnswer(new double[] {0, 0, 0, 0}));
    
  }
}
