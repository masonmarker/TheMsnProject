import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Used for quick creation and display of data on the front-end.
 * 
 * @author Mason Marker
 * @version 1.0 - 05/31/2022
 */
public class DataFrame extends JFrame {

  private static final long serialVersionUID = 4784561148866622355L;
  private JPanel contentPane;

  /**
   * Create the frame.
   */
  public DataFrame(String[][] data) {
    createAndShowFrame(data);
  }

  public DataFrame(String[] data) {
    String[][] newD = new String[1][data.length];
    for (int i = 0; i < newD[0].length; i++) {
      newD[0][i] = data[i];
    }
    createAndShowFrame(newD);
  }



  public void createAndShowFrame(String[][] data) {
    Timer t = new Timer();
    t.start();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 672, 516);
    contentPane = new JPanel();
    contentPane.setBackground(Color.LIGHT_GRAY);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBackground(Color.BLACK);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE));
    panel.setLayout(new GridLayout(data.length, data[0].length, 0, 0));
    contentPane.setLayout(gl_contentPane);

    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        JLabel l = new JLabel(data[i][j]);
        l.setHorizontalTextPosition(SwingConstants.CENTER);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setForeground(Color.white);
        l.setBackground(Color.black);
        l.setOpaque(true);

        panel.add(l);
      }
    }
    setLocationRelativeTo(null);
    pack();
    t.stop();
    setTitle("time to create data frame : " + t.runtime() + " ms");
    
    setVisible(true);
  }


}
