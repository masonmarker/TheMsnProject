import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class CrossHair extends JFrame implements MouseListener {

  private JPanel contentPane;

  private static Cell[][] cells;

  private Cell[] union3;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          CrossHair frame = new CrossHair();
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
  public CrossHair() {

    setUndecorated(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 682, 644);
    contentPane = new JPanel();
    contentPane.setBackground(Color.GRAY);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBackground(Color.PINK);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE));
    panel.setLayout(new GridLayout(80, 125, 0, 0));
    contentPane.setLayout(gl_contentPane);

    cells = new Cell[80][125];
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
    // for (int i = 0; i < cells.length; i++) {
    // for (int j = 0; j < cells[i].length; j++) {
    // if (Msn.coinflip())
    // cells[i][j].setBackground(Msn.multiBrighten(cells[i][j].getBackground(), 3));
    // else
    // cells[i][j].setBackground(Msn.multiDarken(cells[i][j].getBackground(), 3));
    // }
    // }
  }

  public Cell[] convert(Object[] array) {
    Cell[] conv = new Cell[array.length];
    for (int i = 0; i < array.length; i++)
      conv[i] = (Cell) array[i];
    return conv;
  }

  class Cell extends JPanel {

    private int i;
    private int j;

    public Cell(int i, int j) {
      setBackground(Color.black);
      this.i = i;
      this.j = j;
    }

    public Cell[] convert(Object[] array) {
      Cell[] conv = new Cell[array.length];
      for (int i = 0; i < array.length; i++)
        conv[i] = (Cell) array[i];
      return conv;
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mousePressed(MouseEvent e) {
    Cell cell = (Cell) e.getSource();
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "nw", i, true)).setBackground(Color.red);
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "ne", i, true)).setBackground(Color.red);
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "sw", i, true)).setBackground(Color.red);
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "se", i, true)).setBackground(Color.red);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    Cell cell = (Cell) e.getSource();
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "nw", i, true)).setBackground(Color.black);
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "ne", i, true)).setBackground(Color.black);
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "sw", i, true)).setBackground(Color.black);
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "se", i, true)).setBackground(Color.black);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    Cell cell = (Cell) e.getSource();
    int[] pos = {cell.i, cell.j};
    cell.setBackground(Color.cyan);
    Cell[] allLeft = convert(Msn.leftAll(cells, pos));
    Cell[] allRight = convert(Msn.rightAll(cells, pos));
    Cell[] allBelow = convert(Msn.belowAll(cells, pos));
    Cell[] allAbove = convert(Msn.aboveAll(cells, pos));

    union3 = convert(Msn.union(false, allLeft, allRight, allBelow, allAbove));

    for (int i = 0; i < allLeft.length; i++)
      allLeft[i].setBackground(Color.cyan);

    for (int i = 0; i < allRight.length; i++)
      allRight[i].setBackground(Color.cyan);

    for (int i = 0; i < allBelow.length; i++)
      allBelow[i].setBackground(Color.cyan);

    for (int i = 0; i < allAbove.length; i++)
      allAbove[i].setBackground(Color.cyan);

  }

  @Override
  public void mouseExited(MouseEvent e) {
    Cell cell = (Cell) e.getSource();
    ((Cell) e.getSource()).setBackground(Color.black);
    for (int i = 0; i < union3.length; i++)
      union3[i].setBackground(Color.black);
    
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "nw", i, true)).setBackground(Color.black);
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "ne", i, true)).setBackground(Color.black);
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "sw", i, true)).setBackground(Color.black);
    for (int i = 0; i < 30; i++) 
      ((Cell) Msn.directionalMulti(cells, new int[] {cell.i, cell.j}, "se", i, true)).setBackground(Color.black);
  }

}
