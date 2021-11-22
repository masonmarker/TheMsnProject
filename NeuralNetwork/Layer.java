
/**
   * A single Layer of Neurons.
   * 
   * @author Mason Marker
   * @version 1.0
   */
   public class Layer {

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