package highlight;

import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;

/**
 * Highlight for a single cube.
 *
 */
public class SingleCubeHighlight extends AbstractBuildingHighlight {

	/*
	 * Position of last highlighted block.
	 */
	private Vector3i lastHighlightPos;

	/*
	 * Last highlighted block.
	 */
	private Block lastHighlightBlock;

	public SingleCubeHighlight(String color) {
		super(color);

	}

	@Override
	public void putHighlight(Vector3i pos) {
		world = CoreRegistry.get(WorldProvider.class);
		blockManager = CoreRegistry.get(BlockManager.class);
		BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", color));
		Block block = blockFamily.getArchetypeBlock();
		if (lastHighlightBlock != null) {
			world.setBlock(lastHighlightPos, BlockManager.getAir());
		}
		lastHighlightBlock = world.getBlock(pos);
		world.setBlock(pos, block);
		lastHighlightPos = pos;

	}

	public boolean removeHighlight() {
		if (lastHighlightBlock != null) {
			world.setBlock(lastHighlightPos, BlockManager.getAir());
			lastHighlightBlock = null;
			lastHighlightPos = null;
			return true;
		}
		return false;
	}

}
