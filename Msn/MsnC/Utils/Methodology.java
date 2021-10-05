package MsnC.Utils;

import java.util.LinkedHashMap;
import javax.swing.JPanel;

public class Methodology {

  private JPanel suggestionarea;
  private String text;
  private int caretposition;
  
  public Methodology(JPanel suggestionarea, String text, int caretposition, LinkedHashMap<String, Object> vars) {
    this.suggestionarea = suggestionarea;
    this.text = text;
    this.caretposition = caretposition;
  }
  
  /**
   * Evaluates code pre-period.
   */
  public void evaluatepreperiod() {
    int i = 2;
    while (text.charAt(caretposition) != ' ') {
      
    }
  }
  
}
