package org.terasology.codecity.world.structure;

public class NullCodeClass extends CodeClass {
	
	public NullCodeClass(){
		this("",0,0,"","");
	}

	public NullCodeClass(String name, int variables, int length, String path,
			String github) {
		super(name, variables, length, path, github);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean validCode() {
		return false;
	}
	
	@Override
	public int size() {
		return 0;
	}

}
