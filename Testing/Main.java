import java.util.HashMap;
import java.util.Map;

import MsnLib.Msn;
import MsnStructures.MsnDoubleMap;

class Main {
  static int a = 1;

  public static void main(String[] args) {

    int a = 0;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        for (int k = 0; k < 2; k++) {
          for (int l = 0; l < 2; l++) {
            a += 1;
          }
        }
      }
    }
    
    
    System.out.println(a);
    
  }


}
