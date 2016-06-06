package org.terasology.codecity.world.structure.scale;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Manages the horizontal and vertical scales.
 * @author adderou
 *
 */
public class CodeScaleManager {

	private CodeScale horizontal, vertical;
	private HashMap<String,CodeScale> scaleMap = new HashMap<>();
	private ArrayList<String> scaleTypes = new ArrayList<>();

	
	/**
	 * Default constructor.
	 */
	public CodeScaleManager() {
		
		registerCodeScale("linear",new LinearCodeScale());
		registerCodeScale("square-root",new SquareRootCodeScale());
		registerCodeScale("half-linear",new HalfLinearCodeScale());
		this.horizontal = scaleMap.get("square-root");
		this.vertical = scaleMap.get("linear");
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

	public CodeScale getScaleFromString(String scale) {
		return scaleMap.get(scale);
	}
	
	public ArrayList<String> getAvailableScaleNames() {
	    return scaleTypes;
	}
	
	private void registerCodeScale(String scaleName, CodeScale scale) {
		scaleTypes.add(scaleName);
		scaleMap.put(scaleName, scale);
	}
}
