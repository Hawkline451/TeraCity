package searchMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.terasology.codecity.world.map.CodeBuilding;
import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.metrics.AST;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.ISearchCommands;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.logic.players.PlayerSystem;
import org.terasology.math.Vector2i;
import org.terasology.math.geom.Vector3i;
import org.terasology.network.ClientComponent;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;

/**
 * @author mrgcl
 */
@RegisterSystem
public class SearchCommands extends BaseComponentSystem implements ISearchCommands{
	@In
	private Console console;
	@In
	private EntityManager entityManager;
	@In
	private PlayerSystem playerSystem;
	
	private EntityRef localClientEntity;
	
	private Vector3i lastHighlightPos;
	private Block lastHighlightBlock;
	
	private List<Vector3i> buildingHighlighted = new ArrayList<Vector3i>();
	private List<Integer> highlightWidths = new ArrayList<Integer>();
	
	private HashMap<String, Vector3i> bookMarks = new HashMap<String, Vector3i>();
	private HashMap<String, String> bookMarksName = new HashMap<String, String>();
	
	private HashMap<Vector3i, Block> modifiedBlocks = new HashMap<Vector3i, Block>();
	
	
	
	private static final String FLY = "flight";
	
	
	@Command(shortDescription = "Searches for the className building and moves the player " +
		    "towards it if it exists.",
		    requiredPermission = PermissionManager.NO_PERMISSION)
		  public String search(@CommandParam(value="className", required=true)  String className) {
		  String message = "Class not found.";
		  console.addMessage("Starting search...");
		  
//		  if(bookMarks.containsKey(className)){
//		    Vector3i pos = bookMarks.get(className);
//		    putHighlightBlockAt(new Vector3i(pos.getX(), pos.getY()+10, pos.getZ()));
////		    highlightRoof(pos, widths.get(i), "red");
//		    
//		    String command = String.format("teleport %d %d %d", pos.getX(), pos.getY()+15, pos.getZ());
//		    console.execute(command, getLocalClientEntity());
//		    console.execute(FLY, getLocalClientEntity());
//		    message = "Class found, teleporting!";
//		    return message;				
//		  }
		  
		  
		  CodeMap codeMap = CoreRegistry.get(CodeMap.class);
		  Set<MapObject> mapObjects = codeMap.getPosMapObjects();
		  
		  for(MapObject object : mapObjects){
		    if(object.containsClass(className)){
		    DrawableCodeSearchVisitor visitor = new DrawableCodeSearchVisitor(className);
		      object.getObject().accept(visitor);
		  
		      while(true){
		        if(visitor.resultReady()){
		          Vector3i pos = visitor.getPosition();
		          int width = visitor.getWidth();
		          
		          CodeBuildingPositions b = new CodeBuildingPositions(pos, width, object);
		          restoreModifiedBlocks();
		          color2DArray(b.getRoofPos(), "red");
		          color2DArray(b.getNorthFacePos(), "red");
		          color2DArray(b.getSouthFacePos(), "blue");
		          color2DArray(b.getEastFacePos(), "green");
		          color2DArray(b.getWestFacePos(), "yellow");
		          
		          colorLine(0, b.getSouthFacePos(), "red");
		          colorLine(2, b.getSouthFacePos(), "red");
		          
		          colorLines(4, 8, b.getSouthFacePos(), "red");
		          
		          Vector3i[][] north_face = b.getNorthFacePos();
		          int count = 1;
		          
		          for(int j = 0; j < b.getHeight(); j++){
		        	  count = (j % 2);
		        	  for (int i = 0; i < b.getWidth()-2; i++ )
		        		  if(count == 1){
		        			  colorRowCol(j, i, north_face, "blue");
		        			  count--;
		        		  }
		        		  else{
		        			  count++	;
		        		  }
		        	  }
		          for (Vector3i[] vv : north_face)
		        	  for (Vector3i v : vv){
		        		  
		        	  }
		          
//		          int height = pos.getY();
//		          highlightRoof(pos, width, "red");
//		          pos.setY(pos.getY()+10); 
//		          putHighlightBlockAt(pos);
//		          colorWalls(pos, width, height, "red");
		          String command = String.format("teleport %d %d %d", pos.getX(), pos.getY()+5, pos.getZ());
		          console.execute(command, getLocalClientEntity());
		          console.execute(FLY, getLocalClientEntity());
		          message = "Class found, teleporting!";
		          break;
		        }
		      }
		      break;
		    }	
		  }		
		      return message;
		  }
	
