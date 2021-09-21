import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Hub for all Neural Network applications that exist in the NeuralNetwork folder.
 * 
 * @author Mason Marker 
 * @version 1.0 - 06/03/2021
 */
@SuppressWarnings("serial")
public class NetworkHub extends JFrame {

  private JPanel contentPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          NetworkHub frame = new NetworkHub();
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
  public NetworkHub() {

    setTitle("Msn Network Hub");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 331, 622);
    contentPane = new JPanel();
    contentPane.setBackground(new Color(233, 150, 122));
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBackground(new Color(255, 218, 185));
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE));
    panel.setLayout(new GridLayout(3, 2, 5, 5));

    JPanel panel_1 = new JPanel();
    panel_1.setBackground(new Color(255, 235, 205));
    panel_1.setBorder(null);
    panel.add(panel_1);

    JLabel lblNewLabel = new JLabel("Network Tutorial (Console)");
    lblNewLabel.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 14));
    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

    JButton NetworkTutorialConsoleButton = new JButton("Launch");
    NetworkTutorialConsoleButton.addActionListener(new ActionListener() {
      @SuppressWarnings("static-access")
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            new Demonstration().main(new String[] {});
            return null;
          }
        };
        worker.execute();
      }
    });
    NetworkTutorialConsoleButton.setFocusPainted(false);
    NetworkTutorialConsoleButton.setBackground(new Color(233, 150, 122));
    NetworkTutorialConsoleButton.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    GroupLayout gl_panel_1 = new GroupLayout(panel_1);
    gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_1.createSequentialGroup().addGap(11)
            .addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addGap(12))
        .addGroup(
            gl_panel_1.createSequentialGroup().addGap(67).addComponent(NetworkTutorialConsoleButton,
                GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE).addGap(68)));
    gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_1.createSequentialGroup().addGap(55).addComponent(lblNewLabel)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(NetworkTutorialConsoleButton,
                GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
            .addContainerGap(71, Short.MAX_VALUE)));
    panel_1.setLayout(gl_panel_1);

    JPanel panel_2 = new JPanel();
    panel_2.setBackground(new Color(255, 235, 205));
    panel.add(panel_2);

    JLabel lblNetworkVisualization = new JLabel("Network Visualization");
    lblNetworkVisualization.setHorizontalAlignment(SwingConstants.CENTER);
    lblNetworkVisualization.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 14));

    JButton VisualizationButton = new JButton("Launch");
    VisualizationButton.addActionListener(new ActionListener() {
      @SuppressWarnings("static-access")
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            new NetworkVisualization().main(new String[] {});
            return null;
          }
        };
        worker.execute();
      }
    });
    VisualizationButton.setFocusPainted(false);
    VisualizationButton.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    VisualizationButton.setBackground(new Color(233, 150, 122));
    GroupLayout gl_panel_2 = new GroupLayout(panel_2);
    gl_panel_2
        .setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
                .addComponent(lblNetworkVisualization, GroupLayout.DEFAULT_SIZE, 176,
                    Short.MAX_VALUE)
                .addGap(13))
            .addGroup(gl_panel_2.createSequentialGroup().addGap(67)
                .addComponent(VisualizationButton, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                .addGap(68)));
    gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_2.createSequentialGroup().addGap(55)
            .addComponent(lblNetworkVisualization, GroupLayout.PREFERRED_SIZE, 24,
                GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(VisualizationButton,
                GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
            .addContainerGap(71, Short.MAX_VALUE)));
    panel_2.setLayout(gl_panel_2);

    JPanel panel_3 = new JPanel();
    panel_3.setBackground(new Color(255, 235, 205));
    panel.add(panel_3);

    JLabel lblLearnToAdd = new JLabel("Learn to Add (Console)");
    lblLearnToAdd.setHorizontalAlignment(SwingConstants.CENTER);
    lblLearnToAdd.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 14));

    JButton NetworkTutorialConsoleButton_1 = new JButton("Launch");
    NetworkTutorialConsoleButton_1.addActionListener(new ActionListener() {
      @SuppressWarnings("static-access")
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            new LearnToAdd().main(new String[] {});
            return null;
          }
        };
        worker.execute();
      }
    });
    NetworkTutorialConsoleButton_1.setFocusPainted(false);
    NetworkTutorialConsoleButton_1
        .setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    NetworkTutorialConsoleButton_1.setBackground(new Color(233, 150, 122));
    GroupLayout gl_panel_3 = new GroupLayout(panel_3);
    gl_panel_3
        .setHorizontalGroup(
            gl_panel_3.createParallelGroup(Alignment.LEADING)
                .addGroup(
                    gl_panel_3.createSequentialGroup()
                        .addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
                            .addGroup(gl_panel_3.createSequentialGroup().addGap(11).addComponent(
                                lblLearnToAdd, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                            .addGroup(gl_panel_3.createSequentialGroup().addGap(67)
                                .addComponent(NetworkTutorialConsoleButton_1,
                                    GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                .addGap(56)))
                        .addGap(12)));
    gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_3.createSequentialGroup().addGap(55)
            .addComponent(lblLearnToAdd, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(NetworkTutorialConsoleButton_1, GroupLayout.PREFERRED_SIZE, 31,
                GroupLayout.PREFERRED_SIZE)
            .addContainerGap(71, Short.MAX_VALUE)));
    panel_3.setLayout(gl_panel_3);

    JPanel panel_4 = new JPanel();
    panel_4.setBackground(new Color(255, 235, 205));
    panel.add(panel_4);

    JLabel lblFillTheSquare = new JLabel("Fill the Square");
    lblFillTheSquare.setHorizontalAlignment(SwingConstants.CENTER);
    lblFillTheSquare.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 14));

    JButton FillSquareButton = new JButton("Launch");
    FillSquareButton.addActionListener(new ActionListener() {
      @SuppressWarnings("static-access")
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

          @Override
          protected Void doInBackground() throws Exception {
            new NeuralNetworkGame().main(new String[] {});
            return null;
          }
        };
        worker.execute();
      }
    });
    FillSquareButton.setFocusPainted(false);
    FillSquareButton.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    FillSquareButton.setBackground(new Color(233, 150, 122));
    GroupLayout gl_panel_4 = new GroupLayout(panel_4);
    gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_4.createSequentialGroup().addGap(67)
            .addComponent(FillSquareButton, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
            .addGap(68))
        .addGroup(gl_panel_4.createSequentialGroup().addGap(11)
            .addComponent(lblFillTheSquare, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
            .addGap(12)));
    gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_4.createSequentialGroup().addGap(55)
            .addComponent(lblFillTheSquare, GroupLayout.PREFERRED_SIZE, 24,
                GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(FillSquareButton,
                GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
            .addContainerGap(71, Short.MAX_VALUE)));
    panel_4.setLayout(gl_panel_4);

    JPanel panel_5 = new JPanel();
    panel_5.setBackground(new Color(255, 235, 205));
    panel.add(panel_5);

    JLabel lblSpace = new JLabel("Space");
    lblSpace.setHorizontalAlignment(SwingConstants.CENTER);
    lblSpace.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 14));

    JButton SpaceButton = new JButton("Launch");
    SpaceButton.addActionListener(new ActionListener() {
      @SuppressWarnings("static-access")
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            new Space().main(new String[] {});
            return null;
          }
        };
        worker.execute();
      }
    });
    SpaceButton.setFocusPainted(false);
    SpaceButton.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    SpaceButton.setBackground(new Color(233, 150, 122));
    GroupLayout gl_panel_5 = new GroupLayout(panel_5);
    gl_panel_5.setHorizontalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_5.createSequentialGroup().addGap(11)
            .addComponent(lblSpace, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE).addGap(12))
        .addGroup(gl_panel_5.createSequentialGroup().addGap(67)
            .addComponent(SpaceButton, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE).addGap(68)));
    gl_panel_5
        .setVerticalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_panel_5.createSequentialGroup().addGap(60)
                .addComponent(lblSpace, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(SpaceButton,
                    GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE)));
    panel_5.setLayout(gl_panel_5);

    JPanel panel_6 = new JPanel();
    panel_6.setBackground(new Color(255, 235, 205));
    panel.add(panel_6);

    JLabel Coexist = new JLabel("Coexist");
    Coexist.setHorizontalAlignment(SwingConstants.CENTER);
    Coexist.setFont(new Font("Yu Gothic Medium", Font.PLAIN, 14));

    JButton CoexistButton = new JButton("Launch");
    CoexistButton.addActionListener(new ActionListener() {
      @SuppressWarnings("static-access")
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

          @Override
          protected Void doInBackground() throws Exception {
            new War().main(new String[] {});
            return null;
          }
        };
        worker.execute();
      }
    });
    CoexistButton.setFocusPainted(false);
    CoexistButton.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
    CoexistButton.setBackground(new Color(233, 150, 122));
    GroupLayout gl_panel_6 = new GroupLayout(panel_6);
    gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
            .addComponent(Coexist, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE).addGap(13))
        .addGroup(gl_panel_6.createSequentialGroup().addGap(67)
            .addComponent(CoexistButton, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
            .addGap(68)));
    gl_panel_6
        .setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
            .addGroup(gl_panel_6.createSequentialGroup().addGap(59)
                .addComponent(Coexist, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(CoexistButton,
                    GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE)));
    panel_6.setLayout(gl_panel_6);
    contentPane.setLayout(gl_contentPane);

    pack();
    setLocationRelativeTo(null);

  }
}
