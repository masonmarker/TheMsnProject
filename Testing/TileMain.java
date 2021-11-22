

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import javax.swing.SwingWorker;

/**
 * The main program should generate the entire state graph of the 3x3 tile game. It should present
 * the user with a prompt that allows the user to play the tile game, and/or get a one move hint.
 * 
 * @author Mason Marker
 * @version 1.0 - start date: October 16
 */
public class TileMain {

  static HashSet<TileState> states;
  static TileState current;
  static MasonGraph graph;
  static HashMap<Character, TileState> neighmoves;

  static LinkedList<Integer> path;

  public static void main(String[] args) {

    System.out.println("[*] Generating tile states...");


    createCurrent();
    generateAllStates();
    generateStates();

    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        int f = 0;
        while (true) {
          LinkedList<Integer> test = new LinkedList<>(
              graph.shortest(graph.indexOf(current), graph.indexOf(TileGraph.solvedState())));
          if (f != 0 && test.size() < path.size()) {
            path = test;
          } else if (f == 0) {
            path = test;
          }
          f++;
        }
      }
    };
    worker.execute();
    Scanner s = new Scanner(System.in);

    boolean solved = false;
    while (!solved) {

      String answer = prompt(s);
      if (answer.equalsIgnoreCase("Q")) {
        System.exit(0);
      } else if (answer.equalsIgnoreCase("N")) {
        restart();
      } else if (answer.equalsIgnoreCase("A") && neighmoves.get('A') != null) {
        current = current.neighborByMove('A');
        generateStates();
        solved = current.solved();
      } else if (answer.equalsIgnoreCase("B") && neighmoves.get('B') != null) {
        current = current.neighborByMove('B');
        generateStates();
        solved = current.solved();
      } else if (answer.equalsIgnoreCase("L") && neighmoves.get('L') != null) {
        current = current.neighborByMove('L');
        generateStates();
        solved = current.solved();
      } else if (answer.equalsIgnoreCase("R") && neighmoves.get('R') != null) {
        current = current.neighborByMove('R');
        generateStates();
        solved = current.solved();
      } else if (answer.equalsIgnoreCase("C")) {
        System.out.println("There are " + graph.connectedComponents() + " connected components.");
      } else if (answer.equalsIgnoreCase("H")) {
        System.out.println("you really needed a hint? i guess so, let me think...");
        System.out.println();
        if (path == null) {
          System.out.println(
              "I haven't calculated the shortest path just yet, try again in a few seconds...");
        } else {
          try {
            System.out.println(boxed(graph.vertexData(path.pop()).toString()));
            System.out.println("there ya go!");
            System.out.println();
          } catch (Exception e) {
            System.out
                .println("Wahooo!! you did it!! *cough* finally *cough*, now for the real game...");
            restart();
          }
        }
      } else
        dummy();
    }
  }

  /**
   * Generates a String that includes the char passed count amount of times.
   * 
   * @param c the character to repeat
   * @param count the amount of times the char may appear
   * @return the repeated characters
   * @since 0.1.5.3.3
   */
  public static String generate(char c, int count) {
    StringBuilder s = new StringBuilder("");
    IntStream.range(0, count).forEach(i -> s.append(c));
    return s.toString();
  }

  /**
   * Converts a String into an array of lines existing in that String.
   * 
   * @param s the String to use
   * @return array value of each line in the String given
   * @since 0.1.1.5.0
   */
  public static String[] toLineArray(String s) {
    Scanner kb = new Scanner(s);
    String[] lineArray = new String[countLines(s)];
    for (int i = 0; i < lineArray.length; i++)
      if (kb.hasNextLine())
        lineArray[i] = kb.nextLine();
      else
        lineArray[i] = "";
    return lineArray;
  }

  /**
   * Removes all empty lines in a given String.
   * 
   * @param s the String to use
   * @return the String representation with no empty lines
   * @since 0.1.2.1.0
   */
  public static String removeEmptyLines(String s) {
    return s.replaceAll("(?m)^\\s", "");
  }

  /**
   * Counts the number of existing lines in a String.
   * 
   * @param input the String to read
   * @return the amount of lines containing at least one character
   * @since 0.1.1.0.0
   */
  public static int countLines(String input) {
    String noEmpties = removeEmptyLines(input);
    Scanner kb = new Scanner(noEmpties);
    int count = 0;
    while (kb.hasNextLine()) {
      count++;
      kb.nextLine();
    }
    return count;
  }

  /**
   * Boxes a String.
   * 
   * @param s the String
   * @return the boxed String
   */
  public static String boxed(String s) {
    String b = "";
    String[] lines = toLineArray(s);
    int max = lines[0].length();
    for (int i = 0; i < lines.length; i++)
      if (lines[i].length() > max)
        max = lines[i].length();
    String seq = generate('-', max + 2);
    b += "+" + seq + "+\n";
    for (String l : lines) {
      b += "| " + l;
      b += generate(' ', max - l.length() + 1) + "|\n";
    }
    b += "+" + seq + "+\n";
    return b;
  }

  public static void dummy() {
    System.out.println();
    if (new Random().nextBoolean())
      System.out.println("uhh thats not a valid move bro :(");
    else
      System.out.println("yeah i can't let you do that :(");
    System.out.println();
  }

  public static void restart() {
    System.out.println();
    System.out.println("[*] Restarting game...");
    createCurrent();
    generateStates();
  }

  public static void createCurrent() {
    current = new TileState(TileState.generate());
  }

  public static void generateAllStates() {
    states = new HashSet<>(current.allStates());
  }

  public static void generateStates() {
    graph = new MasonGraph(states.toArray(TileState[]::new));
    neighmoves = current.neighboringStates();
    graph.bfsFrom(graph.indexOf(current));
    HashMap<LinkedHashMap<Integer, Integer>, LinkedHashMap<Integer, Boolean>> collected =
        graph.bfsFromPred(graph.indexOf(current));
    LinkedHashMap<Integer, Boolean> visited = null;
    for (Map.Entry<LinkedHashMap<Integer, Integer>, LinkedHashMap<Integer, Boolean>> entry : collected
        .entrySet()) {
      visited = entry.getValue();
    }
    if (!visited.get(graph.indexOf(TileGraph.solvedState()))) {
      System.out.println("[-] Unsolvable board generated, retrying...");
      restart();
    }
  }

  public static String prompt(Scanner s) {
    System.out.println("================================================");
    System.out.println(current.toString());
    System.out.print("Available moves: " + buildprompt() + "\r\n"
        + "You can also ask for a (H)int, or the number of connected \r\n"
        + "(C)omponents, a (N)ew game, or to (Q)uit.\r\n" + "What do you want to do? ");
    return s.nextLine();
  }

  public static String buildprompt() {
    String s = "";
    for (Map.Entry<Character, TileState> en : neighmoves.entrySet()) {
      if (en.getValue() != null) {
        switch (en.getKey()) {
          case 'A':
            s += "(A)bove ";
            break;
          case 'B':
            s += "(B)elow ";
            break;
          case 'L':
            s += "(L)eft ";
            break;
          case 'R':
            s += "(R)ight ";
            break;
        }
      }
    }
    return s;
  }

  public static int[] primitive(Integer[] a) {
    int[] arr = new int[a.length];
    for (int i = 0; i < a.length; i++)
      arr[i] = (int) a[i];
    return arr;
  }
}
