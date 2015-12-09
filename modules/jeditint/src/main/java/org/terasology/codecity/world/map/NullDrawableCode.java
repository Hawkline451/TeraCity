package org.terasology.codecity.world.map;

import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.world.block.Block;

public class NullDrawableCode implements DrawableCode {

	@Override
	public CodeRepresentation getBase() {
		return new NullCodeRepresentation();
	}

	@Override
	public int getSize(CodeScale scale, CodeMapFactory factory) {
		return 0;
	}

	@Override
	public int getHeight(CodeScale scale, CodeMapFactory factory) {
		return 0;
	}

	@Override
	public CodeMap getSubmap(CodeScale scale, CodeMapFactory factory) {
		return new CodeMapNull();
	}

	@Override
	public int getWidth(CodeScale scale, CodeMapFactory factory) {
		return 0;
	}

	@Override
	public int[] getLineLength() {
		// TODO Auto-generated method stub
		return null;
	}

}
