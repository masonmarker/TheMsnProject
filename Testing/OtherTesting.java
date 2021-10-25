import java.util.ArrayList;
import java.util.Arrays;
import MsnLib.Msn;

public class OtherTesting {

  public static void main(String[] args) {

    Timer t = new Timer();
    int[] nums = Msn.createIntArray(100);
    Msn.pa(nums);
    t.start();
    System.out.println(Arrays.toString(evenOdd(nums)));
    t.stop();
  
    
    t.printHistory();
     
  }

  public static int[] evenOdd(int[] nums) {
    ArrayList<Integer> even = new ArrayList<>();
    ArrayList<Integer> odd = new ArrayList<>();
    for (int i : nums)
      if (i % 2 == 0)
        even.add(i);
      else
        odd.add(i);
    even.addAll(odd);
    return Msn.toInt(odd.toArray());
  }
}
