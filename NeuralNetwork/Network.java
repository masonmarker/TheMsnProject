import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for Neural Network capabilities.
 * 
 * To train this Network, run the forward and backward methods in unison to create a decision making
 * path for multiple inputs and desired outputs.
 *
 * CURRENTLY ONLY WORKS WITH ONE OUTPUT NEURON!
 * 
 * @author Mason Marker
 * @version 1.0 - 05/13/2021
 */
public class Network {

  private double learningrate;
  private Layer inputLayer;
  private Layer[] hiddenLayers;
  private Layer outputLayer;

  /**
   * Network constructor.
   * 
   * @param inputs the amount of input Neuron
   * @param hiddenLayerCount the amount of hidden Layers
   * @param hiddenPerLayer the amount of hidden Neuron per hidden Layer
   * @param outputs the amount of output Neuron
   */
  public Network(int inputs, int hiddenLayerCount, int hiddenPerLayer, int outputs) {
    if (outputs != 1)
      throw new IllegalArgumentException(
          "The current version of this Network does not support more than 1 output Neuron");
    ArrayList<Layer> Layers = new ArrayList<>();
    int index = 0;
    learningrate = 0.8;
    inputLayer = new Layer(new Neuron[inputs], index);
    Layers.add(inputLayer);
    index++;
    for (int i = 0; i < inputLayer.getNeurons().length; i++)
      inputLayer.getNeurons()[i] = new Neuron(hiddenPerLayer);
    hiddenLayers = new Layer[hiddenLayerCount];
    for (int i = 0; i < hiddenLayers.length; i++) {
      hiddenLayers[i] = new Layer(new Neuron[hiddenPerLayer], index);
      Layers.add(hiddenLayers[i]);
      index++;
      for (int j = 0; j < hiddenLayers[i].getNeurons().length; j++)
        hiddenLayers[i].getNeurons()[j] = new Neuron(hiddenPerLayer);
    }
    outputLayer = new Layer(new Neuron[outputs], index);
    Layers.add(outputLayer);
    index++;
    for (int i = 0; i < outputLayer.getNeurons().length; i++)
      outputLayer.getNeurons()[i] = new Neuron(hiddenPerLayer);
    Layer[] all = Layers.toArray(new Layer[Layers.size()]);
    inputLayer.setLayers(all);
    for (Layer l : hiddenLayers)
      l.setLayers(all);
    outputLayer.setLayers(all);
  }

  /**
   * Forward propogation, attempts to calculate an answer for the given inputs while referencing
   * previous inputs.
   * 
   * @param inputs the inputs
   * @return this Network
   */
  public Network forward(double[] inputs) {
    for (int i = 0; i < inputLayer.getNeurons().length; i++)
      inputLayer.getNeurons()[i].setOutput(inputs[i]);
    for (int i = 0; i < hiddenLayers.length; i++) {
      for (int j = 0; j < hiddenLayers[i].getNeurons().length; j++) {
        if (i == 0)
          hiddenLayers[i].getNeurons()[j]
              .activate(Msn.weightedSum(hiddenLayers[i].getNeurons()[j].getWeights(),
                  inputLayer.getOutputs(), hiddenLayers[i].getNeurons()[j].getBias()));
        else
          hiddenLayers[i].getNeurons()[j]
              .activate(Msn.weightedSum(hiddenLayers[i].getNeurons()[j].getWeights(),
                  hiddenLayers[i - 1].getOutputs(), hiddenLayers[i].getNeurons()[j].getBias()));
      }
    }
    for (int i = 0; i < outputLayer.getNeurons().length; i++)
      outputLayer.getNeurons()[i].activate(Msn.weightedSum(outputLayer.getNeurons()[i].getWeights(),
          hiddenLayers[hiddenLayers.length - 1].getOutputs(),
          outputLayer.getNeurons()[i].getBias()));
    return this;
  }

