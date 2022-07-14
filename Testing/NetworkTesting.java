public class NetworkTesting {

  public static void main(String[] args) {

    MsnNetwork n =
        new MsnNetwork("network", 3, new String[] {"0", "1", "2", "3", "4", "5", "6", "7"});

    n.setNeurons(1, 3);
    n.setLearningRate(0.9);


    double[] d0 = {0.0, 0.0, 0.0};
    double[] d1 = {0.0, 0.0, 1.0};
    double[] d2 = {0.0, 1.0, 0.0};
    double[] d3 = {0.0, 1.0, 1.0};
    double[] d4 = {1.0, 0.0, 0.0};
    double[] d5 = {1.0, 0.0, 1.0};
    double[] d6 = {1.0, 1.0, 0.0};
    double[] d7 = {1.0, 1.0, 1.0};


    for (int i = 0; i < 100000; i++) {
      n.train(d0, "0", 100);
      n.train(d1, "1", 100);
      n.train(d2, "2", 100);
      n.train(d3, "3", 100);
      n.train(d4, "4", 100);
      n.train(d5, "5", 100);
      n.train(d6, "6", 100);
      n.train(d7, "7", 100);
    }


    n.printAnswer(d0, "0");
    n.printAnswer(d1, "1");
    n.printAnswer(d2, "2");
    n.printAnswer(d3, "3");
    n.printAnswer(d4, "4");
    n.printAnswer(d5, "5");
    n.printAnswer(d6, "6");
    n.printAnswer(d7, "7");
    
    

  }

}

