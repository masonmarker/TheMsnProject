import MsnStructures.MsnMultimap;

public class Main {

  public static void main(String[] args) {


    MsnMultimap<String, Integer> map = new MsnMultimap<>();
    
    
    map.put("hey", 3);
    map.put("hey", 1);
    map.put("hey", 6);

    
    map.put("sup", 2);

    System.out.println(map);
    
    
    map.removeValue(3);
    map.removeValue(1);
    map.removeValue(6);
    
    System.out.println(map);
    
    
    
  }
}
