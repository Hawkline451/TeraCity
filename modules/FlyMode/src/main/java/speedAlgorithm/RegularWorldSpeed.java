package speedAlgorithm;

import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.Region3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.In;
import org.terasology.world.chunks.ChunkConstants;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.World;
import org.terasology.world.generator.WorldGenerator;

@RegisterSystem
public class RegularWorldSpeed extends Speed {
    @In
    private WorldGenerator worldGenerator;
    
    private float calculatedSpeed;

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
			// We take the log2 of the module of the resultant vector, for a better scaling
			float calculatedVelocity = (float) ((float) Math.log(Math.sqrt(meanX*meanX + meanY*meanY + meanZ*meanZ)) / Math.log(2));
			console.addMessage("The calculated Velocity according to the map is :" + calculatedVelocity);
			return calculatedVelocity;
		}
		return 0;
	}

	public float getCalculatedSpeed() {
		return calculatedSpeed;
	}

}
