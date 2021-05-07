import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Drawing extends JFrame implements MouseListener {

  private JPanel contentPane; 
  private DrawCell[][] cells;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Drawing frame = new Drawing();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public Drawing() {
    setUndecorated(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 577, 554);
    contentPane = new JPanel();
    contentPane.setBackground(SystemColor.inactiveCaptionText);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBackground(Color.DARK_GRAY);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE));

    JPanel drawpanel = new JPanel();
    drawpanel.setBackground(Color.GRAY);
    GroupLayout gl_panel = new GroupLayout(panel);
    gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        .addComponent(drawpanel, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE));
    gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(
        drawpanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE));
    drawpanel.setLayout(new GridLayout(75, 75, 0, 0));
    panel.setLayout(gl_panel);
    contentPane.setLayout(gl_contentPane);

    cells = new DrawCell[75][75];
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        cells[i][j] = new DrawCell(this, i, j);
        drawpanel.add(cells[i][j]);
      }
    }

    pack();
    setLocationRelativeTo(null);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mousePressed(MouseEvent e) {
    DrawCell cell = (DrawCell) e.getSource();
    turnOnNeighbors(cell);
  }

  public void turnOnNeighbors(DrawCell c) {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        DrawCell[] neighbors = getNeighbors(c);
        for (DrawCell cell : neighbors) {
          DrawCell[] neighbors2 = getNeighbors(cell);
          for (DrawCell cellulite : neighbors2) {
            cellulite.turnOn();
          }
        }
        return null;
      }
    };
    worker.execute();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    DrawCell c = (DrawCell) e.getSource();
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        DrawCell[] neighbors = getNeighbors(c);
        for (DrawCell cell : neighbors) {
          DrawCell[] newn = getNeighbors(cell);
          for (DrawCell cellular : newn) {
            cellular.turnOn();
          }
        }
        return null;
      }
    };
    worker.execute();
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    DrawCell cell = (DrawCell) e.getSource();
    cell.turnOn();
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  public DrawCell[] getNeighbors(DrawCell cell) {
    ArrayList<DrawCell> n = new ArrayList<>();
    try {
      n.add((DrawCell) Msn.above(cells, new int[] {cell.getI(), cell.getJ()}));
    } catch (ClassCastException | NullPointerException e) {
    }
    try {
      n.add((DrawCell) Msn.below(cells, new int[] {cell.getI(), cell.getJ()}));
    } catch (ClassCastException | NullPointerException e) {
    }
    try {
      n.add((DrawCell) Msn.leftOf(cells, new int[] {cell.getI(), cell.getJ()}));
    } catch (ClassCastException | NullPointerException e) {
    }
    try {
      n.add((DrawCell) Msn.rightOf(cells, new int[] {cell.getI(), cell.getJ()}));
    } catch (ClassCastException | NullPointerException e) {
    }
    return n.toArray(new DrawCell[n.size()]);
  }
}
