package org.terasology.utilities.jedit;
import java.util.ArrayList;

import org.terasology.codecity.world.structure.CodeClass;
import org.terasology.codecity.world.structure.CodePackage;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.CodeVisitor;

public class ClassPathVisitor implements CodeVisitor{
	private ArrayList<String> claseNames = new ArrayList<String>();

	@Override
	public void visitCodePackage(CodePackage code) {
		for(CodeRepresentation cr: code.getContent() ){
			cr.accept(this);			
		}
		
	}

	@Override
	public void visitCodeClass(CodeClass code) {
		claseNames.add(code.getPath());
		
	}
	public ArrayList<String> getPaths(){
		return claseNames;
	}

}
