import java.util.Scanner;

public class Demonstration {

  public static void main(String[] args) {

    Network n = new Network(3, 3, 2, 1);

    System.out.println("Network Visualization");
    System.out.println("---------------------");
    System.out.println("O -> Neuron");
    System.out.println();

    for (int i = 0; i < n.inputs(); i++)
      System.out.print("O     ");
    System.out.print("input");
    System.out.println();
    for (int i = 0; i < n.hiddenLayers(); i++) {
      for (int j = 0; j < n.neuronsPerHiddenLayer(); j++)
        System.out.print("O     ");
      System.out.print("hidden");
      System.out.println();
    }
    for (int i = 0; i < n.outputs(); i++)
      System.out.print("O     ");
    System.out.print("output");
    System.out.println();
    System.out.println();
    double[][] inputs = {{2.0, 6.0, 1.0}, {9.0, 0.0, 0.0}, {3.0, 6.0, 5.0}, {0.0, 0.0, 0.0}};
    double[] targets = {0.2, 0.9, 0.3, 0.0};

    System.out.println("Training inputs (input per input Neuron): ");
    Msn.pa(inputs);
    System.out.println();
    System.out.println(
        "For each set of inputs, we'll set\nthe target answer to the first value\nof the inputs divided by 10");
    System.out.println();
    System.out
        .println("For example, the inputs [2.0, 6.0, 1.0] will have\na target answer of 0.2, etc.");
    System.out.println();

    Scanner kb = new Scanner(System.in);
    while (true) {
      System.out.print("How many training iterations? ");
      int times = kb.nextInt();
      System.out.println("training...");
      n.bulkTrain(inputs, targets, times);
      System.out.println("Training Results");
      System.out.println("----------------");
      System.out.println(
          "input: [2.0, 6.0, 1.0]: Target: " + targets[0] + "      Network says: " + n.getAnswer(inputs[0]));
      System.out.println(
          "input: [9.0, 0.0, 0.0]: Target: " + targets[1] + "      Network says: " + n.getAnswer(inputs[1]));
      System.out.println(
          "input: [3.0, 6.0, 5.0]: Target: " + targets[2] + "      Network says: " + n.getAnswer(inputs[2]));
      System.out.println(
          "input: [0.0, 0.0, 0.0]: Target: " + targets[3] + "      Network says: " + n.getAnswer(inputs[3]));
    }
  }
}
