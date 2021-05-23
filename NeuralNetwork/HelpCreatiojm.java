import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class HelpCreatiojm extends JFrame {

  private JPanel contentPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          HelpCreatiojm frame = new HelpCreatiojm();
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
  public HelpCreatiojm() {
    
    setTitle("Current Network Training Information");
    
    setBounds(100, 100, 384, 364);
    contentPane = new JPanel();
    contentPane.setBackground(Color.GRAY);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    
    JPanel panel = new JPanel();
    panel.setBackground(Color.GRAY);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(
      gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
    );
    gl_contentPane.setVerticalGroup(
      gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
    );
    
    JScrollPane scrollPane = new JScrollPane();
    
    JLabel Inputslabel = new JLabel("Inputs");
    Inputslabel.setHorizontalAlignment(SwingConstants.CENTER);
    Inputslabel.setForeground(Color.WHITE);
    
    JLabel lblTarget = new JLabel("Target");
    lblTarget.setHorizontalAlignment(SwingConstants.CENTER);
    lblTarget.setForeground(Color.WHITE);
    
    JLabel lblPercentError = new JLabel("Percent Error");
    lblPercentError.setHorizontalAlignment(SwingConstants.CENTER);
    lblPercentError.setForeground(Color.WHITE);
    GroupLayout gl_panel = new GroupLayout(panel);
    gl_panel.setHorizontalGroup(
      gl_panel.createParallelGroup(Alignment.LEADING)
        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
        .addGroup(gl_panel.createSequentialGroup()
          .addContainerGap()
          .addComponent(Inputslabel, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
          .addGap(69)
          .addComponent(lblTarget, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
          .addGap(114)
          .addComponent(lblPercentError, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
          .addGap(60))
    );
    gl_panel.setVerticalGroup(
      gl_panel.createParallelGroup(Alignment.TRAILING)
        .addGroup(gl_panel.createSequentialGroup()
          .addContainerGap(14, Short.MAX_VALUE)
          .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
            .addComponent(Inputslabel)
            .addComponent(lblTarget)
            .addComponent(lblPercentError))
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 349, GroupLayout.PREFERRED_SIZE))
    );
    
    JPanel panel_1 = new JPanel();
    panel_1.setBackground(Color.LIGHT_GRAY);
    scrollPane.setViewportView(panel_1);
    panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
    panel.setLayout(gl_panel);
    contentPane.setLayout(gl_contentPane);
    
    pack();
    setLocationRelativeTo(null);
  }
}
