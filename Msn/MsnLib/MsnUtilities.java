package MsnLib;

/**
 * Utilities class for Msn.
 * 
 * @author Mason Marker
 * @version 1.0 - 05/29/2021
 */
public class MsnUtilities {

  /**
   * Counts the number of methods that exist in the Msn class.
   * 
   * @param code the entirety of the Msn class source code (CTRL-A)
   */
  public static int countMethods(String code) {
    int count = Msn.countFreq(code, "public static");
    System.out.println(count + " methods are in the current version of Msn");
    return count;
  }
}
