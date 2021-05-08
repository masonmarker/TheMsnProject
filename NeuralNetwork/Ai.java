import java.awt.Color;
import javax.swing.JPanel;

public class Ai extends JPanel {

  private int i;
  private int j;
  
  public Ai(int i, int j) {
    setBackground(Color.GREEN);
    this.i = i;
    this.j = j;
  }
  
  public int[] position() {
    return new int[] {i, j};
  }
  
  public void setPosition(int i, int j) {
    this.i = i;
    this.j = j;
  }
  
  public int getI() {
    return i;
  }
  
  public int getJ() {
    return j;
  }
  
}
