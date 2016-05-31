package highlight;

import org.terasology.math.geom.Vector3i;

/**
 * Interface for every type of highlight for buildings.
 *
 */
public interface BuildingHighlight {
	
	/**
	 * Apply the highlight.
	 */
	public void putHighlight(Vector3i pos);
}
