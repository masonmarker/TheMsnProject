import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Timer class, gives the difference between two runtimes during .
 * 
 * Also keeps track of Timer usage
 * 
 * @author Mason Marker
 * @version 1.1 - 05/03/2021
 */
public class Timer {

  private boolean verbose;

  private ArrayList<Integer> runtimes;

  private long starttime;
  private long endtime;

  public Timer() {
    starttime = 0;
    endtime = 0;
    runtimes = new ArrayList<>();
    verbose = false;
  }

  public void start() {
    if (starttime == 0) {
      starttime = System.nanoTime();
      if (verbose)
        System.out.println("Timer started");
    } else {
      if (verbose)
        System.out.println("Timer restarted");
      starttime = endtime;
    }
  }

  public void stop() {
    if (verbose)
      System.out.println("Timer stopped");
    endtime = System.nanoTime();
    if (runtimes.isEmpty())
      runtimes.add(runtime());
    else
      runtimes.add(runtime());
  }

  public int runtime() {
    if (endtime != 0)
      return (int) TimeUnit.NANOSECONDS.toMillis(endtime - starttime);
    throw new IllegalStateException("Timer must be started and ended to obtain runtime");
  }

  public int getStartTime() {
    return (int) TimeUnit.NANOSECONDS.toMillis(starttime);
  }

  public int getEndTime() {
    return (int) TimeUnit.NANOSECONDS.toMillis(endtime);
  }

  public double getAvgRuntime() {
    double sum = 0;
    for (int i : runtimes)
      sum += i;
    return sum / runtimes.size();
  }

  public int getRuntimeFrom(int index) {
    return runtimes.get(index);
  }

  public int[] getHistory() {
    return toInt(runtimes.toArray());
  }

  public void printHistory() {
    for (int i = 0; i < runtimes.size(); i++)
      System.out.println("runtime #" + (i + 1) + ": " + runtimes.get(i) + "ms");
  }

  public void setVerbosity(boolean newVerbose) {
    verbose = newVerbose;
  }

  // --------------------------HELPER--------------------------

  protected int[] toInt(Object[] arr) {
    int[] converted = new int[arr.length];
    for (int i = 0; i < arr.length; i++)
      converted[i] = (int) arr[i];
    return converted;
  }
}
