import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class GrowDriver extends JFrame implements MouseListener {

  private JPanel contentPane;
  private static JPanel[][] cells;
  static int time;
  
  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          GrowDriver frame = new GrowDriver();
          frame.setVisible(true);

          SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
              while (true) {
                update();
                if (time < 10)
                  time = 11;
                Thread.sleep(time -= 10);
              }
            }
          };
          worker.execute();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public GrowDriver() {
    
    time = 500;
    
    setUndecorated(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 553, 498);
    contentPane = new JPanel();
    contentPane.setBackground(Color.BLACK);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    
    JPanel panel = new JPanel();
    panel.setBackground(Color.BLACK);
    GroupLayout groupLayout = new GroupLayout(getContentPane());
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup().addContainerGap()
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
            .addContainerGap()));
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup().addContainerGap()
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
            .addContainerGap()));
    panel.setLayout(new GridLayout(150, 100, 0, 0));
    getContentPane().setLayout(groupLayout);

    cells = new JPanel[150][100];
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        cells[i][j] = new JPanel();
        cells[i][j].addMouseListener(this);
        cells[i][j].setBackground(Color.black);
        panel.add(cells[i][j]);
      }
    }
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setLocationRelativeTo(null);
  }

  public static void update() {
    for (Point p : whitepoints()) {
      ((JPanel) Msn.randomElement(Msn.adjacent(cells, new int[] {(int) p.getX(), (int) p.getY()})))
          .setBackground(Color.white);
    }
  }

  public static HashSet<Point> whitepoints() {
    HashSet<Point> panels = new HashSet<>();
    for (int i = 0; i < cells.length; i++)
      for (int j = 0; j < cells[i].length; j++)
        if (Msn.equals(cells[i][j].getBackground(), Color.white))
          panels.add(new Point(i, j));
    return panels;
  }

  @Override
  public void mouseClicked(MouseEvent e) {}
  @Override
  public void mousePressed(MouseEvent e) {
    JPanel p = (JPanel) e.getSource();
    p.setBackground(Color.white);
  }

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}
}
