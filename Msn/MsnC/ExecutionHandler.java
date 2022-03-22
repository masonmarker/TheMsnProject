package MsnC;

import java.util.ArrayList;
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
 * @version 1.0 - 03/13/2022
 */
public class ExecutionHandler {

  CodeLine[] lines;
  JTextArea console;
  public LinkedHashMap<String, Object> vars;
  public LinkedHashSet<Function> functions;
  public LinkedHashSet<Obj> objects;
  private long start;
  public int linesrun;

  public ExecutionHandler(String code, JTextArea console) {
    this.console = console;
    linesrun = 0;
    functions = new LinkedHashSet<>();
    vars = new LinkedHashMap<>();
    objects = new LinkedHashSet<>();
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
      printToConsole("[*] possibly missing ';'  (line count not equal to semicolon count)", true);
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
      if (splitline[0].equals("import")) {
        String cl = null;
        try {
          cl = splitline[1];
        } catch (ArrayIndexOutOfBoundsException e) {
          error("failed to import library (did you spell the library name correctly?)", line);
        }
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
          case "function":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\function.txt");
            break;
          case "games":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\games.txt");
            break;
          case "point":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\point.txt");
            break;
          case "arraylist":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\arraylist.txt");
            break;
          case "object":
            code = Msn.contentsOfNoEmptyLines(
                "C:\\Users\\mason\\OneDrive\\Documents\\GitHub\\TheMsnProject\\Msn\\object.txt");
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
        try {
          return vars.get(split[0]).equals(vars.get(split[1]));
        } catch (NullPointerException e) {
          try {
            return objEquals(getObjByName(split[0]), getObjByName(split[1]));
          } catch (NullPointerException e1) {
            try {
              error("undefined argument in boolean expression", line);
            } catch (Exception e2) {
              // TODO Auto-generated catch block
              e2.printStackTrace();
            }

          }
        }
      case "!=":
        try {
          return !vars.get(split[0]).equals(vars.get(split[1]));
        } catch (NullPointerException e) {
          try {
            return !objEquals(getObjByName(split[0]), getObjByName(split[1]));
          } catch (NullPointerException e1) {
            try {
              error("undefined argument in boolean expression", line);
            } catch (Exception e2) {
              // TODO Auto-generated catch block
              e2.printStackTrace();
            }

          }
        }
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

  @SuppressWarnings("unchecked")
  private boolean objEquals(Obj o1, Obj o2) {
    String[] o1vars = o1.getVariables();
    String[] o2vars = o2.getVariables();
    if (o1vars.length != o2vars.length) {
      return false;
    }
    for (int i = 0; i < o1vars.length; i++) {
      Object first = vars.get(o1vars[i]);
      Object second = vars.get(o2vars[i]);


      if (Syntax.isList(first)) {
        MsnStream<Object> firstlist = (MsnStream<Object>) first;
        MsnStream<Object> secondlist = (MsnStream<Object>) second;
        if (!firstlist.equals(secondlist)) {
          return false;
        }
      } else {
        if (!first.equals(second)) {
          return false;
        }
      }
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
    String[] cut = line.line().split(" ");
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
          String[] split = cut;
          String listop = split[1];

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
              try {
                MsnStream<Object> adding = (MsnStream<Object>) vars.get(cut[2]);
                list.add(adding.copyOf());
              } catch (NullPointerException | ClassCastException e4) {
                list.add(evaluate(joined));
              }
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
              int eval = Integer.MIN_VALUE;
              try {
                eval = (int) (double) evaluate(split[0]);
                vars.put(location, list.get(eval));
              } catch (IndexOutOfBoundsException e) {
                error("index out of bounds: " + eval, line);
              }
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
            case "hash":
              list._hashed();
              break;
            case "shuffle":
              list._shuffled();
              break;
            case "rmwhitespace":
              try {
                list.remove(" ");
              } catch (IndexOutOfBoundsException e) {
                try {
                  list.remove(' ');
                } catch (Exception e1) {
                  error("list does not contain string", line);
                }
              }
              break;
            case "clear":
              list._empty();
              break;
          }
        }
      } else if (cut[1].equals("->")) {
        String[] split = cut;
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
            error("unknown variable, line " + line.index() + " (" + line.line() + ")", line);
          }
        }
      }
    } else if (line.command().equals("i")) {
      try {
        vars.put(line.variable(),
            (int) (double) Double.valueOf(String.valueOf(evaluate(line.postop()))));
      } catch (RuntimeException e) {
        error("couldn't instantiate variable", line);
      }
    } else if (line.command().equals("d")) {
      try {
        vars.put(line.variable(), Double.valueOf(String.valueOf(evaluate(line.postop()))));
      } catch (Exception e) {
        error("couldn't instantiate variable", line);
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
      if (line.postop().equals("&")) {
        vars.put(line.variable(), ' ');
      } else {
        vars.put(line.variable(), String.valueOf(evaluate(line.postop())).charAt(0));
      }
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
    } else if (line.command().equals("f") && cut.length == 2) {
      String[] split = cut;
      String def = "no comments";
      functions.add(new Function(split[1], def));

    } else if (line.command().equals("f") && cut.length == 3) {
      String[] split = cut;
      String def = null;
      if (split[2].contains("_def")) {
        def = String.valueOf(vars.get(split[2]));
        functions.add(new Function(split[1], def));
      } else {
        String p = String.valueOf(vars.get(split[2]));
        Function func = new Function(split[1], def);
        String[] params = Syntax.params(p);
        String[] returns = Syntax.returns(p);
        for (String param : params) {
          func.addParam(param);
        }
        for (String r : returns) {
          func.addReturn(r);
        }
        functions.add(func);
      }
    } else if (line.command().equals("f") && cut.length == 4) {
      String[] split = cut;
      String def = null;
      Function funct = null;
      String p = null;
      if (split[2].contains("_def")) {
        def = String.valueOf(vars.get(split[2]));
        funct = new Function(split[1], def);
        p = String.valueOf(vars.get(split[3]));
        String[] params = Syntax.params(p);
        String[] returns = Syntax.returns(p);
        for (String param : params) {
          funct.addParam(param);
        }
        for (String r : returns) {
          funct.addReturn(r);
        }
        functions.add(funct);
      } else if (split[3].contains("_def")) {
        def = String.valueOf(vars.get(split[3]));
        p = String.valueOf(vars.get(split[2]));
        funct = new Function(split[1], def);
        String[] params = Syntax.params(p);
        String[] returns = Syntax.returns(p);
        for (String param : params) {
          funct.addParam(param);
        }
        for (String r : returns) {
          funct.addReturn(r);
        }
        functions.add(funct);
      }
    } else if (isFunction(line.preop()) && line.line().contains(" = ")) {
      Function func = getFunctionByName(line.preop());
      if (!func.defined) {
        for (int i = 0; i < functions.size(); i++) {
          if (getAt(i).name().equals(line.preop())) {
            getAt(i).addDef(line);
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
            String currentparam = f.params().get(i);
            if (Syntax.isInt(vars.get(currentparam))) {
              vars.put(currentparam, (int) (double) evaluate(split[i]));
            } else if (Syntax.isDouble(vars.get(currentparam))) {
              vars.put(currentparam, evaluate(split[i]));
            } else if (Syntax.isString(vars.get(currentparam))) {
              if (vars.get(split[i]) != null) {
                vars.put(currentparam, vars.get(split[i]));
              } else {
                vars.put(currentparam, split[i]);
              }
            } else if (Syntax.isList(vars.get(currentparam))) {
              MsnStream<Object> list = (MsnStream<Object>) vars.get(split[i]);
              vars.put(currentparam, list.copyOf());
            } else if (Syntax.isChar(vars.get(currentparam))) {
              if (vars.get(split[i]) != null) {
                vars.put(currentparam, vars.get(split[i]));
              } else {
                vars.put(currentparam, split[i].charAt(0));
              }
            }
          }
          if (isFunction(line.preop())) {
            getFunctionByName(line.preop()).run();
          } else {
            try {
              getFunctionByName(line.command()).run();
            } catch (NullPointerException e) {
              error("invalid arguments", line);
            }
          }
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        try {
          getFunctionByName(line.preop()).run();
        } catch (NullPointerException e1) {
          error("invalid arguments", line);
        }
      }

    } else if (line.command().equals("l")) {
      vars.put(line.variable(), new MsnStream<Object>());
    } else if (line.command().equals("move")) {
      String[] split = cut;
      if (split[2].equals("to")) {
        vars.put(split[3], vars.get(split[1]));
      } else if (split[2].equals("out")) {
        vars.put(split[3], new MsnStream<Object>());
      }
    } else if (line.command().equals("end")) {
      String[] split = cut;
      try {
        getFunctionByName(split[1]).setDefined();
        vars.put(":" + split[1] + ":", split[1]);
      } catch (NullPointerException e) {
        error("unknown function", line);
      }
    } else if (line.command().equals("run")) {
      String function = String.valueOf(vars.get(cut[1])) + ";";
      interpret(toCodeLines(function), true);
    } else if (line.command().equals("object")) {
      objects.add(new Obj(cut[1]));
    } else if (isStruct(line.command())) {
      Obj o = getObjByName(line.command());
      String[] split = cut;
      ArrayList<String> dropping = new ArrayList<>(List.of(split));
      dropping.remove(0);
      dropping.remove(0);
      if (cut[1].equals("has")) {
        for (String s : dropping) {
          if (!isFunction(s)) {
            o.addVariable(s, vars.get(s));
          } else {
            o.addFunction(getFunctionByName(s));
          }
        }
      }
    } else if (line.command().equals("create")) {
      String structname = cut[1];
      String varname = cut[2];
      objects.add(getObjByName(structname).instance(varname));
    } else if (line.command().equals("sleep")) {
      Thread.sleep(Long.parseLong(String.valueOf(vars.get(cut[1]))));
    } else if (line.command().equals("destroy")) {
      if (isFunction(cut[1])) {
        destroyFunctionVariables(cut[1]);
      } else {
        vars.remove(cut[1]);
      }
    }

    else {
      error("unknown command", line);
    }
    linesrun++;
  }

  public void destroyFunctionVariables(String name) {
    Function f = getFunctionByName(name);
    for (CodeLine c : f.inside) {
      String[] split = c.line().split(" ");
      for (String s : split) {
        if (Msn.countChars(s, '_') == 2) {
          try {
            vars.remove(s);
          } catch (Exception e) {

          }
        }
      }
    }
  }

  public Obj getObjByName(String name) {
    for (Obj o : objects) {
      if (o.name.equals(name)) {
        return o;
      }
    }
    return null;
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

  public void checkFunction(String[] split) {

  }

  public void error(String msg, CodeLine line) throws Exception {
    printToConsole("[-] error (" + line.index() + ") : " + msg + " : " + "'" + line.line() + "'",
        true);
    throw new Exception(msg);
  }

  public boolean isStruct(String name) {
    for (Obj o : objects) {
      if (o.name.equals(name)) {
        return true;
      }
    }
    return false;
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
      if (Syntax.isString(divided[i]) && isVariable(divided[i].replace("@", ""))
          || (divided[i].contains("@")) && isFunction(divided[i].replace("@", "")))
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
    MsnStream<String> params;
    MsnStream<String> ret;
    public MsnStream<CodeLine> inside;

    public boolean defined;

    public Function(String name, String comments) {
      this.name = name;
      this.comments = comments;
      inside = new MsnStream<>();
      params = new MsnStream<>();
      ret = new MsnStream<>();
      defined = false;
    }

    public Function(String name, String comments, MsnStream<String> params, MsnStream<String> ret,
        MsnStream<CodeLine> inside) {
      this.name = name;
      this.comments = comments;
      this.params = params;
      this.ret = ret;
      this.inside = inside;
    }

    public void setDefined() {
      defined = true;
    }

    public void addInside(CodeLine c) {
      inside.add(c);
    }

    public String name() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
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

    public MsnStream<String> params() {
      return params;
    }

    public MsnStream<String> returns() {
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

    public Function copyOf() {
      Function f =
          new Function(name, comments, params.copyOf(), ret.copyOf(), new MsnStream<CodeLine>());
      for (CodeLine c : inside) {
        f.addInside(c.copyOf());
      }
      return f;
    }

    public String toString() {
      return name;
    }

  }

  /**
   * Acts as an Object in MSNC.
   * 
   * @author Mason Marker
   */
  class Obj {

    String name;

    public Obj(String name) {
      this.name = name;
    }

    public void addVariable(String varname, Object value) {
      // TODO
      vars.put(varname, value);

    }

    public void addFunction(Function f) {
      Function copy = f.copyOf();
      if (copy.name().contains("#")) {
        String toDrop = "";
        for (int i = 0; i < copy.name().length(); i++) {
          if (copy.name().charAt(i) == '#') {
            toDrop += "#";
            break;
          }
          toDrop += copy.name().charAt(i);
        }
        String newname = copy.name().replace(toDrop, name + "#");
        copy.setName(newname);
      } else {
        copy.setName(name + "#" + copy.name());
      }
      functions.add(copy);
    }

    public String[] getVariables() {
      ArrayList<String> v = new ArrayList<>();
      for (Map.Entry<String, Object> en : vars.entrySet()) {
        if (en.getKey().contains(name + "#")) {
          v.add(en.getKey());
        }
      }
      return v.toArray(String[]::new);
    }

    public String[] getFunctions() {
      ArrayList<String> v = new ArrayList<>();
      for (Function f : functions) {
        if (f.name().contains(name + "#")) {
          v.add(f.name());
        }
      }
      return v.toArray(String[]::new);
    }

    @SuppressWarnings("unchecked")
    public Obj instance(String newname) {
      String toDrop = name + "#";
      String toAdd = newname + "#";
      String[] variables = getVariables();
      Obj instance = new Obj(newname);
      for (String var : variables) {
        Object putting = vars.get(var);

        if (Syntax.isInt(putting)) {
          int in = (Integer) putting;
          vars.put(var.replaceAll(toDrop, toAdd), in);
        } else if (Syntax.isDouble(putting)) {
          double d = (Double) putting;
          vars.put(var.replaceAll(toDrop, toAdd), d);
        } else if (Syntax.isString(putting)) {
          String s = "" + putting;
          vars.put(var.replaceAll(toDrop, toAdd), s);
        } else if (Syntax.isList(putting)) {
          MsnStream<Object> stream = (MsnStream<Object>) putting;
          vars.put(var.replaceAll(toDrop, toAdd), stream.copyOf());
        } else if (Syntax.isObject(putting)) {
          vars.put(var.replaceAll(toDrop, toAdd), vars.get(var));
        }
        instance.addVariable(var.replaceAll(toDrop, toAdd), putting);
      }
      String[] funcs = getFunctions();
      for (String func : funcs) {
        Function f = getFunctionByName(func).copyOf();
        for (CodeLine c : f.inside) {
          String newline = c.line().replaceAll(toDrop, toAdd);
          c.setLine(newline);
        }
        instance.addFunction(f);
      }
      return instance;
    }



  }


}
