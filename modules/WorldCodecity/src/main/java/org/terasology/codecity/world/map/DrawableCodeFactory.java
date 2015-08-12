package org.terasology.codecity.world.map;

import org.terasology.codecity.world.structure.CodeClass;
import org.terasology.codecity.world.structure.CodePackage;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.CodeVisitor;

/**
 * This class is used to generate DrawableCode class using a CodeRepresentation
 */
public class DrawableCodeFactory implements CodeVisitor {
    private DrawableCode value;
    
    public DrawableCodeFactory() {
        value = null;
    }
    
    /**
     * Generate a DrawableCode
     * @param code Base of the DrawableCode 
     * @return The DrawableCode that represent the CodeRepresentation object
     */
    public DrawableCode generateDrawableCode(CodeRepresentation code) {
        code.accept(this);
        return value;
    }

    @Override
    public void visitCodePackage(CodePackage code) {
        value = new DrawableCodePackage(code);
        
    }

    @Override
    public void visitCodeClass(CodeClass code) {
        value = new DrawableCodeClass(code);
        
    }
}
