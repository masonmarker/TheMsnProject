import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

public class Texas {

  public static void main(String[] args) {

    Timer t = new Timer();
    
    
    char[] hand = {'K', 'Q', 'J', 'T', '9'};

    t.start();
    printHand(hand);

    t.stop();

    t.printHistory();

  }

  public static void printHand(char[] hand) {
    String[] bits = bitFields(hand);
    String fields = bits[0];
    String sixbits = bits[1];
    BigInteger dec = new BigInteger(sixbits, 2);
    boolean onePair = dec.mod(new BigInteger("" + 15)).equals(new BigInteger("" + 6));
    boolean twoPair = dec.mod(new BigInteger("" + 15)).equals(new BigInteger("" + 7));
    boolean three = dec.mod(new BigInteger("" + 15)).equals(new BigInteger("" + 9));
    boolean fullHouse = dec.mod(new BigInteger("" + 15)).equals(new BigInteger("" + 10));
    boolean four = dec.mod(new BigInteger("" + 15)).equals(new BigInteger("" + 1));
    boolean straight = isStraight(new BigInteger(fields));

    String s = "";
    s += "fields: " + fields + "\n";
    s += "bin rep: " + sixbits + "\n";
    s += "dec rep: " + dec + "\n";
    s += "1 pair: " + onePair + "\n";

    s += "2 pair: " + twoPair + "\n";
    s += "3 of a kind: " + three + "\n";
    s += "full house: " + fullHouse + "\n";
    s += "4 of a kind: " + four + "\n";
    s += "straight: " + straight + "\n";

    System.out.println(s);
  }

  public static boolean isStraight(BigInteger fields) {
    
    BigInteger lsb = fields.and(fields.negate());
    
    return fields.divide(lsb).equals(BigInteger.valueOf(31));
  }

  private static void increase(ArrayList<Boolean> l, int loc) {
    int p = loc * 4 - 1;
    for (int i = p; i > p - 4; i--)
      if (l.get(i) == false) {
        l.set(i, true);
        break;
      }
  }

  /**
   * Obtains #1 ('b.toString()') and #2 ('six') bit Strings.
   * 
   * @param hand the hand
   * @return duple of bit strings in ArrayList form
   */
  public static String[] bitFields(char[] hand) {
    ArrayList<Boolean> six = new ArrayList<>();
    BigInteger b = BigInteger.ZERO;

    for (int i = 0; i < 60; i++)
      six.add(false);

    for (char c : hand) {
      switch (c) {
        case 'A':
          b = b.or(BigInteger.valueOf(16384));
          increase(six, 1);
          break;
        case 'K':
          b = b.or(BigInteger.valueOf(8192));
          increase(six, 2);
          break;
        case 'Q':
          b = b.or(BigInteger.valueOf(4096));
          increase(six, 3);
          break;
        case 'J':
          b = b.or(BigInteger.valueOf(2048));
          increase(six, 4);
          break;
        case 'T':
          b = b.or(BigInteger.valueOf(1024));
          increase(six, 5);
          break;
        case '9':
          b = b.or(BigInteger.valueOf(512));
          increase(six, 6);
          break;
        case '8':
          b = b.or(BigInteger.valueOf(256));
          increase(six, 7);
          break;
        case '7':
          b = b.or(BigInteger.valueOf(128));
          increase(six, 8);
          break;
        case '6':
          b = b.or(BigInteger.valueOf(64));
          increase(six, 9);
          break;
        case '5':
          b = b.or(BigInteger.valueOf(32));
          increase(six, 10);
          break;
        case '4':
          b = b.or(BigInteger.valueOf(16));
          increase(six, 11);
          break;
        case '3':
          b = b.or(BigInteger.valueOf(8));
          increase(six, 12);
          break;
        case '2':
          b = b.or(BigInteger.valueOf(4));
          increase(six, 13);
          break;
      }
    }
    return new String[] {b.toString(), bitjoined(six)};
  }

  public static String bitjoined(ArrayList<Boolean> l) {
    String s = "";
    for (Boolean b : l) {
      if (b) {
        s += "1";
      } else {
        s += "0";
      }
    }
    return s;
  }

}

