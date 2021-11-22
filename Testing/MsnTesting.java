import java.util.LinkedHashMap;
import MsnLib.Msn;

public class MsnTesting {

  public static void main(String[] args) {

   
    
    LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
    
    
    for (int i = 0; i < 10; i++) {
      map.put(Msn.randomString(5), Msn.randomInt(1, 10));
    }
    
    System.out.println(map);
    
    
    
    
    
  }
  public String concat(String[] strings, char c) {
    String st = "";
    for (String s : strings) {
      if (s.charAt(0) == c) {
        st += s;
      } 
    }
    return st;
  }

  
  public static void split(String string) {
    String substring = "";
    String front = "";
    String back = "";
    for (int i = 0; i < string.length() / 2; i++)
      front += "" + string.charAt(i);
    for (int i = string.length() / 2; i < string.length(); i++)
      back += "" + string.charAt(i);

    for (char c : back.toCharArray()) {
      if (!front.contains("" + c)) {
        back.replaceAll("" + c, "");
      }
    }

    System.out.println(back + "         " + front);

  }



  public static String sameEnds(String string) {
    String substring = "";

    String front = "";
    String back = "";

    for (int i = 0; i < string.length() / 2; i++) {
      System.out.println(string.charAt(i) + " " + string.charAt(string.length() - 1 - i));
      if (string.charAt(i) == string.charAt(string.length() - 1 - i)) {
        substring += "" + string.charAt(i);
      } else {
        break;
      }
    }


    return substring;
  }

  public static int sum(int[] array) {
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      count += array[i];
    }
    return count;
  }


  public static int min(int[] array) {
    int m = array[0];
    for (int i = 0; i < array.length; i++) {
      if (array[i] < m) {
        m = array[i];
      }
    }
    return m;
  }

  public static int max(int[] array) {
    int m = array[0];
    for (int i = 0; i < array.length; i++) {
      if (array[i] > m) {
        m = array[i];
      }
    }
    return m;
  }

  public static int freq(int[] array, int num) {
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == num) {
        count++;
      }
    }
    return count;
  }

  public static int[] remove(int[] array, int num) {
    int freq = freq(array, num);
    if (freq > 1) {
      int[] newarr = new int[array.length - 1];
      int index = -1;
      for (int i = 0; i < array.length; i++) {
        if (array[i] != num) {
          newarr[i] = array[i];
        } else {
          index = i;
          break;
        }
      }
      for (int j = index + 1; j < array.length; j++) {
        newarr[index] = array[j];
        index++;
      }
      return newarr;
    }
    return array;
  }
}
