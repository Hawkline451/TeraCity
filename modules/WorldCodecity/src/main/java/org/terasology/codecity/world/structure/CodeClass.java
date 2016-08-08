package org.terasology.codecity.world.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import metrics.AST;

/**
 * This class represent a Class of a project, saving the variables and length
 */
public class CodeClass extends CodeRepresentation implements Serializable {

  private static final long serialVersionUID = -5550203407291855976L;
  private int variables;
  private int length;
  private int[] lineLength;
  private AST ast;
  private Map<Integer, BlameInfo> blames = new HashMap<Integer, BlameInfo>();

  private int[][] binaryRepr;

  /**
   * Create a new CodeClass Object.
   * 
   * @param name
   *          Name of the class.
   * @param variables
   *          Number of variables in the class.
   * @param length
   *          Number of lines in the class.
   */
  public CodeClass(String name, int variables, int length, String path,
      String github) {
    this(name, variables, path, github, DummyArray.getArray(length), null);
  }

  public CodeClass(String name, int variables, String path, String github,
      Integer[] lineLength, int[][] binaryRepr) {
    super(name, path, github);
    this.variables = variables;
    this.length = fixLength(lineLength.length);
    this.lineLength = fixLineLength(lineLength);
    this.binaryRepr = fixBinary(binaryRepr);
    ast = new AST(path);
    try {
      createBlame(path);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Adds the information of author and last modification to each line of code.
   * 
   * @param path
   *          Path to the file that's being analysed.
   * @throws IOException
   */
  private void createBlame(String path) throws IOException {
    if (path != null && (new File(path).isFile())) {
      String os = System.getProperty("os.name");
      ProcessBuilder pb = null;
      if (os.startsWith("Windows")) {
        pb = new ProcessBuilder("git.exe", "blame", "-p", path);
      } else {
        // :DDDDDDDD
      }

      if (pb != null) {
        pb.redirectErrorStream(true);
        Process pr = pb.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(
            pr.getInputStream()));
        String line;

        Integer number = null;
        String author = "No information";
        String time = "0";
        String tz = "-0000";
        boolean first = true;
        Pattern p = Pattern.compile("^[a-z0-9]*$");
        while ((line = in.readLine()) != null) {
          String[] ln = parseBlameLine(line, p);
          if (ln[0].equals("line_number")) {
            if (!first) {
              blames.put(number, new BlameInfo(author, time, tz));
            }
            first = false;
            number = Integer.parseInt(ln[1]);
          } else if (ln[0].equals("author")) {
            author = ln[1];
          } else if (ln[0].equals("author-time")) {
            time = ln[1];
          } else if (ln[0].equals("author-tz")) {
            tz = ln[1];
          }

        }
        if (!first) {
          blames.put(number, new BlameInfo(author, time, tz));

        }
      }

    }

  }

  /**
   * Takes a read line and parses into a format of [parameter, information] if
   * the information is useful.
   * 
   * @param line
   *          line to parse
   * @param p
   *          Pattern matcher for the ID line
   * @return
   */
  private String[] parseBlameLine(String line, Pattern p) {
    line = line.trim();
    if (line != null) {
      String[] seps = line.split(" ");
      if (seps[0].length() == 40 && p.matcher(seps[0]).matches()) {
        return new String[] { "line_number", seps[2] };
      } else if (seps[0].equals("author")) {
        seps[0] = "";
        StringBuilder builder = new StringBuilder();
        for (String s : seps) {
          builder.append(s);
          builder.append(" ");
        }
        return new String[] { "author", builder.toString().trim() };
      } else if (seps[0].equals("author-time")) {
        return new String[] { "author-time", seps[1] };
      } else if (seps[0].equals("author-tz")) {
        return new String[] { "author-tz", seps[1] };
      }
    }
    return new String[] { "no match", "no info" };
  }

  /**
   * @return Number of variables in the code
   */
  public int getVariableNumber() {
    return variables;
  }

  /**
   * 
   * @return Array of line lengths
   */
  public int[] getLineLengths() {
    return lineLength;
  }

  /**
   * 
   * @return The AST of the file that represents
   */
  public AST getAst() {
    return ast;
  }

  /**
   * @return Number of lines in the code
   */
  public int getClassLength() {
    return length;
  }

  @Override
  public void accept(CodeVisitor visitor) {
    visitor.visitCodeClass(this);
  }

  @Override
  public int size() {
    return 1;
  }

  public BlameInfo getLineInfo(int i) {
    if (blames.containsKey(i)) {
      return blames.get(i);
    }
    return new BlameInfo("No information", "0", "-0000");
  }

  public int getLongestLineLength() {
    int max = 0;
    for (Integer i : lineLength) {
      max = Math.max(max, i);
    }
    return max;
  }

  public int[][] getBinaryRow(int row, int column) {
    int[][] temp = new int[2][2];
    try {
      temp[0][0] = this.binaryRepr[row * 2][column * 2];
      temp[0][1] = this.binaryRepr[row * 2][column * 2 + 1];
      temp[1][0] = this.binaryRepr[row * 2 + 1][column * 2];
      temp[1][1] = this.binaryRepr[row * 2 + 1][column * 2 + 1];
    } catch (IndexOutOfBoundsException e) {
      System.err.println("Row col: " + row + " " + column + " "
          + e.getMessage());
      return null;
    }

    return temp;
  }

  // Section that adds two empty lines at the beginning of the class
  // so the roof of the building is empty

  private static int fixLength(int length) {
    return length + 2;
  }

  private static int[] fixLineLength(Integer[] lineLength) {
    int[] fixed = new int[lineLength.length + 2];
    for (int i = 0; i < fixed.length; i++) {
      if (i == 0 || i == 1) {
        fixed[i] = 0;
      } else {
        fixed[i] = lineLength[i - 2];
      }
    }
    return fixed;
  }

  private static int[][] fixBinary(int[][] binary) {
    if (binary != null) {
      int[][] fixed = new int[binary.length + 2][binary[0].length];
      for (int i = 0; i < fixed.length; i++) {
        if (i == 0 || i == 1) {
          for (int j = 0; j < fixed[0].length; j++) {
            fixed[i][j] = 0;
          }
        } else {
          for (int j = 0; j < fixed[0].length; j++) {
            fixed[i] = binary[i - 2];
          }
        }
      }
      return fixed;
    } else {
      return binary;
    }
  }

  public int[][] getFullBinary() {
    return this.binaryRepr;
  }
}
