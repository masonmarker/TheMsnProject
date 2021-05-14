public class MsnTesting {

  public static void main(String[] args) throws Exception {

    double[][] inputs = Msn.create2DDoubleArray(5, 3);
    double[] targets = {0, .1, .2, .3, .4, .5};

    Msn.pa(inputs);

    NetworkUtilities.findBest(inputs, targets, 2500000, true);

  }
}
