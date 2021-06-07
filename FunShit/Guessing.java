import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

/**
 * Guess a random number within a range in less tries than the AI!
 * 
 * @author Mason Marker
 * @version 1.0 - 01/15/2021
 */
@SuppressWarnings("serial")
public class Guessing extends JFrame {

  private JPanel contentPane;

  private int attempts;
  private int aiattempts;
  private int answer;
  private JTextField textField;

  private JLabel toolabel;
  private JLabel attemptslabel;
  private JLabel aiguesslabel;
  private JLabel aiattemptslabel;
  private JLabel aitoolabel;
  private JLabel aiguesseslabel;
  private ArrayList<Integer> aiguesses;

  public static void main(String[] args) {
    // range
    new Guessing(1, 100);
  }

  /**
   * Create the frame.
   */
  public Guessing(int min, int max) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 544, 518);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    aiguesses = new ArrayList<>();
    updateAnswer(min, max);
    attempts = 0;
    aiattempts = 0;

    JPanel panel = new JPanel();
    panel.setBackground(Color.LIGHT_GRAY);
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_contentPane.createSequentialGroup()
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE).addGap(1)));
    gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addGroup(gl_contentPane.createSequentialGroup()
            .addComponent(panel, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE).addGap(0)));

    JLabel titlelabel = new JLabel("Guessing 1.0");
    titlelabel.setForeground(Color.DARK_GRAY);
    titlelabel.setFont(new Font("Tahoma", Font.BOLD, 15));
    titlelabel.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel titlelabel_1 = new JLabel("Pick a number between " + min + " and " + max);
    titlelabel_1.setHorizontalAlignment(SwingConstants.CENTER);
    titlelabel_1.setForeground(Color.DARK_GRAY);
    titlelabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));

    textField = new JTextField();
    textField.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          try {
            if (Integer.valueOf(textField.getText()) > answer) {
              toolabel.setForeground(Color.RED);
              toolabel.setText("Too High");
              increaseAttempts();
            } else if (Integer.valueOf(textField.getText()) < answer) {
              toolabel.setForeground(Color.RED);
              toolabel.setText("Too Low");
              increaseAttempts();
            } else {
              textField.setEnabled(false);
              toolabel.setForeground(Color.GREEN.darker());
              increaseAttempts();
              toolabel.setText(answer + " is correct! Running AI");
              runAI(min, max);
            }
          } catch (NumberFormatException e1) {
            toolabel.setForeground(Color.RED);
            toolabel.setText("Answer must be an integer");
          }
          textField.setText("");
        }

      }

      @Override
      public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

      }

    });
    textField.setColumns(10);

    attemptslabel = new JLabel("Your Attempts: 0");
    attemptslabel.setHorizontalAlignment(SwingConstants.CENTER);
    attemptslabel.setForeground(Color.DARK_GRAY);
    attemptslabel.setFont(new Font("Tahoma", Font.BOLD, 12));

    toolabel = new JLabel(" ");
    toolabel.setHorizontalAlignment(SwingConstants.CENTER);
    toolabel.setForeground(Color.DARK_GRAY);
    toolabel.setFont(new Font("Tahoma", Font.BOLD, 12));

    aiguesslabel = new JLabel(" ");
    aiguesslabel.setHorizontalAlignment(SwingConstants.CENTER);
    aiguesslabel.setForeground(Color.DARK_GRAY);
    aiguesslabel.setFont(new Font("Tahoma", Font.BOLD, 12));

    aiattemptslabel = new JLabel("AI Attempts: 0");
    aiattemptslabel.setHorizontalAlignment(SwingConstants.CENTER);
    aiattemptslabel.setForeground(Color.DARK_GRAY);
    aiattemptslabel.setFont(new Font("Tahoma", Font.BOLD, 12));

    aitoolabel = new JLabel(" ");
    aitoolabel.setHorizontalAlignment(SwingConstants.CENTER);
    aitoolabel.setForeground(Color.DARK_GRAY);
    aitoolabel.setFont(new Font("Tahoma", Font.BOLD, 12));

    aiguesseslabel = new JLabel(" ");
    aiguesseslabel.setHorizontalAlignment(SwingConstants.CENTER);
    aiguesseslabel.setForeground(Color.DARK_GRAY);
    aiguesseslabel.setFont(new Font("Tahoma", Font.BOLD, 9));
    GroupLayout gl_panel = new GroupLayout(panel);
    gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
        .addGroup(gl_panel.createSequentialGroup()
            .addGroup(gl_panel
                .createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup().addGap(37).addComponent(toolabel,
                    GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE))
                .addGroup(
                    gl_panel.createSequentialGroup().addGap(180)
                        .addComponent(titlelabel, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                        .addGap(142))
                .addGroup(gl_panel.createSequentialGroup().addGap(37)
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup().addGap(178)
                            .addComponent(textField).addGap(178))
                        .addComponent(titlelabel_1, GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                        .addComponent(attemptslabel, GroupLayout.DEFAULT_SIZE, 442,
                            Short.MAX_VALUE))))
            .addGap(38))
        .addGroup(gl_panel.createSequentialGroup().addGap(24)
            .addComponent(aiattemptslabel, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
            .addGap(24))
        .addGroup(Alignment.LEADING, gl_panel.createSequentialGroup().addGap(53)
            .addComponent(aiguesslabel, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE).addGap(53))
        .addGroup(Alignment.LEADING,
            gl_panel.createSequentialGroup().addGap(53)
                .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                    .addGroup(gl_panel.createSequentialGroup()
                        .addComponent(aiguesseslabel, GroupLayout.PREFERRED_SIZE, 411,
                            GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(gl_panel.createSequentialGroup()
                        .addComponent(aitoolabel, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                        .addGap(53)))));
    gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
        .createSequentialGroup().addContainerGap()
        .addComponent(titlelabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(titlelabel_1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
            GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.UNRELATED)
        .addComponent(toolabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        .addGap(76)
        .addComponent(aiguesslabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(aitoolabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(aiguesseslabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
        .addComponent(aiattemptslabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(attemptslabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)));

    panel.setLayout(gl_panel);
    contentPane.setLayout(gl_contentPane);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public void runAI(int min, int max) {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        int answer = aiHelper(min, max, (min + max) / 2);
        return null;
      }
    };
    worker.execute();
  }

  public int aiHelper(int min, int max, int current) {
    aiguesses.add(current);
    try {
      Thread.sleep(700);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (current > answer) {
      aitoolabel.setForeground(Color.RED);
      aitoolabel.setText("Too High");
      aiguesslabel.setText("AI guessed " + current);
      increaseAIAttempts();
      updateAIGuesses();
      return aiHelper(min, current, (min + current) / 2);
    } else if (current < answer) {
      aitoolabel.setForeground(Color.RED);
      aitoolabel.setText("Too Low");
      aiguesslabel.setText("AI guessed " + current);
      increaseAIAttempts();
      updateAIGuesses();
      return aiHelper(current, max, (max + current) / 2);
    }
    aitoolabel.setForeground(Color.GREEN.darker());
    aiguesslabel.setText("AI guessed " + current);
    aitoolabel.setText("Correct!");
    increaseAIAttempts();
    updateAIGuesses();
    return current;
  }

  public void increaseAttempts() {
    attempts++;
    attemptslabel.setText("Your Attempts: " + attempts);
  }

  public void increaseAIAttempts() {
    aiattempts++;
    aiattemptslabel.setText("AI Attempts: " + aiattempts);
  }

  public void updateAIGuesses() {
    String text = "";
    for (int i = 0; i < aiguesses.size(); i++) {
      if (i < aiguesses.size() - 1) {
        text += aiguesses.get(i) + "->";
      } else {
        text += aiguesses.get(i);
      }
    }
    aiguesseslabel.setText(text);
  }

  public void updateAnswer(int min, int max) {
    answer = Msn.randomInt(min, max);
  }

}
