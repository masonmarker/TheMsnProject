import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import MsnLib.Msn;

/**
 * Provides new printing capabilities.
 * 
 * @author Mason Marker
 * @version 1.0 - 12/08/2021
 */
public class MsnPrinter {

  // Possible modes
  public static final int LINKED = 0;
  public static final int LINKEDLIST = -1;
  public static final int INDEXED = 1;
  public static final int INFORMATIVE = 2;
  public static final int BOXED = 3;

  private HashSet<Integer> modes;

  public MsnPrinter() {
    modes = new HashSet<>();
  }

  public boolean addMode(int mode) {
    return modes.add(mode);
  }

  public boolean removeMode(int mode) {
    return modes.remove(mode);
  }

  public void clearModes() {
    modes.clear();
  }

  @SuppressWarnings("unchecked")
  public <T> void println(Collection<T> collection) {
    println((T[]) collection.toArray());
  }

  public <T> void println(T[] array) {
    String s = "";
    if (modes.contains(0))
      for (int i = 0; i < array.length; i++)
        if (i != array.length - 1)
          s += array[i] + "-";
        else
          s += array[i];
    else if (modes.contains(-1)) {
      for (int i = 0; i < array.length; i++)
        if (i != array.length - 1)
          s += array[i] + ">";
        else
          s += array[i];
    }
    else
      for (int i = 0; i < array.length; i++)
        if (i != array.length - 1)
          s += array[i] + " ";
        else
          s += array[i];

    if (modes.contains(1)) {
      s += "\n";
      int index = 0;
      int l = s.length();
      for (int i = 0; i < l; i++)
        if (s.charAt(i) == ' ' || s.charAt(i) == '>' || s.charAt(i) == '-') {
          if (i != l - 1) {
            s += index + " ";
          } else {
            s += index + " " + index;
          }
          index++;
        }
      
    }
    
    if (modes.contains(2)) {
      s += "\n" + array.getClass();
      s += "\nsize:" + array.length;
    }
    if (modes.contains(3)) {
      s = Msn.boxed(s);
    }
    System.out.print(s);
    
  }

  public void enableAllModes() {
    modes.addAll(List.of(LINKED, LINKEDLIST, INDEXED, INFORMATIVE, BOXED));
  }
  
  public String[] getModes() {
    return modes.toArray(String[]::new);
  }

}
