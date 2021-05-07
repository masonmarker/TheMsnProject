import java.awt.Color;
import javax.swing.JPanel;

public class NoiseCell extends JPanel {

  private int i;
  private int j;

  public NoiseCell(int i, int j) {
    setBackground(Color.gray);
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
