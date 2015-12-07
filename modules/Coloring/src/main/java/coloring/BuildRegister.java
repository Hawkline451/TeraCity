
package coloring;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.world.block.BlockPart;

/**
 * Almacena información sobre el estado actual del coloreo de un edificio 
 */
public class BuildRegister {

	public String name;
	public Map<BlockPart, AssetUri> tiles;
	public Map<BlockPart, Integer> damage;
	
	public BuildRegister(String name) {
		this.name = name;
		
		tiles  = new HashMap<>();
		damage = new HashMap<>();
		for (BlockPart part : BlockPart.values()) {
    		tiles.put(part , new AssetUri(AssetType.BLOCK_TILE, "Core", "stone"));
    		damage.put(part , new Integer(0));
		}
	}
	
	
	public void updateRegister(List<BlockPart> sides, AssetUri uri, int damage) {
		
		for (BlockPart part : sides) {
            this.damage.put(part, new Integer(damage));
    		this.tiles.put(part , uri);
    	}
	}
	
	public static List<BlockPart> getValidSides() {
		return Arrays.asList(BlockPart.FRONT, BlockPart.RIGHT, BlockPart.LEFT, BlockPart.BACK);
	}
	
}