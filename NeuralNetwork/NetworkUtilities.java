import java.util.Random;
import java.util.TreeMap;
import javax.swing.SwingWorker;
import MsnLib.Msn;

/**
 * Network Utilities class, offers extended capabilities for the Network class.
 * 
 * @author Mason Marker
 * @version 1.0 - 05/14/2021
 */
public class NetworkUtilities {

  public NetworkUtilities() {}

  /**
   * (WIP) Saves the information stored within a Network to the path specified.
   * 
   * @param n the Network to save
   * @param the path of the savefile
   */
  public void save(Network n, String path) {
    String write = "" + n.getLearningRate() + " lr\n";
    write += "" + (n.getHiddenLayers().length + 2) + " layers\n";
    write += n.getInputLayer().getNeurons().length + " neurons in input\n";
    for (Neuron non : n.getInputLayer().getNeurons()) {
      write += "" + non.getBias() + " " + non.getError() + " " + non.getOutput() + "\n";
      write += "" + non.getWeights().length + " weights\n";
      for (int i = 0; i < non.getWeights().length; i++) {
        if (i != non.getWeights().length) {
          write += "" + non.getWeights()[i] + " ";
        } else {
          write += "" + non.getWeights()[i] + "\n";
        }
      }
    }
  }

  /**
   * (WIP) Loads a Network from the path specified.
   * 
   * @param path the path of the Network
   * @return the read Network
   */
  public Network load(String path) {
    return null;
  }

  /**
   * Calculates an an estimate of hidden Neurons that should exist given the number of samples in
   * the training set to prevent over-fitting.
   * 
   * @param inputs the amount of inputs
   * @param outputs the amount of outputs
   * @param samples the number of samples
   * @return an estimate of hidden Neurons
   */
  public static int calculateHidden(int inputs, int outputs, int samples) {
    return (int) (((double) samples) / (2 * (inputs + outputs)));
  }

  /**
   * Creates a double array containing valid output possibilities from 0-1.
   * 
   * @param possible the amount of possible outputs
   * @return the possible Network outputs
   */
  public static double[] getPossible(int possible) {
    double[] d = new double[possible];
    for (int i = 0; i < d.length; i++)
      d[i] = Msn.decFormat((1.0 / possible) * i, 4);
    return d;
  }

  /**
   * Performs a Sigmoid-Logistic operation on the number specified.
   * 
   * @param d the number
   * @return the number after applying the sigmoid function
   * @since 0.1.5.2.7
   */
  public static double sigmoid(double d) {
    return 1 / (1 + Math.exp(-d));
  }

  /**
   * Calculates an initial weight value for a neural network.
   * 
   * @param inputs the number of input neurons
   * @param outputs the number of output neurons
   * @return an initial weight value
   * @since 0.1.5.2.7
   */
  public static double weight(int inputs, int outputs) {
    return new Random().nextGaussian() * (Math.sqrt(2.0 / (inputs + outputs)));
  }

  /**
   * Calculates an initial weight value for a neural network.
   * 
   * @return an initial weight value
   * @since 0.1.5.2.8
   */
  public static double weight() {
    return .5 - Math.random();
  }

  /**
   * Calculates the dot product of the given weights and outputs, and appends the bias to the
   * calculation.
   * 
   * @param weights the weights
   * @param outputs the outputs
   * @param bias the bias
   * @return the weighted sum
   * @since 0.1.5.2.8
   */
  public static double weightedSum(double[] weights, double[] outputs, double bias) {
    double sum = 0;
    for (int i = 0; i < weights.length; i++)
      sum += weights[i] * outputs[i];
    return sum + bias;
  }

  /**
   * Interprets the output of an output Neuron of the Network class.
   * 
   * @param output the output
   * @return the interpreted output
   */
  public static int interpret(double output, int decplaces) {
    if (output == 1)
      return 1;
    else if (output == 0)
      return 0;
    else {
      if (decplaces > 8)
        decplaces = 8;
      String s = String.valueOf(Msn.decFormat(output, decplaces));
      String fix = "";
      for (int i = 2; i < s.length(); i++)
        fix += s.charAt(i);
      return Integer.valueOf(fix);
    }
  }

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

    System.out.println("[*] Finding best Network for " + inputs.length + " inputs via "
        + trainingIterations + " training iterations...");
    System.out.println("[*] Estimated trials: " + (inputs.length * 6));
    TreeMap<Integer, Network> map = new TreeMap<>();
    Timer t = new Timer();
    for (int i = 2; i < 8; i++) {
      System.out.println("[*] Running Network with " + i + " Layers and:");
      for (int j = inputs[0].length; j >= 2; j--) {
        System.out.println(j + " Neurons per Layer");
        Network n = new Network(inputs[0].length, i, j, 1);
        t.start();
        n.bulkTrain(inputs, targets, trainingIterations);
        t.stop();
        if (verify(n, inputs, targets)) {
          map.put(t.runtime(), n);
          String s = "[+] Possible Network found!\n";
          s += n;
          System.out.println(Msn.boxed(s));
        }
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
