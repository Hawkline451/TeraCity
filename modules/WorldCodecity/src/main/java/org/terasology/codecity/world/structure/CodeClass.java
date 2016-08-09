package org.terasology.codecity.world.structure;


import java.io.Serializable;

import org.terasology.codecity.world.structure.metric.GitBlameMetric;

import org.terasology.codecity.world.metrics.AST;

/**
 * This class represent a Class of a project, saving the variables and length
 */
public class CodeClass extends CodeRepresentation implements Serializable {

  private static final long serialVersionUID = -5550203407291855976L;
  private int variables;
  private int length;
  private int[] lineLength;
  private AST ast;

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
    //test
    /*GitBlameMetric asdf = new GitBlameMetric(getPath());
    for (int i= 1; asdf.existsLineInfo(i); i++) {
    	System.out.println(asdf.getLineInfo(i));
    }*/
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