  /**
   * Backwards propogation, traverses backwards through the Network, changing weights and biases of
   * each Neuron according to how "wrong" the Network's answer was using the means squared error
   * function.
   * 
   * Use this method only after using forwards().
   * 
   * @param target the target for the current inputs
   * @return this Network with corrected weights and biases
   */
  public Network backward(double target) {
    for (int i = 0; i < outputLayer.getNeurons().length; i++) {
      outputLayer.getNeurons()[i].setError(
          (target - outputLayer.getNeurons()[i].getOutput()) * outputLayer.getNeurons()[i].dx());
      outputLayer.getNeurons()[i].setBias(outputLayer.getNeurons()[i].getBias()
          + learningrate * outputLayer.getNeurons()[i].getError());
      for (int j = 0; j < outputLayer.before().getNeurons().length; j++)
        outputLayer.getNeurons()[i].getWeights()[j] = outputLayer.getNeurons()[i].getWeights()[j]
            + learningrate * outputLayer.getNeurons()[i].getError()
                * outputLayer.before().getNeurons()[j].getOutput();
    }
    for (int i = hiddenLayers.length - 1; i >= 0; i--) {
      for (int j = 0; j < hiddenLayers[i].getNeurons().length; j++) {
        hiddenLayers[i].getNeurons()[j].setError(
            (outputLayer.getNeurons()[0].getWeights()[1] * outputLayer.getNeurons()[0].getError())
                * hiddenLayers[i].getNeurons()[j].dx());
        hiddenLayers[i].getNeurons()[j].setBias(hiddenLayers[i].getNeurons()[j].getBias()
            + learningrate * hiddenLayers[i].getNeurons()[j].getError());
        for (int k = 0; k < hiddenLayers[i].getNeurons()[j].getWeights().length; k++) {
          hiddenLayers[i].getNeurons()[j].getWeights()[k] =
              hiddenLayers[i].getNeurons()[j].getWeights()[k]
                  + learningrate * hiddenLayers[i].getNeurons()[j].getError()
                      * hiddenLayers[i].before().getNeurons()[k].getOutput();
          hiddenLayers[i].getNeurons()[j].getWeights()[k] =
              hiddenLayers[i].getNeurons()[j].getWeights()[k]
                  + learningrate * hiddenLayers[i].getNeurons()[j].getError()
                      * hiddenLayers[i].before().getNeurons()[k].getOutput();
        }
      }
    }
    return this;
  }

  /**
   * Gets the output for the specified inputs. This method must be ran after training for valid
   * results.
   * 
   * @return the estimate of this Network for the inputs given
   */
  public double getAnswer(double[] inputs) {
    return forward(inputs).outputLayer.getNeurons()[0].getOutput();
  }

  /**
   * Trains this Network for one iteration of inputs.
   * 
   * @param input the inputs
   * @param target the target answer
   * @return this Network after training
   */
  public Network train(double[] inputs, double target) {
    if (inputs.length != inputs())
      throw new IllegalArgumentException(
          "inputs[] length must be equal to the number of inputs in this Network");
    forward(inputs).backward(target);
    return this;
  }

  /**
   * Trains this Network of a single set of inputs over a specified amount of iterations.
   * 
   * WARNING: Using this method too often can skew data, use bulkTrain() for multiple sets of data
   * at one time.
   * 
   * @param input the input values
   * @param target the target value
   * @param times the amount of iterations
   * @return this Network after training
   */
  public Network train(double[] inputs, double target, int times) {
    for (int i = 0; i < times; i++)
      train(inputs, target);
    return this;
  }

  /**
   * Trains this Network with the given inputs and outputs.
   * 
   * Works well with large quantities of data, as training data individually can result in skewed
   * data.
   * 
   * @param inputs the inputs
   * @param times the amount of training iterations
   * @param targets the targets
   * @return this Network
   */
  public Network bulkTrain(double[][] inputs, double[] targets, int times) {
    for (int i = 0; i < times; i++)
      for (int j = 0; j < inputs.length; j++)
        train(inputs[j], targets[j]);
    return this;
  }

  /**
   * Gets the learning rate of this Network.
   * 
   * @return the learning rate
   */
  public double getLearningRate() {
    return learningrate;
  }

  /**
   * Sets the learning rate of this Network.
   * 
   * @param learningrate the new learning rate
   */
  public void setLearningRate(double learningrate) {
    this.learningrate = learningrate;
  }

  /**
   * Gets the total amount of neural layers in this Network.
   * 
   * @return the total layers
   */
  public int layers() {
    return 1 + hiddenLayers() + 1;
  }

  /**
   * Gets the number of input Neurons.
   * 
   * @return the amount of inputs
   */
  public int inputs() {
    return inputLayer.getNeurons().length;
  }

  /**
   * The amount of hidden layers
   * 
   * @return the hidden layers
   */
  public int hiddenLayers() {
    return hiddenLayers.length;
  }

  /**
   * The amount of Neurons per hidden Layer.
   * 
   * @return Neurons per hidden layer
   */
  public int neuronsPerHiddenLayer() {
    return hiddenLayers[0].getNeurons().length;
  }

