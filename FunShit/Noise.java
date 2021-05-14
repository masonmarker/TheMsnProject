import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

/**
 * For testing with 2D arrays and GUIS
 * 
 * @author Mason Marker
 * @version 1.1 - 05/04/2021
 */
public class Noise extends JFrame implements MouseListener {

  private JPanel contentPane;
  private NoiseCell[][] cells;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    Noise frame = new Noise(60, 100);
    frame.setVisible(true);
    while (frame.countBlack(frame.collectColors()) > 100) {
      try {
        Thread.sleep(40);
      } catch (InterruptedException e) {
      }
      frame.update();
    }
  }

  /**
   * Create the frame.
   */
  public Noise(int rows, int cols) {
    setUndecorated(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setBackground(Color.black);
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBackground(Color.DARK_GRAY);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE));
    panel.setLayout(new GridLayout(rows, cols, 0, 0));
    contentPane.setLayout(gl_contentPane);
    cells = new NoiseCell[rows][cols];
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        cells[i][j] = new NoiseCell(i, j);
        cells[i][j].addMouseListener(this);
        panel.add(cells[i][j]);
      }
    }
    pack();
    setLocationRelativeTo(null);
  }

  public Color[] collectColors() {
    ArrayList<Color> colors = new ArrayList<>();
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        colors.add(cells[i][j].getBackground());
      }
    }
    return colors.toArray(new Color[colors.size()]);
  }

  public void update() {

  }

  public int countBlack(Color[] colors) {
    int count = 0;
    for (Color c : colors) {
      if (Color.black.equals(c))
        count++;
    }
    return count;
  }

  public boolean containsNotWhiteNorBlack(Color[] colors) {
    Color[] colors1 = collectColors();
    for (Color color : colors1) {
      if (!color.equals(Color.white) && !color.equals(Color.black)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }


  @Override
  public void mousePressed(MouseEvent e) {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        for (int k = 0; k < 15; k++) {
          NoiseCell clicked = (NoiseCell) e.getSource();
          Object[] cls =
              Msn.circular(cells, k, new int[] {clicked.getI(), clicked.getJ()}).toArray();
          NoiseCell[] converted = new NoiseCell[cls.length];
          for (int i = 0; i < converted.length; i++) {
            converted[i] = (NoiseCell) cls[i];
          }
          for (int i = 0; i < converted.length; i++) {
            converted[i].setBackground(Color.white);
          }
          try {
            Thread.sleep(30);
          } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
          for (int i = 0; i < converted.length; i++) {
            converted[i].setBackground(Color.LIGHT_GRAY);
          }
        }
        return null;
      }
    };
    worker.execute();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseEntered(MouseEvent e) {


  }

  @Override
  public void mouseExited(MouseEvent e) {


  }

  class NoiseCell extends JPanel {

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
}
