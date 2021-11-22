package MsnC;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTextArea;
import MsnC.Utils.CodeLine;
import MsnC.Utils.Syntax;
import MsnLib.Msn;

/**
 * Interprets code line by line and collects variables.
 * 
 * @author Mason Marker
 * @version 1.0 - 09/22/2021
 */
public class ExecutionHandler {

  CodeLine[] lines;
  JTextArea console;
  LinkedHashMap<String, Object> vars;

  public ExecutionHandler(String code, JTextArea console) {
    this.console = console;
    vars = new LinkedHashMap<>();
    ArrayList<Character> chars = new ArrayList<>(List.of(Msn.box(code.toCharArray())));
    ArrayList<String> codelines = new ArrayList<>();
    String s = "";
    for (Character c : chars)
      if (c != ';')
        s += c;
      else {
        codelines.add(s);
        s = "";
      }
    ArrayList<CodeLine> codes = new ArrayList<>();
    int index = 1;
    for (String str : codelines)
      codes.add(new CodeLine(str, index++));
    lines = codes.toArray(CodeLine[]::new);
  }

  public void interpret() {
    
    
    
    
    
    for (int i = 0; i < lines.length; i++) {
      CodeLine line = lines[i];
      if (isVariable(line.command())) {
        if (line.op().equals("=")) {
          vars.put(line.command(), evaluate(line.postop()));
        } else if (line.op().equals("+=")) {
          if (Syntax.isInt(vars.get(line.command()))) {
            vars.put(line.command(),
                (int) (double) evaluate(line.postop()) + (int) vars.get(line.command()));
          } else if (Syntax.isDouble(vars.get(line.command()))) {
            vars.put(line.command(), (double) vars.get(line.command())
                + Double.valueOf(String.valueOf(evaluate(line.postop()))));
          } else if (Syntax.isString(vars.get(line.command()))) {
            vars.put(line.command(),
                (String) vars.get(line.command()) + String.valueOf(evaluate(line.postop())));
          }
        } else if (line.op().equals("-=")) {
          if (Syntax.isInt(vars.get(line.command()))) {
            vars.put(line.command(),
                (int) vars.get(line.command()) - (int) (double) evaluate(line.postop()));
          } else if (Syntax.isDouble(vars.get(line.command()))) {
            vars.put(line.command(), (double) vars.get(line.command())
                - Double.valueOf(String.valueOf(evaluate(line.postop()))));
          }
        } else if (line.op().equals("*=")) {
          if (Syntax.isInt(vars.get(line.command()))) {
            vars.put(line.command(),
                (int) vars.get(line.command()) * (int) (double) evaluate(line.postop()));
          } else if (Syntax.isDouble(vars.get(line.command()))) {
            vars.put(line.command(), (double) vars.get(line.command())
                * Double.valueOf(String.valueOf(evaluate(line.postop()))));
          }
        } else if (line.op().equals("/=")) {
          if (Syntax.isInt(vars.get(line.command()))) {
            vars.put(line.command(),
                (int) vars.get(line.command()) / (int) (double) evaluate(line.postop()));
          } else if (Syntax.isDouble(vars.get(line.command()))) {
            vars.put(line.command(), (double) vars.get(line.command())
                / Double.valueOf(String.valueOf(evaluate(line.postop()))));
          }
        } else if (line.op().equals("^=")) {
          if (Syntax.isInt(vars.get(line.command()))) {
            vars.put(line.command(),
                Math.pow((int) vars.get(line.command()), (int) (double) evaluate(line.postop())));
          } else if (Syntax.isDouble(vars.get(line.command()))) {
            vars.put(line.command(), Math.pow((double) vars.get(line.command()),
                Double.valueOf(String.valueOf(evaluate(line.postop())))));
          }
        } else if (line.op().equals("<>")) {
          if (Syntax.isInt(vars.get(line.command()))) {
            int postopval = (int) vars.get(line.postop());
            vars.put(line.postop(), vars.get(line.command()));
            vars.put(line.command(), postopval);
          } else if (Syntax.isDouble(vars.get(line.command()))) {
            double postopval = (double) vars.get(line.postop());
            vars.put(line.postop(), vars.get(line.command()));
            vars.put(line.command(), postopval);
          }
        } else if (line.op().equals("??")) {
          if (Syntax.isInt(vars.get(line.command()))) {
            vars.put(line.preop(), Syntax.DEFAULT_INT);
          } else if (Syntax.isDouble(vars.get(line.command()))) {
            vars.put(line.preop(), Syntax.DEFAULT_DOUBLE);
          } else if (Syntax.isChar(vars.get(line.command()))) {
            vars.put(line.preop(), Syntax.DEFAULT_CHAR);
          } else if (Syntax.isString(vars.get(line.command()))) {
            vars.put(line.preop(), Syntax.DEFAULT_STRING);
          } else {
            vars.put(line.preop(), Syntax.DEFAULT_OBJECT);
          }
        }


      } else if (line.command().equals("i")) {
        vars.put(line.variable(),
            (int) (double) Double.valueOf(String.valueOf(evaluate(line.postop()))));
      } else if (line.command().equals("d")) {
        vars.put(line.variable(), Double.valueOf(String.valueOf(evaluate(line.postop()))));
      } else if (line.command().equals("s")) {
        String[] w = Msn.getWords(line.postop());
        applyVariables(w);
        vars.put(line.variable(), Msn.toSequence(w));
      } else if (line.command().equals("c") && line.postop().length() == 1) {
        vars.put(line.variable(), line.postop().charAt(0));
      } else if (line.command().equals("b")) {
        vars.put(line.variable(), line.postop());
      } else if (line.command().equals("i[]")) {
        vars.put(line.variable(), Msn.toInt(Msn.extractNumbers(line.postop())));
      } else if (line.command().equals("d[]")) {
        vars.put(line.variable(), Msn.extractNumbers(line.postop()));
      } else if (line.command().equals("println")) {
        printToConsole(divided(line.line()), true);
      } else if (line.command().equals("print")) {
        printToConsole(divided(line.line()), false);
      } else if (line.command().equals("for") && Msn.getWords(line.line())[2].equals("until")) {

        if (!vars.containsKey(line.variable())) {
          vars.put(line.variable(), 0);
        } else {
          vars.put(line.variable(), (int) Msn.extractNumbers(Msn.getWords(line.line())[3])[0]);
        }



      } else {
        System.out.println("skipped line " + line.index());
      }


    }
  }