	@Command(shortDescription = "Add a bookmark to a specific Class in format: class bookmark",
			requiredPermission = PermissionManager.NO_PERMISSION)
    public String addBookmark(@CommandParam(value="className", required=true)  String className, 
    		@CommandParam(value="bookName", required=true)  String bookName) {
		String message = "Class not found.";
		console.addMessage("Searching class to mark....");
		if(addBookmarkBool(className, bookName)){
			message = "Bookmarked!";
		}
        return message;
    }
	
	public boolean addBookmarkBool(String className, String bookName) {
		CodeMap codeMap = CoreRegistry.get(CodeMap.class);
		Set<MapObject> mapObjects = codeMap.getPosMapObjects();
		for(MapObject object : mapObjects){
			if(object.containsClass(className)){
				DrawableCodeSearchVisitor visitor = new DrawableCodeSearchVisitor(className);
				object.getObject().accept(visitor);
				while(true){
					if(visitor.resultReady()){
						Vector3i pos = visitor.getPosition();
						this.bookMarks.put(bookName, pos);
						this.bookMarksName.put(bookName, className);
						return true;
					}
				}
			}	
		}
		return false;
	};
	
	@Command(shortDescription = "Display all the bookmarks",
			requiredPermission = PermissionManager.NO_PERMISSION)
    public String displayBookmarks() {
		String message = "There is no bookmarks assigned";
		console.addMessage("Displaying Bookmarks");
		Set<String> classNames = bookMarksName.keySet();
		for(String s : classNames){
			console.addMessage("Class "+this.bookMarksName.get(s)+" with Bookmark "+s);
			message = "";
		}
        return message;
    }
	
	public HashMap<String, String> getBookmarks(){
		return this.bookMarksName;
	}
	
	@Command(shortDescription = "Remove highlights marker",
			requiredPermission = PermissionManager.NO_PERMISSION)
    public String removeHighlight() {
		String message = "There is no marker to remove";
		console.addMessage("Removing Highlight marker");
		if(lastHighlightBlock != null){
			WorldProvider world = CoreRegistry.get(WorldProvider.class);
    		world.setBlock(lastHighlightPos, BlockManager.getAir());
    		message = "Marker removed";
    		lastHighlightBlock = null;
    		lastHighlightPos = null;
    	}
        return message;
    }
	
	/**
	 * Get the current local client entity.
	 * 
     * @return localClientEntity A reference to the local client Entity
     */
    private EntityRef getLocalClientEntity() {
        if (localClientEntity == null) {
            for (EntityRef entityRef : entityManager.getEntitiesWith(ClientComponent.class)) {
                ClientComponent clientComponent = entityRef.getComponent(ClientComponent.class);
                if (clientComponent.local) {
                    localClientEntity = entityRef;
                    break;
                }
            }
        }
        return localClientEntity;
    }
    
    /**
    * Puts a highlight block at the given position.
    * @param pos the position of the block
    */
    private void putHighlightBlockAt(Vector3i pos) {
    	
    	BlockManager blockManager = CoreRegistry.get(BlockManager.class);
    	BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", "pink")); //CHANGE TO PINK
    	Block block = blockFamily.getArchetypeBlock();
    	WorldProvider world = CoreRegistry.get(WorldProvider.class);
    	if(lastHighlightBlock != null){
    		world.setBlock(lastHighlightPos, BlockManager.getAir());
    	}
    	world.setBlock(pos, block);
    	lastHighlightPos = pos;
    	lastHighlightBlock = block;
    }
    
    /**
     * Gets the name of the target in the center of the screen.
     * @return name of the target.
     */
    public String getTarget(){
      CameraTargetSystem cameraTarget = CoreRegistry.get(CameraTargetSystem.class);
      CodeRepresentation code = CodeRepresentation.getCodeRepresentation(cameraTarget);
      return code.getName();
    }
    
    /**
     * Similar to getTarget, but gives a message if there is no target.
     * @return target's name or message if no target found.
     */
    @Command(shortDescription = "Get target's name in the screen center",
        requiredPermission = PermissionManager.NO_PERMISSION)
    public String getTargetName(){
      String name = getTarget();
      if (name == null)
        return "No target found";
      return name;
    }
    
