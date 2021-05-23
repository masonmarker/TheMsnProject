import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

@SuppressWarnings("serial")
public class NetworkVisualization extends JFrame {

  private JPanel contentPane;
  private JTextField targetfield;

  private Network network;
  private double[] inputs = {1.0, 3.1, 7.4};
  private double target;
  private JLabel percenterror;

  private JLabel in1output;
  private JLabel in1error;
  private JLabel in1bias;

  private JLabel in2output;
  private JLabel in2error;
  private JLabel in2bias;

  private JLabel in3output;
  private JLabel in3error;
  private JLabel in3bias;

  private JLabel hn11output;
  private JLabel hn11error;
  private JLabel hn11bias;

  private JLabel hn12output;
  private JLabel hn12error;
  private JLabel hn12bias;

  private JLabel hn13output;
  private JLabel hn13error;
  private JLabel hn13bias;

  private JLabel hn21output;
  private JLabel hn21error;
  private JLabel hn21bias;

  private JLabel hn22output;
  private JLabel hn22error;
  private JLabel hn22bias;

  private JLabel hn23output;
  private JLabel hn23error;
  private JLabel hn23bias;

  private JLabel opoutput;
  private JLabel operror;
  private JLabel opbias;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          NetworkVisualization frame = new NetworkVisualization();
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
  public NetworkVisualization() {

    network = new Network(3, 2, 3, 1);

    setTitle("Msn Neural Network Visualization 1.0");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 856, 743);
    contentPane = new JPanel();
    contentPane.setBackground(new Color(169, 169, 169));
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
    panel.setBackground(Color.LIGHT_GRAY);

