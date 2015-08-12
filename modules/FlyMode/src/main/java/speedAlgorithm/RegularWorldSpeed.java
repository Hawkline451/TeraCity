package speedAlgorithm;

import org.terasology.math.Region3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.In;
import org.terasology.world.chunks.ChunkConstants;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.World;
import org.terasology.world.generator.WorldGenerator;

import speedMath.RegularWorldSpeedMath;

/**
 * @author cristian
 * Object that represents the speed of the RegularWorldSpeed map.
 */
public class RegularWorldSpeed extends Speed {
    @In
    private WorldGenerator worldGenerator;
    
    private float calculatedSpeed;
    private int maxHeight;

    public RegularWorldSpeed(){
    	this.calculatedSpeed = calculateSpeed();
    }
	
	/**
	 * @return A float value of the calculated Velocity.
	 * Method that calculates the velocity of the character according to the size of the original WorldMap.
	 */
	 float calculateSpeed() {
		World world = worldGenerator.getWorld();
		if (world != null) {
			Region worldRegion = world.getWorldData(Region3i.createFromMinAndSize(new Vector3i(0, 0, 0),ChunkConstants.CHUNK_SIZE));
			// We get the mean of the Max and Min values of each coordenate
			float meanX = (float) (( worldRegion.getRegion().maxX() - worldRegion.getRegion().minX() ) / 2.0);
			float meanY = (float) (( worldRegion.getRegion().maxY() - worldRegion.getRegion().minY() ) / 2.0);
			float meanZ = (float) (( worldRegion.getRegion().maxZ() - worldRegion.getRegion().minZ() ) / 2.0);
			this.maxHeight = worldRegion.getRegion().maxZ();
			// We calculate the velocity according to the formula given in the RegularWorldSpeedMath class
			float calculatedVelocity = new RegularWorldSpeedMath(meanX,meanY,meanZ).getResult();
			return calculatedVelocity;
		}
		return 0;
	}

	@Override
	public float getCalculatedSpeed() {
		return calculatedSpeed;
	}

	@Override
	public int getMaxHeight() {
		return this.maxHeight;
	}

}
