
/**
 * Time class, contains information regarding the current time and date.
 * 
 * @author Mason Marker
 * @version 1.0 - 07/03/2021
 */
public class Time {

  /**
   * Gets the String representation of the current local time.
   * 
   * @return the local time
   */
  public String currentTime() {
    return java.time.LocalTime.now().toString();
  }

  /**
   * Gets the String representation of the current local date.
   * 
   * @return the local date
   */
  public String currentDate() {
    return java.time.LocalDate.now().toString();
  }

  /**
   * Gets the current local date and time.
   * 
   * @return the current local date and time
   */
  public String currentDateAndTime() {
    return java.util.Calendar.getInstance().getTime().toString();
  }

}
