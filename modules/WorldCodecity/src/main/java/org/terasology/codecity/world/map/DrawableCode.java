package org.terasology.codecity.world.map;

import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.world.block.Block;

/**
 * This class represent a part of the code that can be drawed in the map
 */
public interface DrawableCode {
	public CodeRepresentation getBase();
	/**
     * @param factory
     *            The builder of the map
     * @return The size of the base of the building.
     */
    public int getSize(CodeMapFactory factory);

    /**
     * @param factory
     *            The builder of the map
     * @return The height of the base of the building.
     */
    public int getHeight(CodeMapFactory factory);
    
    /**
     * @param factory
     *            The builder of the map
     * @return The width of the base of the building.
     */
    
    public int getWidth(CodeMapFactory factory);

    /**
     * @param scale
     *            The scale used in the code
     * @param factory
     *            The builder of the map
     * @return The submap inside the code
     */
    public CodeMap getSubmap(CodeMapFactory factory);
    
    /**
     * @param className the class name
     * @return whether the class is contained 
     */
    public boolean containsClass(String className);
    
    public void accept(DrawableCodeVisitor visitor);
	public int[] getLineLength();
}
