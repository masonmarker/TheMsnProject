import java.util.ArrayList;
import MsnLib.Msn;

public class SpeedTesting {


  public static void main(String[] args) {
    Timer t = new Timer();

    
    String[] s = Msn.getWords("hello how are you doing? im doing just fine lol bouta code n stuff yk");
    
    
    t.start();
    
    String[] fixed = Msn.dropWords(s, 3);
    Msn.pa(fixed);
    t.stop();
    
    t.start();
    
    ArrayList<String> list = new ArrayList<>();
    for (String string : s) {
      list.add(string);
    }
    list.remove(0);
    list.remove(0);
    list.remove(0);
    String[] fixed2 = list.toArray(String[]::new);
    
    
    t.stop();
    
    
    

    t.printHistory();
  }

}
