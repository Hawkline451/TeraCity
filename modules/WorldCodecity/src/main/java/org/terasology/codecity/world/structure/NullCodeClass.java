package org.terasology.codecity.world.structure;

public class NullCodeClass extends CodeClass {
	
	private static final long serialVersionUID = 1L;

	public NullCodeClass(){
		this("","","");
	}

	public NullCodeClass(String name, String path, String github) {
		super(name, path, github);
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
