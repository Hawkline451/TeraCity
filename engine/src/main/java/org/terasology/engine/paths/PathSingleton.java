package org.terasology.engine.paths;

public class PathSingleton {
	private static PathSingleton instance = null;
	String path = "";
	
	protected PathSingleton(String _path){
		this.path = _path;
	}
	
	public static PathSingleton getInstance(String _path){
		if (instance == null){
			instance = new PathSingleton(_path);
		}
		return instance;
	}
	
	public String getPath(){
		return this.path;
	}
}
