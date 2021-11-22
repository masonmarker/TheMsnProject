import java.util.Arrays;

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
      bias = NetworkUtilities.weight();
      output = 0;
      error = 0;
      weights = new double[weightcount];
      for (int i = 0; i < weights.length; i++)
        weights[i] = NetworkUtilities.weight();
    }

    /**
     * Neuron constructor.
     * 
     * @param bias the bias of this Neuron
     * @param weights the weights
     * @param output the output value
     * @param error the error value
     */
    public Neuron(double bias, double[] weights, double output, double error) {
      this.bias = bias;
      this.output = output;
      this.error = error;
      this.weights = weights;
    }

    /**
     * Activates this Neuron using the Sigmoid -Logistic function.
     * 
     * @param weightsum the weighted sum calculation
     */
    public void activate(double weightsum) {
      output = NetworkUtilities.sigmoid(weightsum);
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
