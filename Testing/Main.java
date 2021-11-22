public class Main {

  public static void main(String[] args) {
    
    double[] inputs = {4.4, 8.6, 2.2};
    
    Network n = new Network(3, 2, 2, 1);
    
    n.train(inputs, .4, 1000);
    
    System.out.println(n.getAnswer(inputs));
    
     
  }
}
