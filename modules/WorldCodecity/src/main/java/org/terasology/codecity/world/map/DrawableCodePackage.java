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
    private int sizeCache = -1;
    private CodeMap submapCache;

    /**
     * Create a new DrawableCodePackage
     * 
     * @param baseContent
     *            Content of the package
     */
    public DrawableCodePackage(CodePackage base) {
        this.base = base;
        submapCache = null;
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
        if (sizeCache == -1) {
        	CodeMap map = factory.generateMap(contentList);
        	sizeCache = 2 + map.getSize();
        }
        return sizeCache;
    }

    @Override
    public int getHeight(CodeScale scale, CodeMapFactory factory) {
        return 1;
    }

    @Override
    public CodeMap getSubmap(CodeScale scale, CodeMapFactory factory) {
    	if (submapCache == null)
    		submapCache = factory.generateMap(contentList);
        return submapCache;
    }

	@Override
	public int getWidth(CodeScale scale, CodeMapFactory factory) {
		return 1;
	}
}
