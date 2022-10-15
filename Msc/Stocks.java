import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import MsnLib.Msn;
import javax.swing.JTextArea;

public class Stocks extends JFrame {

  private static final long serialVersionUID = 9196060776174384804L;

  private JPanel contentPane;
  private JTextField tagField;
  private JTextField buyField;
  private JTextField sellField;
  private JLabel balanceLabel;
  private JLabel earningsLabel;

  private int balance;
  private int earnings;
  private JTextField addFundsField;
  private JButton addTradeButton;
  private JButton addFundsButton;
  private JLabel removeFundsLabel;
  private JTextField removeFundsField;
  private JButton removeFundsButton;
  private JLabel lblNewLabel_3_3;
  private JTextField sharesField;

  private ArrayList<Trade> trades;
  private JTextArea historyPanel;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Stocks frame = new Stocks();
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
  public Stocks() {
    trades = new ArrayList<>();
    balance = 0;
    earnings = 0;

    load();


    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 979, 709);
    contentPane = new JPanel();
    contentPane.setBackground(Color.GRAY);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    JPanel aefapwefk = new JPanel();
    aefapwefk.setBackground(new Color(255, 255, 240));
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(aefapwefk, GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(aefapwefk, GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE));
    SpringLayout sl_aefapwefk = new SpringLayout();
    aefapwefk.setLayout(sl_aefapwefk);

