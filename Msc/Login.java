
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame implements KeyListener {

  private JPanel contentPane;
  private JTextField textField;
  private JPasswordField passwordField;

  private JLabel invalidlabel;
  private HashMap<String, String> logins;
  private boolean isLoggedIn;

  public static void main(String[] args) {
    
    HashMap<String, String> logins = new HashMap<>();
    
    logins.put("mason", "password");
    logins.put("harris", "universe");
    logins.put("marko", "valorant");
    
    Login login = new Login(logins, "Test");
    
    while (!login.isLoggedIn()) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    System.out.println("logged in");
  }
  
  /**
   * Create the frame.
   */
  public Login(HashMap<String, String> logins, String name) {
    this.logins = logins;
    isLoggedIn = false;
    setUndecorated(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 352, 412);
    contentPane = new JPanel();
    contentPane.setBackground(new Color(255, 255, 255));
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel panel = new JPanel();
    panel.addKeyListener(this);
    panel.setBackground(new Color(105, 105, 105));
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE));

    JLabel loginlabel = new JLabel("Login");
    loginlabel.setForeground(new Color(255, 255, 255));
    loginlabel.setHorizontalAlignment(SwingConstants.CENTER);
    loginlabel.setFont(new Font("Tahoma", Font.PLAIN, 18));

    JLabel lblNewLabel = new JLabel("Username");
    lblNewLabel.setForeground(new Color(255, 255, 255));
    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel lblNewLabel_1 = new JLabel("Password");
    lblNewLabel_1.setForeground(new Color(255, 255, 255));
    lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);

    textField = new JTextField();
    textField.addKeyListener(this);
    textField.setColumns(10);

    passwordField = new JPasswordField();
    passwordField.addKeyListener(this);

    invalidlabel = new JLabel(" ");
    invalidlabel.setForeground(new Color(255, 69, 0));
    invalidlabel.setHorizontalAlignment(SwingConstants.CENTER);

    JButton loginbutton = new JButton("Login");
    loginbutton.setForeground(new Color(255, 255, 255));
    loginbutton.setBackground(Color.DARK_GRAY);
    loginbutton.setFocusPainted(false);
    loginbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        login();
      }
    });

    JLabel loginlabel_1 = new JLabel(name);
    loginlabel_1.setForeground(new Color(255, 255, 255));
    loginlabel_1.setHorizontalAlignment(SwingConstants.CENTER);
    loginlabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));

    GroupLayout gl_panel = new GroupLayout(panel);
    gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel.createSequentialGroup().addGap(107)
            .addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE).addGap(108))
        .addGroup(gl_panel.createSequentialGroup().addGap(120)
            .addComponent(textField, 102, 102, 102).addGap(120))
        .addGroup(gl_panel.createSequentialGroup().addGap(120)
            .addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
            .addGap(120))
        .addGroup(gl_panel.createSequentialGroup().addGap(119)
            .addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
            .addGap(119))
        .addGroup(gl_panel.createSequentialGroup().addGap(141)
            .addComponent(loginlabel, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE).addGap(142))
        .addGroup(gl_panel.createSequentialGroup().addGap(137)
            .addComponent(loginbutton, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE).addGap(132))
        .addGroup(
            gl_panel.createSequentialGroup().addGap(83)
                .addComponent(loginlabel_1, GroupLayout.PREFERRED_SIZE, 177,
                    GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        .addGroup(gl_panel.createSequentialGroup().addGap(52)
            .addComponent(invalidlabel, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
            .addGap(51)));
    gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_panel.createSequentialGroup().addGap(40)
            .addComponent(loginlabel_1, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED).addComponent(loginlabel).addGap(30)
            .addComponent(lblNewLabel).addGap(6)
            .addComponent(textField, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE).addGap(30)
            .addComponent(lblNewLabel_1).addGap(6)
            .addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
            .addPreferredGap(ComponentPlacement.UNRELATED)
            .addComponent(loginbutton, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE).addGap(11)
            .addComponent(invalidlabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addGap(74)));
    panel.setLayout(gl_panel);
    contentPane.setLayout(gl_contentPane);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public boolean isLoggedIn() {
    return isLoggedIn;
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      login();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  public void login() {
    for (Map.Entry<String, String> entry : logins.entrySet()) {
      if (textField.getText().equals(entry.getKey())
          && String.valueOf(passwordField.getPassword()).equals(entry.getValue())) {
        isLoggedIn = true;
        dispose();
      } else {
        SwingWorker<Void, Void> invalidworker = new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            invalidlabel.setText("Invalid Username or Password");
            Thread.sleep(1500);
            invalidlabel.setText(" ");
            return null;
          }
        };
        invalidworker.execute();
      }
    }
  }

}
