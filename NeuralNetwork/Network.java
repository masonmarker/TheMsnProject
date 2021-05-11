import java.util.Arrays;
import java.util.stream.IntStream;

public class Network {

  private double learningrate;
  private int ipn;
  private int hn;
  private int opn;
  private Neuron[] neurons;

  public Network(int inputs, int hidden, int outputs) {
    learningrate = .8;
    ipn = inputs;
    hn = hidden;
    opn = outputs;
    neurons = new Neuron[inputs + hidden + outputs];
    IntStream.range(0, ipn).forEach(i -> neurons[i] = new Neuron("ip", hn));
    IntStream.range(ipn, ipn + hn).forEach(i -> neurons[i] = new Neuron("hn", hn));
    IntStream.range(ipn + hn, ipn + hn + opn).forEach(i -> neurons[i] = new Neuron("op", hn));
  }

  public Neuron[] getNeurons() {
    return neurons;
  }

  public Network forward(double[] input) {
    for (int i = 0; i < neurons.length; i++) {
      Neuron n = neurons[i];
      if (n.getLayer().equals("ip")) {
        n.setOutput(input[i]);
      } else if (n.getLayer().equals("hn")) {
        n.activate(n.getBias() + n.getWeights()[0] * neurons[0].getOutput()
            + n.getWeights()[1] * neurons[1].getOutput());
      } else if (n.getLayer().equals("op")) {
        n.activate(n.getBias() + n.getWeights()[0] * neurons[2].getOutput()
            + n.getWeights()[1] * neurons[3].getOutput());
      }
    }
    return this;
  }

  public Network backward(double target) {
    
    neurons[4].setError(target - neurons[4].getOutput() * neurons[4].dx());
    neurons[4].setBias(neurons[4].getBias() + learningrate * neurons[4].getError());
    neurons[4].getWeights()[0] = neurons[4].getWeights()[0] + learningrate * neurons[4].getError() * neurons[2].getOutput();
    neurons[4].getWeights()[1] = neurons[4].getWeights()[1] + learningrate * neurons[4].getError() * neurons[3].getOutput();
    
    neurons[3].setError(neurons[4].getWeights()[1] * neurons[4].getError() * neurons[3].dx());
    neurons[3].setBias(neurons[3].getBias() + learningrate * neurons[3].getError());
    neurons[3].getWeights()[0] = neurons[3].getWeights()[0] + learningrate * neurons[3].getError() * neurons[0].getOutput();
    neurons[3].getWeights()[1] = neurons[3].getWeights()[1] + learningrate * neurons[3].getError() * neurons[1].getOutput();
    
    neurons[2].setError(neurons[4].getWeights()[1] * neurons[4].getError() * neurons[2].dx());
    neurons[2].setBias(neurons[2].getBias() + learningrate * neurons[2].getError());
    neurons[2].getWeights()[0] = neurons[2].getWeights()[0] + learningrate * neurons[2].getError() * neurons[0].getOutput();
    neurons[2].getWeights()[1] = neurons[2].getWeights()[1] + learningrate * neurons[2].getError() * neurons[1].getOutput();
       
    return this;
  }



  public String toString() {
    return Arrays.toString(neurons);
  }


}
