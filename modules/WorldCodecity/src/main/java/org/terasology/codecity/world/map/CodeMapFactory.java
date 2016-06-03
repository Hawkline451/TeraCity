package org.terasology.codecity.world.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.CodeScaleManager;
import org.terasology.codecity.world.structure.scale.HalfLinearCodeScale;
import org.terasology.codecity.world.structure.scale.LinearCodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.registry.CoreRegistry;

/**
 * This class is in charge of creating maps in base of a list of code.
 */
public class CodeMapFactory {

	
	public CodeMapFactory() {
		this(new SquareRootCodeScale());
	}
	
    /**
     * Create a new CodeMapFactory using a default scale
     */
    public CodeMapFactory(CodeScale scale) {
    	// Wat
    }

    /**
     * Generate a new map with the given list of content
     * 
     * @param contentList
     *            Content of the map
     * @return New map in base of the given content
     */
    public CodeMap generateMap(List<DrawableCode> contentList) {
        // Sort the content by scale
        List<DrawableCode> sortedContent = new ArrayList<DrawableCode>(contentList); 
        Collections.sort(sortedContent, new DrawableCodeSizeComparator(this));

        // Start drawing in the map
        CodeMap map = new CodeMapHash();

        for (DrawableCode content : sortedContent)
            insertInMap(map, content);
            
        return map;
    }

    /**
     * Insert an object in the map
     * 
     * @param map
     *            Map where the object will be inserted
     * @param content
     *            Object to be added
     */
    private void insertInMap(CodeMap map, DrawableCode content) {
        CodeScaleManager man = CoreRegistry.get(CodeScaleManager.class);
        if (map.isEmpty()) {
            map.insertContent(content, man.getHorizontalScale(), this, 0, 0);
            return;
        }

        int z = 0;
        while (true) {
            for (int x = 0; x < map.getSize(); x++) {
                if (map.canPlaceContent(content, man.getHorizontalScale(), this, x, z)) {
                    map.insertContent(content, man.getHorizontalScale(), this, x, z);
                    return;
                }
            }
            z += 1;
        }

    }
    
    public CodeScale getScale() {
        CodeScaleManager man = CoreRegistry.get(CodeScaleManager.class);
    	return man.getHorizontalScale();
    }
}

/**
 * This class is used to compare two DrawableCode objects
 */
class DrawableCodeSizeComparator implements Comparator<DrawableCode> {
    private CodeMapFactory factory;

    public DrawableCodeSizeComparator(CodeMapFactory factory) {
        this.factory = factory;
    }

    @Override
    public int compare(DrawableCode c1, DrawableCode c2) {
        return c1.getSize(factory) - c2.getSize(factory);
    }
}
