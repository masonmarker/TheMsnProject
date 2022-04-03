package MsnC.Utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import MsnC.ExecutionHandler;
import MsnLib.Msn;

public class KeywordStyledDocument extends DefaultStyledDocument {
  private static final long serialVersionUID = 1L;
  ExecutionHandler handler;

  public KeywordStyledDocument(ExecutionHandler h) {
    handler = h;
  }

  public void setHandler(ExecutionHandler h) {
    handler = h;

  }

  public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
    super.insertString(offset, str, a);
    refreshDocument();
  }

  public void remove(int offs, int len) throws BadLocationException {
    super.remove(offs, len);
    refreshDocument();
  }

  private synchronized void refreshDocument() throws BadLocationException {
    String text = getText(0, getLength());
    final List<HiliteWord> list = processWords(text);


    StyleContext styleContext = new StyleContext();


    Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
    Style cwStyle = styleContext.addStyle("ConstantWidth", null);


    setCharacterAttributes(0, text.length(), defaultStyle, true);
    try {
      for (HiliteWord word : list) {
        if (Msn.countChars(word._word, '_') > 1) {
          StyleConstants.setFontSize(cwStyle, 10);
        } else {
          StyleConstants.setFontSize(cwStyle, 13);
        }
        int p0 = word._position;
        if (handler.isVariable(word._word)) {
          StyleConstants.setBold(cwStyle, true);
          StyleConstants.setForeground(cwStyle, Color.YELLOW);
        } else if (handler.isFunction(word._word)) {
          StyleConstants.setBold(cwStyle, true);
          StyleConstants.setForeground(cwStyle, Color.RED.brighter());
        } else if (word._word.equals("end")) {
          StyleConstants.setBold(cwStyle, true);
          StyleConstants.setForeground(cwStyle, Color.RED.brighter());
        } else if (Msn.isNumber(word._word)) {
          StyleConstants.setBold(cwStyle, true);
          StyleConstants.setForeground(cwStyle, Color.GREEN.brighter());
        } else if (Syntax.isValidCommand(word._word)) {
          StyleConstants.setBold(cwStyle, true);
          StyleConstants.setForeground(cwStyle, Color.ORANGE);
        } else if (handler.isStruct(word._word)) {
          StyleConstants.setBold(cwStyle, true);
          StyleConstants.setForeground(cwStyle, Color.CYAN);
        } else if (Syntax.isValidFunctionalTerm(word._word)) {
          StyleConstants.setBold(cwStyle, true);
          StyleConstants.setForeground(cwStyle, Color.LIGHT_GRAY);
        } else if (Syntax.isValidImport(word._word)) {
          StyleConstants.setBold(cwStyle, false);
          StyleConstants.setForeground(cwStyle, Color.CYAN.brighter());
        }
        setCharacterAttributes(p0, word._word.length(), cwStyle, true);
      }
    } catch (NullPointerException e) {
    }
  }

  private List<HiliteWord> processWords(String content) {
    content += " ";
    List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
    int lastWhitespacePosition = 0;
    String word = "";
    char[] data = content.toCharArray();

    for (int index = 0; index < data.length; index++) {
      char ch = data[index];
      if (!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')) {
        lastWhitespacePosition = index;
        if (word.length() > 0) {
          if (isReservedWord(word)) {
            hiliteWords.add(new HiliteWord(word, (lastWhitespacePosition - word.length())));
          }
          word = "";
        }
      } else {
        word += ch;
      }
    }
    return hiliteWords;
  }

  private boolean isReservedWord(String word) {
    try {
      return Syntax.isKeyword(word) || word.equals("end") || handler.isVariable(word)
          || handler.isFunction(word) || Msn.isNumber(word) || handler.isStruct(word);
    } catch (NullPointerException e) {
      return false;
    }
  }


}
