import MsnStructures.MsnGenerator;
import MsnStructures.MsnStream;

public class MsnStreamTesting {


  public static void main(String[] args) {

    MsnStream<Double> stream = new MsnStream<>();
    
    stream._import(MsnGenerator.generateDoubleSet(100))._shuffled().statistics().visualize();
    
    
    
    
  }


}
