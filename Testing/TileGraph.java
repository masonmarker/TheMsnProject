

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import MsnLib.Msn;

/**
 * This is your graph implementation. You need to decide whether it is better to store it as an
 * adjacency matrix or an adjacency list, or an adjacency set data structure, and why?
 *
 * Then you need to implement the interface IGraph here.
 */
public class TileGraph implements IGraph<TileState> {

  // I suggest you implement the graph using
  // and adjacency set data structure, store vertex
  // data implicitly in a separate array, and
  // store a lookup table that maps a given tile state
  // to its index in a table.
  public ArrayList<HashSet<Integer>> neighborsOf;
  public TileState[] stateOf;
  public Hashtable<TileState, Integer> indexOf;

  public LinkedHashMap<Integer, Boolean> visitation;
  Network n;


  public TileGraph(TileState[] allStates) {
    stateOf = allStates;
    Hashtable<TileState, Integer> in = new Hashtable<>();
    for (int i = 0; i < stateOf.length; i++)
      in.put(stateOf[i], i);
    indexOf = in;
    ArrayList<HashSet<Integer>> neigh = new ArrayList<>();
    for (int i = 0; i < stateOf.length; i++) {
      HashMap<Character, TileState> ne = stateOf[i].neighboringStates();
      HashSet<Integer> possible = new HashSet<>();
      for (Map.Entry<Character, TileState> en : ne.entrySet()) {
        if (en.getValue() != null) {
          possible.add(indexOf.get(en.getValue()));
        }
      }
      neigh.add(possible);
    }
    neighborsOf = new ArrayList<>();
    neighborsOf.addAll(neigh);
    n = new Network(4, 2, 2, 1);
  }

  @Override
  public int vertexCount() {
    return stateOf.length;
  }

  @Override
  public int edgeCount() {
    int count = 0;
    HashSet<HashSet<Integer>> rem = new HashSet<>();
    for (int i = 0; i < neighborsOf.size(); i++)
      rem.add(neighborsOf.get(i));
    for (HashSet<Integer> h : rem)
      count += h.size();
    return count;
  }

  @Override
  public TileState vertexData(int u) {
    for (Map.Entry<TileState, Integer> en : indexOf.entrySet())
      if (en.getValue() == u)
        return en.getKey();
    return null;
  }

  public Map.Entry<TileState, Boolean> getAt(int index, Map<TileState, Boolean> map) {
    int count = 0;
    for (Map.Entry<TileState, Boolean> en : map.entrySet()) {
      if (count == index) {
        return en;
      }
      count++;
    }
    return null;
  }