    JScrollPane scrollPane = new JScrollPane();
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, aefapwefk);
    sl_aefapwefk.putConstraint(SpringLayout.WEST, scrollPane, -400, SpringLayout.EAST, aefapwefk);
    sl_aefapwefk.putConstraint(SpringLayout.SOUTH, scrollPane, 396, SpringLayout.NORTH, aefapwefk);
    sl_aefapwefk.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, aefapwefk);
    aefapwefk.add(scrollPane);

    JLabel lblNewLabel = new JLabel("\r\nTAG / BUY / SELL / SHARES");
    lblNewLabel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
    scrollPane.setColumnHeaderView(lblNewLabel);

    earningsLabel = new JLabel("earnings: $0.00");
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, earningsLabel, 6, SpringLayout.SOUTH,
        scrollPane);
    sl_aefapwefk.putConstraint(SpringLayout.EAST, earningsLabel, 0, SpringLayout.EAST, scrollPane);
    aefapwefk.add(earningsLabel);

    balanceLabel = new JLabel("balance: $0.00");
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, balanceLabel, 6, SpringLayout.SOUTH, scrollPane);
    sl_aefapwefk.putConstraint(SpringLayout.WEST, balanceLabel, 0, SpringLayout.WEST, scrollPane);
    aefapwefk.add(balanceLabel);

    JLabel lblNewLabel_3 = new JLabel("buy price");
    sl_aefapwefk.putConstraint(SpringLayout.WEST, lblNewLabel_3, 10, SpringLayout.WEST, aefapwefk);
    sl_aefapwefk.putConstraint(SpringLayout.EAST, lblNewLabel_3, -457, SpringLayout.WEST,
        scrollPane);
    lblNewLabel_3.setHorizontalTextPosition(SwingConstants.CENTER);
    lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
    aefapwefk.add(lblNewLabel_3);

    JLabel lblNewLabel_3_1 = new JLabel("sell price");
    sl_aefapwefk.putConstraint(SpringLayout.WEST, lblNewLabel_3_1, 10, SpringLayout.WEST,
        aefapwefk);
    sl_aefapwefk.putConstraint(SpringLayout.EAST, lblNewLabel_3_1, 0, SpringLayout.EAST,
        lblNewLabel_3);
    lblNewLabel_3_1.setHorizontalTextPosition(SwingConstants.CENTER);
    lblNewLabel_3_1.setHorizontalAlignment(SwingConstants.CENTER);
    aefapwefk.add(lblNewLabel_3_1);

    JLabel lblNewLabel_3_2 = new JLabel("tag");
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, lblNewLabel_3_2, 34, SpringLayout.NORTH,
        aefapwefk);
    sl_aefapwefk.putConstraint(SpringLayout.WEST, lblNewLabel_3_2, 10, SpringLayout.WEST,
        aefapwefk);
    sl_aefapwefk.putConstraint(SpringLayout.EAST, lblNewLabel_3_2, -457, SpringLayout.WEST,
        scrollPane);

    historyPanel = new JTextArea();
    historyPanel.setEditable(false);
    scrollPane.setViewportView(historyPanel);
    lblNewLabel_3_2.setHorizontalAlignment(SwingConstants.CENTER);
    aefapwefk.add(lblNewLabel_3_2);

    tagField = new JTextField();
    sl_aefapwefk.putConstraint(SpringLayout.WEST, tagField, 10, SpringLayout.WEST, aefapwefk);
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, lblNewLabel_3, 6, SpringLayout.SOUTH, tagField);
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, tagField, 6, SpringLayout.SOUTH,
        lblNewLabel_3_2);
    aefapwefk.add(tagField);
    tagField.setColumns(10);

    buyField = new JTextField();
    sl_aefapwefk.putConstraint(SpringLayout.WEST, buyField, 10, SpringLayout.WEST, aefapwefk);
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, lblNewLabel_3_1, 6, SpringLayout.SOUTH,
        buyField);
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, buyField, 6, SpringLayout.SOUTH, lblNewLabel_3);
    buyField.setColumns(10);
    aefapwefk.add(buyField);

    sellField = new JTextField();
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, sellField, 6, SpringLayout.SOUTH,
        lblNewLabel_3_1);
    sl_aefapwefk.putConstraint(SpringLayout.WEST, sellField, 10, SpringLayout.WEST, aefapwefk);
    sellField.setColumns(10);
    aefapwefk.add(sellField);

    addTradeButton = new JButton("add");
    addTradeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        double bought = Double.parseDouble(buyField.getText().replaceAll("$", ""));
        double sold = Double.parseDouble(sellField.getText().replaceAll("$", ""));
        double shares = Double.parseDouble(sharesField.getText());

        double totalbought = bought * shares;
        balance -= totalbought;
        double totalsold = sold * shares;
        balance += totalsold;
        balanceLabel.setText("balance: " + Msn.moneyFormat(balance));


        double result = totalsold - totalbought;

        if (result > 0) {
          earnings += result;
        }
        earningsLabel.setText("earnings: " + Msn.moneyFormat(earnings));



        trades.add(new Trade(tagField.getText(), bought, sold, shares));



        updateHistory();
      }
    });
    sl_aefapwefk.putConstraint(SpringLayout.WEST, addTradeButton, 0, SpringLayout.WEST,
        lblNewLabel_3);
    addTradeButton.setFocusPainted(false);
    aefapwefk.add(addTradeButton);

    JLabel lblNewLabel_1 = new JLabel("add funds");
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 0, SpringLayout.NORTH,
        lblNewLabel_3_2);
    aefapwefk.add(lblNewLabel_1);

    addFundsField = new JTextField();
    sl_aefapwefk.putConstraint(SpringLayout.WEST, lblNewLabel_1, 0, SpringLayout.WEST,
        addFundsField);
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, addFundsField, 0, SpringLayout.NORTH, tagField);
    sl_aefapwefk.putConstraint(SpringLayout.WEST, addFundsField, 22, SpringLayout.EAST, tagField);
    aefapwefk.add(addFundsField);
    addFundsField.setColumns(10);

    addFundsButton = new JButton("add");
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, addFundsButton, -4, SpringLayout.NORTH,
        lblNewLabel_3);
    sl_aefapwefk.putConstraint(SpringLayout.WEST, addFundsButton, 0, SpringLayout.WEST,
        lblNewLabel_1);
    addFundsButton.setFocusPainted(false);
    addFundsButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          balance += Double.parseDouble(addFundsField.getText().replaceAll("$", ""));
          balanceLabel.setText("balance: " + Msn.moneyFormat(balance));
        } catch (NumberFormatException e1) {

        }
      }
    });
    aefapwefk.add(addFundsButton);

    removeFundsLabel = new JLabel("remove funds");
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, removeFundsLabel, 0, SpringLayout.NORTH,
        lblNewLabel_3_1);
    sl_aefapwefk.putConstraint(SpringLayout.WEST, removeFundsLabel, 0, SpringLayout.WEST,
        lblNewLabel_1);
    aefapwefk.add(removeFundsLabel);

    removeFundsField = new JTextField();
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, removeFundsField, 0, SpringLayout.NORTH,
        sellField);
    sl_aefapwefk.putConstraint(SpringLayout.EAST, removeFundsField, 0, SpringLayout.EAST,
        addFundsField);
    removeFundsField.setColumns(10);
    aefapwefk.add(removeFundsField);

    removeFundsButton = new JButton("remove");
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, removeFundsButton, 6, SpringLayout.SOUTH,
        removeFundsField);
    removeFundsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          balance -= Double.parseDouble(removeFundsField.getText().replaceAll("$", ""));
          balanceLabel.setText("balance: " + Msn.moneyFormat(balance));
        } catch (NumberFormatException e1) {

        }
      }
    });
    sl_aefapwefk.putConstraint(SpringLayout.WEST, removeFundsButton, 0, SpringLayout.WEST,
        lblNewLabel_1);
    removeFundsButton.setFocusPainted(false);
    aefapwefk.add(removeFundsButton);

    lblNewLabel_3_3 = new JLabel("shares");
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, lblNewLabel_3_3, 6, SpringLayout.SOUTH,
        sellField);
    sl_aefapwefk.putConstraint(SpringLayout.WEST, lblNewLabel_3_3, 0, SpringLayout.WEST,
        lblNewLabel_3);
    sl_aefapwefk.putConstraint(SpringLayout.EAST, lblNewLabel_3_3, 0, SpringLayout.EAST,
        lblNewLabel_3);
    lblNewLabel_3_3.setHorizontalTextPosition(SwingConstants.CENTER);
    lblNewLabel_3_3.setHorizontalAlignment(SwingConstants.CENTER);
    aefapwefk.add(lblNewLabel_3_3);

    sharesField = new JTextField();
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, addTradeButton, 6, SpringLayout.SOUTH,
        sharesField);
    sl_aefapwefk.putConstraint(SpringLayout.NORTH, sharesField, 6, SpringLayout.SOUTH,
        lblNewLabel_3_3);
    sl_aefapwefk.putConstraint(SpringLayout.WEST, sharesField, 0, SpringLayout.WEST, lblNewLabel_3);
    sharesField.setColumns(10);
    aefapwefk.add(sharesField);
    contentPane.setLayout(gl_contentPane);



    setLocationRelativeTo(null);
  }

  public void load() {

    updateHistory();
  }

  public void save() {



  }

  public void updateHistory() {
    try {
    String text = "";
    for (Trade t : trades) {
      text += historyPanel.getText() + t.getTag() + "  " + Msn.moneyFormat(t.getBuy()) + "  "
          + Msn.moneyFormat(t.getSell()) + "  " + t.getShares() + "\n";
    }
    historyPanel.setText(text);
    repaint();
    revalidate();
  } catch (NullPointerException e) {
      
    }
  }



  class Trade {
    private String tag;
    private double buy;
    private double sell;
    private double shares;

    public Trade(String tag, double buy, double sell, double shares) {
      this.tag = tag;
      this.buy = buy;
      this.sell = sell;
      this.shares = shares;
    }

    public String getTag() {
      return tag;
    }

    public double getBuy() {
      return buy;
    }

    public double getSell() {
      return sell;
    }

    public double getShares() {
      return shares;
    }



  }
}
