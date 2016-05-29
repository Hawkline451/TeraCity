package org.terasology.codecity.world.structure;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class represent a Class of a project, saving the variables and length
 */
public class CodeClass extends CodeRepresentation implements Serializable {
    private static final long serialVersionUID = -5550203407291855976L;
    private int variables;
    private int length;
    private int[] lineLength;
    
    private int[][] binaryRepr;

    /**
     * Create a new CodeClass Object.
     * @param name Name of the class.
     * @param variables Number of variables in the class.
     * @param length Number of lines in the class.
     */
    public CodeClass(String name, int variables, int length, String path, String github) {
    	this(name, variables, length, path, github, DummyArray.getArray(length),null);
    }
    public CodeClass(String name, int variables, int length, String path, String github, int[] lineLength,
    		int[][] binaryRepr) {
        super(name, path, github);
        this.variables = variables;
        this.length = length;
        this.lineLength = lineLength;
        
        this.binaryRepr = binaryRepr;
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
	public int[][] getBinaryRow(int row,int column) {
		int[][] temp = new int[2][2];
		temp[0][0] = this.binaryRepr[row][column];
		temp[0][1] = this.binaryRepr[row][column+1];
		temp[1][0] = this.binaryRepr[row+1][column];
		temp[1][1] = this.binaryRepr[row+1][column+1];
		
		return temp;
	}

}
