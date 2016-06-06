package org.terasology.codecity.world.map;

import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.structure.CodePackage;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.DummyArray;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.world.block.Block;

/**
 * This class represent a Package that can be drawed in the map
 */
public class DrawableCodePackage implements DrawableCode {
    private CodePackage base;
    
    // Almacena las clases/paquetes que contiene el package
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
    public int getSize(CodeMapFactory factory) {
        if (sizeCache == -1) {
        	CodeMap map = factory.generateMap(contentList);
        	sizeCache = 2 + map.getSize();
        }
        return sizeCache;
    }

    @Override
    public int getHeight(CodeMapFactory factory) {
        return 1;
    }

    @Override
    public CodeMap getSubmap(CodeMapFactory factory) {
    	if (submapCache == null)
    		submapCache = factory.generateMap(contentList);
        return submapCache;
    }

	@Override
	public int getWidth(CodeMapFactory factory) {
		return 1;
	}

	@Override
	public boolean containsClass(String className) {		
		for(DrawableCode code : contentList){
			if(code.containsClass(className)) return true;
		}
		return false;
	}
	
	@Override
	public void accept(DrawableCodeVisitor visitor) {
		visitor.visit(this);
	}
	public int[] getLineLength() {
		return null;
	}
	
}
