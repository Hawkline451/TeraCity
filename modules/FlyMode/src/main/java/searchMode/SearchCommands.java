package searchMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
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

/**
 * @author mrgcl
 */
@RegisterSystem
public class SearchCommands extends BaseComponentSystem{
	@In
	private Console console;
	@In
	private EntityManager entityManager;
	@In
	private PlayerSystem playerSystem;
	
	private EntityRef localClientEntity;
	
	private HashMap<String, Vector3i> bookMarks = new HashMap<String, Vector3i>();
	private HashMap<String, String> bookMarksName = new HashMap<String, String>();
	
	@Command(shortDescription = "Searches for the className building and moves the player " +
			"towards it if it exists.",
			requiredPermission = PermissionManager.NO_PERMISSION)
    public String search(@CommandParam(value="className", required=true)  String className) {
		String message = "Class not found.";
		console.addMessage("Starting search...");
		
		if(bookMarks.containsKey(className)){
			Vector3i pos = bookMarks.get(className);
			String command = String.format("teleport %d %d %d", pos.getX(), pos.getY()+15, pos.getZ());
			console.execute(command, getLocalClientEntity());
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
						String command = String.format("teleport %d %d %d", pos.getX(), pos.getY()+15, pos.getZ());
						console.execute(command, getLocalClientEntity());
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
						message = "Bookmarked!";
						break;
					}
				}
				break;
			}	
		}
        return message;
    }
	
	@Command(shortDescription = "Display all the bookmarks",
			requiredPermission = PermissionManager.NO_PERMISSION)
    public String displayBookmarks() {
		String message = "There is no bookmarks assigned";
		console.addMessage("Displaying Bookmarks");
		Set<String> classNames = bookMarksName.keySet();
		for(String s : classNames){
			console.addMessage("Class "+this.bookMarksName.get(s)+" with Bookmark "+s);
			return "";
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
}
