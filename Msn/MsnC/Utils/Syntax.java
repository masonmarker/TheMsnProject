package MsnC.Utils;

import java.util.LinkedHashSet;
import MsnLib.Msn;

/**
 * Offers capabilities as far as syntax in MSNC.
 * 
 * @author Mason Marker
 * @version 1.0 - 09/23/2021
 */
public class Syntax {

  public static final String[] VALID_COMMANDS =
      {"print", "println", "i", "d", "s", "c", "o", "l", "f"};

  public static final String VALID_COMMENT = "//";
  public static final String[] VALID_OPERATORS = {"+", "-", "=", "/", "^", "%", "==", "!=", ">",
      "<", "<=", ">=", "+=", "-=", "*=", "/=", "^=", "<>", "??", "++", "r=", "m="};
  public static final String[] VALID_ESCAPES = {"\n"};

  public static final String[] VALID_PARAMETERS = {"iparam1", "iparam2", "iparam3", "iparam4",
      "dparam1", "dparam2", "dparam3", "dparam4", "cparam1", "cparam2", "cparam3", "cparam4",
      "sparam1", "sparam2", "sparam3", "sparam4", "lparam1", "lparam2", "lparam3", "lparam4",
      "oparam1", "oparam2", "oparam3", "oparam4", "fparam1", "fparam2", "fparam3", "fparam4"};

  public static final String[] VALID_RETURNS =
      {"ireturn", "dreturn", "creturn", "sreturn", "oreturn", "lreturn", "freturn"};

  public static final int DEFAULT_INT = 0;
  public static final double DEFAULT_DOUBLE = 0.0;
  public static final String DEFAULT_STRING = "";
  public static final Object DEFAULT_OBJECT = null;
  public static final Object DEFAULT_CHAR = '?';

  /**
   * Decides whether the String passed is a valid return keyword.
   * 
   * @param s the String to check
   * @return whether the String passed is a valid return keyword.
   */
  public static boolean isValidReturn(String s) {
    return Msn.contains(VALID_RETURNS, s);
  }

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
   * Finds the named parameters that a function uses;
   * 
   * @param str the String to check
   * @return the params used in the function
   */
  public static String[] params(String s) {
    LinkedHashSet<String> found = new LinkedHashSet<>();
    String[] split = s.split(" ");
    for (String term : split) {
      if (Syntax.isValidParameter(term)) {
        found.add(term);
      }
    }
    return found.toArray(String[]::new);
  }

  /**
   * Extracts the return keywords from the String.
   * 
   * @param s the String to check
   * @return the returns used in the function
   */
  public static String[] returns(String s) {
    LinkedHashSet<String> found = new LinkedHashSet<>();
    String[] split = s.split(" ");
    for (String term : split) {
      if (Syntax.isValidReturn(term)) {
        found.add(term);
      }
    }
    return found.toArray(String[]::new);
  }

  /**
   * Decides whether the String passed is a valid function parameter.
   * 
   * @param s the String
   */
  public static boolean isValidParameter(String s) {
    return Msn.contains(VALID_PARAMETERS, s);
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

  public static boolean isList(Object o) {

    return o.getClass().getTypeName().equals("MsnStructures.MsnStream");
  }

  public static boolean isObject(Object o) {
    return o.getClass().getTypeName().contains("Object");
  }

  /**
   * Determines if the String array passed contains an operator.
   * 
   * @param split the split String
   */
  public static boolean containsOperator(String string) {
    for (String s : VALID_OPERATORS) {
      if (string.contains(s)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isBooleanOperator(String string) {
    String[] bops = {"==", ">", "<", ">=", "<=", "!="};
    return Msn.contains(bops, string);
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

}
