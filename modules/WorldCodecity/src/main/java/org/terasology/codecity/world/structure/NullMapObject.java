package org.terasology.codecity.world.structure;

import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.DrawableCode;
import org.terasology.codecity.world.map.IMapObject;
import org.terasology.codecity.world.structure.scale.CodeScale;

public class NullMapObject implements IMapObject {

	@Override
	public int getPositionX() {
        return 0;
    }
	
	@Override
	public int getPositionZ() {
        return 0;
    }
	
	@Override
	 public int getHeight(CodeScale scale, CodeMapFactory factory) {
        return 0;
    }
	
	@Override
	public boolean isOrigin() {
        return false;
	}
	
	@Override
	public DrawableCode getObject(){
		return new NullDrawableCode();
	}
}
