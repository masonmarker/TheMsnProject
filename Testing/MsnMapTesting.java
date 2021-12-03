import MsnStructures.MsnGenerator;
import MsnStructures.MsnMap;

public class MsnMapTesting {

  public static void main(String[] args) {
    MsnMap<String, Boolean> map = new MsnMap<>();

    map.put("sup", false);

    MsnGenerator.populate(map, 10);

    map.toHashMap();

    map.toMsnStream().forEach(System.out::println);



  }

}
