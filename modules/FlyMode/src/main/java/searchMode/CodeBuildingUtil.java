package searchMode;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;

public class CodeBuildingUtil {
	
	private static HashMap<Vector3i, Block> modifiedBlocks = new HashMap<Vector3i, Block>();
	
	public static void modifyBlock(Vector3i pos, Block block){
		WorldProvider world = CoreRegistry.get(WorldProvider.class);
		//We only store the orginal block
		if(!modifiedBlocks.containsKey(pos)){
			//Find the current block in the world at the position
			Block current_block = world.getBlock(pos);
			//We save the current block
			modifiedBlocks.put(pos, current_block);
		}
		world.setBlock(pos, block);
	}
	
	public static void replace2DArray(Vector3i[][] positions, Block block){
		for (Vector3i[] vv : positions)
			for(Vector3i v : vv){
				modifyBlock(v, block);
			}
	}
	
	public static void replaceLine(int row, Vector3i[][] positions, Block block){
		for(Vector3i v : positions[row]){
			modifyBlock(v, block);
		}
	}

	public static void color2DArray(Vector3i[][] positions, String color){
			BlockManager blockManager = CoreRegistry.get(BlockManager.class);
			BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", color));
			Block block = blockFamily.getArchetypeBlock();
			replace2DArray(positions, block);
	}
	
	/**
	 * Color line at position
	 * 
	 * @param row
	 * @param positions
	 * @param color
	 */
	public static void colorLine(int row, Vector3i[][] positions,  String color){
		BlockManager blockManager = CoreRegistry.get(BlockManager.class);
		BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", color));
		Block block = blockFamily.getArchetypeBlock();
		replaceLine(row, positions, block);
	}
	
	
	/**
	 * Color all lines between i and j [i, j]
	 * @param i
	 * @param j
	 * @param positions
	 * @param color
	 */
	public static void colorLines(int i, int j, Vector3i[][] positions,  String color){
		for (int x = i; x < j; x++)
			colorLine(x, positions, color);
	}
	
	public static void replaceLines(int i, int j, Vector3i[][] positions, Block block){
		for (int x = i; x < j; x++)
			replaceLine(x, positions, block);
	}
	
	/**
	 * 
	 * Color block at position [row][col]
	 * @param row
	 * @param col
	 * @param positions
	 * @param color
	 */
	public static void colorPosXY(int row, int col, Vector3i[][] positions,  String color){
		BlockManager blockManager = CoreRegistry.get(BlockManager.class);
		BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", color));
		Block block = blockFamily.getArchetypeBlock();
		replacePosXY(row, col, positions, block);
	}
	
	public static void replacePosXY(int row, int col, Vector3i[][] positions, Block block){
		modifyBlock(positions[row][col], block);
	}
	
	public static void restoreModifiedBlocks(){
		WorldProvider world = CoreRegistry.get(WorldProvider.class);
		for(Entry<Vector3i,Block> entry : modifiedBlocks.entrySet()){
			Vector3i pos = entry.getKey();
			Block block = entry.getValue();
			if(block != null){
				world.setBlock(pos, block);
			}
		}
		modifiedBlocks = new HashMap<Vector3i, Block>();
	}
}
