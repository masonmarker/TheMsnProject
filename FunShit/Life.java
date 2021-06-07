import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Simulation of John Conway's Game of Life.
 * 
 * @author Mason Marker
 * @version 1.0 - 06/5/2021
 */
@SuppressWarnings("serial")
public class Life extends JFrame implements MouseListener {

  private JPanel contentPane;

  private static Cell[][] cells;
  private static boolean paused;

  private String preset;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Life frame = new Life();
          frame.setVisible(true);

          SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
              while (true) {
                if (!paused)
                  update();
                Thread.sleep(100);
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
  public Life() {

    preset = "single";
    paused = true;

    setTitle("Life");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 689, 687);
    contentPane = new JPanel();
    contentPane.setBackground(Color.GRAY);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBackground(Color.LIGHT_GRAY);

    JButton pausebutton = new JButton("Unpause");
    pausebutton.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    pausebutton.setFocusPainted(false);
    pausebutton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (pausebutton.getText().equals("Pause")) {
          paused = true;
          pausebutton.setText("Unpause");
        } else {
          paused = false;
          pausebutton.setText("Pause");
        }
      }
    });
    pausebutton.setBackground(new Color(216, 191, 216));
    pausebutton.setForeground(Color.GRAY);

    JPanel panel_1 = new JPanel();
    panel_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_1.setBackground(Color.LIGHT_GRAY);

    JLabel lblNewLabel = new JLabel("Presets ->");
    lblNewLabel.setForeground(Color.WHITE);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
        .addGroup(gl_contentPane.createSequentialGroup()
            .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addComponent(
                    pausebutton, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
                .addGroup(
                    gl_contentPane.createSequentialGroup().addGap(28).addComponent(lblNewLabel)))
            .addGap(18).addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_contentPane.createSequentialGroup()
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addComponent(pausebutton, GroupLayout.PREFERRED_SIZE, 23,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED).addComponent(lblNewLabel)
                    .addGap(5))
                .addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 60,
                    GroupLayout.PREFERRED_SIZE))));

    JButton btnSingle = new JButton("Single");
    btnSingle.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        preset = "single";
      }
    });
    btnSingle.setForeground(Color.GRAY);
    btnSingle.setFocusPainted(false);
    btnSingle.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    btnSingle.setBackground(new Color(216, 191, 216));

    JButton btnGlider = new JButton("Glider");
    btnGlider.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        preset = "glider";
      }
    });
    btnGlider.setForeground(Color.GRAY);
    btnGlider.setFocusPainted(false);
    btnGlider.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    btnGlider.setBackground(new Color(216, 191, 216));

    JButton btnBlinker = new JButton("Blinker");
    btnBlinker.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        preset = "blinker";
      }
    });
    btnBlinker.setForeground(Color.GRAY);
    btnBlinker.setFocusPainted(false);
    btnBlinker.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    btnBlinker.setBackground(new Color(216, 191, 216));

    JButton btnPentadecathlon = new JButton("Penta-Decathlon");
    btnPentadecathlon.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        preset = "penta";
      }
    });
    btnPentadecathlon.setForeground(Color.GRAY);
    btnPentadecathlon.setFocusPainted(false);
    btnPentadecathlon.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    btnPentadecathlon.setBackground(new Color(216, 191, 216));

    JButton btnSmallSpaceship = new JButton("Small Spaceship");
    btnSmallSpaceship.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        preset = "ss";
      }
    });
    btnSmallSpaceship.setForeground(Color.GRAY);
    btnSmallSpaceship.setFocusPainted(false);
    btnSmallSpaceship.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    btnSmallSpaceship.setBackground(new Color(216, 191, 216));

    JButton btnReset = new JButton("Reset");
    btnReset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < cells.length; i++)
          for (int j = 0; j < cells[i].length; j++)
            cells[i][j].turnOff();
      }
    });
    btnReset.setForeground(Color.GRAY);
    btnReset.setFocusPainted(false);
    btnReset.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    btnReset.setBackground(new Color(216, 191, 216));

    JButton btnQuadBlinker = new JButton("Quad Blinker");
    btnQuadBlinker.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        preset = "qb";
      }
    });
    btnQuadBlinker.setForeground(Color.GRAY);
    btnQuadBlinker.setFocusPainted(false);
    btnQuadBlinker.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    btnQuadBlinker.setBackground(new Color(216, 191, 216));
    GroupLayout gl_panel_1 = new GroupLayout(panel_1);
    gl_panel_1
        .setHorizontalGroup(gl_panel_1
            .createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup()
                .addContainerGap().addGroup(gl_panel_1
                    .createParallelGroup(Alignment.LEADING, false)
                    .addComponent(btnSmallSpaceship, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(gl_panel_1.createSequentialGroup().addComponent(btnSingle)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(btnGlider,
                            GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)))
                .addGap(6)
                .addGroup(
                    gl_panel_1.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_1.createSequentialGroup()
                            .addComponent(btnBlinker, GroupLayout.PREFERRED_SIZE, 53,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnPentadecathlon, GroupLayout.PREFERRED_SIZE, 101,
                                GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnQuadBlinker, GroupLayout.PREFERRED_SIZE, 81,
                                GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 39,
                            GroupLayout.PREFERRED_SIZE))
                .addGap(63)));
    gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addGroup(gl_panel_1
            .createParallelGroup(Alignment.LEADING)
            .addComponent(btnQuadBlinker, GroupLayout.PREFERRED_SIZE, 16,
                GroupLayout.PREFERRED_SIZE)
            .addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1
                .createParallelGroup(Alignment.BASELINE)
                .addComponent(btnGlider, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                .addComponent(btnBlinker, GroupLayout.PREFERRED_SIZE, 16,
                    GroupLayout.PREFERRED_SIZE)
                .addComponent(btnPentadecathlon, GroupLayout.PREFERRED_SIZE, 16,
                    GroupLayout.PREFERRED_SIZE)
                .addComponent(btnSingle)).addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
                    .addComponent(btnSmallSpaceship, GroupLayout.PREFERRED_SIZE, 16,
                        GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 16,
                        GroupLayout.PREFERRED_SIZE))))
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    panel_1.setLayout(gl_panel_1);
    panel.setLayout(new GridLayout(50, 75, 0, 0));
    contentPane.setLayout(gl_contentPane);

    cells = new Cell[50][75];
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        cells[i][j] = new Cell(i, j);
        cells[i][j].addMouseListener(this);
        panel.add(cells[i][j]);
      }
    }

    pack();
    setLocationRelativeTo(null);
  }

  public static void update() {
    int[][] neighbors = new int[cells.length][cells[0].length];
    for (int i = 0; i < cells.length; i++)
      for (int j = 0; j < cells[i].length; j++)
        neighbors[i][j] = cells[i][j].aroundLive();

    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        int current = neighbors[i][j];
        if (cells[i][j].isOn()) {
          if (current < 2 || current > 3)
            cells[i][j].turnOff();
          else if (current == 3)
            cells[i][j].turnOn();
        } else if (current == 3)
          cells[i][j].turnOn();
      }
    }
  }

  class Cell extends JPanel {

    private int i;
    private int j;

    public Cell(int i, int j) {
      turnOff();
      this.i = i;
      this.j = j;
    }

    public boolean isOn() {
      return getBackground().equals(Color.white);
    }

    public void turnOn() {
      setBackground(Color.white);
    }

    public void turnOff() {
      setBackground(Color.black);
    }

    public Cell[] around() {
      Object[] adj = Msn.adjacent(cells, new int[] {i, j});
      Cell[] around = new Cell[adj.length];
      for (int i = 0; i < adj.length; i++)
        around[i] = (Cell) adj[i];
      return around;
    }

    public int aroundLive() {
      Cell[] around = around();
      int count = 0;
      for (int i = 0; i < around.length; i++)
        if (around[i].isOn())
          count++;
      return count;
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mousePressed(MouseEvent e) {
    Cell cell = (Cell) e.getSource();
    int[] pos = {cell.i, cell.j};

    try {
      if (preset.equals("glider")) {
        ((Cell) Msn.below(cells, pos)).turnOn();
        ((Cell) Msn.above(cells, pos)).turnOn();
        ((Cell) Msn.swOf(cells, pos)).turnOn();
        ((Cell) Msn.seOf(cells, pos)).turnOn();
        ((Cell) Msn.rightOf(cells, pos)).turnOn();
      } else if (preset.equals("single")) {
        single(cell);
      } else if (preset.equals("blinker")) {
        cell.turnOn();
        ((Cell) Msn.below(cells, pos)).turnOn();
        ((Cell) Msn.above(cells, pos)).turnOn();
      } else if (preset.equals("penta")) {
        cell.turnOn();
        Cell left = (Cell) Msn.leftOf(cells, pos);
        Cell right = (Cell) Msn.rightOf(cells, pos);
        int[] rightpos = {right.i, right.j};
        int[] leftpos = {left.i, left.j};
        ((Cell) Msn.above(cells, pos)).turnOn();
        left.turnOn();
        right.turnOn();
        ((Cell) Msn.neOf(cells, pos)).turnOn();
        ((Cell) Msn.nwOf(cells, pos)).turnOn();
        ((Cell) Msn.directionalMulti(cells, pos, "south", 2, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, pos, "south", 3, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, pos, "south", 4, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, pos, "south", 5, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, leftpos, "south", 2, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, leftpos, "south", 5, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, rightpos, "south", 2, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, rightpos, "south", 5, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, pos, "north", 3, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, pos, "north", 4, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, pos, "north", 5, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, pos, "north", 6, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, leftpos, "north", 3, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, leftpos, "north", 6, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, rightpos, "north", 3, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, rightpos, "north", 6, true)).turnOn();
      } else if (preset.equals("ss")) {
        Cell leftwall = ((Cell) Msn.directionalMulti(cells, pos, "east", 2, true));
        int[] leftwallpos = {leftwall.i, leftwall.j};
        Cell abovewall = ((Cell) Msn.directionalMulti(cells, pos, "north", 2, true));
        int[] abovewallpos = {abovewall.i, abovewall.j};
        Cell rightwall = ((Cell) Msn.directionalMulti(cells, pos, "west", 2, true));
        int[] rightwallpos = {rightwall.i, rightwall.j};
        ((Cell) Msn.seOf(cells, pos)).turnOn();
        leftwall.turnOn();
        ((Cell) Msn.directionalMulti(cells, pos, "north", 2, true)).turnOn();
        ((Cell) Msn.directionalMulti(cells, pos, "ne", 2, true)).turnOn();
        ((Cell) Msn.above(cells, leftwallpos)).turnOn();
        ((Cell) Msn.leftOf(cells, abovewallpos)).turnOn();
        ((Cell) Msn.rightOf(cells, abovewallpos)).turnOn();
        ((Cell) Msn.above(cells, rightwallpos)).turnOn();
        ((Cell) Msn.below(cells, rightwallpos)).turnOn();
      } else if (preset.equals("qb")) {
        Cell[] around = cell.around();
        for (int i = 0; i < around.length; i++)
          around[i].turnOn();
      }
    } catch (Exception e1) {
      single(cell);
    }
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
    // TODO Auto-generated method stub

  }

  public void single(Cell c) {
    if (c.isOn())
      c.turnOff();
    else
      c.turnOn();
  }

}
