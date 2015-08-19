package speedMath;

/**
 * @author cristian
 * Object that represents the math calculations for the CodeMap world.
 */
public class CodeMapSpeedMath implements SpeedMath{

	private int xDimension;
	private int yDimension;
	private int zDimension;
	
	public CodeMapSpeedMath(int x, int y, int z){
		xDimension = x;
		yDimension = y;
		zDimension = z;
	}
	
	public float getResult() {
		return (float) ((float) Math.log(Math.sqrt( (float)(xDimension*xDimension + yDimension*yDimension + zDimension*zDimension) / Math.log(2) )));
	}

}
