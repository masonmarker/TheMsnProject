import java.util.Arrays;

public class Neuron {

  private String layer;
  private double bias;
  private double[] weights;
  private double output;
  private double error;

  public Neuron(String layer, int weightcount) {
    this.layer = layer;
    bias = Msn.weight();
    output = 0;
    error = 0;
    weights = new double[weightcount];
    for (int i = 0; i < weights.length; i++)
      weights[i] = Msn.weight();
  }

  public void activate(double weightsum) {
    output = Msn.sigmoid(weightsum);
  }

  public double dx() {
    return output * (1 - output);
  }
  
  public String getLayer() {
    return layer;
  }

  public double getBias() {
    return bias;
  }

  public void setBias(double bias) {
    this.bias = bias;
  }

  public double[] getWeights() {
    return weights;
  }

  public double getOutput() {
    return output;
  }

  public void setOutput(double output) {
    this.output = output;
  }

  public double getError() {
    return error;
  }

  public void setError(double error) {
    this.error = error;
  }

  public String toString() {
    String s = "";
    s += "Neuron:\n";
    s += "Layer: " + layer + "\n";
    s += "Bias / Threshold: " + bias + "\n";
    s += "Weights: " + Arrays.toString(weights) + "\n";
    s += "Output: " + output + "\n";
    s += "Error: " + error + "\n";
    return s;
  }

}
