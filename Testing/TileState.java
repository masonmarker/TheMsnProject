

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents the state of a 3x3 tile game.
 *
 * Internally the state is represented as a flat array with 9 slots storing the numbers 0 through 8.
 * 0 is considered to be the blank.
 * 
 * So, for example, the game state:
 * 
 * 3 2 1 6 5 4 7 8
 *
 * is internally stored as {0, 3, 2, 1, 6, 5, 4, 7, 8}.
 * 
 * @author John C. Bowers and Mason Marker
 */
public class TileState {

  // The flattened tile stored in row major order.
  private final int[] state;
  private final int[][] tworep;

  public TileState(int[] state) {
    this.state = state;
    tworep = to2DArray(3, 3, state);
  }

  public int[] state() {
    return state;
  }
  
  public int[][] tworep() {
    return tworep;
  }
  
  /**
   * @param <T> Generic
   * @return a list of all possible tile states.
   */
  public ArrayList<TileState> allStates() {
    List<List<Integer>> combo = permute(state);
    ArrayList<TileState> accum = new ArrayList<>();
    combo.forEach(l -> {
      accum.add(new TileState(primitive(l.toArray(Integer[]::new))));
    });
    return accum;
  }

  public static int[] generate() {
    ArrayList<Integer> init =
        IntStream.range(0, 9).boxed().collect(Collectors.toCollection(ArrayList<Integer>::new));
    Collections.shuffle(init);
    return primitive(init.toArray(Integer[]::new));
  }

  public static List<List<Integer>> permute(int[] arr) {
    List<List<Integer>> list = new ArrayList<>();
    ph(list, new ArrayList<>(), arr);
    return list;
  }

  private static void ph(List<List<Integer>> list, List<Integer> result, int[] a) {
    if (result.size() == a.length)
      list.add(new ArrayList<>(result));
    else {
      for (int i = 0; i < a.length; i++) {
        if (result.contains(a[i]))
          continue;
        result.add(a[i]);
        ph(list, result, a);
        result.remove(result.size() - 1);
      }
    }
  }

  public boolean solved() {
    int[] solved = {1, 2, 3, 4, 5, 6, 7, 8, 0};
    return Arrays.equals(state, solved);
  }

  public static Integer[] box(int[] arr) {
    Integer[] a = new Integer[arr.length];
    for (int i = 0; i < arr.length; i++)
      a[i] = Integer.valueOf(arr[i]);
    return a;
  }

  public static int[] primitive(Integer[] a) {
    int[] arr = new int[a.length];
    for (int i = 0; i < a.length; i++)
      arr[i] = (int) a[i];
    return arr;
  }

  /**
   * Returns the tile at row i and column j in the tile game.
   * 
   * @param i The row index.
   * @param j The column index.
   * @return The tile number between 1 and 8, or 0 for blank at row i column j.
   */
  public int tileAt(int i, int j) {
    return state[3 * i + j];
  }

  /**
   * @param c The move to make, either 'A', 'B', 'L', or 'R'.
   * @return The TileState for the move that slides the appropriate tile into the blank, or null if
   *         the move is not valid.
   */
  public TileState neighborByMove(char c) {
    int[][] statecopy = arraycopy(tworep);
    switch (c) {
      case 'A':
        return up(statecopy);
      case 'B':
        return down(statecopy);
      case 'L':
        return leftt(statecopy);
      case 'R':
        return rightt(statecopy);
    }
    return null;
  }

  private Point blank(int[][] arr) {
    for (int i = 0; i < arr.length; i++)
      for (int j = 0; j < arr[i].length; j++)
        if (arr[i][j] == 0)
          return new Point(i, j);
    return null;
  }

  private int above(int i, int j, int[][] arr) {
    try {
      return arr[i - 1][j];
    } catch (IndexOutOfBoundsException e) {
      return -1;
    }
  }

  private int left(int i, int j, int[][] arr) {
    try {
      return arr[i][j - 1];
    } catch (IndexOutOfBoundsException e) {
      return -1;
    }
  }

  private int right(int i, int j, int[][] arr) {
    try {
      return arr[i][j + 1];
    } catch (IndexOutOfBoundsException e) {
      return -1;
    }
  }

  private int below(int i, int j, int[][] arr) {
    try {
      return arr[i + 1][j];
    } catch (IndexOutOfBoundsException e) {
      return -1;
    }
  }

