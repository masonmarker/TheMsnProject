import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Watch an artificial intelligence agent learn to avoid walls!
 * 
 * @author Mason Marker
 * @version 1.0 - 05/14/2021
 */
@SuppressWarnings("serial")
public class NeuralNetworkGame extends JFrame implements MouseListener {

  private JPanel contentPane;

  private static JPanel[][] cells;
  private static JLabel scorelabel;
  private static JLabel genlabel;

  private static Ai ai;
  static int gen;
  static int score;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          NeuralNetworkGame frame = new NeuralNetworkGame();
          frame.setVisible(true);
          Network network = new Network(4, 2, 4, 1);
          SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
              while (true) {
                makeMove(network);
                Msn.pa(ai.position());
                Thread.sleep(50);
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
  public NeuralNetworkGame() {

    gen = 1;
    score = 0;

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 454, 428);
    contentPane = new JPanel();
    contentPane.setBackground(Color.DARK_GRAY);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    setUndecorated(true);

    JPanel panel = new JPanel();
    panel.setBackground(Color.DARK_GRAY);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE));

    JPanel panel_1 = new JPanel();
    panel_1.setBorder(new LineBorder(Color.WHITE, 4));
    panel_1.setBackground(Color.BLACK);

    genlabel = new JLabel("Generation: " + gen);
    genlabel.setHorizontalTextPosition(SwingConstants.CENTER);
    genlabel.setHorizontalAlignment(SwingConstants.CENTER);
    genlabel.setForeground(Color.WHITE);
    genlabel.setFont(new Font("Yu Gothic", Font.PLAIN, 15));

    scorelabel = new JLabel("Score: " + score);
    scorelabel.setHorizontalTextPosition(SwingConstants.CENTER);
    scorelabel.setHorizontalAlignment(SwingConstants.CENTER);
    scorelabel.setForeground(Color.WHITE);
    scorelabel.setFont(new Font("Yu Gothic", Font.PLAIN, 15));
    GroupLayout gl_panel = new GroupLayout(panel);
    gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        .addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
        .addGroup(gl_panel.createSequentialGroup().addGap(169)
            .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                .addComponent(scorelabel, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addComponent(genlabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE))
            .addGap(168)));
    gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel.createSequentialGroup().addGap(4)
            .addComponent(genlabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(scorelabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)));
    panel_1.setLayout(new GridLayout(50, 50, 0, 0));
    panel.setLayout(gl_panel);
    contentPane.setLayout(gl_contentPane);

    cells = new JPanel[50][50];
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        if (i == 25 && j == 25) {
          ai = new Ai(i, j);
          cells[i][j] = ai;
          panel_1.add(cells[i][j]);
        } else {
          cells[i][j] = new JPanel();
          cells[i][j].addMouseListener(this);
          cells[i][j].setBackground(Color.black);
          panel_1.add(cells[i][j]);
        }
      }
    }
    obstacleInit();
    pack();
    setLocationRelativeTo(null);
  }

  /**
   * Makes a move based on the decision making of this neural network.
   */
  public static void makeMove(Network n) {

    double[] around = around();
    double[] possible = {0, .3, .6, 1};
    double answer = n.getAnswer(around);
    double closest = Msn.closestTo(answer, possible);

    if (closest == 0) {
      if (moveUp())
        n.train(around, 0, 100000);
      else {
        double rand = Msn.randomElement(possible);
        while (rand == 0)
          rand = Msn.randomElement(possible);
        n.train(around, rand, 10000);
        reset();
      }
    } else if (closest == .3) {
      if (moveDown())
        n.train(around, .3, 100000);
      else {
        double rand = Msn.randomElement(possible);
        while (rand == .3)
          rand = Msn.randomElement(possible);
        n.train(around, rand, 10000);
        reset();
      }
    } else if (closest == .6) {
      if (moveLeft())
        n.train(around, .6, 100000);
      else {
        double rand = Msn.randomElement(possible);
        while (rand == .6)
          rand = Msn.randomElement(possible);
        n.train(around, rand, 10000);
        reset();
      }
    } else if (closest == 1) {
      if (moveRight())
        n.train(around, 1, 100000);
      else {
        double rand = Msn.randomElement(possible);
        while (rand == 1)
          rand = Msn.randomElement(possible);
        n.train(around, rand, 10000);
        reset();
      }
    }
    increaseScore();
  }

  public static void increaseScore() {
    score++;
    scorelabel.setText("Score: " + score);
  }

  public static void reset() {
    for (int i = 0; i < cells.length; i++)
      for (int j = 0; j < cells[i].length; j++)
        cells[i][j].setBackground(Color.black);
    score = 0;
    scorelabel.setText("Score: " + score);
    gen++;
    genlabel.setText("Generation: " + gen);
    obstacleInit();
    cells[25][25].setBackground(Color.green);
    ai.setPosition(25, 25);
  }

  public static void obstacleInit() {
    cells[20][15].setBackground(Color.red);
    cells[21][15].setBackground(Color.red);
    cells[22][15].setBackground(Color.red);
    cells[23][15].setBackground(Color.red);
    cells[24][15].setBackground(Color.red);
    cells[25][15].setBackground(Color.red);
    cells[26][15].setBackground(Color.red);
    cells[27][15].setBackground(Color.red);
    cells[28][15].setBackground(Color.red);
    cells[29][15].setBackground(Color.red);
    cells[30][15].setBackground(Color.red);

    cells[20][16].setBackground(Color.red);
    cells[20][17].setBackground(Color.red);
    cells[20][18].setBackground(Color.red);
    cells[20][19].setBackground(Color.red);
    cells[20][20].setBackground(Color.red);
    cells[20][21].setBackground(Color.red);
    cells[20][22].setBackground(Color.red);
    cells[20][23].setBackground(Color.red);
    cells[20][24].setBackground(Color.red);
    cells[20][25].setBackground(Color.red);
    cells[20][26].setBackground(Color.red);
    cells[20][27].setBackground(Color.red);
    cells[20][28].setBackground(Color.red);
    cells[20][29].setBackground(Color.red);
    cells[20][30].setBackground(Color.red);

    cells[20][30].setBackground(Color.red);
    cells[21][30].setBackground(Color.red);
    cells[22][30].setBackground(Color.red);
    cells[23][30].setBackground(Color.red);
    cells[24][30].setBackground(Color.red);
    cells[25][30].setBackground(Color.red);
    cells[26][30].setBackground(Color.red);
    cells[27][30].setBackground(Color.red);
    cells[28][30].setBackground(Color.red);
    cells[29][30].setBackground(Color.red);
    cells[30][30].setBackground(Color.red);

    cells[30][16].setBackground(Color.red);
    cells[30][17].setBackground(Color.red);
    cells[30][18].setBackground(Color.red);
    cells[30][19].setBackground(Color.red);
    cells[30][20].setBackground(Color.red);
    cells[30][21].setBackground(Color.red);
    cells[30][22].setBackground(Color.red);
    cells[30][23].setBackground(Color.red);
    cells[30][24].setBackground(Color.red);
    cells[30][25].setBackground(Color.red);
    cells[30][26].setBackground(Color.red);
    cells[30][27].setBackground(Color.red);
    cells[30][28].setBackground(Color.red);
    cells[30][29].setBackground(Color.red);
    cells[30][30].setBackground(Color.red);
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {
    reset();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseEntered(MouseEvent e) {
    JPanel p = (JPanel) e.getSource();
    p.setBackground(Color.red);

  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  public static double[] around() {
    double[] d = new double[4];
    d[0] = above();
    d[1] = below();
    d[2] = left();
    d[3] = right();
    return d;
  }

  public static int above() {
    JPanel p = (JPanel) Msn.above(cells, ai.position());
    if (p.getBackground().equals(Color.red)) {
      return 1;
    }
    return 0;
  }

  public static int below() {
    JPanel p = (JPanel) Msn.below(cells, ai.position());
    if (p.getBackground().equals(Color.red)) {
      return 1;
    }
    return 0;
  }

  public static int left() {
    JPanel p = (JPanel) Msn.leftOf(cells, ai.position());
    if (p.getBackground().equals(Color.red)) {
      return 1;
    }
    return 0;
  }

  public static int right() {
    JPanel p = (JPanel) Msn.rightOf(cells, ai.position());
    if (p.getBackground().equals(Color.red)) {
      return 1;
    }
    return 0;
  }

  public static boolean moveUp() {
    JPanel above = (JPanel) Msn.above(cells, ai.position());
    if (above.getBackground().equals(Color.red))
      return false;
    cells[ai.getI()][ai.getJ()].setBackground(Color.black);
    above.setBackground(Color.green);
    ai.setPosition(ai.getI() - 1, ai.getJ());
    return true;
  }

  public static boolean moveLeft() {
    JPanel left = (JPanel) Msn.leftOf(cells, ai.position());
    if (left.getBackground().equals(Color.red))
      return false;
    cells[ai.getI()][ai.getJ()].setBackground(Color.black);
    left.setBackground(Color.green);
    ai.setPosition(ai.getI(), ai.getJ() - 1);
    return true;
  }

  public static boolean moveRight() {
    JPanel right = (JPanel) Msn.rightOf(cells, ai.position());
    if (right.getBackground().equals(Color.red))
      return false;
    cells[ai.getI()][ai.getJ()].setBackground(Color.black);
    right.setBackground(Color.green);
    ai.setPosition(ai.getI(), ai.getJ() + 1);
    return true;
  }

  public static boolean moveDown() {
    JPanel below = (JPanel) Msn.below(cells, ai.position());
    if (below.getBackground().equals(Color.red))
      return false;
    cells[ai.getI()][ai.getJ()].setBackground(Color.black);
    below.setBackground(Color.green);
    ai.setPosition(ai.getI() + 1, ai.getJ());
    return true;
  }

  class Ai extends JPanel {

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

}
