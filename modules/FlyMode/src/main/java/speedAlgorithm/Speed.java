package speedAlgorithm;

/**
 * @author cristian
 * Abstract class that represents an Speed object according to the map we are interested in.
 */
public abstract class Speed {
	
	/**
	 * @return A float value representing the velocity calculated.
	 * Method that calculates the speed according to the algorithm of the map we are in. 
	 */
	abstract float calculateSpeed();
	
	/**
	 * @return A float value representing the velocity calculated.
	 * Public method for getting the calculated speed given by calculateSpeed method.
	 */
	public abstract float getCalculatedSpeed();
	
	/**
	 * @return A int representing the height of the map. (Z axis)
	 * Getter for the height of the map, calculated according to the world we are in.
	 */
	public abstract int getMaxHeight();
	
}
