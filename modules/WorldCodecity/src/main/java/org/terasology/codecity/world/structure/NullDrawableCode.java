package org.terasology.codecity.world.structure;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.CodeMapNull;
import org.terasology.codecity.world.map.DrawableCode;
import org.terasology.codecity.world.map.DrawableCodeVisitor;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.world.block.Block;

public class NullDrawableCode implements DrawableCode {

	@Override
	public CodeRepresentation getBase() {
		return new NullCodeRepresentation();
	}

	@Override
	public int getSize(CodeMapFactory factory) {
		return 0;
	}

	@Override
	public int getHeight(CodeMapFactory factory) {
		return 0;
	}

	@Override
	public CodeMap getSubmap(CodeMapFactory factory) {
		return new CodeMapNull();
	}

	@Override
	public int getWidth(CodeMapFactory factory) {
		return 0;
	}

	@Override
	public boolean containsClass(String className) {
		return false;
	}

	@Override
	public void accept(DrawableCodeVisitor visitor) {
		//intentionally blank.
	}
	public int[] getLineLength() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[][] getLowResFromLine(int row,int col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[][] getFullRep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean useTexture() {
		// TODO Auto-generated method stub
		return false;
	}

}
