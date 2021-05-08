import java.util.Arrays;

class FeedForwardProgram {
  public static void main(String[] args) {
    try {
      System.out.println("\nBegin neural network feed-forward demo\n");

      System.out.println("Creating a 3-input, 4-hidden, 2-output NN");
      System.out.println("Using log-sigmoid function");

      int numInput = 3;
      int numHidden = 4;
      int numOutput = 2;

      Network nn = new Network(numInput, numHidden, numOutput);

      double[] weights = new double[] {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2,
          -2.0, -6.0, -1.0, -7.0, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, -2.5, -5.0};

      System.out.println("\nWeights and biases are:");
      Msn.pa(weights);

      System.out.println("Loading neural network weights and biases");
      nn.SetWeights(weights);

      System.out.println("\nSetting neural network inputs:");
      double[] xValues = new double[] {2.0, 3.0, 4.0};
      Msn.pa(xValues);

      System.out.println("Loading inputs and computing outputs\n");
      double[] yValues = nn.ComputeOutputs(xValues);

      System.out.println("\nNeural network outputs are:");
      Msn.pa(yValues);

      System.out.println("\nEnd neural network demo\n");
    } catch (Exception ex) {
      System.out.println("error");

    }
  }


}


class Network {
  private int numInput;
  private int numHidden;
  private int numOutput;

  private double[] inputs;
  private double[][] ihWeights; // input-to-hidden
  private double[] ihBiases;
  private double[][] hoWeights; // hidden-to-output
  private double[] hoBiases;
  private double[] outputs;

  public Network(int numInput, int numHidden, int numOutput) {
    inputs = new double[numInput];
    ihWeights = new double[numInput][numHidden];
    ihBiases = new double[numHidden];
    hoWeights = new double[numHidden][numOutput];
    hoBiases = new double[numOutput];
    outputs = new double[numOutput];
    this.numInput = numInput;
    this.numHidden = numHidden;
    this.numOutput = numOutput; 
  }

  public void SetWeights(double[] weights) throws Exception {
    int k = 0; // Points into weights param

    for (int i = 0; i < numInput; ++i)
      for (int j = 0; j < numHidden; ++j)
        ihWeights[i][j] = weights[k++];

    for (int i = 0; i < numHidden; ++i)
      ihBiases[i] = weights[k++];

    for (int i = 0; i < numHidden; ++i)
      for (int j = 0; j < numOutput; ++j)
        hoWeights[i][j] = weights[k++];

    for (int i = 0; i < numOutput; ++i)
      hoBiases[i] = weights[k++];
  }

  public double[] ComputeOutputs(double[] xValues) throws Exception {
    double[] ihSums = new double[numHidden]; // Scratch
    double[] ihOutputs = new double[numHidden];
    double[] hoSums = new double[numOutput];

    for (int i = 0; i < xValues.length; ++i) // xValues to inputs
      this.inputs[i] = xValues[i];

    System.out.println("input-to-hidden weights:");
    Msn.pa(this.ihWeights);

    for (int j = 0; j < numHidden; ++j) // Input-to-hidden weighted sums
      for (int i = 0; i < numInput; ++i)
        ihSums[j] += inputs[i] * ihWeights[i][j];

    System.out.println("input-to-hidden sums before adding i-h biases:");
    Msn.pa(ihSums);

    System.out.println("input-to-hidden biases:");
    Msn.pa(ihBiases);

    for (int i = 0; i < numHidden; ++i) // Add biases
      ihSums[i] += ihBiases[i];

    System.out.println("input-to-hidden sums after adding i-h biases:");
    Msn.pa(ihSums);

    for (int i = 0; i < numHidden; ++i) // Input-to-hidden output
      ihOutputs[i] = Msn.sigmoid(ihSums[i]);

    System.out.println("input-to-hidden outputs after log-sigmoid activation:");
    Msn.pa(ihOutputs);

    System.out.println("hidden-to-output weights:");
    Msn.pa(hoWeights);

    for (int j = 0; j < numOutput; ++j) // Hidden-to-output weighted sums
      for (int i = 0; i < numHidden; ++i)
        hoSums[j] += ihOutputs[i] * hoWeights[i][j];

    System.out.println("hidden-to-output sums before adding h-o biases:");
    Msn.pa(hoSums);

    System.out.println("hidden-to-output biases:");
    Msn.pa(hoBiases);

    for (int i = 0; i < numOutput; ++i) // Add biases
      hoSums[i] += hoBiases[i];

    System.out.println("hidden-to-output sums after adding h-o biases:");
    Msn.pa(hoSums);

    for (int i = 0; i < numOutput; ++i) // Hidden-to-output result
      outputs[i] = Msn.sigmoid(hoSums[i]);

    double[] result = new double[numOutput]; // Copy to this.outputs
    result = Arrays.copyOf(outputs, outputs.length);

    return result;
  }
} // Class
