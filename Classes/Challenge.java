import java.util.ArrayList;

public class Challenge {

  public static boolean isHappy(int n) {
    ArrayList<Integer> list = new ArrayList<>();
    int i = n;
    while (i != 1) {
      String stringrep = String.valueOf(i);
      for (int j = 0; j < stringrep.length(); j++) {
        list.add(Integer.valueOf(stringrep.charAt(j)));
      }
      int sum = 0;
      for (int integer : list) {
        sum += integer * integer;
      }
      i = sum;
    }
    return true;
  }
  
  public static void main(String[] args) {
    System.out.println(isHappy(19));
  }


}


