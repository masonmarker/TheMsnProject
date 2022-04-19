package MsnC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class HelpMePls extends JFrame {

  private JPanel contentPane;
  private JTextPane variablepanel;
  private JTabbedPane tabbedPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          HelpMePls frame = new HelpMePls();
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
  public HelpMePls() {
    setUndecorated(true);
    setTitle("MSNC Guide");
    setBounds(100, 100, 825, 633);
    contentPane = new JPanel();
    contentPane.setBackground(new Color(138, 43, 226));
    contentPane.setBorder(null);
    setContentPane(contentPane);
    SpringLayout sl_contentPane = new SpringLayout();
    contentPane.setLayout(sl_contentPane);

    JPanel panel = new JPanel();
    sl_contentPane.putConstraint(SpringLayout.NORTH, panel, 31, SpringLayout.NORTH, contentPane);
    sl_contentPane.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, contentPane);
    sl_contentPane.putConstraint(SpringLayout.SOUTH, panel, -5, SpringLayout.SOUTH, contentPane);
    sl_contentPane.putConstraint(SpringLayout.EAST, panel, 810, SpringLayout.WEST, contentPane);
    panel.setBackground(new Color(138, 43, 226));
    contentPane.add(panel);
    SpringLayout sl_panel = new SpringLayout();
    panel.setLayout(sl_panel);

    tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
    sl_panel.putConstraint(SpringLayout.NORTH, tabbedPane, 0, SpringLayout.NORTH, panel);
    sl_panel.putConstraint(SpringLayout.WEST, tabbedPane, 0, SpringLayout.WEST, panel);
    sl_panel.putConstraint(SpringLayout.SOUTH, tabbedPane, 587, SpringLayout.NORTH, panel);
    sl_panel.putConstraint(SpringLayout.EAST, tabbedPane, 810, SpringLayout.WEST, panel);
    tabbedPane.setFont(new Font("Monospaced", Font.PLAIN, 13));
    tabbedPane.setBackground(Color.white);
    panel.add(tabbedPane);


    String variables = "variables can have 6 different types, each defined in their own way.\n\n";
    variables += "integer:          i integer = 3;\n";
    variables += "double:           d double = 3.5;\n";
    variables += "character:        c char = p;\n";
    variables += "string:           s string = hello;\n";
    variables += "list:             l list;\n";
    variables += "object:           o obj = &;\n";
    variables += "polymorphic:      object obj;\n\n";

    variables += "any code line in MSNC must be followed by a ';'.\n\n";

    variables += "& is used a null in MSNC, and can only be applied to a string or object.\n";
    variables += "when applied to a string, the string then becomes an empty string, \n";
    variables +=
        "when applied to an object, the object becomes Java's null until specified otherwise.\n\n";

    variables += "when using operators in MSNC, the operator MUST be surrounded by a whitespace.\n";
    variables += "i integer=5;                 is INVALID\n";
    variables += "d double=  5.76              is INVALID\n";
    variables += "i integer    =      5;       is OK\n";
    variables += "s string  =    what's up?;   is OK\n\n";

    variables +=
        "variable names must adhere to the MSNC standard (sounds funny but they actually have to).\n";
    variables +=
        "this means variable names cannot be an existing keyword in the language, this includes i, d, c, s, and o.\n";
    variables +=
        "keywords will be highlighted in the MSNC IDE to avoid unintentional usage of a keyword.\n\n";

    variables += "comments in MSNC should start with '::' and MUST end with a ';'\n\n";

    variables += "-------------------\n";
    variables += "INTEGERS\n\n";

    variables += "integers must be instantiated, and never declared.\n";
    variables += "i integer;             is INVALID\n";
    variables += "i integer = -1;        is OK\n";

    variables +=
        "integers can be declared in the same way as most other 'higher' level languages.\n\n";
    variables += "example code with integers:\n";
    variables += "1. i int        = 3 + 3;\n";
    variables += "2. i int2       = int - 4;\n";
    variables += "3. println @int2 is int2;\n";
    variables += "4. :: outputs 'int2 is 2';\n\n";

    variables +=
        "again, all operators in MSNC should be surrounded by whitespace, though this isn't always\n";
    variables += "the case, it can still cause issues.\n\n";

    variables +=
        "MSNC is flexible with type casting, meaning types that would normally be casted in Java\n";
    variables += "can simply be assigned to a new variable as a substitute for casting.\n\n";
    variables += "example code:\n";
    variables += "1. d double = 34.75;\n";
    variables += "2. i integer = double;\n";
    variables += "3. println @integer is integer;\n";
    variables += "4. :: outputs 'integer is 34';\n";
    variables += "-------------------\n\n";

    variables += "-------------------\n";
    variables += "DOUBLES\n\n";

    variables += "double instantiation is no different from integer instantiation.\n\n";

    variables += "example code:\n";
    variables += "1. d variable = -2.1\n";
    variables += "2. variable += 1.1\n";
    variables += "3. println @variable is variable;\n";
    variables += "4. :: outputs 'variable is -1.0';\n\n";

    variables += "randomness has been hard coded into the language.\n";
    variables += "you can obtain a random double between 0 and 1 using the post-operator '?'.\n";
    variables += "note that a double must be instantiated, then the randomness is applied later.\n";
    variables += "example code:\n";
    variables += "1. d double = -4.58294; :: value of double before randomness doesn't matter;\n";
    variables += "2. double = ?;\n";
    variables += "3. println double; :: prints a random double between 0 and 1;\n";
    variables += "-------------------\n\n";

    variables += "-------------------\n";
    variables += "CHARACTERS\n\n";

    variables += "characters in MSNC are similar to the primitive char in Java.\n\n";
    variables += "example code:\n";
    variables += "1. c char = p;\n";
    variables += "2. println your character is char;\n";
    variables += "3. :: outputs 'your character is p';\n";
    variables += "4. char = hello;\n";
    variables += "5. println char;\n";
    variables += "6. :: outputs 'h';\n";
    variables += "-------------------\n\n";

    variables += "-------------------\n";
    variables += "STRINGS\n\n";
    variables +=
        "strings in MSNC don't require any special notation different from integers or doubles.\n";
    variables += "this includes lacking the \"\" required by Java's Strings.\n\n";
    variables += "example code:\n";
    variables += "1. s string = hello how are you?;\n";
    variables += "2. println @string is string;\n";
    variables += "3. :: outputs 'string is hello how are you?';\n\n";

    variables += "more example code:\n";
    variables += "1. i integer = 3;\n";
    variables += "2. s string = @integer is integer;\n";
    variables += "3. println string;\n";
    variables += "4. :: outputs integer is 3;\n\n";

    variables += "strings can be concatinated using '++'.\n\n";
    variables += "example code:\n";
    variables += "1. s string = &;\n";
    variables += "2. string ++ hello whats up?;\n";
    variables += "3. println string;\n";
    variables += "4. :: outputs 'hello whats up?';\n";
    variables += "5. :: a whitespace can be added to a string with ':w:';\n";
    variables += "6. string ++ :w:;\n";
    variables += "7. string ++ nothing much;\n";
    variables += "8. println string;\n";
    variables += "9. :: outputs 'hello whats up? nothing much';\n\n";
    variables += "other string functions can be found in the 'methodology' tab.\n";

    variables += "-------------------\n\n";

    variables += "-------------------\n";
    variables += "LISTS\n\n";

    variables += "lists can only be declared, not instantiated.\n";
    variables +=
        "lists are a prime example of a type in MSNC that involves heavy use of secondary commands.\n";
    variables += "find more about secondary functions in '2nd commands'\n\n";

    variables +=
        "lists store all data as strings, where they would later be automatically casted to their respective type when retrieved. this allows for lists to contain 'any' data type.\n\n";

    variables += "list creation and usage example:\n";
    variables += "1. l list;\n";
    variables += "2. list add 34.543;\n";
    variables += "3. i integer = 10;\n";
    variables += "4. list add integer;\n";
    variables += "5. list add hello;\n";
    variables += "6. println @list is list;\n";
    variables += "7. :: outputs 'list is [34.543, 10, hello]';\n";
    variables += "8. s lastelement = &;\n";
    variables += "9. list getat 2 -> lastelement;\n";
    variables += "10. println last element is lastelement;\n";
    variables += "11. :: outputs 'last element is hello';\n\n";

    variables +=
        "'getat' is an example of a secondary command, as it isn't the first command in the code line.\n";
    variables += "see all secondary commands in '2nd commands'\n";

    variables += "-------------------\n\n";


    variables += "-------------------\n";
    variables += "OBJECTS\n\n";


    variables += "-------------------\n\n";

    variables += "-------------------\n";
    variables += "POLYMORPHIC OBJECTS\n\n";


    variables += "-------------------\n\n";

    JScrollPane scroller1 = new JScrollPane();
    scroller1.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scroller1.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("variables", scroller1);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(variables);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scroller1.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);

    String operators = "MSNC offers a vast arsenal of operators.\n";
    JScrollPane scroller2 = new JScrollPane();
    scroller2.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scroller2.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("operators", scroller2);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(operators);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scroller2.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);

    String conditional = "MSNC permits conditional lines of code\n";

    JScrollPane scrollerconditional = new JScrollPane();
    scrollerconditional.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scrollerconditional.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("conditionals", scrollerconditional);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(conditional);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scrollerconditional.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);

    String loops = "MSNC implements repetitive execution of a line of code.\n";

    JScrollPane scrollerloops = new JScrollPane();
    scrollerloops.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scrollerloops.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("loops", scrollerloops);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(loops);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scrollerloops.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);



    String commands = "MSNC includes several built in first-keyword commands.\n";
    JScrollPane scroller3 = new JScrollPane();
    scroller3.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scroller3.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("commands", scroller3);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(commands);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scroller3.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);


    String secondarycommands = "MSNC also includes several secondary commands.\n\n";
    secondarycommands +=
        "secondary commands are built in commands that aren't used as first-keyword commands.\n";
    JScrollPane scroller4 = new JScrollPane();
    scroller4.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scroller4.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("2nd commands", scroller4);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(secondarycommands);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scroller4.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);

    String functions = "MSNC allows for the usage of code blocks, or methods.\n\n";
    functions += "the phrases 'function,' 'method,' and 'code block' are used interchangeably\n";
    functions +=
        "due to MSNC's permissibility of code blocks being used as either a simple sequence of codelines, or a parameterized function.\n";

    JScrollPane scroller5 = new JScrollPane();
    scroller5.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scroller5.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("methodology", scroller5);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(functions);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scroller5.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);

    String poly = "MSNC implements simple polymorphic objects.\n";
    JScrollPane scroller6 = new JScrollPane();
    scroller6.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scroller6.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("polymorphism", scroller6);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(poly);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scroller6.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);

    String inheritance = "MSNC's polymorphic objects can utilize parent-child inheritance.\n";
    JScrollPane scroller7 = new JScrollPane();
    scroller7.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scroller7.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("inheritance", scroller7);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(inheritance);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scroller7.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);

    JButton btnNewButton = new JButton("close");
    sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton, 0, SpringLayout.NORTH,
        contentPane);
    sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton, 376, SpringLayout.WEST,
        contentPane);
    sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton, -6, SpringLayout.NORTH, panel);
    btnNewButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    btnNewButton.setFocusPainted(false);
    btnNewButton.setForeground(Color.WHITE);
    btnNewButton.setBackground(Color.GRAY);
    btnNewButton.setFont(new Font("Monospaced", Font.PLAIN, 13));
    contentPane.add(btnNewButton);

    String about = "MSN Code Version 1.0\n";

    about += "created by Mason Marker\n\n";



    JScrollPane scrollerabout = new JScrollPane();
    scrollerabout.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scrollerabout.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("about", scrollerabout);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(about);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scrollerabout.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);

    String knownissues = "KNOWN ISSUES as of MSN Code 1.0\n\n";

    knownissues += "-  validator can no longer use lists.\n";
    knownissues += "   this could indicate that lists may not be usable after\n";
    knownissues += "   several hundred lines of usage, however it could also only\n";
    knownissues += "   be cause by a malfunctioning second command.\n\n";

    JScrollPane scrollerknown = new JScrollPane();
    scrollerknown.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
    scrollerknown.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
    tabbedPane.addTab("known issues", scrollerknown);
    variablepanel = new JTextPane();
    variablepanel.setFocusable(false);
    variablepanel.setForeground(new Color(255, 255, 255));
    variablepanel.setText(knownissues);
    variablepanel.setFont(new Font("Monospaced", Font.PLAIN, 13));
    variablepanel.setEditable(false);
    variablepanel.setBackground(Color.DARK_GRAY);
    scrollerknown.setViewportView(variablepanel);
    variablepanel.setCaretPosition(0);


    setLocationRelativeTo(null);

  }
}
