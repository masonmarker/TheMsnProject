import MsnStructures.MsnStream;

public class Main {

  public static void main(String[] args) {


    new MsnStream<Integer>()._importRange(0, 5)._addDuplicate()._print()._isolateDuplicates()
        ._print()._map(i -> i += 2)._add(0)._addDuplicate()._print()._forEach(i -> {
          System.out.print(" el=");
          System.out.print(i);
        })._print("\n").halfSize()._add(1)._sorted()._map(i -> i += 1)._print().halfSize()._addMax()
        ._addMin()._print()._withoutDuplicates()._sorted()._print()._importRange(0, 5)
        ._map(i -> i -= 2)._withoutDuplicates()._sorted()._print()._map(i -> i = 0)._forEach(i -> {
          System.out.print("fuck");
        })._print("\n")._print()._isolateDuplicates()._empty()._importRange(5, 10)._map(i -> i += i)
        ._print()._addMax()._insert(0, 1000)._insert(0, 1000)._print()._importRange(0, 5)
        ._addDuplicate()._print()._isolateDuplicates()._print()._map(i -> i += 2)._add(0)
        ._addDuplicate()._print()._forEach(i -> {
          System.out.print(" el=");
          System.out.print(i);
        })._print("\n").halfSize()._add(1)._sorted()._map(i -> i += 1)._print().halfSize()._addMax()
        ._addMin()._print()._withoutDuplicates()._sorted()._print()._importRange(0, 5)
        ._map(i -> i -= 2)._withoutDuplicates()._sorted()._print()._map(i -> i = 0)._forEach(i -> {
          System.out.print("fuck");
        })._print("\n")._print()._isolateDuplicates()._empty()._importRange(5, 10)._map(i -> i += i)
        ._print()._addMax()._insert(0, 1000)._insert(0, 1000)._print()._importRange(0, 5)
        ._addDuplicate()._print()._isolateDuplicates()._print()._map(i -> i += 2)._add(0)
        ._addDuplicate()._print()._forEach(i -> {
          System.out.print(" el=");
          System.out.print(i);
        })._print("\n").halfSize()._add(1)._sorted()._map(i -> i += 1)._print().halfSize()._addMax()
        ._addMin()._print()._withoutDuplicates()._sorted()._print()._importRange(0, 5)
        ._map(i -> i -= 2)._withoutDuplicates()._sorted()._print()._map(i -> i = 0)._forEach(i -> {
          System.out.print("fuck");
        })._print("\n")._print()._isolateDuplicates()._empty()._importRange(5, 10)._map(i -> i += i)
        ._print()._addMax()._insert(0, 1000)._insert(0, 1000)._print()._importRange(0, 5)
        ._addDuplicate()._print()._isolateDuplicates()._print()._map(i -> i += 2)._add(0)
        ._addDuplicate()._print()._forEach(i -> {
          System.out.print(" el=");
          System.out.print(i);
        })._print("\n").halfSize()._add(1)._sorted()._map(i -> i += 1)._print().halfSize()._addMax()
        ._addMin()._print()._withoutDuplicates()._sorted()._print()._importRange(0, 5)
        ._map(i -> i -= 2)._withoutDuplicates()._sorted()._print()._map(i -> i = 0)._forEach(i -> {
          System.out.print("fuck");
        })._print("\n")._print()._isolateDuplicates()._empty()._importRange(5, 10)._map(i -> i += i)
        ._print()._addMax()._insert(0, 1000)._insert(0, 1000)._print();

  }
}
