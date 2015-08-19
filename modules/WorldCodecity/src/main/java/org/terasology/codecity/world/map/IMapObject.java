package org.terasology.codecity.world.map;

import org.terasology.codecity.world.structure.scale.CodeScale;

public interface IMapObject {
	
	public int getPositionX();
	
	public int getPositionZ();
	
	public int getHeight(CodeScale scale, CodeMapFactory factory);
	
	public boolean isOrigin();
	
	public DrawableCode getObject();
		
}
