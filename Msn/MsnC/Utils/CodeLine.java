package MsnC.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import MsnLib.Msn;

/**
 * Interprets lines of MSNC.
 * 
 * @author Mason Marker
 * @version 1.0 - 09/23/2021
 */
public class CodeLine {

  String line;
  String command;
  String variable;
  String preop;
  String op;
  String postop;
  int index;

  public CodeLine(String codeline, int index) {
    this.line = codeline.replaceAll("\r\n", "").replaceAll("\n", "");
    try {
      if (line.charAt(0) == ' ') {
        ArrayList<String> l = new ArrayList<>(List.of(Msn.getWords(line)));
        l.remove(0);
        line = Msn.toSequence(l.toArray(String[]::new));
      }
    } catch (IndexOutOfBoundsException e) {
    }
    this.index = index;
    Scanner sc = new Scanner(line);
    preop = "";
    op = "";
    postop = "";
    boolean opfound = false;
    while (sc.hasNext()) {
      String current = sc.next();
      if (Syntax.isValidOperator(current) && !opfound) {
        opfound = true;
        op = current;
      } else if (opfound)
        if (sc.hasNext())
          postop += current + " ";
        else
          postop += current;
      else if (sc.hasNext())
        preop += current + " ";
      else
        preop += current;
    }
    try {
      if (postop.charAt(postop.length() - 1) == ' ') {
        ArrayList<String> l = new ArrayList<>(List.of(Msn.getWords(postop)));
        l.remove(postop.length() - 1);
        postop = Msn.toSequence(l.toArray(String[]::new));
      }
    } catch (IndexOutOfBoundsException e) {
    }
    String[] words = Msn.getWords(line);
    command = words[0].replaceAll(" ", "");
    try {
      variable = words[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      variable = Syntax.DEFAULT_STRING;
    }
  }

  public String line() {
    return line;
  }

  public String preop() {
    return preop;
  }

  public String op() {
    return op;
  }

  public String postop() {
    return postop;
  }

  public String command() {
    return command;
  }

  public String variable() {
    return variable;
  }

  public CodeLine copy() {
    CodeLine copy = new CodeLine(line, index);
    return copy;
  }
  
  public int index() {
    return index;
  }

  public void setLine(String line) {
    CodeLine c = new CodeLine(line, index);
    this.line = line;
    this.command = c.command();
    this.op = c.op();
    this.postop = c.postop();
    this.preop = c.preop();
    this.variable = c.variable();
  }

  public String toString() {

    String s = "-----\n";
    s += "CODELINE " + index + ":\n";
    s += "preop: " + preop + ", op: " + op + ", postop: " + postop + ", command: " + command
        + ", variable: " + variable + "\n";
    s += "-----\n";
    return s;
  }

}
