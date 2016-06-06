package org.terasology.codecity.world.structure;

import java.io.Serializable;

import org.terasology.codecity.world.metrics.AST;

/**
 * This class represent a Class of a project, saving the variables and length
 */
public class CodeClass extends CodeRepresentation implements Serializable {
    private static final long serialVersionUID = -5550203407291855976L;
	private AST ast;

    /**
     * Create a new CodeClass Object.
     * @param name Name of the class.
     * @param path Location of file
     * @param github 
     */
    public CodeClass(String name, String path, String github) {
        super(name, path, github);
        this.ast = new AST(path);
    }

    /**
     * @return Number of variables in the code
     */
    public int getVariableNumber() {
        return ast.getFields().size();
    }
    
    /**
     * @return Array of line lengths
     */
    public int[] getLineLengths() {
    	return ast.getLinesLength();
    }

    /**
     * @return Number of lines in the code
     */
    public int getClassLength() {
        return ast.getLength();
    }

    @Override
    public void accept(CodeVisitor visitor) {
        visitor.visitCodeClass(this);
    }

    /**
     * @return Length of code
     */
	@Override
	public int size() {
		return ast.getLength();
	}

	/**
	 * @return Length of longest line in code
	 */
	public int getLongestLineLength() {
		int max = 0;
		for (Integer i : ast.getLinesLength()) {
			max = Math.max(max, i);
		}
		return max;
	}

}
