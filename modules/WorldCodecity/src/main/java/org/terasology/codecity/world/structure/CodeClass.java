package org.terasology.codecity.world.structure;

import java.io.Serializable;

/**
 * This class represent a Class of a project, saving the variables and length
 */
public class CodeClass extends CodeRepresentation implements Serializable {
    private static final long serialVersionUID = -5550203407291855976L;
    private int variables;
    private int length;

    /**
     * Create a new CodeClass Object.
     * @param name Name of the class.
     * @param variables Number of variables in the class.
     * @param length Number of lines in the class.
     */
    public CodeClass(String name, int variables, int length, String path, String github) {
        super(name, path, github);
        this.variables = variables;
        this.length = length;
    }

    /**
     * @return Number of variables in the code
     */
    public int getVariableNumber() {
        return variables;
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
}
