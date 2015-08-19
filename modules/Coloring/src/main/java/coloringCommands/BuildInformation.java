package coloringCommands;

import org.terasology.codecity.world.map.MapObject;

public class BuildInformation {
	private int x;
	private int y;
	private int z;
	private int height;
	private int width;
	private MapObject info;
	
	public BuildInformation(int x,int y, int z, int height, int width, MapObject object){
		this.x = x;
		this.y = y;
		this.z = z;
		this.height = height;
		this.info = object;
		this.width = width;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getZ(){
		return z;
	}
	public String getName(){
		return info.getObject().getBase().getName();
	}
	
	public String getPath(){
		return info.getObject().getBase().getPath();
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}

}