import MsnStructures.MsnStream;

public class Main {

  public static void main(String[] args) {


    new MsnStream<Integer>()._importRange(0, 5)._addDuplicate()._print()._isolateDuplicates()
        ._print()._forEach(integer -> {
          integer += 2;
          integer -= 2;
        })._add(10)._add(10)._isolateDuplicates()._print();

  }
}
