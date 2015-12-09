package coloring.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.HalfLinearCodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.health.DoDamageEvent;
import org.terasology.logic.health.HealthComponent;
import org.terasology.math.Vector2i;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.cameras.Camera;
import org.terasology.rendering.nui.layers.ingame.coloring.FaceToPaint;
import org.terasology.rendering.world.WorldRenderer;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockPart;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;

import coloring.AbstractColoring;
import coloring.BuildRegister;
import coloring.ColoringRegistry;
import coloring.ColoringState;

@RegisterSystem
public class PlaceBlockCommand extends BaseComponentSystem {

    private String[] colors = {"Red", "Blue", "Green", "Yellow"};
    
    //Same that in CodeCityBuildingProvider
    private final CodeScale scale = new HalfLinearCodeScale();
    private final CodeMapFactory factory = new CodeMapFactory(scale);
    
    
    @Command(shortDescription = "Places a block in front of the player of the color specified({Red,Blue,Green} implemented)")
    public String placeColorBlock(@CommandParam("colorBlock") String colorBlock) {
    	if(!isImplementedColor(colorBlock))
    		return "Put an implemented color in {Red, Blue, Green, Yellow}";
    	WorldRenderer renderer = CoreRegistry.get(WorldRenderer.class);
    	Camera camera= renderer.getActiveCamera();
    	
    	Vector3f spawnPos = camera.getPosition();
        Vector3f offset = camera.getViewingDirection();
        offset.scale(3);
        spawnPos.add(offset);

        BlockFamily blockFamily = getBlockFamily(colorBlock);
        WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
            world.setBlock(new Vector3i((int) spawnPos.x, (int) spawnPos.y, (int) spawnPos.z), blockFamily.getArchetypeBlock());

            StringBuilder builder = new StringBuilder();
            builder.append(blockFamily.getArchetypeBlock());
            builder.append(" block placed at position (");
            builder.append((int) spawnPos.x).append((int) spawnPos.y).append((int) spawnPos.z).append(")");
            return builder.toString();
        }
        throw new IllegalArgumentException("Sorry, something went wrong!");
    }

    /**
     * Return whether or not the color is implemented
     * @param colorBlock
     * @return
     */
	private boolean isImplementedColor(String colorBlock) 
	{
		for(String color : colors)
		{
			if (colorBlock.equals(color)) return true;
		}
		return false;
	}
	
	@Command(shortDescription = "Puts the floor of the constant city of the color specified({Red,Blue,Green} implemented)")
    public String placeColorFloor(@CommandParam("colorBlock") String colorBlock) {
    	if(!isImplementedColor(colorBlock)) {
    		return "Put an implemented color in {Red, Blue, Green, Yellow}";
    	}
    	
        BlockFamily blockFamily = getBlockFamily(colorBlock);
        
        WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
        	for(int x = 0; x<=19; ++x) {
        		for(int z = 0; z<=19; ++z) {
        			world.setBlock(new Vector3i(x, 10, z), blockFamily.getArchetypeBlock());
        		}
        	}
            return "Success";
        }
        throw new IllegalArgumentException("Sorry, something went wrong!");
    }
	
	@Command(shortDescription = "Create a building  of the color specified({Red,Blue,Green} implemented)")
    public String placeColorBuilding(@CommandParam("colorBlock") String colorBlock,
    		                         @CommandParam("X") int xpos,
    		                         @CommandParam("Y") int ypos,
    		                         @CommandParam("Z") int zpos,
    		                         @CommandParam("size") int size) {
		
		return placeColorBuildingCommon(colorBlock, xpos, ypos, zpos, size, 1);
    }
	
	private String getColorName(String color, int damage) {
		
		int maxHealth = ColoringState.MAX_HEALTH;
	
		double hueValue = Math.floor((maxHealth - damage)*6.0/maxHealth);
        hueValue = Math.min(6, hueValue);
        hueValue = Math.max(1, hueValue);
        
        return  color + (int)hueValue;
	}
	
	public String placeColorBuildingCommon(String family, int xpos, int ypos, int zpos, int height, int width) {
		return placeColorBuildingCommon(family, family, FaceToPaint.ALL.toString(), xpos, ypos, zpos, height, width, 0);
	}
    
    public String placeColorBuildingCommon(String familyName, String color, String face, int xpos, int ypos, int zpos, int height, int width, int damage) {
        
    	prepareBlockFamily(familyName, color, face, damage);    
        
        refreshBuild(familyName, xpos, ypos, zpos, height, width);
        return "Success";
    }
    
    private void prepareBlockFamily(String familyName, String color, String face, int damage) {
    	
    	ColoringRegistry coloringRegistry = ColoringRegistry.getRegister();
        BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        
        // update build data
        BuildRegister build = coloringRegistry.getBuild(familyName);
        List<BlockPart> faces = BuildRegister.getValidSides();
        if ( ! face.equals( FaceToPaint.ALL.toString() ) ) {
        	BlockPart part = FaceToPaint.fromString(face).getBlockPart();
        	faces = Arrays.asList(part);
        }
        
        String tilename = getColorName(color, damage);
        AssetUri tileuri = new AssetUri(AssetType.BLOCK_TILE, "Coloring", tilename);
        build.updateRegister(faces, tileuri, damage);
        coloringRegistry.updateRegistry(build);
        
        // create custom family
        AssetUri templateUri = new AssetUri(AssetType.BLOCK_DEFINITION, "Coloring", "baseline");
        AssetUri familyUri   = new AssetUri(AssetType.BLOCK_DEFINITION, "Coloring", familyName);
        blockManager.createBlockFamily(templateUri, familyUri, build.tiles);
        
    }
    
    
    private void refreshBuild(String buildname, int xpos, int ypos, int zpos, int height, int width) {

    	WorldProvider world = CoreRegistry.get(WorldProvider.class);
    	ColoringRegistry coloringRegistry = ColoringRegistry.getRegister();
    	BlockEntityRegistry blockEntityRegistry = CoreRegistry.get(BlockEntityRegistry.class);
    	
    	// get block
        BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", buildname));
        Block block = blockFamily.getArchetypeBlock();
        
        
        // coloring data
        ColoringState state = coloringRegistry.getState();
        BuildRegister build = coloringRegistry.getBuild(buildname);
        
        
        int maxHealth = ColoringState.MAX_HEALTH;
        boolean renderQuakes = state.renderQuakes();
        
        // max build damage. this will be drawn
        int damage = 0;
        if (renderQuakes) {
        	for (BlockPart part : BuildRegister.getValidSides() ) {
            	int partDamage = build.damage.get(part).intValue();
            	damage = Math.max(damage, partDamage);
            }
        }
        
        // update world
        block.setHardness(maxHealth);
		for (int i = 0; i < width; i++) {
			for(int j = 0; j < width; j++) {
	        	for(int y = 0; y < height; ++y) {
	        		
	        		Vector3i blockPos = new Vector3i((xpos + i), (ypos + y), (zpos + j));
	        		
	        		// delete previous block to override all functionalities
	        		blockEntityRegistry.getEntityAt(blockPos).destroy();
	        		
	        		// place new block
	        		world.setBlock(blockPos, block);
	        		
	        		// set health/damage properties
	        		EntityRef entity = blockEntityRegistry.getEntityAt(blockPos);
	        		HealthComponent health;
	        		if (entity.hasComponent(HealthComponent.class)) {
	        			health = entity.getComponent(HealthComponent.class);
	        			health.maxHealth = maxHealth;
	        			health.regenRate = 0;
	        			entity.saveComponent(health);
	        		} else {
	        			health = new HealthComponent(maxHealth,0,0);
	        			entity.addComponent(health);
	        		}
	        		
	        		// send damage damage event
	        		if (renderQuakes) {
	        			entity.send(new DoDamageEvent(damage));
	        		}
	        	}
			}
		}
		
	}
	
	private BlockFamily getBlockFamily(String familyUri) {
		BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        return blockManager.getBlockFamily(familyUri);
	}

	@Command(shortDescription = "Colors the entire city of the color specified({Red,Blue,Green} implemented)")
    public String colorCity(@CommandParam("colorBlock") String colorBlock) {
    	if(!isImplementedColor(colorBlock))
    		return "Put an implemented color in {Red, Blue, Green}";
    	
    	BlockFamily blockFamily = getBlockFamily(colorBlock);
        WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
        	CodeMap map = CoreRegistry.get(CodeMap.class);
        	processMap(map, Vector2i.zero(), 10, world, blockFamily.getArchetypeBlock());//10 default ground level
            return "Success";
        }
        throw new IllegalArgumentException("Sorry, something went wrong!");
    }

	private void processMap(CodeMap map, Vector2i offset, int level, WorldProvider world, Block block) {
        for (MapObject obj : map.getMapObjects()) {
            int x = obj.getPositionX() + offset.getX();
            int y = obj.getPositionZ() + offset.getY();
            int height = obj.getHeight(scale, factory) + level;
            for (int z = level; z < height; z++)
            	world.setBlock(new Vector3i(x, z, y), block);
            if (obj.isOrigin()){
            	System.out.println(obj.getObject().getBase().getName());
                processMap(obj.getObject().getSubmap(scale, factory), new Vector2i(x+1, y+1), height, world, block);
            }
        }
    }
	
	@Command(shortDescription = "Get the name of the clases")
	public String getInfoClass(){
		WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
        	CodeMap map = CoreRegistry.get(CodeMap.class);
			ClassNameGetterVisitor visitor = new ClassNameGetterVisitor();
    		MapObject any = map.getMapObjects().iterator().next();
    		any.getObject().getBase().accept(visitor);
    		return visitor.getClasses().toString();
        }
        return "Sorry, something went wrong!";
	}
	
	@Command(shortDescription = "give Color to a Build")
	public String ColorBuild(
			@CommandParam("Name") String name,
			@CommandParam("Color") String color)
	{
		return ColorBuildCommon(name, color);
	}
	
	public String ColorBuildCommon(String name, String color) {
		return ColorBuildCommon(name, color, FaceToPaint.ALL.toString(), 0);
	}
	
	public void refreshCity() {
		
		ArrayList<String> paths = AbstractColoring.getClassPaths();
		ArrayList <BuildInformation> builds = getInfo();
		
		for (String buildname : paths) {
			for (BuildInformation element : builds) {
				
				if (element.getPath().equals(buildname)) {
					
					refreshBuild(
							buildname,
							element.getX(),
							element.getZ(),
							element.getY(),
							element.getHeight() - element.getZ(),
							element.getWidth()
					);
					continue;
				}
				
			}
		}
	}
	
	public String ColorBuildCommon(String name, String color, String face, int damage) {
		
		ArrayList <BuildInformation> builds = getInfo();
		
		for (BuildInformation element:builds) {
			
			if (element.getPath().equals(name)) {
				
				placeColorBuildingCommon(name,
						color,
						face,
						element.getX(),
						element.getZ(),
						element.getY(),
						element.getHeight() - element.getZ(),
						element.getWidth(),
						damage
				);
				return "Success";
			}
			
		}
		return "Class doesn't exist";
	}
	
	
	@Command(shortDescription = "give Color to a Build to some percentage and left the rest of another color")
	public String ColorPartialBuild(@CommandParam("Name") String name,
								@CommandParam("Color") String color, @CommandParam("ColorDelResto") String restColor, @CommandParam("PorcentacePrimerColor") int n){
		n = Math.min(100, n);
		
		ArrayList <BuildInformation> builds = getInfo();
		for (BuildInformation element:builds){
			if (element.getName().equals(name)){
				int width = element.getWidth();
				for (int i = 0;i < width;i++){
					for(int j = 0;j < width;j++){
						placeColorBuilding(restColor, element.getX() + i,element.getZ(),element.getY() + j,element.getHeight()-element.getZ());
						int firstColor = (int) (Math.ceil(1.0*n/100 *(element.getHeight()-element.getZ())));
						placeColorBuilding(color, element.getX() + i,element.getZ(),element.getY() + j,firstColor);
					}
				}
				return "Succes";
				
			}
		}
		return "Class doesn't exists";
		
	}
	
	
    private ArrayList <BuildInformation> getInfo() {
        WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
        	CodeMap map = CoreRegistry.get(CodeMap.class);
        	ArrayList<BuildInformation> result = processInfo(map, Vector2i.zero(), 10, world);//10 default ground level
        	return result;
        }
        throw new IllegalArgumentException("Sorry, something went wrong!");
    }

	private ArrayList<BuildInformation> processInfo(CodeMap map, Vector2i offset, int level, WorldProvider world) {
		ArrayList <BuildInformation> list = new ArrayList<BuildInformation>();
        for (MapObject obj : map.getMapObjects()) {
            int x = obj.getPositionX() + offset.getX();
            int y = obj.getPositionZ() + offset.getY();
            int height = obj.getHeight(scale, factory) + level;
            int width = obj.getWidth(scale, factory);
            if (obj.isOrigin()){
            	list.add(new BuildInformation(x,y,level,height,width,obj));
            	list.addAll(processInfo(obj.getObject().getSubmap(scale, factory), new Vector2i(x+1, y+1), height, world));
            }
        }
        return list;
    }
	@Command(shortDescription = "Restore city to default")
    public String restoreCity() {    	
		Block block = CoreRegistry.get(BlockManager.class).getBlock("core:stone");
        WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
        	CodeMap map = CoreRegistry.get(CodeMap.class);
        	processMap(map, Vector2i.zero(), 10, world, block);//10 default ground level
            return "Success";
        }
        throw new IllegalArgumentException("Sorry, something went wrong!");
    }
}
