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
public class CodeLine implements Comparable<CodeLine> {

  String line;
  String command;
  String variable;
  String preop;
  String op;
  String postop;
  String lp;
  int index;

  boolean loop;
  boolean bool;
  String boolexp;
  String boolop;

  String boperand1;
  String boperand2;

  public CodeLine(String codeline, int index) {
    this.line = codeline.replaceAll("\r\n", "").replaceAll("\n", "").replaceAll(" +", " ").trim();
    String[] split = line.split(" ");
    try {
      if (line.charAt(0) == ' ') {
        ArrayList<String> l = new ArrayList<>(List.of(split));
        l.remove(0);
        line = Msn.toSequence(l.toArray(String[]::new));
      }
    } catch (IndexOutOfBoundsException e) {
    }
    this.index = index;
    boolop = "";
    String[] split2 = line.split(" ");
    if (split2[0].contains("{") && split2[0].contains("}")) {
      bool = true;
      boolexp = split2[0].replace("}", "").replace("{", "");
      for (String op : Syntax.VALID_OPERATORS) {
        if (split2[0].contains(op)) {
          boolop = op;
        }
      }
      String[] splitb = boolexp.split(boolop);
      try {
        boperand1 = splitb[0];
        boperand2 = splitb[1];
      } catch (ArrayIndexOutOfBoundsException e) {
      }
      ArrayList<String> list = new ArrayList<>(List.of(split2));
      list.remove(0);
      String jn = "";
      for (int i = 0; i < list.size(); i++) {
        if (i != list.size() - 1) {
          jn += list.get(i) + " ";
        } else {
          jn += list.get(i);
        }
      }
      setLine(jn);
    }

    Scanner sc = new Scanner(line);
    preop = "";
    op = "";
    postop = "";
    loop = false;
    boolean opfound = false;
    while (sc.hasNext()) {
      String current = sc.next();
      if (Syntax.isValidOperator(current) && !Syntax.isBooleanOperator(current) && !opfound) {
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
    lp = "";
    preop = preop.trim();
    String last = split[split.length - 1];
    lp = last;
    if (last.contains("[") && last.contains("]") && last.contains(":") && !line.contains(" = ")) {
      loop = true;
      ArrayList<String> l = new ArrayList<>(List.of(split));
      l.remove(l.size() - 1);
      String[] fixed = Msn.toString(l.toArray());
      String joined = "";
      for (int j = 0; j < fixed.length; j++) {
        if (j != fixed.length - 1) {
          joined += fixed[j] + " ";
        } else {
          joined += fixed[j];
        }
      }
      setLine(joined);
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

  public boolean loop() {
    return loop;
  }

  public String lp() {
    return lp;
  }

  public int index() {
    return index;
  }

  public boolean bool() {
    return bool;
  }

  public String boolexp() {
    return boolexp;
  }

  public String bop1() {
    return boperand1;
  }

  public String bop2() {
    return boperand2;
  }

  public String boolop() {
    return boolop;
  }

  public boolean equals(Object o) {
    if (o instanceof CodeLine) {
      return ((CodeLine) o).line().equals(line());
    }
    return false;
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
    s += line + "\n";
    s += "preop: " + preop + ", op: " + op + ", postop: " + postop + ", command: " + command
        + ", variable: " + variable + ", loop: " + loop + ", lp: " + lp + ", bool:" + bool
        + ", boolexp: " + boolexp + "\n";
    s += "-----\n";
    return s;
  }
  
  public CodeLine copyOf() {
    return new CodeLine(line, index);
  }

  @Override
  public int compareTo(CodeLine o) {
    if (o.line().equals(line())) {
      return 1;
    }
    if (o.line().length() > line().length()) {
      return 1;
    }
    return -1;
  }

}
