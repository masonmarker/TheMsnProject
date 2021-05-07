import java.awt.Color;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class DrawCell extends JPanel {

  private int i;
  private int j;

  /**
   * Create the panel.
   */
  public DrawCell(MouseListener listener, int i, int j) {
    setBackground(Color.DARK_GRAY);
    addMouseListener(listener);
    this.i = i;
    this.j = j;
  }

  public void turnOn() {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        setBackground(Color.WHITE);
        Thread.sleep(50);
        setBackground(Color.DARK_GRAY);
        this.cancel(true);
        return null;
      }
    };
    worker.execute();
  }

  public int getI() {
    return i;
  }

  public int getJ() {
    return j;
  }

  public String toString() {
    return "Cell: i:" + i + " j:" + j;
  }


}
