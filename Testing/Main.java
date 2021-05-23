import java.io.FileNotFoundException;
import java.util.stream.Stream;

public class Main {

  public static void main(String[] args) throws FileNotFoundException {

    double[][] inputs = {{0, 0, 1, 0, 0, 0, 0, 1}, {1, 1, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 1, 0, 0, 0, 1}, {0, 0, 0, 0, 0, 0, 0, 1}, {1, 1, 1, 1, 0, 0, 0, 1}};
    double[] targets = {0, .33, .66, .66, 1};

    Network n = new Network(8, 2, 5, 1);

    n.train(inputs[0], targets[0], 10000);

    System.out.println(n.getAnswer(inputs[0]));

  }

}
