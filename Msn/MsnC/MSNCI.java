package MsnC;

import java.util.Scanner;

/**
 * Interprets MSNC via the console.
 * 
 * @author Mason Marker
 * @version 1.0 - 04/03/2022
 */
public class MSNCI {

  public static void main(String[] args) {
    
    System.out.println("Msn Code Interpreter :: 1.0");
    System.out.println("  see msnc.txt for help");
    
    
    
    ExecutionHandler handler = new ExecutionHandler("", null);
    Scanner sc  = new Scanner(System.in);
    while (true) {
      System.out.print(">  ");
      String line = sc.nextLine();
      if (line.charAt(line.length() - 1) != ';') {
        line += ';';
      }
      try {
        handler.interpret(handler.toCodeLines(line), false);
        System.out.println();
        System.out.println("** " + handler.linesrun + " lines interpreted **");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
