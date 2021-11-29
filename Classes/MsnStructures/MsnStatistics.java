package MsnStructures;

import java.util.Collection;
import MsnLib.Msn;

public class MsnStatistics {

  private double[] array;

  public MsnStatistics(double[] array) {
    this.array = array;
  }

  public MsnStatistics(int[] array) {
    this.array = Msn.toDouble(array);
  }

  public MsnStatistics(Collection<Double> collection) {
    array = Msn.toDouble(Msn.toInt(collection.toArray()));
  }
  
  public double getMax() {
    return Msn.max(array);
  }
  
  public double getMin() {
    return Msn.min(array);
  }
  
  public double getAvg() {
    return Msn.avg(array);
  }
  
  public double getFirst() {
    return array[0];
  }
  
  public double getLast() {
    return array[array.length - 1];
  }
  
  public double[] getArray() {
    return array;
  }

}
