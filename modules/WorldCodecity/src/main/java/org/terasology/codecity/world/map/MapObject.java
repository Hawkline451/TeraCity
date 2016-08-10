package org.terasology.codecity.world.map;

import java.util.List;

import org.terasology.codecity.world.metrics.AST;
import org.terasology.codecity.world.structure.scale.CodeScale;

/**
 * This class represent a object in the map
 */
public class MapObject implements IMapObject {
    private DrawableCode object;
    private int x;
    private int z;
    private boolean isOrigin;
    boolean isInner;
    
    private int codeColumn; //What part of the code texture row is ? -1 means border
    private int maxYCord; //This variable make possible to calculate the row given a Z coordinate


    /**
     * Create a new Object in map
     * 
     * @param object
     *            Object to be represented
     * @param x
     *            The position of the object in the x coordinate
     * @param z
     *            The position of the object in the z coordinate
     */
    public MapObject(DrawableCode object, int x, int z, boolean isOrigin) {
        this.object = object;
        this.x = x;
        this.z = z;
        this.isOrigin = isOrigin;
        this.isInner = false;
    }
    
    /**
     * Creates a new Object in map, defining if the object represents inner section of a class.
     * @param object
     * @param x
     * @param z
     * @param isOrigin
     * @param isInner
     */
    public MapObject(DrawableCode object, int x, int z, boolean isOrigin, boolean isInner) {
        this.object = object;
        this.x = x;
        this.z = z;
        this.isOrigin = isOrigin;
        this.isInner = isInner;
    }
    
    @Override
    public String toString(){
    	return this.object.getBase().getName();
    }

	/**
     * @return The position of the object in the x coordinate
     */
    @Override
    public int getPositionX() {
        return x;
    }

    /**
     * @return The position of the object in the z coordinate
     */
    @Override
    public int getPositionZ() {
        return z;
    }

    /**
     * Get the height of the object
     * 
     * @param scale
     * @param factory
     * @return
     */
    @Override
    public int getHeight(CodeMapFactory factory) {
        return object.getHeight(factory);
    }
    
    /**
     * Get the width of the object
     * 
     * @param scale
     * @param factory
     * @return
     */
    public int getWidth(CodeMapFactory factory){
    	return object.getWidth(factory);
    }

    /**
     * @return The object that is represented
     */
    @Override
    public DrawableCode getObject() {
        return object;
    }
    @Override
    public boolean isOrigin() {
        return isOrigin;
    }
    
    @Override
    public boolean isInner() {
        return isInner;
    }
  //ww
    /**
     * Returns true if this map object contains the class searched for.
     * @param className the name of the class.
     * @return whether the class is contained in this object.
     */
    public boolean containsClass(String className){
    	return object.containsClass(className);
    }
    
    
    
	public int getColumn(){
		return this.codeColumn;
	}
	public void setCodeColumn(int column){
		this.codeColumn = column;
	}
	public int getMaxY(){
		return this.maxYCord;
	}
	public void setMaxY(int maxY){
		this.maxYCord = maxY;
	}
	
	/**
	 * Search text <code>query</code> inside the AST of object's base.
	 * @param query
	 * @return
	 */
	public boolean containsText(String query) {
		return object.containsText(query);
	}

	public boolean hasText(String query) {
		AST ast = object.getBase().getAst();
		if (ast == null)
			return false;
		return ast.contains(query);
	}

	public boolean isInPackage(String packageName) {
		String pack = object.getBase().getPackage();
		if (pack == null)
			return false;
		return pack.equals(packageName);
	}

	public boolean containsPackage(String asterix) {
		return object.containsPackage(asterix);
	}

	
	public boolean isDirectedImported(List<String> directImports){
		String name = object.getBase().getName().trim(); 
		for (String imp: directImports)
			if (name.equals(imp)) return true;
		return false;
	}
	
	public boolean containsImport(List<String> directImports) {
		for (String imp : directImports){
			if (object.containsClass(imp)) return true;
		}
		return false;
	}


}
