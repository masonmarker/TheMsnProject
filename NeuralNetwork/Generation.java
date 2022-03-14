import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A 'Generation' is a collection of Networks that are all given the same inputs, however may
 * generate different outputs.
 * 
 * @author Mason Marker
 * @version 1.0 - 01/02/2022
 */
public class Generation implements Iterable<MsnNetwork> {

  private MsnNetwork[] ns;

  /**
   * Constructor for a Generation of MsnNetworks.
   * 
   * @param inputs the inputs
   * @param children the amount of children networks
   * @param outputs the String outputs (not needed if outputs are quantitative)
   */
  public Generation(int inputs, int children, String... outputs) {
    ns = new MsnNetwork[children];
    for (int i = 0; i < ns.length; i++)
      ns[i] = new MsnNetwork("network #" + i, inputs, outputs);
  }

  /**
   * Trains this collection of Networks.
   * 
   * @param inputs the input values
   * @param target the target
   * @param times the amount of times to train this generation of Networks
   */
  public void train(double[] inputs, String target, int times) {
    for (MsnNetwork n : ns)
      n.train(inputs, target, times);
  }
  
  /**
   * Trains this collection of Networks.
   * 
   * @param inputs the input values
   * @param target the target
   * @param times the amount of times to train this generation of Networks
   */
  public void train(double[] inputs, double target, int times) {
    for (MsnNetwork n : ns)
      n.train(inputs, target, times);
  }

  /**
   * Gets the answers from each child MsnNetwork.
   * 
   * @param inputs the inputs
   * @return a HashMap consisting of each MsnNetwork and its String answer
   */
  public HashMap<MsnNetwork, String> answers(double[] inputs) {
    HashMap<MsnNetwork, String> map = new HashMap<>();
    for (MsnNetwork n : ns)
      map.put(n, n.getAnswer(inputs));
    return map;
  }

  /**
   * Gets the answers from each child MsnNetwork.
   * 
   * This method does not use closestTo()
   * 
   * @param inputs the inputs
   * @return a HashMap consisting of each MsnNetwork and its double answer
   */
  public HashMap<MsnNetwork, Double> answersByValue(double[] inputs) {
    HashMap<MsnNetwork, Double> map = new HashMap<>();
    for (MsnNetwork n : ns)
      map.put(n, n.getAnswerByValue(inputs));
    return map;
  }

  /**
   * Gets the MsnNetworks in this Generation.
   * 
   * @return the MsnNetworks in this Generation
   */
  public MsnNetwork[] networks() {
    return ns;
  }

  @Override
  public Iterator<MsnNetwork> iterator() {
    return new ArrayList<MsnNetwork>(List.of(ns)).iterator();
  }
}