  public static TileState solvedState() {
    return new TileState(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 0});
  }

  @Override
  public int indexOf(TileState data) {
    return indexOf.get(data);
  }

  @Override
  public boolean hasEdge(int u, int v) {
    return neighborsOf.get(u).contains(v);
  }

  @Override
  public Iterable<Integer> neighborsOf(int u) {
    return new Iterable<Integer>() {
      @Override
      public Iterator<Integer> iterator() {
        return neighborsOf.get(u).iterator();
      }
    };
  }

  public LinkedHashMap<Integer, Boolean> bfsFrom(int u) {
    LinkedHashMap<Integer, Boolean> ret = new LinkedHashMap<>();
    visitation = new LinkedHashMap<>();
    for (TileState state : stateOf)
      visitation.put(indexOf(state), false);
    visitation.put(u, true);
    while (!visitation.isEmpty()) {
      Map.Entry<Integer, Boolean> u1 = pop();
      if (!u1.getValue()) {
        ret.put(u1.getKey(), true);
        Iterator<Integer> n = neighborsOf(u1.getKey()).iterator();
        while (n.hasNext()) {
          int next = n.next();
          if (visitation.get(next) != null && !visitation.get(next)) {
            visitation.put(next, true);
          }
        }
      }
    }
    return ret;
  }

  private Map.Entry<Integer, Boolean> pop() {
    Map.Entry<Integer, Boolean> entry = null;
    for (Map.Entry<Integer, Boolean> en : visitation.entrySet()) {
      entry = en;
      break;
    }
    visitation.remove(entry.getKey());
    return entry;
  }

  public HashMap<LinkedHashMap<Integer, Integer>, LinkedHashMap<Integer, Boolean>> bfsFromPred(
      int u) {
    LinkedHashMap<Integer, Integer> q = new LinkedHashMap<>();
    LinkedHashMap<Integer, Boolean> visitation2 = new LinkedHashMap<>();
    for (TileState state : stateOf)
      visitation2.put(indexOf(state), false);
    for (TileState state : stateOf)
      q.put(indexOf(state), -1);
    q.put(u, -1);
    visitation2.put(u, true);
    while (!q.isEmpty()) {
      Map.Entry<Integer, Integer> en = pop2(q);
      if (!visitation2.get(en.getKey())) {
        visitation2.put(en.getKey(), true);
        en.setValue(q.get(en.getKey()));
        Iterator<Integer> it = neighborsOf(en.getKey()).iterator();
        while (it.hasNext()) {
          int v = it.next();
          if (!visitation2.get(v)) {
            visitation2.put(v, true);
            q.put(v, en.getKey());
          }
        }
      }
    }
    HashMap<LinkedHashMap<Integer, Integer>, LinkedHashMap<Integer, Boolean>> collected =
        new HashMap<>();
    collected.put(q, visitation2);
    return collected;
  }

  private Map.Entry<Integer, Integer> pop2(LinkedHashMap<Integer, Integer> map) {
    Map.Entry<Integer, Integer> entry = null;
    for (Map.Entry<Integer, Integer> en : map.entrySet()) {
      entry = en;
      break;
    }
    map.remove(entry.getKey());
    return entry;
  }

  @Override
  public int connectedComponents() {
    HashMap<Integer, Boolean> visitt = new HashMap<>();
    for (TileState state : stateOf)
      visitt.put(indexOf(state), false);
    int count = 0;
    for (TileState state : stateOf) {
      int index = indexOf(state);
      if (!visitt.get(index)) {
        count++;
        ccBFSHelper(index, visitt);
      }
    }
    return count;
  }

  public void ccBFSHelper(int u, HashMap<Integer, Boolean> visitt) {
    LinkedList<Integer> q = new LinkedList<>();
    q.add(u);
    while (!q.isEmpty()) {
      u = q.pop();
      if (!visitt.get(u)) {
        visitt.put(u, true);
        Iterator<Integer> it = neighborsOf(u).iterator();
        while (it.hasNext()) {
          int v = it.next();
          if (!visitt.get(v)) {
            q.add(v);
          }
        }
      }
    }
  }

  @Override
  public List<Integer> shortestPath(int u, int v) {
    ArrayList<Integer> path = new ArrayList<>();

    Network n = new Network(9, 3, 3, 1);
    double[] possible = {0, .33, .66, 1};

    int perm = u;
    ArrayList<Double> avg = new ArrayList<>();
    while (u != v) {

      TileState data = vertexData(u);
      double choice = Msn.closestTo(n.getAnswer(Msn.toDouble(data.state())), possible);
      int score = score(data.state());

      System.out.println("score: " + score + ", avg score: " + Msn.decFormat(Msn.avg(Msn.toDouble(avg.toArray())), 3));

      Msn.printboxed(data.toString());



      // 0 is up
      // .33 is down
      // .66 is left
      // 1 is right

      HashMap<Character, TileState> dir = data.neighboringStates();
      if (choice == 0) {
        try {
          u = indexOf(dir.get('A'));
          int[] state = vertexData(u).state();
          int newscore = score(state);
          if (newscore > score) {
            n.train(Msn.toDouble(data.state()), 0, 20000);
          } else {
            n.train(Msn.toDouble(data.state()), Msn.randomElement(new double[] {.33, .66, 1}), 1000);
          }
        } catch (Exception e) {
          n.train(Msn.toDouble(data.state()), Msn.randomElement(new double[] {.33, .66, 1}), 1000);
        }
      } else if (choice == .33) {
        try {
          u = indexOf(dir.get('B'));
          int[] state = vertexData(u).state();
          int newscore = score(state);
          if (newscore > score) {
            n.train(Msn.toDouble(data.state()), .33, 20000);
          } else {
            n.train(Msn.toDouble(data.state()), Msn.randomElement(new double[] {0, .66, 1}), 1000);
          }
        } catch (Exception e) {
          n.train(Msn.toDouble(data.state()), Msn.randomElement(new double[] {0, .66, 1}), 1000);

        }
      } else if (choice == .66) {
        try {
          u = indexOf(dir.get('L'));
          int[] state = vertexData(u).state();
          int newscore = score(state);
          if (newscore > score) {
            n.train(Msn.toDouble(data.state()), .66, 20000);
          } else {
            n.train(Msn.toDouble(data.state()), Msn.randomElement(new double[] {0, .33, 1}), 1000);
          }
        } catch (Exception e) {
          n.train(Msn.toDouble(data.state()), Msn.randomElement(new double[] {0, .33, 1}), 1000);
        }
      } else if (choice == 1) {
        try {
          u = indexOf(dir.get('R'));
          int[] state = vertexData(u).state();
          int newscore = score(state);
          if (newscore > score) {
            n.train(Msn.toDouble(data.state()), 1, 20000);
          } else {
            n.train(Msn.toDouble(data.state()), Msn.randomElement(new double[] {0, .33, .66}), 1000);
          }
        } catch (Exception e) {
          n.train(Msn.toDouble(data.state()), Msn.randomElement(new double[] {0, .33, .66}), 1000);

        }
      }
      avg.add((double) score);
      path.add(u);
    }

    System.out.println("beginning trimming...");
    return path;
  }

  public int score(int[] state) {
    int count = 0;
    int[] solved = solvedState().state();
    for (int i = 0; i < state.length; i++)
      if (state[i] == solved[i])
        count++;
    return count;
  }

  /**
   * Finds the number in the options array closest to i.
   * 
   * @param i the integer
   * @param options numbers to compare
   * @return the int that i is closest to
   * @since 0.1.5.2.2
   */
  public static int closestTo(int i, int[] options) {
    int closest = options[0];
    for (int integer : options) {
      if (Math.abs(integer - i) < Math.abs(closest - i))
        closest = integer;
    }
    return closest;
  }

  /**
   * Converts an Object array to the specified primitive array.
   * 
   * @param array the array to use
   * @return the fixed array
   * @since 0.1.2.3.7
   */
  public static int[] toInt(Object[] array) {
    int[] fixed = new int[array.length];
    for (int i = 0; i < fixed.length; i++)
      fixed[i] = (int) array[i];
    return fixed;
  }

}
