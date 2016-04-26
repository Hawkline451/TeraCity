package searchMode;

import java.util.HashMap;
import java.util.Set;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.ISearchCommands;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.logic.players.PlayerSystem;
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
	
	private HashMap<String, Vector3i> bookMarks = new HashMap<String, Vector3i>();
	private HashMap<String, String> bookMarksName = new HashMap<String, String>();
	
	private static final String FLY = "flight";
	
	@Command(shortDescription = "Searches for the className building and moves the player " +
			"towards it if it exists.",
			requiredPermission = PermissionManager.NO_PERMISSION)
    public String search(@CommandParam(value="className", required=true)  String className) {
		String message = "Class not found.";
		console.addMessage("Starting search...");
		
		if(bookMarks.containsKey(className)){
			Vector3i pos = bookMarks.get(className);
			putHighlightBlockAt(new Vector3i(pos.getX(), pos.getY()+10, pos.getZ()));
			String command = String.format("teleport %d %d %d", pos.getX(), pos.getY()+15, pos.getZ());
			console.execute(command, getLocalClientEntity());
			console.execute(FLY, getLocalClientEntity());
			message = "Class found, teleporting!";
			return message;				
		}
		
		
		CodeMap codeMap = CoreRegistry.get(CodeMap.class);
		Set<MapObject> mapObjects = codeMap.getPosMapObjects();
		for(MapObject object : mapObjects){
			if(object.containsClass(className)){
				DrawableCodeSearchVisitor visitor = new DrawableCodeSearchVisitor(className);
				object.getObject().accept(visitor);
				while(true){
					if(visitor.resultReady()){
						Vector3i pos = visitor.getPosition();
						pos.setY(pos.getY()+10); 
						putHighlightBlockAt(pos);
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
}
