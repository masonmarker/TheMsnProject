package MsnStructures;

import MsnLib.Msn;

/**
 * Solves String expressions.
 * 
 * @author Mason Marker
 * @version 1.0 - 12/01/2021
 */
public class MsnSolver {

  String exp;
  
  public MsnSolver(String expression) {
    exp = expression;
  }
  
  public String getExp() {
    return exp;
  }

  public void setExp(String exp) {
    this.exp = exp;
  }

  public double compute() {
    return solve(exp);
  }

  /** 
   * Allows for editing of the current expression and instant computation.
   */
  public void expand() {
    MsnCalculator c = new MsnCalculator(exp);
    c.update();
    c.setVisible(true); 
  }
  
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
