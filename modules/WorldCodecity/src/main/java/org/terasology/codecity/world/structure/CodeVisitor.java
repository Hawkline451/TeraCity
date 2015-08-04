package org.terasology.codecity.world.structure;

/**
 * This interface is used to visit the current CodeRepresentation classes
 * @author neecro
 *
 */
public interface CodeVisitor {
    /**
     * Visit a CodePackage class
     * @param code 
     */
    public void visitCodePackage(CodePackage code);
    
    /**
     * Visit a CodeClass class
     * @param code
     */
    public void visitCodeClass(CodeClass code);
}
