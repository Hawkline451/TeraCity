package speedMath;

/**
 * @author cristian
 * Object that represents the math calculations for the RegularWorldSpeedMath world.
 */
public class RegularWorldSpeedMath implements SpeedMath {

	private float xDimension;
	private float yDimension;
	private float zDimension;
	
	public RegularWorldSpeedMath(float x, float y, float z){
		xDimension = x;
		yDimension = y;
		zDimension = z;
	}
	
	@Override
	public float getResult() {
		return (float) ((float) Math.log(Math.sqrt(xDimension*xDimension + yDimension*yDimension + zDimension*zDimension)) / Math.log(2));
	}

}
