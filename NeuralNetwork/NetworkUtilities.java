import java.util.TreeMap;

/**
 * Network Utilities class, offers extended capabilities for the Network class.
 * 
 * @author Mason Marker
 * @version 1.0 - 05/14/2021
 */
public class NetworkUtilities {

  /**
   * Finds the most optimal amount of Layers and Neurons per Layer for the data given.
   * 
   * @param inputs the inputs
   * @param targets the targets
   * @return the most optimal Network
   * @throws Exception thrown if the number of iterations is too low and the Networks tested are
   *         unable to resolve to all of the targets
   */
  public static Network findBest(double[][] inputs, double[] targets, int trainingIterations,
      boolean printNetworkAttributes) throws Exception {
    TreeMap<Integer, Network> map = new TreeMap<>();
    Timer t = new Timer();
    for (int i = 2; i < 6; i++) {
      for (int j = inputs[0].length; j >= 2; j--) {
        Network n = new Network(inputs[0].length, i, j, 1);
        t.start();
        n.bulkTrain(inputs, targets, trainingIterations);
        t.stop();
        if (verify(n, inputs, targets))
          map.put(t.runtime(), n);
      }
    }
    if (map.isEmpty())
      throw new Exception(
          "Verification failed, not enough training iterations: All Network combinations could not resolve to the correct targets");
    if (printNetworkAttributes) {
      System.out.println();
      System.out.println("Time(ms)=Network Attributes");
      System.out.println(map);
      System.out.println();
      System.out.println("Optimal Network Information for " + inputs.length + " inputs");
      System.out.println(trainingIterations + " training iterations");
      System.out.println("------------------------------------------");
      System.out.println("Inputs: " + map.firstEntry().getValue().inputs());
      System.out.println("Hidden Layers: " + map.firstEntry().getValue().hiddenLayers());
      System.out.println(
          "Neurons per hidden Layer: " + map.firstEntry().getValue().neuronsPerHiddenLayer());
      System.out.println("Outputs: " + map.firstEntry().getValue().outputs());
    }
    return map.firstEntry().getValue();
  }

  private static boolean verify(Network n, double[][] data, double[] tgts) {
    for (int i = 0; i < data.length; i++)
      if (Msn.closestTo(n.getAnswer(data[i]), tgts) != tgts[i])
        return false;
    return true;
  }

}
