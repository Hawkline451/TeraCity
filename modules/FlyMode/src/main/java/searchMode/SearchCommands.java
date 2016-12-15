package searchMode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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
//public class SearchCommands extends BaseComponentSystem implements ISearchCommands{
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
	
	private CodeBuilding building;
	
	private static final String FLY = "flight";
	
	
	@Command(shortDescription = "Searches for the className building and moves the player " +
		    "towards it if it exists.",
		    requiredPermission = PermissionManager.NO_PERMISSION)
		  public String search(@CommandParam(value="className", required=true)  String className) {
		  String message = "Class not found.";
		  console.addMessage("Starting search...");
		  
		  CodeBuilding b = CodeBuilding.getCodeBuilding(className);
		  if (b != null){
	          CodeBuildingUtil.restoreModifiedBlocks();
	          Vector3i pos = b.getPosition();
	          CodeBuildingUtil.color2DArray(b.getRoofPos(), "red");		          
	          String command = String.format("teleport %d %d %d", pos.getX(), pos.getY()+5, pos.getZ());
	          console.execute(command, getLocalClientEntity());
	          console.execute(FLY, getLocalClientEntity());		          
	          message = "Class found, teleporting!";
		  }	
	      return message;
	  }
	
	@Command(shortDescription = "Searches for the className building and moves the player " +
		    "towards it if it exists. Return the code Building",
		    requiredPermission = PermissionManager.NO_PERMISSION)
	  public void colorBuilding(@CommandParam(value="className", required=true)  String className, 
			  					@CommandParam(value="color", required=true) String color,
			  					@CommandParam(value="face", required=true) String face) {
		  CodeBuilding b = CodeBuilding.getCodeBuilding(className);
		  if (b != null){
	          Vector3i pos = b.getPosition();
	          CodeBuildingUtil.color2DArray(b.getRoofPos(), "red");
	          if (face.equals("N")){
	        	  CodeBuildingUtil.color2DArray(b.getNorthFacePos(), color);
	          }
	          if (face.equals("S")){
	        	  CodeBuildingUtil.color2DArray(b.getSouthFacePos(), color);
	          }
	          if (face.equals("E")){
	        	  CodeBuildingUtil.color2DArray(b.getEastFacePos(), color);
	          }
	          if (face.equals("W")){
	        	  CodeBuildingUtil.color2DArray(b.getWestFacePos(), color);
	          }
		  }	
		  this.building = b;
	  }
	
	@Command(shortDescription = "Searches for the className building and moves the player " +
		    "towards it if it exists. Return the code Building",
		    requiredPermission = PermissionManager.NO_PERMISSION)
	  public void colorBuildingLine(@CommandParam(value="className", required=true)  String className, 
			  					@CommandParam(value="color", required=true) String color,
			  					@CommandParam(value="face", required=true) String face,
			  					@CommandParam(value="row", required=true) String row) {
		  CodeBuilding b = CodeBuilding.getCodeBuilding(className);
		  if (b != null){
	          Vector3i pos = b.getPosition();
	          CodeBuildingUtil.color2DArray(b.getRoofPos(), "red");
	          if (face.equals("N")){
	        	  CodeBuildingUtil.colorLine(Integer.parseInt(row), b.getNorthFacePos(), color);
	          }
	          if (face.equals("S")){
	        	  CodeBuildingUtil.colorLine(Integer.parseInt(row), b.getSouthFacePos(), color);
	          }
	          if (face.equals("E")){
	        	  CodeBuildingUtil.colorLine(Integer.parseInt(row), b.getEastFacePos(), color);
	          }
	          if (face.equals("W")){
	        	  CodeBuildingUtil.colorLine(Integer.parseInt(row), b.getWestFacePos(), color);
	          }
		  }	
		  this.building = b;
	  }


	@Command(shortDescription = "Restore original blocks that were modified by a command",
		    requiredPermission = PermissionManager.NO_PERMISSION)
		 public void cleanBuildings() {  	  
			CodeBuildingUtil.restoreModifiedBlocks();
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
		List<boolean[]> lines = new ArrayList<boolean[]>();
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
						lines.addAll(visitor.getLines());
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
        	//Color multiple lines
        	CodeBuilding b = CodeBuilding.getCodeBuilding(names.get(i));
        	for (int rowNum = 0; rowNum<lines.get(i).length; rowNum++){
        		if (lines.get(i)[rowNum]){        			
        				CodeBuildingUtil.colorLine(Math.round(rowNum/2) +1, b.getNorthFacePos(), "transparentGreen");
        		}
        		//console.addMessage(lines.get(i).toString() + "  asd  " + names.get(i));
        	}
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
	
	////
	////
	//// New Methods for coloring
	
	private void readTsvFile(String tsvPath){
		// This method read a Tsv File and Set data hashtable...
		Hashtable<String, Integer> data = new Hashtable();
		String linea;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(tsvPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while((linea=br.readLine())!=null){
				String [] args = linea.split("\t");
				data.put(args[0], Integer.parseInt(args[1]));
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		
	}

}
