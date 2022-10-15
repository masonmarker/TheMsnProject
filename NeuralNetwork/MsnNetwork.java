import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import MsnLib.Msn;

/**
 * Simplifies the Network training and reading process.
 * 
 * @author Mason Marker
 * @version 1.0 - 01/04/2022
 */
public class MsnNetwork {

  private String name;
  private HashMap<String, Double> op;
  private double[] possible;
  private Network n;

  // for setting learning rate
  public static final double SLOWEST_LEARNING = 0.1;
  public static final double SLOW_LEARNING = 0.3;
  public static final double MEDIOCRE_LEARNING = 0.6;
  public static final double FAST_LEARNING = 0.8;
  public static final double FASTEST_LEARNING = 1;

  /**
   * Constructor for a Network for scenarios where data has not already been gathered.
   * 
   * Network defaults to 2 hidden Layers, each comprising of 'input' amount of Neurons per Layer.
   * 
   * @param name the common name for this MsnNetwork
   * @param inputs the amount of inputs
   * @param outputs the amount of outputs
   */
  public MsnNetwork(String name, int inputs, String[] outputs) {
    this.name = name;
    op = new HashMap<>();
    n = new Network(inputs, 2, inputs, 1);
    possible = NetworkUtilities.getPossible(outputs.length);
    for (int i = 0; i < possible.length; i++)
      op.put(outputs[i], possible[i]);
  }

  /**
   * Constructor for a Network for scenarios where data has already been gathered.
   * 
   * Constructs Networks' Layers and hidden Neurons per Layer using the number of samples.
   * 
   * @param name the common name for this MsnNetwork
   * @param samples the number of samples already gathered
   * @param inputs the amount of inputs
   * @param outputs the amount of outputs
   */
  public MsnNetwork(String name, int samples, int inputs, String[] outputs) {
    this.name = name;
    op = new HashMap<>();
    int neurons = NetworkUtilities.calculateHidden(inputs, outputs.length, samples);
    n = new Network(inputs, 3, neurons / 3, 1);
    possible = NetworkUtilities.getPossible(outputs.length);
    for (int i = 0; i < possible.length; i++)
      op.put(outputs[i], possible[i]);
  }

  /**
   * Sets the learning rate of this MsnNetwork.
   * 
   * Use final values of this wrapper class for learning rate simplicity
   * 
   * @param rate the new learning rate
   */
  public void setLearningRate(double rate) {
    n.setLearningRate(rate);
  }

  /**
   * Allows for setting of this MsnNetwork's Layer count as well as Neurons per Layer.
   * 
   * Note that this method resets all internals of this MsnNetwork.
   * 
   * @param layers the amount of Layers
   * @param perlayer the amount of Neurons per Layer
   */
  public void setNeurons(int layers, int perlayer) {
    int inputs = n.inputs();
    n = new Network(inputs, layers, perlayer, 1);
  }

  /**
   * Trains this MsnNetwork for the given data set "times" amount of times.
   * 
   * @param inputs the amount of inputs
   * @param target the String target value
   * @param times the number of training iterations
   */
  public void train(double[] inputs, String target, int times) {
    n.train(inputs, op.get(target), times);
  }

  /**
   * Trains this MsnNetwork for the given data set "times" amount of times.
   * 
   * @param inputs the amount of inputs
   * @param target the double target value
   * @param times the number of training iterations
   */
  public void train(double[] inputs, double target, int times) {
    n.train(inputs, target, times);
  }

  /**
   * Retrieves this MsnNetwork's answer for the given inputs, in its String representation.
   * 
   * @param inputs the inputs
   * @return this Network's output in its String representation
   */
  public String getAnswer(double[] inputs) {
    return outputName(Msn.closestTo(n.getAnswer(inputs), possible));
  }

  /**
   * Calculates the confidence that this Network will retrieve the correct target.
   * 
   * @param inputs the inputs
   * @param target the target value
   * @return a double 0 - 1
   */
  public double percentError(double[] inputs, String target) {
    double t = op.get(target);
    double e = n.getAnswer(inputs);

    if (t == 0 || e == 0) {
      return Msn.decFormat(Math.abs(e - t), 3);
    }

    return Msn.decFormat((Math.abs(e - t) / Math.abs(t)) * 100, 3);
  }

  /**
   * Gets the answer for this MsnNetwork in its double value.
   * 
   * This method does not run closestTo()
   * 
   * @param inputs the inputs
   * @return the double value of this MsnNetwork's answer
   */
  public double getAnswerByValue(double[] inputs) {
    return n.getAnswer(inputs);
  }

  /**
   * Prints the answer of this Network for the given inputs with details.
   * 
   * @param inputs the input values
   */
  public void printAnswer(double[] inputs, String expectedoptional) {
    System.out.println("-----------");
    System.out.println("expecting " + expectedoptional);
    System.out.println(name + " received " + Arrays.toString(inputs));
    System.out.println(name + " returned " + getAnswer(inputs) + " with a "
        + percentError(inputs, expectedoptional) + "% error");
    System.out.println("-----------");
  }

  /**
   * Gets the name of the output from its double value.
   * 
   * @param d the output's double value
   * @return the name of the output
   */
  private String outputName(double d) {
    for (Map.Entry<String, Double> e : op.entrySet())
      if (e.getValue().equals(d))
        return e.getKey();
    return null;
  }

  /**
   * Gets the name of this MsnNetwork.
   * 
   * @return the common name of this MsnNetwork
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the non-simplified Network hosted by this MsnNetwork.
   * 
   * @return the main Network
   */
  public Network getNetwork() {
    return n;
  }

  /**
   * Gets the String representation of this MsnNetwork.
   */
  public String toString() {
    return name + "\n" + op.toString() + "\n" + n.toString();
  }


}
