
/**
 * Neuron class.
 * 
 * @author Mason Marker
 * @version 1.0
 */
public class Neuron {

  public boolean value;
  public boolean isEven;
  public double bias;
  
  public Neuron() {
    bias = 5.0;
  }
  
  public double getBias() {
    return bias;
  }
  
  public boolean getValue() {
    return value;
  }
  
}
