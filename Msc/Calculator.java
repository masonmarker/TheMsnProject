
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import MsnLib.Msn;

/**
 * Calculator for general
 * 
 * @author mason
 *
 */
@SuppressWarnings("serial")
public class Calculator extends JFrame {


  private JPanel contentPane;
  private static String expression;

  private static JLabel expressionlabel;
  private static JLabel outputlabel;


  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Calculator frame = new Calculator();
          frame.pack();
          frame.setLocationRelativeTo(null);
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
  public Calculator() {

    expression = "";

    setTitle("Msn Calculator 1.0");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 388, 543);
    contentPane = new JPanel();
    contentPane.setBackground(Color.DARK_GRAY);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBackground(Color.LIGHT_GRAY);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE));

    expressionlabel = new JLabel("");
    expressionlabel.setBorder(new LineBorder(new Color(255, 255, 255), 2));
    expressionlabel.setFont(new Font("Monospaced", Font.PLAIN, 17));
    expressionlabel.setOpaque(true);
    expressionlabel.setForeground(Color.WHITE);
    expressionlabel.setBackground(Color.BLACK);
    expressionlabel.setHorizontalAlignment(SwingConstants.RIGHT);

    outputlabel = new JLabel("");
    outputlabel.setBorder(new LineBorder(Color.WHITE, 2));
    outputlabel.setOpaque(true);
    outputlabel.setHorizontalAlignment(SwingConstants.RIGHT);
    outputlabel.setForeground(Color.WHITE);
    outputlabel.setFont(new Font("Monospaced", Font.PLAIN, 15));
    outputlabel.setBackground(Color.BLACK);

    JPanel panel_1 = new JPanel();
    panel_1.setBorder(new LineBorder(Color.WHITE, 2));
    panel_1.setBackground(Color.GRAY);
    GroupLayout gl_panel = new GroupLayout(panel);
    gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
        .addGroup(gl_panel.createSequentialGroup().addContainerGap()
            .addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                .addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 342,
                    Short.MAX_VALUE)
                .addComponent(expressionlabel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 342,
                    GroupLayout.PREFERRED_SIZE)
                .addComponent(outputlabel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 342,
                    GroupLayout.PREFERRED_SIZE))
            .addContainerGap()));
    gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel.createSequentialGroup().addContainerGap()
            .addComponent(expressionlabel, GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(outputlabel, GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
            .addContainerGap()));
    panel_1.setLayout(new GridLayout(0, 3, 2, 2));

    JButton onebutton = new JButton("1");
    onebutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "1";
        update();
      }
    });
    onebutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    onebutton.setForeground(Color.WHITE);
    onebutton.setFocusPainted(false);
    onebutton.setBackground(Color.DARK_GRAY);
    onebutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    panel_1.add(onebutton);

    JButton twobutton = new JButton("2");
    twobutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "2";
        update();
      }
    });
    twobutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    twobutton.setForeground(Color.WHITE);
    twobutton.setFocusPainted(false);
    twobutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    twobutton.setBackground(Color.DARK_GRAY);
    panel_1.add(twobutton);

    JButton threebutton = new JButton("3");
    threebutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "3";
        update();
      }
    });
    threebutton.setForeground(Color.WHITE);
    threebutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    threebutton.setFocusPainted(false);
    threebutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    threebutton.setBackground(Color.DARK_GRAY);
    panel_1.add(threebutton);

    JButton fourbutton = new JButton("4");
    fourbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "4";
        update();
      }
    });
    fourbutton.setForeground(Color.WHITE);
    fourbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    fourbutton.setFocusPainted(false);
    fourbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    fourbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(fourbutton);

    JButton fivebutton = new JButton("5");
    fivebutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "5";
        update();
      }
    });
    fivebutton.setForeground(Color.WHITE);
    fivebutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    fivebutton.setFocusPainted(false);
    fivebutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    fivebutton.setBackground(Color.DARK_GRAY);
    panel_1.add(fivebutton);

    JButton sixbutton = new JButton("6");
    sixbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "6";
        update();
      }
    });
    sixbutton.setForeground(Color.WHITE);
    sixbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    sixbutton.setFocusPainted(false);
    sixbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    sixbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(sixbutton);

    JButton sevenbutton = new JButton("7");
    sevenbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "7";
        update();
      }
    });
    sevenbutton.setForeground(Color.WHITE);
    sevenbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    sevenbutton.setFocusPainted(false);
    sevenbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    sevenbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(sevenbutton);

    JButton eightbutton = new JButton("8");
    eightbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "8";
        update();
      }
    });
    eightbutton.setForeground(Color.WHITE);
    eightbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    eightbutton.setFocusPainted(false);
    eightbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    eightbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(eightbutton);

    JButton ninebutton = new JButton("9");
    ninebutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "9";
        update();
      }
    });
    ninebutton.setForeground(Color.WHITE);
    ninebutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    ninebutton.setFocusPainted(false);
    ninebutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    ninebutton.setBackground(Color.DARK_GRAY);
    panel_1.add(ninebutton);

    JButton plusbutton = new JButton("+");
    plusbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "+";
        update();
      }
    });
    plusbutton.setForeground(Color.WHITE);
    plusbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    plusbutton.setFocusPainted(false);
    plusbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    plusbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(plusbutton);

    JButton zerobutton = new JButton("0");
    zerobutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "0";
        update();
      }
    });
    zerobutton.setForeground(Color.WHITE);
    zerobutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    zerobutton.setFocusPainted(false);
    zerobutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    zerobutton.setBackground(Color.DARK_GRAY);
    panel_1.add(zerobutton);

    JButton minusbutton = new JButton("-");
    minusbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "-";
        update();
      }
    });
    minusbutton.setForeground(Color.WHITE);
    minusbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    minusbutton.setFocusPainted(false);
    minusbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    minusbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(minusbutton);

    JButton timesbutton = new JButton("*");
    timesbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "*";
        update();
      }
    });
    timesbutton.setForeground(Color.WHITE);
    timesbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    timesbutton.setFocusPainted(false);
    timesbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    timesbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(timesbutton);

    JButton dividebutton = new JButton("/");
    dividebutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "/";
        update();
      }
    });
    dividebutton.setForeground(Color.WHITE);
    dividebutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    dividebutton.setFocusPainted(false);
    dividebutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    dividebutton.setBackground(Color.DARK_GRAY);
    panel_1.add(dividebutton);

    JButton squarebutton = new JButton("^");
    squarebutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "^";
        update();
      }
    });
    squarebutton.setForeground(Color.WHITE);
    squarebutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    squarebutton.setFocusPainted(false);
    squarebutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    squarebutton.setBackground(Color.DARK_GRAY);
    panel_1.add(squarebutton);

    JButton leftparbutton = new JButton("(");
    leftparbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "(";
        update();
      }
    });
    leftparbutton.setForeground(Color.WHITE);
    leftparbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    leftparbutton.setFocusPainted(false);
    leftparbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    leftparbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(leftparbutton);

    JButton rightparbutton = new JButton(")");
    rightparbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += ")";
        update();
      }
    });
    rightparbutton.setForeground(Color.WHITE);
    rightparbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    rightparbutton.setFocusPainted(false);
    rightparbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    rightparbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(rightparbutton);

    JButton sqrtbutton = new JButton("sqrt");
    sqrtbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression += "sqrt(";
        update();
      }
    });
    sqrtbutton.setForeground(Color.WHITE);
    sqrtbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    sqrtbutton.setFocusPainted(false);
    sqrtbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    sqrtbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(sqrtbutton);

    JButton backspacebutton = new JButton("backspace");
    backspacebutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression = Msn.backspace(expression);
        update();
      }
    });
    backspacebutton.setForeground(Color.WHITE);
    backspacebutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    backspacebutton.setFocusPainted(false);
    backspacebutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    backspacebutton.setBackground(Color.DARK_GRAY);
    panel_1.add(backspacebutton);

    JButton empty = new JButton("");
    empty.setForeground(Color.WHITE);
    empty.setFont(new Font("Monospaced", Font.PLAIN, 15));
    empty.setFocusPainted(false);
    empty.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    empty.setBackground(Color.DARK_GRAY);
    panel_1.add(empty);

    JButton clearbutton = new JButton("clear");
    clearbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        expression = "";
        update();
      }
    });
    clearbutton.setForeground(Color.WHITE);
    clearbutton.setFont(new Font("Monospaced", Font.PLAIN, 15));
    clearbutton.setFocusPainted(false);
    clearbutton.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    clearbutton.setBackground(Color.DARK_GRAY);
    panel_1.add(clearbutton);
    panel.setLayout(gl_panel);
    contentPane.setLayout(gl_contentPane);
  }

  /**
   * Checks for any change in the input and acts accordingly.
   */
  public static void update() {
    try {
      expressionlabel.setText(expression);
      outputlabel.setText(String.valueOf(Msn.evaluate(expression)));
    } catch (Exception e) {
      outputlabel.setText("");
    }
  }
}
