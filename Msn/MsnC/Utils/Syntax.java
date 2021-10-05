package MsnC.Utils;

import java.util.Arrays;
import MsnLib.Msn;

/**
 * Offers capabilities as far as syntax in MSNC.
 * 
 * @author Mason Marker
 * @version 1.0 - 09/23/2021
 */
public class Syntax {

  public static final String[] VALID_COMMANDS = {"print", "println", "i", "d", "s", "c", "b", "o", "i[]",
      "d[]", "s[]", "c[]", "b[]", "o[]", "if", "for", "while"};

  public static final String VALID_COMMENT = "//";
  public static final String[] VALID_OPERATORS = {"+", "-", "=", "/", "^", "%", "==", "!=", ">",
      "<", "<=", ">=", "+=", "-=", "*=", "/=", "^=", "<>", "??"};
  public static final String[] VALID_ESCAPES = {"\n"};

  public static final int DEFAULT_INT = 0;
  public static final double DEFAULT_DOUBLE = 0.0;
  public static final String DEFAULT_STRING = "";
  public static final Object DEFAULT_OBJECT = null;
  public static final Object DEFAULT_CHAR = '?';

  /**
   * Decides whether the String passed is a valid operator.
   * 
   * @param s the String
   * @return whether the String is equal to a valid operator
   */
  public static boolean isValidOperator(String s) {
    return Msn.contains(s, VALID_OPERATORS);
  }

  /**
   * Decides whether the String passed is a valid command.
   * 
   * @param s the String
   * @return whether the String is equal to a valid command
   */
  public static boolean isValidCommand(String s) {
    return Msn.contains(s, VALID_COMMANDS);
  }

  /**
   * Decides whether the String passed is a valid escape sequence.
   * 
   * @param s the String
   * @return whether the String is equal to a valid escape sequence
   */
  public static boolean isValidEscape(String s) {
    return Msn.contains(s, VALID_ESCAPES);
  }

  /**
   * Checks if the variable passed contains the @ operator.
   * 
   * @param variable the variable
   * @return if the variable passed contains the @ operator
   */
  public static boolean isString(String variable) {
    try {
      return variable.charAt(0) == '@';
    } catch (StringIndexOutOfBoundsException e) {
      return false;
    }
  }

  /**
   * Decides whether the String passed is a valid term within MSNC.
   * 
   * @param s the String
   * @return whether the String is equal to a valid term
   */
  public static boolean isValidTerm(String s) {
    return isValidOperator(s) || isValidCommand(s) || s.equals(VALID_COMMENT) || isValidEscape(s);
  }
  
  /**
   * Converts an Object to its corresponding array.
   * 
   * @param o the Object
   * @return String representation
   */
  public static String arrayToString(Object o) {
    switch (o.getClass().getTypeName()) {
      case "int[]":
        return Arrays.toString((int[]) o);
      case "double[]":
        return Arrays.toString((double[]) o);
      case "String[]":
        return Arrays.toString((String[]) o);
      case "char[]":
        return Arrays.toString((char[]) o);
      case "Object[]":
        return Arrays.toString((Object[]) o);
    }
    return null;
  }
  
  public static boolean isInt(Object o) {
    return o.getClass().getTypeName().equals("java.lang.Integer");
  }
  
  public static boolean isDouble(Object o) {
    return o.getClass().getTypeName().equals("java.lang.Double");
  }
  
  public static boolean isChar(Object o) {
    return o.getClass().getTypeName().equals("java.lang.Character");
  }
  
  public static boolean isString(Object o) {
    return o.getClass().getTypeName().equals("java.lang.String");
  }
  
  public static boolean isIntArray(Object o) {
    return o.getClass().getTypeName().equals("int[]");
  }
  
  public static boolean isDoubleArray(Object o) {
    return o.getClass().getTypeName().equals("double[]");
  }
  
  public static boolean isCharArray(Object o) {
    return o.getClass().getTypeName().equals("char[]");
  }
  
  public static boolean isStringArray(Object o) {
    return o.getClass().getTypeName().equals("String[]");
  }
  
  public static boolean isObjectArray(Object o) {
    return o.getClass().getTypeName().equals("Object[]");
  }
  
}
