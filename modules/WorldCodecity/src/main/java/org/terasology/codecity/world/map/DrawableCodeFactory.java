package org.terasology.codecity.world.map;

import org.terasology.codecity.world.structure.CodeClass;
import org.terasology.codecity.world.structure.CodePackage;
import org.terasology.codecity.world.structure.CodeRepresentation;

public class DrawableCodeFactory {
    public static DrawableCode generateDrawableCode(CodeRepresentation code) {
        if (code instanceof CodeClass)
            return generateDrawableCode((CodeClass)code);
        if (code instanceof CodePackage)
            return generateDrawableCode((CodePackage)code);
        return null;
    }
    
    public static DrawableCode generateDrawableCode(CodeClass code) {
        return new DrawableCodeClass(code);
    }
    
    public static DrawableCode generateDrawableCode(CodePackage code) {
        return new DrawableCodePackage(code);
    }
}