  /**
   * Prints a String to the IDE console.
   * 
   * @param s the String to print
   * @param ln has newline at the end
   */
  private void printToConsole(String s, boolean ln) {
    console.setText(console.getText() + s);
    if (ln)
      console.setText(console.getText() + "\n");
  }

  /**
   * Determines if the String[] passed contains variables.
   * 
   * @param s the String
   * @return if the String[] passed contains variables
   */
  private boolean containsVariables(String[] s) {
    for (int i = 0; i < s.length; i++)
      if (isVariable(s[i]))
        return true;
    return false;
  }

  /**
   * Applies the values of the variables within the String passed.
   * 
   * @param s the String
   * @return the fixed String
   */
  private void applyVariables(String[] divided) {
    for (int i = 0; i < divided.length; i++) {
      if (Syntax.isString(divided[i]) && isVariable(divided[i].replace("@", "")))
        divided[i] = divided[i].replace("@", "");
      else {
        if (isVariable(divided[i])) {
          Object o = vars.get(divided[i]);
          if (!o.getClass().isArray()) {
            divided[i] = String.valueOf(vars.get(divided[i]));
          } else {
            divided[i] = Syntax.arrayToString(o);
          }
        }
      }
    }
  }

  /**
   * Determines if the String passed is a variable.
   * 
   * @param s the String
   * @return if the String passed is a variable
   */
  private boolean isVariable(String s) {
    for (Map.Entry<String, Object> en : vars.entrySet())
      if (en.getKey().equals(s))
        return true;
    return false;
  }

  private String divided(String div) {
    ArrayList<String> toprint = new ArrayList<>(List.of(Msn.getWords(div)));
    toprint.remove(0);
    String[] divided = toprint.toArray(String[]::new);
    applyVariables(divided);
    return Msn.toSequence(divided);
  }

  /**
   * Evaluates the value of the postop.
   * 
   * @param s the CodeLine
   * @return the evaluation
   */
  private Object evaluate(String s) {
    String[] divided = Msn.getWords(s);
    Object ret = null;
    if (!Msn.isNumber(divided[0]) && !containsVariables(divided))
      return s;
    applyVariables(divided);
    String applied = Msn.toSequence(divided);
    if (ret == null)
      ret = Msn.evalulate(applied);
    return ret;
  }

}
