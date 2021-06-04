import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Watch a Neural Network agent attempt to dodge obstacles thrown at it.
 * 
 * The agent can either stay put, move left, or move right per timestep, observe how it uses its
 * surroundings to make its decisions and slowly but surely grasp a crude sense of collision
 * detection.
 * 
 * @author Mason Marker
 * @version 1.0 - 06/02/2021
 */
@SuppressWarnings("serial")
public class Space extends JFrame {

  private JPanel contentPane;
  private static JPanel[][] cells;
  private static ArrayList<Obstacle> obstacles;
  private static Ai ai;

  private static int speed;
  private static int congestion;

  private static long starttime;

  private static int deaths;
  private static JTextField textField;
  private static JTextField textField_1;
  private static JLabel timealivelabel;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Space frame = new Space();
          frame.setVisible(true);
          SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
              while (true) {
                update();
                Thread.sleep(speed);
                frame.revalidate();
                frame.repaint();
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
  public Space() {

    congestion = 3;
    speed = 50;
    deaths = 0;
    obstacles = new ArrayList<>();

    setTitle("Space v1.0");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setBounds(100, 100, 650, 784);
    contentPane = new JPanel();
    contentPane.setBackground(Color.BLACK);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBorder(new LineBorder(Color.WHITE, 2));
    panel.setBackground(Color.BLACK);

    textField = new JTextField();
    textField.setText("50");
    textField.setColumns(10);

    JLabel lblNewLabel = new JLabel("ms per timestep");
    lblNewLabel.setForeground(Color.WHITE);
    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

    JTextArea txtrThisApplicationDemonstrates = new JTextArea();
    txtrThisApplicationDemonstrates.setEditable(false);
    txtrThisApplicationDemonstrates.setFont(new Font("Tahoma", Font.PLAIN, 11));
    txtrThisApplicationDemonstrates.setText(
        "This application demonstrates the Network object, whereas inputs are the squares around the AI agent (green), and the output is either staying put, moving left, or moving right per timestep.");
    txtrThisApplicationDemonstrates.setBackground(Color.BLACK);
    txtrThisApplicationDemonstrates.setForeground(Color.WHITE);
    txtrThisApplicationDemonstrates.setLineWrap(true);
    txtrThisApplicationDemonstrates.setWrapStyleWord(true);

    textField_1 = new JTextField();
    textField_1.setText("3");
    textField_1.setColumns(10);

    JLabel lblObstacleCongestion = new JLabel("obstacle congestion (1-30)");
    lblObstacleCongestion.setForeground(Color.WHITE);

    timealivelabel = new JLabel("time alive: 0s");
    timealivelabel.setForeground(Color.WHITE);
    timealivelabel.setHorizontalAlignment(SwingConstants.CENTER);

    JButton btnNewButton = new JButton("default values");
    btnNewButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        textField.setText("50");
        textField_1.setText("3");
      }
    });
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
        .addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
            .addComponent(txtrThisApplicationDemonstrates, GroupLayout.PREFERRED_SIZE, 147,
                GroupLayout.PREFERRED_SIZE)
            .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup().addGap(141)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addComponent(textField, GroupLayout.PREFERRED_SIZE, 88,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED).addComponent(lblNewLabel))
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 88,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(lblObstacleCongestion, GroupLayout.PREFERRED_SIZE, 169,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnNewButton))))
                .addGroup(gl_contentPane.createSequentialGroup().addGap(162)
                    .addComponent(timealivelabel)))
            .addGap(34)));
    gl_contentPane
        .setVerticalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(
                    gl_contentPane.createSequentialGroup()
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                        .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_contentPane.createSequentialGroup().addGap(8).addComponent(
                                txtrThisApplicationDemonstrates, GroupLayout.PREFERRED_SIZE, 124,
                                GroupLayout.PREFERRED_SIZE))
                            .addGroup(gl_contentPane.createSequentialGroup().addGap(37)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                    .addComponent(textField, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNewLabel))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                    .addComponent(textField_1, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblObstacleCongestion).addComponent(btnNewButton))
                                .addGap(18).addComponent(timealivelabel)))
                        .addContainerGap()));
    panel.setLayout(new GridLayout(50, 50, 0, 0));
    contentPane.setLayout(gl_contentPane);

    cells = new JPanel[50][50];
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        cells[i][j] = new JPanel();
        cells[i][j].setBackground(Color.black);
        panel.add(cells[i][j]);
      }
    }

    wallInit();
    spawn();
    pack();
    setLocationRelativeTo(null);
  }

  public static void update() {

    try {
      speed = Integer.valueOf(textField.getText());
    } catch (Exception e) {
      speed = 50;
    }

    try {
      congestion = Integer.valueOf(textField_1.getText());
    } catch (Exception e) {
      congestion = 3;
    }

    if (Msn.coinflip()) {
      JPanel[] obscanspawn = obstaclespawnable();
      for (int i = 0; i < congestion; i++) {
        int randindex = Msn.randomIndex(obscanspawn);
        Obstacle current = new Obstacle(0, randindex);
        cells[0][randindex] = current;
        obstacles.add(current);
      }
    }

    ai.move();
    obstacles.forEach(o -> o.advance());
    wallInit();
    cells[ai.i][ai.j].setBackground(Color.green);
    timealivelabel.setText(
        "time alive: " + (TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - starttime)) + "s");
  }

  public static void wallInit() {
    cells[cells.length - 2][0].setBackground(Color.red);
    cells[cells.length - 2][cells[0].length - 1].setBackground(Color.red);
  }

  public static JPanel[] obstaclespawnable() {
    Object[] possible = cells[0];
    JPanel[] converted = new JPanel[possible.length];
    ArrayList<JPanel> spwnable = new ArrayList<>();
    for (int i = 0; i < converted.length; i++)
      converted[i] = (JPanel) possible[i];
    for (int i = 0; i < converted.length; i++)
      if (converted[i].getBackground().equals(Color.black))
        spwnable.add(converted[i]);
    return spwnable.toArray(JPanel[]::new);
  }

  public void spawn() {
    ai = new Ai(cells.length - 2, Msn.randomIndex(spawnable()));
    cells[ai.i][ai.j] = ai;
    starttime = System.nanoTime();
  }

  public void respawn() {
    starttime = System.nanoTime();
    cells[ai.i][ai.j].setBackground(Color.black);
    ai.setJ(Msn.randomIndex(spawnable()));
    cells[ai.i][ai.j].setBackground(Color.green);
    deaths++;
    System.out.println("[-] respawned, deaths: " + deaths);
  }

  public JPanel[] spawnable() {
    Object[] bottom = cells[cells.length - 2];
    JPanel[] converted = new JPanel[bottom.length];
    ArrayList<JPanel> blacks = new ArrayList<>();
    for (int i = 0; i < bottom.length; i++)
      converted[i] = (JPanel) bottom[i];
    for (int i = 0; i < converted.length; i++)
      if (converted[i].getBackground().equals(Color.black) && i != 0 && i != converted.length - 1)
        blacks.add(converted[i]);
    return blacks.toArray(JPanel[]::new);
  }

  static class Obstacle extends JPanel {

    private int i;
    private int j;

    public Obstacle(int i, int j) {
      setBackground(Color.red);
      this.i = i;
      this.j = j;
    }

    public void advance() {
      JPanel below = (JPanel) Msn.below(cells, new int[] {i, j});
      if (below == null || below.getBackground().equals(Color.red)) {
        cells[i][j].setBackground(Color.black);
      } else {
        cells[i][j].setBackground(Color.black);
        below.setBackground(Color.red);
        i++;
      }
    }
  }

  class Ai extends JPanel {

    private Network network;

    private int i;
    private int j;
    private int score;

    public Ai(int i, int j) {
      setBackground(Color.green);
      network = new Network(5, 2, 5, 1);
      score = 0;
      this.i = i;
      this.j = j;
    }

    public Network network() {
      return network;
    }

    public void setI(int i) {
      this.i = i;
    }

    public void setJ(int j) {
      this.j = j;
    }

    public int[] pos() {
      return new int[] {i, j};
    }

    public int score() {
      return score;
    }

    public double[] around() {
      double[] around = new double[5];
      JPanel left = (JPanel) Msn.leftOf(cells, pos());
      JPanel right = (JPanel) Msn.rightOf(cells, pos());
      JPanel above = (JPanel) Msn.above(cells, pos());
      JPanel nw = (JPanel) Msn.nwOf(cells, pos());
      JPanel ne = (JPanel) Msn.neOf(cells, pos());
      if (left == null || !left.getBackground().equals(Color.black))
        around[0] = 1.0;
      else
        around[0] = 0.0;
      if (nw == null || !nw.getBackground().equals(Color.black))
        around[1] = 1.0;
      else
        around[1] = 0.0;
      if (above == null || !above.getBackground().equals(Color.black))
        around[2] = 1.0;
      else
        around[2] = 0.0;
      if (ne == null || !ne.getBackground().equals(Color.black))
        around[3] = 1.0;
      else
        around[3] = 0.0;
      if (right == null || !right.getBackground().equals(Color.black))
        around[4] = 1.0;
      else
        around[4] = 0.0;
      return around;
    }

    public void move() {
      double[] around = around();
      double[] possible = {0, .5, 1};
      double netans = Msn.closestTo(network.getAnswer(around), possible);

      if (netans == 0) {
        if (moveRight())
          network.train(around, 0, 10000);
        else {
          double rand = Msn.randomElement(possible);
          while (rand == 0)
            rand = Msn.randomElement(possible);
          network.train(around, rand, 1000);
          respawn();
        }
      } else if (netans == .5) {
        if (stay())
          network.train(around, .5, 10000);
        else {
          double rand = Msn.randomElement(possible);
          while (rand == .5)
            rand = Msn.randomElement(possible);
          network.train(around, rand, 1000);
          respawn();
        }
      } else {
        if (moveLeft())
          network.train(around, 1, 10000);
        else {
          double rand = Msn.randomElement(possible);
          while (rand == 1)
            rand = Msn.randomElement(possible);
          network.train(around, rand, 1000);
          respawn();
        }
      }
    }

    public boolean stay() {
      JPanel above = (JPanel) Msn.above(cells, pos());
      if (above.getBackground().equals(Color.red))
        return false;
      return true;
    }

    public boolean moveRight() {
      JPanel ne = (JPanel) Msn.neOf(cells, pos());
      JPanel right = (JPanel) Msn.rightOf(cells, pos());
      if (right == null || ne == null || !ne.getBackground().equals(Color.black)
          || !right.getBackground().equals(Color.black)) {
        return false;
      }
      cells[i][j].setBackground(Color.black);
      setJ(j + 1);
      int[] pos = pos();
      cells[pos[0]][pos[1]].setBackground(Color.green);
      return true;
    }

    public boolean moveLeft() {
      JPanel nw = (JPanel) Msn.nwOf(cells, pos());
      JPanel left = (JPanel) Msn.leftOf(cells, pos());
      if (left == null || nw == null || !nw.getBackground().equals(Color.black)
          || !left.getBackground().equals(Color.black))
        return false;
      cells[i][j].setBackground(Color.black);
      setJ(j - 1);
      int[] pos = pos();
      cells[pos[0]][pos[1]].setBackground(Color.green);
      return true;
    }

    public String toString() {
      return "{i=" + i + ", j=" + j + "}";
    }

  }
}