    /**
     * Searches the <text> in all files of the project to then highlight the roof of their buildings
     * @param text string to search
     */
	@Command(shortDescription = "Search a string and highlight every building"
			+ "containing it",
			requiredPermission = PermissionManager.NO_PERMISSION)
    public void searchText(@CommandParam(value="text", required=true) String text){
    	CodeMap codeMap = CoreRegistry.get(CodeMap.class);
		Set<MapObject> mapObjects = codeMap.getPosMapObjects();
		List<Vector3i> positions = new ArrayList<Vector3i>();
        List<Integer> widths = new ArrayList<Integer>();
        List<String> names = new ArrayList<String>();
		DrawableCodeSearchTextVisitor visitor;
        console.addMessage("Searching....");
		for(MapObject object : mapObjects){
			visitor = new DrawableCodeSearchTextVisitor(text);
			if(object.containsText(text)){
				object.getObject().accept(visitor);
				while(true){
					if(visitor.resultReady()){
						positions.addAll(visitor.getVectors());
						widths.addAll(visitor.getWidths());
						names.addAll(visitor.getNames());
						break;
					}
				}
			}
		}
		for (String name: names){
			console.addMessage("--> " + name);
		}
		console.addMessage("Total: " + positions.size() + " files contain " + text);
        for (int i = 0; i < positions.size(); i++){
        	highlightRoof(positions.get(i), widths.get(i), "red");
        }
    }
    
	/**
	 * Search of all the files that are referenced by the target building's file, highlighting
	 * all their roofs.
	 */
	@Command(shortDescription = "Highlights every building referenced by the target building",
			requiredPermission = PermissionManager.NO_PERMISSION)
    public void searchReferenceFrom(){
        CameraTargetSystem cameraTarget = CoreRegistry.get(CameraTargetSystem.class);
        CodeRepresentation code = CodeRepresentation.getCodeRepresentation(cameraTarget);
        AST ast = code.getAst();
        if (ast == null){
        	console.addMessage("Not a class!");
        	return;
        }
		List<Vector3i> positions = new ArrayList<Vector3i>();
		List<Integer> widths = new ArrayList<Integer>();
		List<String> names = new ArrayList<String>();
        console.addMessage("Searching....");
        CodeMap codeMap = CoreRegistry.get(CodeMap.class);
		Set<MapObject> mapObjects = codeMap.getPosMapObjects();
		DrawableCodeSearchRefFromVisitor visitor;
        for(MapObject object : mapObjects){
        	 visitor = new DrawableCodeSearchRefFromVisitor(ast);
			object.getObject().accept(visitor);
			while(true){
				if(visitor.resultReady()){
					positions.addAll(visitor.getVectors());
					widths.addAll(visitor.getWidths());
					names.addAll(visitor.getNames());
					break;
				}
			}
		}
        for (String name: names){
			console.addMessage("--> " + name);
		}
		console.addMessage("Total: " + positions.size() + " files are referenced by " + code.getName());
        for (int i = 0; i < positions.size(); i++){
        	highlightRoof(positions.get(i), widths.get(i), "blue");
        }
    }
	
	/**
	 * Search of all the files referencing to the target building's file, highlighting
	 * all their roofs.
	 */
	@Command(shortDescription = "Highlights every building referencing the target building",
			requiredPermission = PermissionManager.NO_PERMISSION)
    public void searchReferenceTo(){
        CameraTargetSystem cameraTarget = CoreRegistry.get(CameraTargetSystem.class);
        CodeRepresentation code = CodeRepresentation.getCodeRepresentation(cameraTarget);
        AST ast = code.getAst();
        if (ast == null){
        	console.addMessage("Not a class!");
        	return;
        }
        console.addMessage("Searching....");
        List<Vector3i> positions = new ArrayList<Vector3i>();
		List<Integer> widths = new ArrayList<Integer>();
		List<String> names = new ArrayList<String>();
        CodeMap codeMap = CoreRegistry.get(CodeMap.class);
		Set<MapObject> mapObjects = codeMap.getPosMapObjects();
		DrawableCodeSearchRefToVisitor visitor;
        for(MapObject object : mapObjects){
        	 visitor = new DrawableCodeSearchRefToVisitor(code);
			object.getObject().accept(visitor);
			while(true){
				if(visitor.resultReady()){
					positions.addAll(visitor.getVectors());
					widths.addAll(visitor.getWidths());
					names.addAll(visitor.getNames());
					break;
				}
			}
		}
        for (String name: names){
			console.addMessage("--> " + name);
		}
		console.addMessage("Total: " + positions.size() + " files reference to " + code.getName());
        for (int i = 0; i < positions.size(); i++){
        	highlightRoof(positions.get(i), widths.get(i), "yellow");
        }
    }
	
