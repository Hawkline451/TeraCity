package org.terasology.codecity.world.map;

import org.terasology.codecity.world.structure.scale.CodeScale;

public interface IMapObject {
	
	public int getPositionX();
	
	public int getPositionZ();
	
	public int getHeight(CodeScale scale, CodeMapFactory factory);
	
	
	/**
	 * Determines if mapObject is the origin of class.
	 * @return true if is the origin of class
	 */
	public boolean isOrigin();
	
	
	/**
	 * Determines if mapObject represents inner section of a class/package
	 * @return
	 */
	public boolean isInner();
	
	public DrawableCode getObject();
		
}
