package org.terasology.codecity.world.map;

import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.structure.CodePackage;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;

/**
 * This class represent a Package that can be drawed in the map
 */
public class DrawableCodePackage implements DrawableCode {
    private CodePackage base;
    private List<DrawableCode> contentList;

    /**
     * Create a new DrawableCodePackage
     * 
     * @param baseContent
     *            Content of the package
     */
    public DrawableCodePackage(CodePackage base) {
        this.base = base;
        contentList = new ArrayList<DrawableCode>();
        DrawableCodeFactory factory = new DrawableCodeFactory();
        for (CodeRepresentation content : base.getContent())
            contentList.add(factory.generateDrawableCode(content));
    }
    
    /**
     * Get the CodePackage which is the base of the drawable representation.
     * @return base of the DrawableCodePackage class.
     */
    public CodePackage getBase() {
        return base;
    }

    @Override
    public int getSize(CodeScale scale, CodeMapFactory factory) {
        CodeMap map = factory.generateMap(contentList);
        return 2 + map.getSize();
    }

    @Override
    public int getHeight(CodeScale scale, CodeMapFactory factory) {
        return 1;
    }

    @Override
    public CodeMap getSubmap(CodeScale scale, CodeMapFactory factory) {
        return factory.generateMap(contentList);
    }
}
