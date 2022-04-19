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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import MsnC.ExecutionHandler.Function;
import MsnC.ExecutionHandler.Obj;
import MsnC.Utils.KeywordStyledDocument;
import MsnC.Utils.TextLineNumber;
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
  private File savefile;
  private JTextArea variableArea;

  private ExecutionHandler h;
  private JTextArea console;
  private JButton runbutton;
  private JButton btnValidate;
  private JTextArea functionArea;
  private JButton formatbutton;
  private JTextPane textArea;

  KeywordStyledDocument styledDocument;
  private JButton helpbutton;

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
    setBounds(100, 100, 1313, 910);
    contentPane = new JPanel();
    contentPane.setBackground(new Color(123, 104, 238));
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.setBackground(new Color(75, 0, 130));
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE));
    SpringLayout sl_panel = new SpringLayout();
    panel.setLayout(sl_panel);

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setBorder(new LineBorder(new Color(176, 196, 222), 3));
    sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, panel);
    sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, panel);
    sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, panel);
    scrollPane.setBackground(Color.BLACK);
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
    contentPane.setLayout(gl_contentPane);

    scrollPane.setRowHeaderView(lines);

    JScrollPane scrollPane_1 = new JScrollPane();
    scrollPane_1.setBorder(new LineBorder(new Color(210, 105, 30), 3));
    sl_panel.putConstraint(SpringLayout.WEST, scrollPane_1, 502, SpringLayout.WEST, panel);
    sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane_1, -10, SpringLayout.SOUTH, panel);
    sl_panel.putConstraint(SpringLayout.EAST, scrollPane_1, -10, SpringLayout.EAST, panel);
    sl_panel.putConstraint(SpringLayout.EAST, scrollPane, -6, SpringLayout.WEST, scrollPane_1);
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
    scrollPane_2.setBorder(new LineBorder(Color.LIGHT_GRAY, 3));
    sl_panel.putConstraint(SpringLayout.NORTH, scrollPane_2, 10, SpringLayout.NORTH, panel);
    sl_panel.putConstraint(SpringLayout.WEST, scrollPane_2, 6, SpringLayout.EAST, scrollPane);
    panel.add(scrollPane_2);
    scrollPane_2.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scrollPane_2.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    JLabel lblConsole_1 = new JLabel("methodology");
    lblConsole_1.setOpaque(true);
    lblConsole_1.setHorizontalAlignment(SwingConstants.CENTER);
    lblConsole_1.setForeground(Color.WHITE);
    lblConsole_1.setFont(new Font("Monospaced", Font.PLAIN, 10));
    lblConsole_1.setBackground(Color.DARK_GRAY);
    scrollPane_2.setColumnHeaderView(lblConsole_1);

    JButton btnClearConsole = new JButton("clear console");
    sl_panel.putConstraint(SpringLayout.SOUTH, btnClearConsole, -294, SpringLayout.SOUTH, panel);
    sl_panel.putConstraint(SpringLayout.NORTH, scrollPane_1, 6, SpringLayout.SOUTH,
        btnClearConsole);
    sl_panel.putConstraint(SpringLayout.EAST, btnClearConsole, -10, SpringLayout.EAST, panel);
    btnClearConsole.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        console.setText("");
      }
    });
    btnClearConsole.setForeground(Color.WHITE);
    btnClearConsole.setFont(new Font("Monospaced", Font.PLAIN, 12));
    btnClearConsole.setFocusPainted(false);
    btnClearConsole.setBackground(Color.DARK_GRAY);
    panel.add(btnClearConsole);

    JScrollPane scrollPane_2_1 = new JScrollPane();
    scrollPane_2_1.setBorder(new LineBorder(new Color(240, 248, 255), 3));
    sl_panel.putConstraint(SpringLayout.EAST, scrollPane_2, -6, SpringLayout.WEST, scrollPane_2_1);
    sl_panel.putConstraint(SpringLayout.NORTH, scrollPane_2_1, 10, SpringLayout.NORTH, panel);
    sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane_2_1, 0, SpringLayout.SOUTH, scrollPane_2);
    sl_panel.putConstraint(SpringLayout.WEST, scrollPane_2_1, 899, SpringLayout.WEST, panel);
    sl_panel.putConstraint(SpringLayout.EAST, scrollPane_2_1, -10, SpringLayout.EAST, panel);
    scrollPane_2_1.setBackground(Color.BLACK);
    scrollPane_2_1.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scrollPane_2_1.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    functionArea = new JTextArea();
    functionArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
    functionArea.setEditable(false);
    functionArea.setBackground(Color.BLACK);
    functionArea.setForeground(Color.WHITE);
    scrollPane_2.setViewportView(functionArea);
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

    runbutton = new JButton("run");
    sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane_2, -6, SpringLayout.NORTH, runbutton);
    sl_panel.putConstraint(SpringLayout.WEST, runbutton, 6, SpringLayout.EAST, scrollPane);

    styledDocument = new KeywordStyledDocument(h);
    textArea = new JTextPane(styledDocument);
    try {
      textArea.setText(Msn.contentsOf("recentmsnworkspace.txt"));
    } catch (NullPointerException e) {
    }
    TextLineNumber tln = new TextLineNumber(textArea);
    scrollPane.setRowHeaderView(tln);
    textArea.setCaretColor(Color.WHITE);
    textArea.setBackground(Color.BLACK);
    textArea.setForeground(Color.WHITE);
    textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
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
        if (e.getKeyCode() == KeyEvent.VK_ALT) {
          runbutton.doClick();
        }
      }
    });
    scrollPane.setViewportView(textArea);
    sl_panel.putConstraint(SpringLayout.SOUTH, runbutton, -6, SpringLayout.NORTH, scrollPane_1);
    runbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          Msn.writeTo(savefile.getAbsolutePath(), textArea.getText());
        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        }
        console.setText("");
        h = new ExecutionHandler(textArea.getText(), console);
        try {
          h.interpret(h.lines(), false);
          styledDocument.setHandler(h);
        } catch (Exception e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        int i = 1;
        variableArea.setText("");
        String s = "";
        for (Map.Entry<String, Object> en : h.vars.entrySet()) {
          String value = "" + en.getValue();
          // s += i++ + ": " + en.getKey() + " (" + en.getValue().getClass().getTypeName() + ") -> "
          // + value + "\n";
          try {
            if (!en.getKey().contains("_def") && !en.getKey().contains("_params")
                && Msn.countChars(en.getKey(), '_') < 2
                && (!en.getKey().contains("::") && Msn.countChars(en.getKey(), ':') != 2)) {
              s += en.getKey() + " :: " + en.getValue().getClass().getTypeName() + "\n";
              s += "-> " + value + "\n\n";
            }
          } catch (NullPointerException e1) {

          }
        }

        String l = Msn.generate('-', 100);
        for (Obj o : h.objects) {

          s += l + "\n";
          s += "object: " + o.name + "\n";
          for (String variable : o.getVariables()) {
            Object value = h.vars.get(variable);
            s += variable + " : " + value + "\n";
          }
          for (String function : o.getFunctions()) {
            Function f = h.getFunctionByName(function);
            s += "f : " + f.name() + "\n";
          }
        }
        variableArea.setText(variableArea.getText() + s);

        String text = "";
        for (Function f : h.functions()) {
          if (Msn.countChars(f.name(), '_') < 2) {
            text += ":: " + f.comments() + "\n";
            text += f.name() + "\n";

            for (String param : f.params()) {
              text += "(" + param + ") ";
            }
            text += " -> ";
            for (String returns : f.returns()) {
              text += "(" + returns + ")";
            }
            text += "\n" + l + "\n";
          }
        }
        functionArea.setText(text);
        updateTitle(h.linesrun);
        textArea.setText(textArea.getText());
      }
    });
    runbutton.setForeground(Color.WHITE);
    runbutton.setFont(new Font("Monospaced", Font.PLAIN, 12));
    runbutton.setFocusPainted(false);
    runbutton.setBackground(Color.DARK_GRAY);
    panel.add(runbutton);

    btnValidate = new JButton("validate");
    sl_panel.putConstraint(SpringLayout.WEST, btnValidate, 6, SpringLayout.EAST, runbutton);
    sl_panel.putConstraint(SpringLayout.SOUTH, btnValidate, -6, SpringLayout.NORTH, scrollPane_1);
    btnValidate.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        String v = Msn.contentsOf("MsnCLib\\validator.msnc");
        ExecutionHandler executionHandler = new ExecutionHandler(v, console);
        executionHandler.printToConsole("[*] validating...", true);
        try {
          executionHandler.interpret(executionHandler.lines(), false);
        } catch (Exception e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        executionHandler.printToConsole("[+] validation complete", true);
        updateTitle(executionHandler.linesrun);
      }

    });
    btnValidate.setForeground(Color.WHITE);
    btnValidate.setFont(new Font("Monospaced", Font.PLAIN, 12));
    btnValidate.setFocusPainted(false);
    btnValidate.setBackground(Color.DARK_GRAY);
    panel.add(btnValidate);

    formatbutton = new JButton("format");
    formatbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String code = textArea.getText();
        LinkedList<Character> chars = new LinkedList<>(List.of(Msn.box(code.toCharArray())));
        for (int i = chars.size() - 1; i >= 0; i--) {
          try {
            if (chars.get(i - 1) == ';') {
              while (chars.get(i) == ' ') {
                chars.remove(i);
              }
              chars.add(i, '\n');
            }
          } catch (Exception e1) {
          }
        }
        String replace = "";
        for (Character c : chars) {
          replace += c;
        }
        replace = Msn.removeEmptyLines(replace.replaceAll(" +", " ").trim());



        textArea.setText(replace);
      }
    });
    sl_panel.putConstraint(SpringLayout.WEST, formatbutton, 6, SpringLayout.EAST, btnValidate);
    sl_panel.putConstraint(SpringLayout.SOUTH, formatbutton, -6, SpringLayout.NORTH, scrollPane_1);
    formatbutton.setForeground(Color.WHITE);
    formatbutton.setFont(new Font("Monospaced", Font.PLAIN, 12));
    formatbutton.setFocusPainted(false);
    formatbutton.setBackground(Color.DARK_GRAY);
    panel.add(formatbutton);

    helpbutton = new JButton("help");
    helpbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new HelpMePls().setVisible(true);
      }
    });
    sl_panel.putConstraint(SpringLayout.NORTH, helpbutton, 6, SpringLayout.SOUTH, scrollPane_2);
    sl_panel.putConstraint(SpringLayout.WEST, helpbutton, 6, SpringLayout.EAST, formatbutton);
    helpbutton.setForeground(Color.WHITE);
    helpbutton.setFont(new Font("Monospaced", Font.PLAIN, 12));
    helpbutton.setFocusPainted(false);
    helpbutton.setBackground(Color.DARK_GRAY);
    panel.add(helpbutton);
    setLocationRelativeTo(null);
  }

  public void updateTitle(int lines) {
    setTitle("Msn Code (MSNC)  ||" + "  interpreted " + lines + " lines");
  }



}
