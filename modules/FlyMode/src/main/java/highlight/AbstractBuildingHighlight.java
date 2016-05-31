package highlight;

import org.terasology.registry.CoreRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.BlockManager;

/**
 * Abtract class for the interface BuildingHighlight.
 *
 */
public abstract class AbstractBuildingHighlight implements BuildingHighlight {
	
	/*
	 * blockManager and world manage the creation of highlighter cubes.
	 */
	protected BlockManager blockManager;
	protected WorldProvider world;
	
	/*
	 * The color of the highlight.
	 */
	protected String color;
	
	protected AbstractBuildingHighlight(String color) {
		this.color = color;
	}
}
