package org.terasology.codecity.world.structure.scale;
/**
 * Manages the horizontal and vertical scales.
 * @author adderou
 *
 */
public class CodeScaleManager {

	private CodeScale horizontal, vertical;
	
	/**
	 * Default constructor.
	 */
	public CodeScaleManager() {
		this.horizontal = new SquareRootCodeScale();
		this.vertical = new HalfLinearCodeScale();
	}
	
	public CodeScaleManager(CodeScale horizontal, CodeScale vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
	}
	
	public void setVerticalScale(CodeScale vertical) {
		this.vertical = vertical;
	}
	
	public void setHorizontalScale(CodeScale horizontal) {
		this.horizontal = horizontal;
	}
	
	public CodeScale getVerticalScale() {
		return vertical;
	}
	
	public CodeScale getHorizontalScale() {
		return horizontal;
	}
}
