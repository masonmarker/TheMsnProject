package MsnC;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.JTextArea;
import MsnC.Utils.CodeLine;
import MsnC.Utils.Syntax;
import MsnLib.Msn;
import MsnStructures.MsnStream;

/**
 * Interprets code line by line and collects variables.
 * 
 * @author Mason Marker
 * @version 1.0 - 09/22/2021
 */
public class ExecutionHandler {

  CodeLine[] lines;
  JTextArea console;
  public LinkedHashMap<String, Object> vars;

  public LinkedHashSet<Function> functions;

  private long start;

  public ExecutionHandler(String code, JTextArea console) {
    this.console = console;
    functions = new LinkedHashSet<>();
    vars = new LinkedHashMap<>();
    lines = toCodeLines(code);
    compile(lines, code);
  }

  public CodeLine[] toCodeLines(String code) {
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
    return codes.toArray(CodeLine[]::new);
  }

  public void compile(CodeLine[] lines, String code) {
    code = Msn.removeEmptyLines(code);
    int semicount = Msn.countChars(code, ';');
    int linecount = Msn.countLines(code);
    if (semicount != linecount) {
      printToConsole("[-] missing ';' || need " + (linecount - semicount) + " more", true);
    }
  }

  public CodeLine[] lines() {
    return lines;
  }

  public void interpret(CodeLine[] lines, boolean functionCall) throws Exception {


    for (int i = 0; i < lines.length; i++) {

      CodeLine line = lines[i];

      String[] splitline = line.line().split(" ");
      if (splitline[0].equals("::")) {
        continue;
      }
      System.out.println(line);
      if (splitline[0].equals("import")) {
        String cl = splitline[1];
        String code = null;

        // no idea why this has to be done this way but just know it does
        switch (cl) {
          case "math":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\math.txt");
            break;
          case "bool":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\bool.txt");
            break;
          case "loop":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\loop.txt");
            break;
          case "string":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\string.txt");
            break;
          case "list":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\list.txt");
            break;
          case "random":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\random.txt");

            break;
        }
        interpret(toCodeLines(code), false);
      } else {

        boolean run = true;
        if (line.bool() && !booleval(line)) {
          run = false;
        }



        if (run && line.line().contains("timestart")) {
          start = System.nanoTime();
        } else if (run && line.line().contains("timestop")) {
          long t = System.nanoTime();
          printToConsole("runtime: " + (t - start) + "ns ("
              + (TimeUnit.NANOSECONDS.toMillis(t - start) + "ms)"), true);
        } else {
          String[] loop = line.lp().replace("[", "").replace("]", "").split(":");
          applyVariables(loop);
          String loopind = Msn.toSequence(loop);
          int[] indices = new int[2];
          indices = Msn.toInt(Msn.extractNumbers(loopind));
          if (line.loop() && run) {
            if (indices[0] > indices[1]) {
              for (int j = indices[0]; j > indices[1]; j--) {
                store(line, functionCall);
              }
            } else if (indices[0] < indices[1]) {
              for (int j = indices[0]; j < indices[1]; j++) {
                store(line, functionCall);
              }
            }
          } else if (run) {
            store(line, functionCall);
          }
        }
      }
    }
  }

  public LinkedHashSet<Function> functions() {
    return functions;
  }

  public LinkedHashMap<String, Object> getVars() {
    return vars;
  }

  private boolean booleval(CodeLine line) {

    String[] split = line.boolexp().split(line.boolop());
    Double v1 = null;
    Double v2 = null;
    try {
      v1 = Double.valueOf(String.valueOf(vars.get(split[0])));
      v2 = Double.valueOf(String.valueOf(vars.get(split[1])));
    } catch (NumberFormatException e) {
    }
    switch (line.boolop()) {
      case "==":
        return vars.get(split[0]).equals(vars.get(split[1]));
      case "!=":
        return !vars.get(split[0]).equals(vars.get(split[1]));
      case ">":
        return v1 > v2;
      case "<":
        return v1 < v2;
      case "<=":
        return v1 <= v2;
      case ">=":
        return v1 >= v2;
    }
    if (line.boolexp().equals("0")) {
      return false;
    } else if (line.boolexp().equals("1")) {
      return true;
    } else if (isVariable(line.boolexp())) {
      return ((int) vars.get(line.boolexp())) == 1;
    }
    return true;
  }

