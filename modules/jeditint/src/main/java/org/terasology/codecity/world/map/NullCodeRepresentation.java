package org.terasology.codecity.world.map;

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
	
}
