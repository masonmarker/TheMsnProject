package MsnStructures;

import MsnLib.Msn;

/**
 * Solves String expressions.
 * 
 * @author Mason Marker
 * @version 1.0 - 12/01/2021
 */
public class MsnSolver {

  /**
   * Solves a math expression in a String representation.
   * 
   * @param expression the math expression
   * @return the evaluation
   */
  public static double solve(String expression) {
    return (double) Msn.evaluate(expression);
  }
}
