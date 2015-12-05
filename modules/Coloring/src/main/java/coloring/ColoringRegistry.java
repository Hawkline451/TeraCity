package coloring;

import java.util.HashMap;
import java.util.Map;

import org.terasology.asset.AssetUri;
import org.terasology.world.block.BlockPart;

public class ColoringRegistry {

    public Map<String, Map<BlockPart, AssetUri> > coloringMap;

    public ColoringRegistry() {
    	coloringMap = new HashMap<>();
    }
    
}