	/**
	 * Highligth the roof of a building.
	 * @param position of the CodeRepresentation of the building.
	 * @param width of the building
	 * @param color of the highlight
	 */
	public void highlightRoof(Vector3i position, Integer width, String color){
		BlockManager blockManager = CoreRegistry.get(BlockManager.class);
    	BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", color));
    	Block block = blockFamily.getArchetypeBlock();
		switchBlock(position, width, block);
    	buildingHighlighted.add(position);
    	highlightWidths.add(width);
	}
	
	/**
	 * Switch the block, to another block type in a roof.
	 * @param position position of the CodeRepresentation of the building.
	 * @param width of the building
	 * @param block uri to be change
	 */
    public void switchBlock(Vector3i position, Integer width, Block block){
    	WorldProvider world = CoreRegistry.get(WorldProvider.class);
    	Vector3i currentPos;
    	for (int x = 0; x < width; x++){
    		for(int z = 0; z < width; z++){
    			currentPos = new Vector3i(position.getX() + x,position.getY() + 10, position.getZ() + z);
    			world.setBlock(currentPos, block);
    		}
    	}
    }
    
    
    /**
	 * Cleans all the highlighted buildings
	 */
	@Command(shortDescription = "Cleans all the highlights",
			requiredPermission = PermissionManager.NO_PERMISSION)
	public void cleanHighlights(){
		BlockManager blockManager = CoreRegistry.get(BlockManager.class);
		BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("core", "stone"));
		Block block = blockFamily.getArchetypeBlock();
		Vector3i currentPos;
		Integer width;
		for (int i = 0; i < buildingHighlighted.size(); i++){
			currentPos = buildingHighlighted.get(i);
			width = highlightWidths.get(i);
			switchBlock(currentPos, width, block);
		}
		for (int i = 0; i < buildingHighlighted.size(); i++){
			buildingHighlighted.remove(i);
			highlightWidths.remove(i);
		}
	}

	public void restoreModifiedBlocks(){
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

	public void colorWalls(Vector3i position, Integer width, Integer height, String color){
		BlockManager blockManager = CoreRegistry.get(BlockManager.class);
		BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", color));
		Block block = blockFamily.getArchetypeBlock();
		WorldProvider world = CoreRegistry.get(WorldProvider.class);
		Vector3i currentPos;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++){
				currentPos = new Vector3i(position.getX() + i, position.getY() - j, position.getZ());
				world.setBlock(currentPos, block);
			}
		}
	}

	public void modifyBlock(Vector3i pos, Block block){
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

	public void color2DArray(Vector3i[][] positions, String color){
			BlockManager blockManager = CoreRegistry.get(BlockManager.class);
			BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", color));
			Block block = blockFamily.getArchetypeBlock();
			
			for (Vector3i[] vv : positions)
				for(Vector3i v : vv){
					modifyBlock(v, block);
				}
	    	
	}
	
	/**
	 * Color line at position
	 * 
	 * @param row
	 * @param positions
	 * @param color
	 */
	public void colorLine(int row, Vector3i[][] positions,  String color){
		BlockManager blockManager = CoreRegistry.get(BlockManager.class);
		BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", color));
		Block block = blockFamily.getArchetypeBlock();
		
		for(Vector3i v : positions[row]){
			modifyBlock(v, block);
		}
	
	}
	
	
	/**
	 * Color all lines between i and j [i, j]
	 * @param i
	 * @param j
	 * @param positions
	 * @param color
	 */
	public void colorLines(int i, int j, Vector3i[][] positions,  String color){
		for (int x = i; x < j; x++)
			colorLine(x, positions, color);

	}
	
	/**
	 * 
	 * Color block at position [row][col]
	 * @param row
	 * @param col
	 * @param positions
	 * @param color
	 */
	public void colorRowCol(int row, int col, Vector3i[][] positions,  String color){
		BlockManager blockManager = CoreRegistry.get(BlockManager.class);
		BlockFamily blockFamily = blockManager.getBlockFamily(new BlockUri("Coloring", color));
		Block block = blockFamily.getArchetypeBlock();
		modifyBlock(positions[row][col], block);
	}
	
	
}