  /**
   * The amount of output Neurons.
   * 
   * @return the amount of outputs
   */
  public int outputs() {
    return outputLayer.getNeurons().length;
  }

  /**
   * Gets the input Layer.
   * 
   * @return the input Layer
   */
  public Layer getInputLayer() {
    return inputLayer;
  }

  /**
   * Gets the hidden Layers.
   * 
   * @return the hidden Layers
   */
  public Layer[] getHiddenLayers() {
    return hiddenLayers;
  }

  /**
   * Gets the output Layer.
   * 
   * @return the output Layer
   */
  public Layer getOutputLayer() {
    return outputLayer;
  }

  public String toString() {
    return "Network: Inputs: " + inputs() + " Hidden Layers: " + hiddenLayers()
        + " Neurons per hidden: " + neuronsPerHiddenLayer() + " Outputs: " + outputs();
  }

  /**
   * A single Layer of Neurons.
   * 
   * @author Mason Marker
   * @version 1.0
   */
  class Layer {

    private Neuron[] neurons;
    private Layer[] Layers;
    private int index;

    /**
     * Layer constructor.
     * 
     * @param neurons the Neurons to be added
     * @param index the index of this Layer in the entire Network
     */
    public Layer(Neuron[] neurons, int index) {
      this.neurons = neurons;
      this.index = index;
    }

    /**
     * Gets the outputs for every Neuron in this Layer.
     * 
     * @return the outputs
     */
    public double[] getOutputs() {
      double[] d = new double[neurons.length];
      for (int i = 0; i < d.length; i++)
        d[i] = neurons[i].getOutput();
      return d;
    }

    /**
     * Stores all Layers, allows for proper Layer iteration when propogating.
     * 
     * @param Layers the Layers to add
     */
    public void setLayers(Layer[] Layers) {
      this.Layers = Layers;
    }

    /**
     * The Layer before this one in the Network.
     * 
     * @return the Layer before
     */
    public Layer before() {
      return Layers[index - 1];
    }

    /**
     * Gets all Neurons in this Layer
     * 
     * @return
     */
    public Neuron[] getNeurons() {
      return neurons;
    }
  }

  /**
   * Neuron class, stores all data for weights and biases.
   * 
   * @author Mason Marker
   * @version 1.0
   */
  class Neuron {

    private double bias;
    private double[] weights;
    private double output;
    private double error;

    /**
     * Neuron constructor.
     * 
     * @param weightcount the amount of weights to store in this Neuron.
     */
    public Neuron(int weightcount) {
      bias = Msn.weight();
      output = 0;
      error = 0;
      weights = new double[weightcount];
      for (int i = 0; i < weights.length; i++)
        weights[i] = Msn.weight();
    }

    /**
     * Activates this Neuron using the Sigmoid -Logistic function.
     * 
     * @param weightsum the weighted sum calculation
     */
    public void activate(double weightsum) {
      output = Msn.sigmoid(weightsum);
    }

    /**
     * Calculates the derivative for the output of this Neuron.
     * 
     * @return thge derivative
     */
    public double dx() {
      return output * (1 - output);
    }

    /**
     * Gets the current bias stored in this Neuron.
     * 
     * @return the bias / threshold
     */
    public double getBias() {
      return bias;
    }

    /**
     * Sets the bias.
     * 
     * @param bias the bias
     */
    public void setBias(double bias) {
      this.bias = bias;
    }

    /**
     * Gets all weights for this Neuron.
     * 
     * @return the weights
     */
    public double[] getWeights() {
      return weights;
    }

    /**
     * Gets the output for this Neuron.
     * 
     * @return the output
     */
    public double getOutput() {
      return output;
    }

    /**
     * Sets the output for this Neuron.
     * 
     * @param output the output
     */
    public void setOutput(double output) {
      this.output = output;
    }

    /**
     * Gets the error for this Neuron.
     * 
     * @return the error
     */
    public double getError() {
      return error;
    }

    /**
     * Sets the error for this Neuron.
     * 
     * @param error the error
     */
    public void setError(double error) {
      this.error = error;
    }

    /**
     * String representation of this Neuron.
     */
    public String toString() {
      String s = "";
      s += "Neuron:\n";
      s += "Bias / Threshold: " + bias + "\n";
      s += "Weights: " + Arrays.toString(weights) + "\n";
      s += "Output: " + output + "\n";
      s += "Error: " + error + "\n";
      return s;
    }
  }
}
