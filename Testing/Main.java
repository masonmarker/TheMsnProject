import java.util.HashMap;
import java.util.Map;

import MsnLib.Msn;
import MsnStructures.MsnDoubleMap;

class Main {
  public static void main(String[] args) {

    Timer timer = new Timer();

    timer.start();
    MsnDoubleMap<Integer, Integer> map = new MsnDoubleMap<Integer, Integer>();
    for (int i = 0; i < 1000000; i++) {
      map.put(Msn.randomInt(0, i), Msn.randomInt(0, i));
    }
    
    boolean[] keyContains = new boolean[1000000];
    boolean[] valueContains = new boolean[1000000];
    for (int i = 0; i < keyContains.length; i++) {
      keyContains[i] = map.containsKey(i);
      valueContains[i] = map.containsValue(i);
    }
    
    timer.stop();


    timer.start();

    Map<Integer, Integer> hashMap = new HashMap<>();
    for (int i = 0; i < 1000000; i++) {
       map.put(Msn.randomInt(0, i), Msn.randomInt(0, i));
    }
    boolean[] keyContains1 = new boolean[1000000];
    boolean[] valueContains1 = new boolean[1000000];
    for (int i = 0; i < keyContains1.length; i++) {
      keyContains1[i] = hashMap.containsKey(i);
      for (Map.Entry<Integer, Integer> entry : hashMap.entrySet()) {
        if (entry.getKey().equals(i)) {
          valueContains1[i] = true;
        }
      }
    }
    
    timer.stop();


    timer.printHistory();

  }



}
