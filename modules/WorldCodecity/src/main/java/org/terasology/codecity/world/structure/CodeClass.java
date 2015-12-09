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

    /**
     * Create a new CodeClass Object.
     * @param name Name of the class.
     * @param variables Number of variables in the class.
     * @param length Number of lines in the class.
     */
    public CodeClass(String name, int variables, int length, String path, String github) {
    	this(name, variables, length, path, github, DummyArray.getArray(length));
    }
    public CodeClass(String name, int variables, int length, String path, String github, int[] lineLength) {
        super(name, path, github);
        this.variables = variables;
        this.length = length;
        this.lineLength = lineLength;
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

}