  private TileState up(int[][] statecopy) {
    Point zero = blank(statecopy);
    int above = above((int) zero.getX(), (int) zero.getY(), statecopy);
    if (above == -1)
      return null;
    statecopy[(int) (zero.getX() - 1)][(int) zero.getY()] = 0;
    statecopy[(int) zero.getX()][(int) zero.getY()] = above;
    return new TileState(to1DArray(statecopy));
  }

  private TileState down(int[][] statecopy) {
    Point zero = blank(statecopy);
    int below = below((int) zero.getX(), (int) zero.getY(), statecopy);
    if (below == -1)
      return null;
    statecopy[(int) (zero.getX() + 1)][(int) zero.getY()] = 0;
    statecopy[(int) zero.getX()][(int) zero.getY()] = below;
    return new TileState(to1DArray(statecopy));
  }

  private TileState leftt(int[][] statecopy) {
    Point zero = blank(statecopy);
    int below = left((int) zero.getX(), (int) zero.getY(), statecopy);
    if (below == -1)
      return null;
    statecopy[(int) zero.getX()][(int) (zero.getY() - 1)] = 0;
    statecopy[(int) zero.getX()][(int) zero.getY()] = below;
    return new TileState(to1DArray(statecopy));
  }

  private TileState rightt(int[][] statecopy) {
    Point zero = blank(statecopy);
    int below = right((int) zero.getX(), (int) zero.getY(), statecopy);
    if (below == -1)
      return null;
    statecopy[(int) zero.getX()][(int) (zero.getY() + 1)] = 0;
    statecopy[(int) zero.getX()][(int) zero.getY()] = below;
    return new TileState(to1DArray(statecopy));
  }

  private int[][] arraycopy(int[][] arr) {
    return Arrays.stream(arr).map(int[]::clone).toArray(int[][]::new);
  }

  /**
   * Creates a 2D array from a one dimensional array.
   * 
   * @param rows amount of rows in the 2D array
   * @param cols amount of cols in the 2D array
   * @param obj the array to use
   * @return the 2D array
   */
  private static int[][] to2DArray(int rows, int cols, int[] array) {
    if (rows * cols != array.length)
      throw new IllegalArgumentException("rows * cols must equals ints.length");
    int[][] integers = new int[rows][cols];
    for (int i = 0; i < integers.length; i++)
      for (int j = 0; j < integers[i].length; j++)
        integers[i][j] = array[(i * cols) + j];
    return integers;
  }

  private static int[] to1DArray(int[][] array) {
    ArrayList<Integer> a = new ArrayList<>();
    Arrays.stream(array).forEach(ar -> {
      Arrays.stream(ar).forEach(a::add);
    });
    return primitive(a.toArray(Integer[]::new));
  }

  /**
   * Generates all states reachable by a single move.
   * 
   * @return a List object containing all the states reachable by one move from this one.
   */
  public HashMap<Character, TileState> neighboringStates() {
    HashMap<Character, TileState> map = new HashMap<>();
    int[][] sc1 = arraycopy(tworep);
    map.put('A', up(sc1));
    int[][] sc2 = arraycopy(tworep);
    map.put('B', down(sc2));
    int[][] sc3 = arraycopy(tworep);
    map.put('L', leftt(sc3));
    int[][] sc4 = arraycopy(tworep);
    map.put('R', rightt(sc4));
    return map;
  }

  /**
   * Returns a hashCode so if two state objects represent the same state, they will hash to the same
   * value.
   * 
   * @return A hashcode for this state.
   */
  public int hashCode() {
    return Arrays.toString(state).hashCode();
  }

  /**
   * Determines whether two state objects represent the same state.
   * 
   * @param o The other object to test.
   * @return true if the other object represents the same state.
   */
  @SuppressWarnings("static-access")
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    } else if (!(o instanceof TileState)) {
      return false;
    } else {
      TileState other = (TileState) o;
      for (int i = 0; i < 9; i++) {
        if (this.state[i] != other.state[i])
          return false;
      }
      return true;
    }
  }

  /**
   * @return the state of the game as a String that prints each board position on a line.
   */
  public String toString() {
    String acc = "";
    for (int i = 0; i < 9; i++) {
      acc += state[i] == 0 ? " " : state[i];
      acc += i % 3 == 2 ? "\n" : " ";
    }
    return acc;
  }

}

