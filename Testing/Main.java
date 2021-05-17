import java.io.FileNotFoundException;
import java.util.stream.Stream;

public class Main {

  public static void main(String[] args) throws FileNotFoundException {
    
    double[][] inputs = {{0, 0, 1, 0}, {1, 1, 0, 0}, {1, 0, 0, 1}, {0, 0, 0, 0}, {1, 1, 1, 1}};
    double[] targets = {0, .33, .66, .66, 1};
    
    Network n = new Network(4, 2, 5, 1);
    
    n.bulkTrain(inputs, targets, 10000);
    
    
    
  }

}
