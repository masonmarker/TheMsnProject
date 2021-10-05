package MsnC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import MsnC.Utils.Methodology;
import MsnC.Utils.Syntax;
import MsnLib.Msn;

/**
 * Msn Code (MSNC): Original coding language.
 * 
 * This is the driver.
 * 
 * @author Mason Marker
 * @version 1.0 - 09/21/2020
 */
@SuppressWarnings("serial")
public class MsnCodeDriver extends JFrame {

  private JPanel contentPane;
  private JTextArea lines;
  private JTextArea textArea;
  private File savefile;
  private JTextArea variableArea;
  private JPanel methodbuttonspanel;

  private ExecutionHandler h;
  private JTextArea console;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          MsnCodeDriver frame = new MsnCodeDriver();
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
  public MsnCodeDriver() {

    savefile = new File("recentmsnworkspace.txt");

    setTitle("Msn Code (MSNC)");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 1096, 773);
    contentPane = new JPanel();
    contentPane.setBackground(Color.GRAY);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBackground(Color.GRAY);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE));
    SpringLayout sl_panel = new SpringLayout();
    panel.setLayout(sl_panel);

    JScrollPane scrollPane = new JScrollPane();
    sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, panel);
    sl_panel.putConstraint(SpringLayout.EAST, scrollPane, 426, SpringLayout.WEST, panel);
    scrollPane.setBackground(Color.BLACK);
    sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, panel);
    panel.add(scrollPane);
    scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));


    lines = new JTextArea("1");
    lines.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
    lines.setFont(new Font("Monospaced", Font.PLAIN, 13));
    lines.setBackground(Color.black);
    lines.setForeground(Color.white);
    lines.setEditable(false);



    JLabel lblNewLabel = new JLabel("editor");
    lblNewLabel.setOpaque(true);
    lblNewLabel.setBackground(Color.DARK_GRAY);
    lblNewLabel.setForeground(Color.WHITE);
    lblNewLabel.setFont(new Font("Monospaced", Font.PLAIN, 10));
    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
    scrollPane.setColumnHeaderView(lblNewLabel);

    textArea = new JTextArea();
    textArea.setText(Msn.contentsOf(savefile.getAbsolutePath()));
    textArea.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

      }

      @Override
      public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

      }

      @Override
      public void keyReleased(KeyEvent e) {
        try {
          Msn.writeTo(savefile.getAbsolutePath(), textArea.getText());
        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        }
        console.setText("");
        h = new ExecutionHandler(textArea.getText(), console);
        h.interpret();
        int i = 1;
        variableArea.setText("");
        String s = "";
        for (Map.Entry<String, Object> en : h.vars.entrySet()) {
          String value = "" + en.getValue();
          if (en.getValue().getClass().isArray())
            value = Syntax.arrayToString(en.getValue());
          s += i++ + ": " + en.getKey() + " (" + en.getValue().getClass().getTypeName() + ") -> "
              + value + "\n";
        }
        variableArea.setText(variableArea.getText() + s);


        if (e.getKeyCode() == KeyEvent.VK_PERIOD) {
          int cursorpos = textArea.getCaretPosition();
          while (cursorpos > textArea.getText().length()) {
            cursorpos--;
          }
          new Methodology(methodbuttonspanel, textArea.getText(), cursorpos, h.vars);
        }
      }
    });
    textArea.setCaretColor(Color.WHITE);
    textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
    textArea.setForeground(Color.WHITE);
    textArea.setBackground(Color.BLACK);
    scrollPane.setViewportView(textArea);
    contentPane.setLayout(gl_contentPane);
    textArea.getDocument().addDocumentListener(new DocumentListener() {

      public String getText() {
        int caretPosition = textArea.getDocument().getLength();
        Element root = textArea.getDocument().getDefaultRootElement();
        String text = "1" + System.getProperty("line.separator");
        for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++)
          text += i + System.getProperty("line.separator");
        return text;
      }

      @Override
      public void changedUpdate(DocumentEvent de) {
        lines.setText(getText());
      }

      @Override
      public void insertUpdate(DocumentEvent de) {
        lines.setText(getText());
      }

      @Override
      public void removeUpdate(DocumentEvent de) {
        lines.setText(getText());
      }
    });

    scrollPane.setRowHeaderView(lines);

    JScrollPane scrollPane_1 = new JScrollPane();
    sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, -6, SpringLayout.NORTH, scrollPane_1);
    sl_panel.putConstraint(SpringLayout.NORTH, scrollPane_1, 590, SpringLayout.NORTH, panel);
    sl_panel.putConstraint(SpringLayout.WEST, scrollPane_1, 10, SpringLayout.WEST, panel);
    sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane_1, -10, SpringLayout.SOUTH, panel);
    sl_panel.putConstraint(SpringLayout.EAST, scrollPane_1, -644, SpringLayout.EAST, panel);
    panel.add(scrollPane_1);
    scrollPane_1.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scrollPane_1.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));

    console = new JTextArea();
    console.setEnabled(false);
    console.setForeground(Color.WHITE);
    console.setFont(new Font("Monospaced", Font.PLAIN, 13));
    console.setBackground(Color.BLACK);
    scrollPane_1.setViewportView(console);

    JLabel lblConsole = new JLabel("console");
    lblConsole.setOpaque(true);
    lblConsole.setHorizontalAlignment(SwingConstants.CENTER);
    lblConsole.setForeground(Color.WHITE);
    lblConsole.setFont(new Font("Monospaced", Font.PLAIN, 10));
    lblConsole.setBackground(Color.DARK_GRAY);
    scrollPane_1.setColumnHeaderView(lblConsole);

    JScrollPane scrollPane_2 = new JScrollPane();
    sl_panel.putConstraint(SpringLayout.NORTH, scrollPane_2, 0, SpringLayout.NORTH, scrollPane);
    sl_panel.putConstraint(SpringLayout.WEST, scrollPane_2, 6, SpringLayout.EAST, scrollPane);
    sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane_2, -311, SpringLayout.SOUTH, panel);
    sl_panel.putConstraint(SpringLayout.EAST, scrollPane_2, -394, SpringLayout.EAST, panel);
    panel.add(scrollPane_2);

    JLabel lblConsole_1 = new JLabel("methodology");
    lblConsole_1.setOpaque(true);
    lblConsole_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblConsole_1.setForeground(Color.WHITE);
    lblConsole_1.setFont(new Font("Monospaced", Font.PLAIN, 10));
    lblConsole_1.setBackground(Color.DARK_GRAY);
    scrollPane_2.setColumnHeaderView(lblConsole_1);

    methodbuttonspanel = new JPanel();
    methodbuttonspanel.setBackground(Color.BLACK);
    scrollPane_2.setViewportView(methodbuttonspanel);

    JButton btnClearConsole = new JButton("clear console");
    btnClearConsole.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        console.setText("");
      }
    });
    sl_panel.putConstraint(SpringLayout.WEST, btnClearConsole, 6, SpringLayout.EAST, scrollPane_1);
    sl_panel.putConstraint(SpringLayout.SOUTH, btnClearConsole, -10, SpringLayout.SOUTH, panel);
    btnClearConsole.setForeground(Color.WHITE);
    btnClearConsole.setFont(new Font("Monospaced", Font.PLAIN, 12));
    btnClearConsole.setFocusPainted(false);
    btnClearConsole.setBackground(Color.DARK_GRAY);
    panel.add(btnClearConsole);

    JScrollPane scrollPane_2_1 = new JScrollPane();
    sl_panel.putConstraint(SpringLayout.EAST, scrollPane_2_1, 384, SpringLayout.EAST, scrollPane_2);
    scrollPane_2_1.setBackground(Color.BLACK);
    sl_panel.putConstraint(SpringLayout.NORTH, scrollPane_2_1, 0, SpringLayout.NORTH, scrollPane);
    sl_panel.putConstraint(SpringLayout.WEST, scrollPane_2_1, 6, SpringLayout.EAST, scrollPane_2);
    sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane_2_1, 403, SpringLayout.NORTH, scrollPane);
    panel.add(scrollPane_2_1);

    JLabel lblConsole_1_1 = new JLabel("variables");
    lblConsole_1_1.setOpaque(true);
    lblConsole_1_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblConsole_1_1.setForeground(Color.WHITE);
    lblConsole_1_1.setFont(new Font("Monospaced", Font.PLAIN, 10));
    lblConsole_1_1.setBackground(Color.DARK_GRAY);
    scrollPane_2_1.setColumnHeaderView(lblConsole_1_1);

    variableArea = new JTextArea();
    variableArea.setEditable(false);
    variableArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
    variableArea.setForeground(Color.WHITE);
    variableArea.setBackground(Color.BLACK);
    scrollPane_2_1.setViewportView(variableArea);


    setLocationRelativeTo(null);
  }
}
