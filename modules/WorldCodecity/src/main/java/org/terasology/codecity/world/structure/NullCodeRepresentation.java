package org.terasology.codecity.world.structure;

import org.terasology.codecity.world.metrics.AST;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.CodeVisitor;

public class NullCodeRepresentation extends CodeRepresentation {

	private static final long serialVersionUID = 1L;

	@Override
	public String getPath(){
		return "";
	}

	@Override
	public void accept(CodeVisitor visitor) {		
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public AST getAst() {
		return null;
	}
}
