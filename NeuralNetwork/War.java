import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

/**
 * Watch 3 neural network agents attempt to coexist!
 * 
 * The concept of this scenario is similar to NeuralNetworkGame and the Tron concepts, however this
 * time there exist multiple agents and each one learns by observing their immediate surroundings,
 * including diagonals.
 * 
 * @author Mason Marker
 * @version 1.0 - 05/18/2021
 */
@SuppressWarnings("serial")
public class War extends JFrame {

  private JPanel contentPane;
  private static JPanel[][] cells;
  private static ArrayList<Ai> ais;

  private static int speed = 0; // ms

  /**
   * Launch the application.
   */
  public static void main(String[] args) {

    War frame = new War();
    frame.setVisible(true);

    SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        while (true) {
          ais.forEach(a -> a.makeMove());
          Thread.sleep(speed);
        }
      }
    };
    w.execute();
  }


  /**
   * Create the frame.
   */
  public War() {

    ais = new ArrayList<>();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 501, 473);
    contentPane = new JPanel();
    contentPane.setBackground(Color.GRAY);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBackground(Color.GRAY);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE));
    panel.setLayout(new GridLayout(50, 50, 0, 0));
    contentPane.setLayout(gl_contentPane);

    cells = new JPanel[50][50];
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        cells[i][j] = new JPanel();
        cells[i][j].setBackground(Color.gray);
        panel.add(cells[i][j]);
      }
    }

    Ai ai1 = new Ai(15, 45, Color.green, 6);
    ais.add(ai1);
    cells[15][45] = ai1;

    Ai ai2 = new Ai(45, 15, Color.blue.brighter(), 7);
    ais.add(ai2);
    cells[45][15] = ai2;

    Ai ai3 = new Ai(25, 37, Color.black, 8);
    ais.add(ai3);
    cells[25][37] = ai3;

    wallInit();
    pack();
    setLocationRelativeTo(null);
  }

  public void wallInit() {
    for (int i = 0; i < cells.length; i++) {
      cells[0][i].setBackground(Color.red);
      cells[i][0].setBackground(Color.red);
      cells[cells.length - 1][i].setBackground(Color.red);
      cells[i][cells.length - 1].setBackground(Color.red);
    }
  }

  class Ai extends JPanel {

    private int[] pos;
    private int[] origin;
    private Network n;
    private ArrayList<JPanel> trail;
    private Color c;

    public Ai(int i, int j, Color c, int hidden) {
      setBackground(c);
      this.c = c;
      pos = new int[] {i, j};
      origin = new int[] {i, j};
      n = new Network(8, 2, hidden, 1);
      trail = new ArrayList<>();
    }

    public ArrayList<JPanel> trail() {
      return trail;
    }

    public Network network() {
      return n;
    }

    public int[] pos() {
      return pos;
    }

    public void setPos(int i, int j) {
      pos[0] = i;
      pos[1] = j;
    }

    public void reset() {
      trail.forEach(j -> j.setBackground(Color.gray));
      trail = new ArrayList<>();
      cells[pos[0]][pos[1]].setBackground(Color.gray);
      setPos(origin[0], origin[1]);
    }

    public double[] adjacent() {
      Object[] obj = Msn.adjacent(cells, pos);
      JPanel[] adj = new JPanel[obj.length];
      double[] inputs = new double[obj.length];
      for (int i = 0; i < obj.length; i++)
        adj[i] = (JPanel) obj[i];
      for (int i = 0; i < adj.length; i++)
        if (!adj[i].getBackground().equals(Color.gray))
          inputs[i] = 1.0;
        else
          inputs[i] = 0.0;
      return inputs;
    }

    public void makeMove() {
      double[] adj = adjacent();
      double[] possible = {0, .33, .66, 1};
      double output = Msn.closestTo(n.getAnswer(adj), possible);

      if (output == 0) {
        if (moveUp(adj)) {
          n.train(adj, 0, 10000);
        } else {
          double rand = Msn.randomElement(possible);
          while (rand == 0)
            rand = Msn.randomElement(possible);
          n.train(adj, rand, 5000);
          reset();
        }
      } else if (output == .33) {
        if (moveDown(adj))
          n.train(adj, .33, 10000);
        else {
          double rand = Msn.randomElement(possible);
          while (rand == .33)
            rand = Msn.randomElement(possible);
          n.train(adj, rand, 5000);
          reset();
        }
      } else if (output == .66) {
        if (moveLeft(adj)) {
          n.train(adj, .66, 10000);
        } else {
          double rand = Msn.randomElement(possible);
          while (rand == .66)
            rand = Msn.randomElement(possible);
          n.train(adj, rand, 5000);
          reset();
        }
      } else {
        if (moveRight(adj))
          n.train(adj, 1, 10000);
        else {
          double rand = Msn.randomElement(possible);
          while (rand == 1)
            rand = Msn.randomElement(possible);
          n.train(adj, rand, 5000);
          reset();
        }
      }
    }

    public boolean moveUp(double[] adj) {
      if (adj[1] == 1)
        return false;
      else {
        JPanel above = (JPanel) Msn.above(cells, pos);
        cells[pos[0]][pos[1]].setBackground(Color.red);
        trail.add(cells[pos[0]][pos[1]]);
        above.setBackground(c);
        setPos(pos[0] - 1, pos[1]);
        return true;
      }
    }

    public boolean moveDown(double[] adj) {
      if (adj[5] == 1)
        return false;
      else {
        JPanel below = (JPanel) Msn.below(cells, pos);
        cells[pos[0]][pos[1]].setBackground(Color.red);
        trail.add(cells[pos[0]][pos[1]]);
        below.setBackground(c);
        setPos(pos[0] + 1, pos[1]);
        return true;
      }
    }

    public boolean moveLeft(double[] adj) {
      if (adj[7] == 1)
        return false;
      else {
        JPanel left = (JPanel) Msn.leftOf(cells, pos);
        cells[pos[0]][pos[1]].setBackground(Color.red);
        trail.add(cells[pos[0]][pos[1]]);
        left.setBackground(c);
        setPos(pos[0], pos[1] - 1);
        return true;
      }
    }

    public boolean moveRight(double[] adj) {
      if (adj[3] == 1)
        return false;
      else {
        JPanel right = (JPanel) Msn.rightOf(cells, pos);
        cells[pos[0]][pos[1]].setBackground(Color.red);
        trail.add(cells[pos[0]][pos[1]]);
        right.setBackground(c);
        setPos(pos[0], pos[1] + 1);
        return true;
      }
    }

    public String toString() {
      return "AI: " + Arrays.toString(pos);
    }

  }



}
