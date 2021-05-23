
public class NetworkTesting {

  public static void main(String[] args) {

    Network n = new Network(8, 2, 6, 1);
    n.setLearningRate(3);

    Timer t = new Timer();

    double[] inputs = new double[8];
    for (int i = 0; i < 100; i++) {
      inputs = Msn.createDoubleArray(8);
      double target = 0.5592589743;
      t.start();
      n.train(inputs, target, 100);
      t.stop();
    }

    System.out.println("Network answer (target is 0.56): " + n.getAnswer(inputs));
    System.out.println("Time: " + t.getAvgRuntime());

  }

}
