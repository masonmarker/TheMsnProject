import java.math.BigInteger;
import java.util.Iterator;
import MsnLib.Msn;

/**
 * Class for extensive binary capabilities, usage is similar to that of BigInteger.
 * 
 * - Currently only works for Integers -
 * 
 * @author Mason Marker
 * @version 1.0 - 01/03/2022
 */
public class Binary {

  BigInteger stringrep;

  public Binary(int decimal) {
    stringrep = BigInteger.valueOf(Long.valueOf("" + binaryStringFrom(decimal)));
  }

  public Binary(String binary) {
    stringrep = BigInteger.valueOf(Long.valueOf(binary));
  }

  public int toInteger() {
    return Integer.valueOf(stringrep.toString(), 2);
  }

  public Binary plus(Binary b) {
    return new Binary(toInteger() + b.toInteger());
  }

  public Binary plus(int decimal) {
    return plus(new Binary(decimal));
  }

  public Binary minus(Binary b) {
    return new Binary(toInteger() - b.toInteger());
  }

  public Binary minus(int decimal) {
    return minus(new Binary(decimal));
  }

  public Binary times(Binary b) {
    return new Binary(toInteger() * b.toInteger());
  }

  public Binary times(int decimal) {
    return times(new Binary(decimal));
  }

  public Binary dividedBy(Binary b) {
    return new Binary(toInteger() / b.toInteger());
  }

  public Binary dividedBy(int decimal) {
    return dividedBy(new Binary(decimal));
  }

  public String toBinaryString() {
    return "" + stringrep;
  }

  public static String binaryStringFrom(int decimal) {
    return Integer.toBinaryString(decimal);
  }

  public static String formatBinary(String binary, int width) {
    return String.format("%" + width + "s", binary).replace(' ', '0');
  }

  public String toString() {
    return Msn.boxed(toBinaryString() + "\n(" + toInteger() + ")");
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Binary)
      return stringrep.equals((BigInteger) o);
    return false;
  }

}
