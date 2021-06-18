public class Main {

  public static void main(String[] args) {
            
    Timer t = new Timer();
    
    
    t.start();
    MsnRandom rand = new MsnRandom();
    t.stop();
    
    
    for (int i = 0; i < 100; i++) 
      System.out.println(rand.nextName(1));
    
    
    t.printHistory();
    
  }

}