  /**
   * Performs the lines intended operation
   * 
   * @param line the line to execute
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void store(CodeLine line, boolean functionCall) throws Exception {
    if (isVariable(line.command())) {
      if (line.op().equals("=")) {
        if (Syntax.isInt(vars.get(line.command()))) {
          if (line.postop().contains("?")) {
            vars.put(line.command(), new Random().nextInt());
          } else {
            vars.put(line.command(), (int) (double) evaluate(line.postop()));
          }
        } else if (Syntax.isString(vars.get(line.command()))) {
          if (line.postop().equals("&")) {
            vars.put(line.command(), "");
          } else {
            String[] split = line.postop().split(" ");
            applyVariables(split);
            vars.put(line.command(), String.valueOf(Msn.toSequence(split)));
          }
        } else if (Syntax.isDouble(vars.get(line.command()))) {
          if (line.postop().contains("?")) {
            vars.put(line.command(), new Random().nextDouble());
          } else {
            vars.put(line.command(), evaluate(line.postop()));
          }
        } else if (Syntax.isChar(vars.get(line.command()))) {
          vars.put(line.command(), String.valueOf(evaluate(line.postop())).charAt(0));
        } else if (Syntax.isObject(vars.get(line.command()))) {
          vars.put(line.command(), evaluate(line.postop()));
        }

        else {
          vars.put(line.command(), evaluate(line.postop()));
        }
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
        } else if (Syntax.isString(vars.get(line.command()))) {
          String copy = (String) vars.get(line.postop());
          vars.put(line.postop(), vars.get(line.command()));
          vars.put(line.command(), copy);
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
      } else if (line.op().equals("++")) {
        if (Syntax.isString(vars.get(line.preop()))) {
          if (line.postop().equals(":w:")) {
            vars.put(line.preop(), ((String) vars.get(line.preop())).concat(" "));
          } else {
            String[] split = line.postop().split(" ");
            applyVariables(split);
            vars.put(line.preop(), ((String) vars.get(line.preop())).concat(Msn.toSequence(split)));
          }
        }
      } else if (line.op().equals("r=")) {
        vars.put(line.preop(), Math.sqrt(Double.valueOf(String.valueOf(evaluate(line.postop())))));
      } else if (line.op().equals("m=")) {
        vars.put(line.preop(),
            ((Integer) vars.get(line.preop())) % ((int) (double) evaluate(line.postop())));
      } else if (Syntax.isList(vars.get(line.command()))) {
        if (vars.get(line.postop()) != null && Syntax.isList(vars.get(line.postop().trim()))) {
          MsnStream<Object> command = (MsnStream<Object>) vars.get(line.command());
          MsnStream<Object> postop = (MsnStream<Object>) vars.get(line.postop().trim());
          command.addAll(postop);
        } else {
          String[] split = line.line().split(" ");
          String listop = split[1];
          System.out.println(line);
          ArrayList<String> l = new ArrayList<>(List.of(split));
          l.remove(0);
          l.remove(0);
          split = l.toArray(String[]::new);
          if (!Msn.contains(split, "->")) {
            applyVariables(split);
          }
          String joined = Msn.toSequence(split);
          MsnStream<Object> list = ((MsnStream<Object>) vars.get(line.command()));
          switch (listop) {
            case "add":
              list.add(evaluate(joined));
              break;
            case "contains":
              Object contains = vars.get(split[0]);
              String boollocation = null;
              if (split[1].equals("->")) {
                boollocation = split[2];
              }
              if (list.contains(contains)) {
                vars.put(boollocation, 1);
              } else {
                vars.put(boollocation, 0);
              }
              break;
            case "remove":
              list.remove(evaluate(joined));
              break;
            case "removeat":
              list.remove((int) (double) evaluate(joined));
              break;
            case "getat":
              String location = null;
              if (split[1].equals("->")) {
                location = split[2];
              }
              vars.put(location, list.get((int) (double) evaluate(split[0])));
              break;
            case "length":
              if (split[0].equals("->")) {
                vars.put(split[1], list.size());
              }
              break;
            case "copy":
              if (split[0].equals("->")) {
                vars.put(split[1], list.copyOf());
              }
              break;
            case "join":
              String loc = split[split.length - 1];
              if (split[1].equals("->")) {

                vars.put(loc, list.join(String.valueOf(vars.get(split[0]))));
                System.out.println("joining");
              }
              break;
            case "reverse":
              list._reversed();
              break;
            case "uniq":
              list._withoutDuplicates();
              break;
            case "sort":
              list._sorted();
              break;
          }
        }
      } else if (Msn.getWords(line.line())[1].equals("->")) {
        String[] split = Msn.getWords(line.line());
        if (Syntax.isString(vars.get(split[0]))) {
          try {
            if (Syntax.isList(vars.get(split[2]))) {
              ArrayList<String> chars = new ArrayList<>();
              String str = String.valueOf(vars.get(split[0]));
              for (int i = 0; i < str.length(); i++) {
                chars.add("" + str.charAt(i));
              }
              vars.put(split[2], new MsnStream<String>(chars));
            }
          } catch (NullPointerException e) {
            error("unknown variable, line " + line.index() + " (" + line.line() + ")",
                line.index());
          }
        }
      }
    } else if (line.command().equals("i")) {
      vars.put(line.variable(),
          (int) (double) Double.valueOf(String.valueOf(evaluate(line.postop()))));
    } else if (line.command().equals("d")) {
      try {
        vars.put(line.variable(), Double.valueOf(String.valueOf(evaluate(line.postop()))));
      } catch (NumberFormatException e) {
        vars.put(line.variable(), Syntax.DEFAULT_OBJECT);
      }
    } else if (line.command().equals("s")) {
      String[] w = Msn.getWords(line.postop());
      applyVariables(w);
      if (w[w.length - 1].equals("&")) {
        vars.put(line.variable(), "");
      } else {
        vars.put(line.variable(), Msn.toSequence(w));
      }
    } else if (line.command().equals("c") && line.postop().length() == 1) {
      vars.put(line.variable(), line.postop().charAt(0));
    } else if (line.command().equals("b")) {
      vars.put(line.variable(), line.postop());
    } else if (line.command().equals("o")) {
      if (line.postop().equals("&")) {
        vars.put(line.variable(), "");
      } else {
        vars.put(line.variable(), evaluate(line.postop()));
      }
    } else if (line.command().equals("i[]")) {
      vars.put(line.variable(), Msn.toInt(Msn.extractNumbers(line.postop())));
    } else if (line.command().equals("d[]")) {
      vars.put(line.variable(), Msn.extractNumbers(line.postop()));
    } else if (line.command().equals("s[]")) {
      vars.put(line.variable(), Msn.getWords(line.postop()));
    } else if (line.command().equals("println")) {
      printToConsole(divided(line.line()), true);
    } else if (line.command().equals("print")) {
      printToConsole(divided(line.line()), false);
    } else if (line.command().equals("assert")) {
      String[] as = line.line().replace("assert", "").replace(";", "").trim().split(" ");
      if (!vars.get(as[0]).equals(vars.get(as[1]))) {
        printToConsole("[-] assertion error (" + line.index() + "): '" + line.line() + "' "
            + vars.get(as[0]) + ", " + vars.get(as[1]), true);
      }
    } else if (line.command().equals("!assert")) {
      String[] as = line.line().replace("!assert", "").replace(";", "").trim().split(" ");
      if (vars.get(as[0]).equals(vars.get(as[1]))) {
        printToConsole("[-] assertion error (" + line.index() + "): '" + line.line() + "' "
            + vars.get(as[0]) + ", " + vars.get(as[1]), true);
      }
    } else if (line.command().equals("f") && Msn.getWords(line.line()).length == 2) {
      String[] split = Msn.getWords(line.line());
      String def = "no comments";
      functions.add(new Function(split[1], def));

    } else if (line.command().equals("f") && Msn.getWords(line.line()).length == 3) {
      String[] split = Msn.getWords(line.line());
      String def = String.valueOf(vars.get(split[2]));
      functions.add(new Function(split[1], def));
    }

    else if (isFunction(line.preop()) && line.line().contains(" = ")) {
      Function func = getFunctionByName(line.preop());
      if (!func.defined) {
        for (int i = 0; i < functions.size(); i++) {
          if (getAt(i).name().equals(line.preop())) {
            getAt(i).addDef(line);
            String[] found = Syntax.params(line.line());
            String[] returns = Syntax.returns(line.line());
            for (String s : found) {
              getAt(i).addParam(s);
            }
            for (String s : returns) {
              getAt(i).addReturn(s);
            }
          }
        }
      }
    } else if (isFunction(line.command()) || isFunction(line.preop())) {
      Function f = (Function) getFunctionByName(line.command());
      String[] split = line.line().split(" ");
      try {
        if (split[1].equals("with")) {
          ArrayList<String> dropping = new ArrayList<>(List.of(split));
          dropping.remove(0);
          dropping.remove(0);
          split = dropping.toArray(String[]::new);
          for (int i = 0; i < f.params().size(); i++) {
            String currentparam = Msn.getAt(i, f.params());
            if (Syntax.isInt(vars.get(currentparam))) {
              vars.put(currentparam, (int) (double) evaluate(split[i]));
            } else if (Syntax.isDouble(vars.get(currentparam))) {
              vars.put(currentparam, evaluate(split[i]));
            } else if (Syntax.isString(vars.get(currentparam))) {
              vars.put(currentparam, String.valueOf(evaluate(split[i])));
            } else if (Syntax.isList(vars.get(currentparam))) {
              MsnStream<Object> list = (MsnStream<Object>) vars.get(split[i]);
              vars.put(currentparam, list.copyOf());
              System.out.println(currentparam);
            }
          }
          if (isFunction(line.preop())) {
            getFunctionByName(line.preop()).run();
          } else {
            getFunctionByName(line.command()).run();
          }
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        try {
          getFunctionByName(line.preop()).run();
        } catch (NullPointerException e1) {
          error("invalid arguments to '" + line.preop() + "'", line.index());
        }
      }

    } else if (line.command().equals("l")) {
      vars.put(line.variable(), new MsnStream<Object>());
    } else if (line.command().equals("move")) {
      String[] split = line.line().split(" ");
      if (split[2].equals("to")) {
        vars.put(split[3], vars.get(split[1]));
      }
    } else if (line.command().equals("end")) {
      String[] split = line.line().split(" ");
      getFunctionByName(split[1]).setDefined();
    }

  }

  public Function getFunctionByName(String name) {
    for (Function f : functions) {
      if (f.name().equals(name)) {
        return f;
      }
    }
    return null;
  }

  public Function getAt(int index) {
    int i = 0;
    for (Function f : functions) {
      if (i == index) {
        return f;
      }
      i++;
    }
    return null;
  }

  public void error(String msg, int index) throws Exception {
    printToConsole("[-] error (" + index + ") : " + msg, true);
    throw new Exception(msg);
  }

  public boolean isFunction(String name) {
    for (Function f : functions) {
      if (f.name.equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Prints a String to the IDE console.
   * 
   * @param s the String to print
   * @param ln has newline at the end
   */
  public void printToConsole(String s, boolean ln) {
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
  public void applyVariables(String[] divided) {
    for (int i = 0; i < divided.length; i++) {
      if (Syntax.isString(divided[i]) && isVariable(divided[i].replace("@", "")))
        divided[i] = divided[i].replace("@", "");
      else {
        if (isVariable(divided[i])) {
          divided[i] = String.valueOf(vars.get(divided[i]));
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

  public void addVariable(String name, Object obj) {
    vars.put(name, obj);
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
    applyVariables(divided);
    if (!Msn.isNumber(divided[0]) && !containsVariables(divided)) {
      return s;
    }
    ret = Msn.evaluate(Msn.toSequence(divided));
    return ret;
  }

  class Function {
    String name;
    String comments;
    LinkedHashSet<String> params;
    LinkedHashSet<String> ret;
    public ArrayList<CodeLine> inside;

    public boolean defined;

    public Function(String name, String comments) {
      this.name = name;
      this.comments = comments;
      inside = new ArrayList<>();
      params = new LinkedHashSet<>();
      ret = new LinkedHashSet<>();
      defined = false;
    }

    public void setDefined() {
      defined = true;
    }

    public String name() {
      return name;
    }

    public String comments() {
      return comments;
    }

    public void run() throws Exception {
      interpret(inside.toArray(CodeLine[]::new), true);
    }

    public void addParam(String param) {
      params.add(param);
    }

    public void addReturn(String r) {
      ret.add(r);
    }

    public HashSet<String> params() {
      return params;
    }

    public HashSet<String> returns() {
      return ret;
    }

    public void addDef(CodeLine line) {
      inside.add(new CodeLine(line.line().replaceFirst(name + " = ", "").trim(), line.index()));
    }

    public boolean equals(Object o) {
      if (o instanceof Function) {
        if (((Function) o).name().equals(name)) {
          return true;
        }
      }
      return false;
    }

  }
}