    JPanel panel_1 = new JPanel();
    panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
    panel_1.setBackground(new Color(128, 128, 128));
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
        .addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_contentPane.createSequentialGroup()
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)));
    panel_1.setLayout(new GridLayout(1, 3, 0, 0));

    JPanel panel_7 = new JPanel();
    panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
    panel_7.setBackground(Color.GRAY);
    panel_1.add(panel_7);

    JLabel lblNewLabel_1 = new JLabel("~ Network Information ~");
    lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel_1.setForeground(Color.WHITE);

    JLabel lblNewLabel_1_1 = new JLabel("3 Input Neurons");
    lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel_1_1.setForeground(Color.WHITE);

    JLabel lblNewLabel_1_1_1 = new JLabel("2 Hidden Layers");
    lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel_1_1_1.setForeground(Color.WHITE);

    JLabel lblNewLabel_1_1_1_1 = new JLabel("6 Hidden Neurons");
    lblNewLabel_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel_1_1_1_1.setForeground(Color.WHITE);

    JLabel lblNewLabel_1_1_1_1_1 = new JLabel("1 Output Neuron");
    lblNewLabel_1_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel_1_1_1_1_1.setForeground(Color.WHITE);
    GroupLayout gl_panel_7 = new GroupLayout(panel_7);
    gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_7.createSequentialGroup().addGap(77)
            .addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_7.createSequentialGroup()
                    .addComponent(lblNewLabel_1_1_1, GroupLayout.PREFERRED_SIZE, 56,
                        Short.MAX_VALUE)
                    .addGap(75))
                .addGroup(gl_panel_7.createSequentialGroup()
                    .addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 121,
                            Short.MAX_VALUE)
                        .addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(75))))
        .addGroup(gl_panel_7.createSequentialGroup().addGap(44)
            .addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
                .addComponent(lblNewLabel_1_1_1_1_1, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addComponent(lblNewLabel_1_1_1_1, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
            .addGap(43)));
    gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_7.createSequentialGroup().addContainerGap()
            .addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(lblNewLabel_1_1, GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(lblNewLabel_1_1_1)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(lblNewLabel_1_1_1_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(lblNewLabel_1_1_1_1_1)
            .addGap(46)));
    panel_7.setLayout(gl_panel_7);

    JPanel panel_8 = new JPanel();
    panel_8.setBorder(new LineBorder(Color.BLACK));
    panel_8.setBackground(Color.GRAY);
    panel_1.add(panel_8);

    JLabel lblNewLabel_1_2 = new JLabel("Set Target Value (0 - 1)");
    lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel_1_2.setForeground(Color.WHITE);

    targetfield = new JTextField();
    targetfield.setColumns(10);

    JButton trainbutton = new JButton("Train");
    trainbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          target = Double.valueOf(targetfield.getText());
          network.train(inputs, target, 50);
          update();
        } catch (Exception e1) {
          targetfield.setText("Invalid Target");
        }
      }
    });

    JLabel lblNewLabel_1_2_2 = new JLabel("Trains the Network for 50 iterations");
    lblNewLabel_1_2_2.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel_1_2_2.setForeground(Color.WHITE);
    GroupLayout gl_panel_8 = new GroupLayout(panel_8);
    gl_panel_8.setHorizontalGroup(gl_panel_8.createParallelGroup(Alignment.TRAILING)
        .addGroup(Alignment.LEADING,
            gl_panel_8.createSequentialGroup().addGap(94).addComponent(targetfield).addGap(93))
        .addGroup(gl_panel_8.createSequentialGroup().addGap(93)
            .addComponent(trainbutton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addGap(91))
        .addGroup(Alignment.LEADING,
            gl_panel_8.createSequentialGroup().addGap(77)
                .addComponent(lblNewLabel_1_2, GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addGap(75))
        .addGroup(gl_panel_8.createSequentialGroup().addGap(29)
            .addComponent(lblNewLabel_1_2_2, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
            .addGap(28)));
    gl_panel_8.setVerticalGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_8.createSequentialGroup().addContainerGap().addComponent(lblNewLabel_1_2)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(targetfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE)
            .addGap(18).addComponent(lblNewLabel_1_2_2).addGap(4).addComponent(trainbutton)
            .addContainerGap(41, Short.MAX_VALUE)));
    panel_8.setLayout(gl_panel_8);

    JPanel panel_9 = new JPanel();
    panel_9.setBorder(new LineBorder(Color.BLACK));
    panel_9.setBackground(Color.GRAY);
    panel_1.add(panel_9);

    JLabel lblNewLabel_1_2_1 = new JLabel("Network Accuracy");
    lblNewLabel_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel_1_2_1.setForeground(Color.WHITE);

    percenterror = new JLabel("Percent Error: 0");
    percenterror.setHorizontalAlignment(SwingConstants.CENTER);
    percenterror.setForeground(Color.WHITE);
    GroupLayout gl_panel_9 = new GroupLayout(panel_9);
    gl_panel_9
        .setHorizontalGroup(gl_panel_9.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_9
            .createSequentialGroup().addGroup(gl_panel_9.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING,
                    gl_panel_9.createSequentialGroup().addGap(77).addComponent(lblNewLabel_1_2_1,
                        GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                .addGroup(Alignment.TRAILING, gl_panel_9.createSequentialGroup().addGap(77)
                    .addComponent(percenterror, GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)))
            .addGap(75)));
    gl_panel_9.setVerticalGroup(gl_panel_9.createParallelGroup(Alignment.LEADING).addGroup(
        gl_panel_9.createSequentialGroup().addContainerGap().addComponent(lblNewLabel_1_2_1)
            .addGap(41).addComponent(percenterror).addContainerGap(71, Short.MAX_VALUE)));
    panel_9.setLayout(gl_panel_9);
    panel.setLayout(new GridLayout(1, 4, 0, 0));

    JPanel panel_2 = new JPanel();
    panel_2.setBackground(Color.LIGHT_GRAY);
    panel.add(panel_2);

    JLabel lblHidden_1_1 = new JLabel("Input");
    lblHidden_1_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblHidden_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));

    JPanel panel_6_1_1_1 = new JPanel();
    panel_6_1_1_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_6_1_1_1.setBackground(SystemColor.inactiveCaption);

    in1bias = new JLabel("Bias: 0");
    in1bias.setFont(new Font("Tahoma", Font.PLAIN, 9));
    in1bias.setHorizontalAlignment(SwingConstants.CENTER);

    in1error = new JLabel("Error: 0");
    in1error.setFont(new Font("Tahoma", Font.PLAIN, 9));
    in1error.setHorizontalAlignment(SwingConstants.CENTER);

    in1output = new JLabel("Output: 0");
    in1output.setFont(new Font("Tahoma", Font.PLAIN, 9));
    in1output.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_6_1_1_1 = new GroupLayout(panel_6_1_1_1);
    gl_panel_6_1_1_1.setHorizontalGroup(gl_panel_6_1_1_1.createParallelGroup(Alignment.LEADING)
        .addGap(0, 113, Short.MAX_VALUE).addGap(0, 111, Short.MAX_VALUE)
        .addGroup(gl_panel_6_1_1_1.createSequentialGroup().addGap(19)
            .addGroup(gl_panel_6_1_1_1.createParallelGroup(Alignment.LEADING)
                .addComponent(in1bias, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addComponent(in1error, GroupLayout.PREFERRED_SIZE, 70, Short.MAX_VALUE)
                .addComponent(in1output, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
            .addGap(22)));
    gl_panel_6_1_1_1.setVerticalGroup(gl_panel_6_1_1_1.createParallelGroup(Alignment.LEADING)
        .addGap(0, 57, Short.MAX_VALUE).addGap(0, 55, Short.MAX_VALUE)
        .addGroup(gl_panel_6_1_1_1.createSequentialGroup().addComponent(in1output)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(in1error)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(in1bias)
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    panel_6_1_1_1.setLayout(gl_panel_6_1_1_1);

    JPanel panel_6_1_1_1_1 = new JPanel();
    panel_6_1_1_1_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_6_1_1_1_1.setBackground(SystemColor.inactiveCaption);

    in2bias = new JLabel("Bias: 0");
    in2bias.setFont(new Font("Tahoma", Font.PLAIN, 9));
    in2bias.setHorizontalAlignment(SwingConstants.CENTER);

    in2error = new JLabel("Error: 0");
    in2error.setFont(new Font("Tahoma", Font.PLAIN, 9));
    in2error.setHorizontalAlignment(SwingConstants.CENTER);

    in2output = new JLabel("Output: 0");
    in2output.setFont(new Font("Tahoma", Font.PLAIN, 9));
    in2output.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_6_1_1_1_1 = new GroupLayout(panel_6_1_1_1_1);
    gl_panel_6_1_1_1_1.setHorizontalGroup(
        gl_panel_6_1_1_1_1.createParallelGroup(Alignment.LEADING).addGap(0, 113, Short.MAX_VALUE)
            .addGap(0, 113, Short.MAX_VALUE).addGap(0, 111, Short.MAX_VALUE)
            .addGroup(gl_panel_6_1_1_1_1.createSequentialGroup().addGap(19)
                .addGroup(gl_panel_6_1_1_1_1.createParallelGroup(Alignment.LEADING)
                    .addComponent(in2bias, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(in2error, GroupLayout.PREFERRED_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(in2output, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addGap(22)));
    gl_panel_6_1_1_1_1.setVerticalGroup(
        gl_panel_6_1_1_1_1.createParallelGroup(Alignment.LEADING).addGap(0, 57, Short.MAX_VALUE)
            .addGap(0, 57, Short.MAX_VALUE).addGap(0, 55, Short.MAX_VALUE)
            .addGroup(gl_panel_6_1_1_1_1.createSequentialGroup().addComponent(in2output)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(in2error)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(in2bias)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    panel_6_1_1_1_1.setLayout(gl_panel_6_1_1_1_1);

    JPanel panel_6_1_1_1_2 = new JPanel();
    panel_6_1_1_1_2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_6_1_1_1_2.setBackground(SystemColor.inactiveCaption);

    in3bias = new JLabel("Bias: 0");
    in3bias.setFont(new Font("Dialog", Font.PLAIN, 9));
    in3bias.setHorizontalAlignment(SwingConstants.CENTER);

    in3error = new JLabel("Error: 0");
    in3error.setFont(new Font("Dialog", Font.PLAIN, 9));
    in3error.setHorizontalAlignment(SwingConstants.CENTER);

    in3output = new JLabel("Output: 0");
    in3output.setFont(new Font("Dialog", Font.PLAIN, 9));
    in3output.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_6_1_1_1_2 = new GroupLayout(panel_6_1_1_1_2);
    gl_panel_6_1_1_1_2.setHorizontalGroup(
        gl_panel_6_1_1_1_2.createParallelGroup(Alignment.LEADING).addGap(0, 113, Short.MAX_VALUE)
            .addGap(0, 113, Short.MAX_VALUE).addGap(0, 111, Short.MAX_VALUE)
            .addGroup(gl_panel_6_1_1_1_2.createSequentialGroup().addGap(19)
                .addGroup(gl_panel_6_1_1_1_2.createParallelGroup(Alignment.LEADING)
                    .addComponent(in3bias, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(in3error, GroupLayout.PREFERRED_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(in3output, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addGap(22)));
    gl_panel_6_1_1_1_2.setVerticalGroup(
        gl_panel_6_1_1_1_2.createParallelGroup(Alignment.LEADING).addGap(0, 57, Short.MAX_VALUE)
            .addGap(0, 57, Short.MAX_VALUE).addGap(0, 55, Short.MAX_VALUE)
            .addGroup(gl_panel_6_1_1_1_2.createSequentialGroup().addComponent(in3output)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(in3error)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(in3bias)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    panel_6_1_1_1_2.setLayout(gl_panel_6_1_1_1_2);
    GroupLayout gl_panel_2 = new GroupLayout(panel_2);
    gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_2.createSequentialGroup().addGap(76)
            .addComponent(lblHidden_1_1, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE).addGap(75))
        .addGroup(gl_panel_2.createSequentialGroup().addGap(47)
            .addComponent(panel_6_1_1_1, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE).addGap(46))
        .addGroup(gl_panel_2.createSequentialGroup().addGap(47)
            .addComponent(panel_6_1_1_1_1, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
            .addGap(46))
        .addGroup(gl_panel_2.createSequentialGroup().addGap(47)
            .addComponent(panel_6_1_1_1_2, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
            .addGap(46)));
    gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
            .addComponent(lblHidden_1_1, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
            .addGap(115)
            .addComponent(panel_6_1_1_1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
            .addGap(18)
            .addComponent(panel_6_1_1_1_1, GroupLayout.PREFERRED_SIZE, 50,
                GroupLayout.PREFERRED_SIZE)
            .addGap(18).addComponent(panel_6_1_1_1_2, GroupLayout.PREFERRED_SIZE, 52,
                GroupLayout.PREFERRED_SIZE)
            .addContainerGap(191, Short.MAX_VALUE)));
    panel_2.setLayout(gl_panel_2);

    JPanel panel_3 = new JPanel();
    panel_3.setBackground(Color.LIGHT_GRAY);
    panel.add(panel_3);

    JLabel lblHidden_1 = new JLabel("Hidden");
    lblHidden_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblHidden_1.setFont(new Font("Tahoma", Font.PLAIN, 18));

    JPanel panel_6_3_1 = new JPanel();
    panel_6_3_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_6_3_1.setBackground(SystemColor.inactiveCaption);

    hn13output = new JLabel("Output: 0");
    hn13output.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn13output.setHorizontalAlignment(SwingConstants.CENTER);

    hn13error = new JLabel("Error: 0");
    hn13error.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn13error.setHorizontalAlignment(SwingConstants.CENTER);

    hn13bias = new JLabel("Bias: 0");
    hn13bias.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn13bias.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_6_3_1 = new GroupLayout(panel_6_3_1);
    gl_panel_6_3_1
        .setHorizontalGroup(gl_panel_6_3_1.createParallelGroup(Alignment.LEADING)
            .addGap(0, 113, Short.MAX_VALUE).addGap(0, 113,
                Short.MAX_VALUE)
            .addGroup(
                gl_panel_6_3_1.createSequentialGroup().addGap(19)
                    .addGroup(
                        gl_panel_6_3_1.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_panel_6_3_1.createSequentialGroup()
                                .addComponent(hn13bias, GroupLayout.DEFAULT_SIZE, 35,
                                    Short.MAX_VALUE)
                                .addGap(22))
                            .addGroup(gl_panel_6_3_1.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel_6_3_1.createSequentialGroup()
                                    .addComponent(hn13error, GroupLayout.PREFERRED_SIZE, 35,
                                        Short.MAX_VALUE)
                                    .addGap(22))
                                .addGroup(
                                    gl_panel_6_3_1.createSequentialGroup()
                                        .addComponent(hn13output, GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(22))))));
    gl_panel_6_3_1.setVerticalGroup(gl_panel_6_3_1.createParallelGroup(Alignment.LEADING)
        .addGap(0, 57, Short.MAX_VALUE).addGap(0, 57, Short.MAX_VALUE)
        .addGroup(gl_panel_6_3_1.createSequentialGroup().addComponent(hn13output)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn13error)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn13bias)
            .addContainerGap(23, Short.MAX_VALUE)));
    panel_6_3_1.setLayout(gl_panel_6_3_1);

    JPanel panel_6_2_1 = new JPanel();
    panel_6_2_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_6_2_1.setBackground(SystemColor.inactiveCaption);

    hn12output = new JLabel("Output: 0");
    hn12output.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn12output.setHorizontalAlignment(SwingConstants.CENTER);

    hn12error = new JLabel("Error: 0");
    hn12error.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn12error.setHorizontalAlignment(SwingConstants.CENTER);

    hn12bias = new JLabel("Bias: 0");
    hn12bias.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn12bias.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_6_2_1 = new GroupLayout(panel_6_2_1);
    gl_panel_6_2_1.setHorizontalGroup(gl_panel_6_2_1.createParallelGroup(Alignment.LEADING)
        .addGap(0, 111, Short.MAX_VALUE)
        .addGroup(gl_panel_6_2_1.createSequentialGroup().addGap(19).addGroup(gl_panel_6_2_1
            .createParallelGroup(Alignment.LEADING)
            .addGroup(gl_panel_6_2_1.createSequentialGroup()
                .addComponent(hn12bias, GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE).addGap(22))
            .addGroup(gl_panel_6_2_1.createSequentialGroup()
                .addGroup(gl_panel_6_2_1.createParallelGroup(Alignment.TRAILING)
                    .addComponent(hn12error, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 70,
                        Short.MAX_VALUE)
                    .addComponent(hn12output, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 70,
                        Short.MAX_VALUE))
                .addGap(22)))));
    gl_panel_6_2_1.setVerticalGroup(gl_panel_6_2_1.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_6_2_1.createSequentialGroup().addComponent(hn12output)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn12error)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn12bias)
            .addContainerGap(20, Short.MAX_VALUE)));
    panel_6_2_1.setLayout(gl_panel_6_2_1);

    JPanel panel_6_1_1 = new JPanel();
    panel_6_1_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_6_1_1.setBackground(SystemColor.inactiveCaption);

    hn11output = new JLabel("Output: 0");
    hn11output.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn11output.setHorizontalAlignment(SwingConstants.CENTER);

    hn11error = new JLabel("Error: 0");
    hn11error.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn11error.setHorizontalAlignment(SwingConstants.CENTER);

    hn11bias = new JLabel("Bias: 0");
    hn11bias.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn11bias.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_6_1_1 = new GroupLayout(panel_6_1_1);
    gl_panel_6_1_1
        .setHorizontalGroup(gl_panel_6_1_1.createParallelGroup(Alignment.LEADING)
            .addGap(0, 113, Short.MAX_VALUE).addGap(0, 113,
                Short.MAX_VALUE)
            .addGroup(
                gl_panel_6_1_1.createSequentialGroup().addGap(19)
                    .addGroup(
                        gl_panel_6_1_1.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_panel_6_1_1.createSequentialGroup()
                                .addComponent(hn11bias, GroupLayout.DEFAULT_SIZE, 35,
                                    Short.MAX_VALUE)
                                .addGap(22))
                            .addGroup(gl_panel_6_1_1.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel_6_1_1.createSequentialGroup()
                                    .addComponent(hn11error, GroupLayout.PREFERRED_SIZE, 35,
                                        Short.MAX_VALUE)
                                    .addGap(22))
                                .addGroup(
                                    gl_panel_6_1_1.createSequentialGroup()
                                        .addComponent(hn11output, GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(22))))));
    gl_panel_6_1_1.setVerticalGroup(gl_panel_6_1_1.createParallelGroup(Alignment.LEADING)
        .addGap(0, 57, Short.MAX_VALUE).addGap(0, 57, Short.MAX_VALUE)
        .addGroup(gl_panel_6_1_1.createSequentialGroup().addComponent(hn11output)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn11error)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn11bias)
            .addContainerGap(23, Short.MAX_VALUE)));
    panel_6_1_1.setLayout(gl_panel_6_1_1);
    GroupLayout gl_panel_3 = new GroupLayout(panel_3);
    gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_3.createSequentialGroup().addGap(76)
            .addComponent(lblHidden_1, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE).addGap(75))
        .addGroup(gl_panel_3.createSequentialGroup().addGap(29)
            .addComponent(panel_6_3_1, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE).addGap(29))
        .addGroup(gl_panel_3.createSequentialGroup().addGap(29)
            .addComponent(panel_6_1_1, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE).addGap(29))
        .addGroup(gl_panel_3.createSequentialGroup().addGap(29)
            .addComponent(panel_6_2_1, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE).addGap(29)));
    gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_3.createSequentialGroup().addContainerGap()
            .addComponent(lblHidden_1, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
            .addGap(116)
            .addComponent(panel_6_1_1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
            .addGap(18)
            .addComponent(panel_6_2_1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
            .addGap(18)
            .addComponent(panel_6_3_1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
            .addContainerGap(192, Short.MAX_VALUE)));
    panel_3.setLayout(gl_panel_3);

    JPanel panel_4 = new JPanel();
    panel_4.setBackground(Color.LIGHT_GRAY);
    panel.add(panel_4);

    JLabel lblHidden = new JLabel("Hidden");
    lblHidden.setHorizontalAlignment(SwingConstants.CENTER);
    lblHidden.setFont(new Font("Tahoma", Font.PLAIN, 18));

    JPanel panel_6_1 = new JPanel();
    panel_6_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_6_1.setBackground(SystemColor.inactiveCaption);

    hn21output = new JLabel("Output: 0");
    hn21output.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn21output.setHorizontalAlignment(SwingConstants.CENTER);

    hn21error = new JLabel("Error: 0");
    hn21error.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn21error.setHorizontalAlignment(SwingConstants.CENTER);

    hn21bias = new JLabel("Bias: 0");
    hn21bias.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn21bias.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_6_1 = new GroupLayout(panel_6_1);
    gl_panel_6_1
        .setHorizontalGroup(gl_panel_6_1.createParallelGroup(Alignment.LEADING)
            .addGap(0, 113,
                Short.MAX_VALUE)
            .addGroup(
                gl_panel_6_1.createSequentialGroup().addGap(19)
                    .addGroup(
                        gl_panel_6_1.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_panel_6_1.createSequentialGroup()
                                .addComponent(hn21bias, GroupLayout.DEFAULT_SIZE, 35,
                                    Short.MAX_VALUE)
                                .addGap(22))
                            .addGroup(gl_panel_6_1.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel_6_1.createSequentialGroup()
                                    .addComponent(hn21error, GroupLayout.PREFERRED_SIZE, 35,
                                        Short.MAX_VALUE)
                                    .addGap(22))
                                .addGroup(
                                    gl_panel_6_1.createSequentialGroup()
                                        .addComponent(hn21output, GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(22))))));
    gl_panel_6_1.setVerticalGroup(
        gl_panel_6_1.createParallelGroup(Alignment.LEADING).addGap(0, 57, Short.MAX_VALUE)
            .addGroup(gl_panel_6_1.createSequentialGroup().addComponent(hn21output)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn21error)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn21bias)
                .addContainerGap(23, Short.MAX_VALUE)));
    panel_6_1.setLayout(gl_panel_6_1);

    JPanel panel_6_2 = new JPanel();
    panel_6_2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_6_2.setBackground(SystemColor.inactiveCaption);

    hn22output = new JLabel("Output: 0");
    hn22output.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn22output.setHorizontalAlignment(SwingConstants.CENTER);

    hn22error = new JLabel("Error: 0");
    hn22error.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn22error.setHorizontalAlignment(SwingConstants.CENTER);

    hn22bias = new JLabel("Bias: 0");
    hn22bias.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn22bias.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_6_2 = new GroupLayout(panel_6_2);
    gl_panel_6_2
        .setHorizontalGroup(gl_panel_6_2.createParallelGroup(Alignment.LEADING)
            .addGap(0, 113,
                Short.MAX_VALUE)
            .addGroup(
                gl_panel_6_2.createSequentialGroup().addGap(19)
                    .addGroup(
                        gl_panel_6_2.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_panel_6_2.createSequentialGroup()
                                .addComponent(hn22bias, GroupLayout.DEFAULT_SIZE, 35,
                                    Short.MAX_VALUE)
                                .addGap(22))
                            .addGroup(gl_panel_6_2.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel_6_2.createSequentialGroup()
                                    .addComponent(hn22error, GroupLayout.PREFERRED_SIZE, 35,
                                        Short.MAX_VALUE)
                                    .addGap(22))
                                .addGroup(
                                    gl_panel_6_2.createSequentialGroup()
                                        .addComponent(hn22output, GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(22))))));
    gl_panel_6_2.setVerticalGroup(
        gl_panel_6_2.createParallelGroup(Alignment.LEADING).addGap(0, 57, Short.MAX_VALUE)
            .addGroup(gl_panel_6_2.createSequentialGroup().addComponent(hn22output)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn22error)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn22bias)
                .addContainerGap(23, Short.MAX_VALUE)));
    panel_6_2.setLayout(gl_panel_6_2);

    JPanel panel_6_3 = new JPanel();
    panel_6_3.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_6_3.setBackground(SystemColor.inactiveCaption);

    hn23output = new JLabel("Output: 0");
    hn23output.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn23output.setHorizontalAlignment(SwingConstants.CENTER);

    hn23error = new JLabel("Error: 0");
    hn23error.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn23error.setHorizontalAlignment(SwingConstants.CENTER);

    hn23bias = new JLabel("Bias: 0");
    hn23bias.setFont(new Font("Dialog", Font.PLAIN, 9));
    hn23bias.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_6_3 = new GroupLayout(panel_6_3);
    gl_panel_6_3
        .setHorizontalGroup(gl_panel_6_3.createParallelGroup(Alignment.LEADING)
            .addGap(0, 113,
                Short.MAX_VALUE)
            .addGroup(
                gl_panel_6_3.createSequentialGroup().addGap(19)
                    .addGroup(
                        gl_panel_6_3.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_panel_6_3.createSequentialGroup()
                                .addComponent(hn23bias, GroupLayout.DEFAULT_SIZE, 35,
                                    Short.MAX_VALUE)
                                .addGap(22))
                            .addGroup(gl_panel_6_3.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel_6_3.createSequentialGroup()
                                    .addComponent(hn23error, GroupLayout.PREFERRED_SIZE, 35,
                                        Short.MAX_VALUE)
                                    .addGap(22))
                                .addGroup(
                                    gl_panel_6_3.createSequentialGroup()
                                        .addComponent(hn23output, GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(22))))));
    gl_panel_6_3.setVerticalGroup(
        gl_panel_6_3.createParallelGroup(Alignment.LEADING).addGap(0, 57, Short.MAX_VALUE)
            .addGroup(gl_panel_6_3.createSequentialGroup().addComponent(hn23output)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn23error)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(hn23bias)
                .addContainerGap(23, Short.MAX_VALUE)));
    panel_6_3.setLayout(gl_panel_6_3);
    GroupLayout gl_panel_4 = new GroupLayout(panel_4);
    gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_4.createSequentialGroup().addGap(76)
            .addComponent(lblHidden, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE).addGap(75))
        .addGroup(gl_panel_4.createSequentialGroup().addGap(28)
            .addComponent(panel_6_3, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE).addGap(28))
        .addGroup(gl_panel_4.createSequentialGroup().addGap(29)
            .addComponent(panel_6_1, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE).addGap(28))
        .addGroup(gl_panel_4.createSequentialGroup().addGap(29)
            .addComponent(panel_6_2, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE).addGap(28)));
    gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_4.createSequentialGroup().addContainerGap()
            .addComponent(lblHidden, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
            .addGap(116)
            .addComponent(panel_6_1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
            .addGap(18)
            .addComponent(panel_6_2, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
            .addGap(18)
            .addComponent(panel_6_3, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
            .addContainerGap(192, Short.MAX_VALUE)));
    panel_4.setLayout(gl_panel_4);

    JPanel panel_5 = new JPanel();
    panel_5.setBackground(Color.LIGHT_GRAY);
    panel.add(panel_5);

    JPanel panel_6 = new JPanel();
    panel_6.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    panel_6.setBackground(SystemColor.inactiveCaption);

    JLabel lblNewLabel = new JLabel("Output");
    lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_5 = new GroupLayout(panel_5);
    gl_panel_5.setHorizontalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_5.createSequentialGroup().addGap(76)
            .addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addGap(75))
        .addGroup(gl_panel_5.createSequentialGroup().addGap(47)
            .addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE).addGap(46)));
    gl_panel_5
        .setVerticalGroup(
            gl_panel_5.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_5.createSequentialGroup().addContainerGap()
                    .addComponent(lblNewLabel).addGap(184).addComponent(panel_6,
                        GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(259, Short.MAX_VALUE)));

    opoutput = new JLabel("Output: 0");
    opoutput.setFont(new Font("Dialog", Font.PLAIN, 9));
    opoutput.setHorizontalAlignment(SwingConstants.CENTER);

    operror = new JLabel("Error: 0");
    operror.setFont(new Font("Dialog", Font.PLAIN, 9));
    operror.setHorizontalAlignment(SwingConstants.CENTER);

    opbias = new JLabel("Bias: 0");
    opbias.setFont(new Font("Dialog", Font.PLAIN, 9));
    opbias.setHorizontalAlignment(SwingConstants.CENTER);
    GroupLayout gl_panel_6 = new GroupLayout(panel_6);
    gl_panel_6
        .setHorizontalGroup(
            gl_panel_6.createParallelGroup(Alignment.LEADING)
                .addGroup(
                    gl_panel_6.createSequentialGroup().addGap(19)
                        .addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_panel_6.createSequentialGroup()
                                .addComponent(opbias, GroupLayout.PREFERRED_SIZE, 35,
                                    Short.MAX_VALUE)
                                .addGap(22))
                            .addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_panel_6.createSequentialGroup()
                                    .addComponent(operror, GroupLayout.PREFERRED_SIZE, 35,
                                        Short.MAX_VALUE)
                                    .addGap(22))
                                .addGroup(
                                    gl_panel_6.createSequentialGroup()
                                        .addComponent(opoutput, GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(22))))));
    gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_6.createSequentialGroup().addComponent(opoutput)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(operror)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(opbias)
            .addContainerGap(23, Short.MAX_VALUE)));
    panel_6.setLayout(gl_panel_6);
    panel_5.setLayout(gl_panel_5);
    contentPane.setLayout(gl_contentPane);

    pack();
    setLocationRelativeTo(null);

  }

  public void update() {
    in1output.setText(
        "Output: " + Msn.decFormat(network.getInputLayer().getNeurons()[0].getOutput(), 3));
    in1error
        .setText("Error: " + Msn.decFormat(network.getInputLayer().getNeurons()[0].getError(), 3));
    in1bias.setText("Bias: " + Msn.decFormat(network.getInputLayer().getNeurons()[0].getBias(), 3));

    in2output.setText(
        "Output: " + Msn.decFormat(network.getInputLayer().getNeurons()[1].getOutput(), 3));
    in2error
        .setText("Error: " + Msn.decFormat(network.getInputLayer().getNeurons()[1].getError(), 3));
    in2bias.setText("Bias: " + Msn.decFormat(network.getInputLayer().getNeurons()[1].getBias(), 3));

    in3output.setText(
        "Output: " + Msn.decFormat(network.getInputLayer().getNeurons()[2].getOutput(), 3));
    in3error
        .setText("Error: " + Msn.decFormat(network.getInputLayer().getNeurons()[2].getError(), 3));
    in3bias.setText("Bias: " + Msn.decFormat(network.getInputLayer().getNeurons()[2].getBias(), 3));

    hn11output.setText(
        "Output: " + Msn.decFormat(network.getHiddenLayers()[0].getNeurons()[0].getOutput(), 3));
    hn11error
        .setText("Error: " + Msn.decFormat(network.getInputLayer().getNeurons()[0].getError(), 3));
    hn11bias
        .setText("Bias: " + Msn.decFormat(network.getInputLayer().getNeurons()[0].getBias(), 3));

    hn12output.setText(
        "Output: " + Msn.decFormat(network.getHiddenLayers()[0].getNeurons()[1].getOutput(), 3));
    hn12error
        .setText("Error: " + Msn.decFormat(network.getInputLayer().getNeurons()[1].getError(), 3));
    hn12bias
        .setText("Bias: " + Msn.decFormat(network.getInputLayer().getNeurons()[1].getBias(), 3));

    hn13output.setText(
        "Output: " + Msn.decFormat(network.getHiddenLayers()[0].getNeurons()[2].getOutput(), 3));
    hn13error
        .setText("Error: " + Msn.decFormat(network.getInputLayer().getNeurons()[2].getError(), 3));
    hn13bias
        .setText("Bias: " + Msn.decFormat(network.getInputLayer().getNeurons()[2].getBias(), 3));



    hn21output.setText(
        "Output: " + Msn.decFormat(network.getHiddenLayers()[1].getNeurons()[0].getOutput(), 3));
    hn21error
        .setText("Error: " + Msn.decFormat(network.getInputLayer().getNeurons()[0].getError(), 3));
    hn21bias
        .setText("Bias: " + Msn.decFormat(network.getInputLayer().getNeurons()[0].getBias(), 3));

    hn22output.setText(
        "Output: " + Msn.decFormat(network.getHiddenLayers()[1].getNeurons()[1].getOutput(), 3));
    hn22error
        .setText("Error: " + Msn.decFormat(network.getInputLayer().getNeurons()[1].getError(), 3));
    hn22bias
        .setText("Bias: " + Msn.decFormat(network.getInputLayer().getNeurons()[1].getBias(), 3));

    hn23output.setText(
        "Output: " + Msn.decFormat(network.getHiddenLayers()[1].getNeurons()[2].getOutput(), 3));
    hn23error
        .setText("Error: " + Msn.decFormat(network.getInputLayer().getNeurons()[2].getError(), 3));
    hn23bias
        .setText("Bias: " + Msn.decFormat(network.getInputLayer().getNeurons()[2].getBias(), 3));

    opoutput.setText(
        "Output: " + Msn.decFormat(network.getOutputLayer().getNeurons()[0].getOutput(), 3));
    operror
        .setText("Error: " + Msn.decFormat(network.getOutputLayer().getNeurons()[0].getError(), 3));
    opbias.setText("Bias: " + Msn.decFormat(network.getOutputLayer().getNeurons()[0].getBias(), 3));

    percenterror.setText("Percent Error: "
        + Msn.decFormat((target - network.getOutputLayer().getNeurons()[0].getOutput())
            / network.getOutputLayer().getNeurons()[0].getOutput(), 3)
        + "%");
  }
}
