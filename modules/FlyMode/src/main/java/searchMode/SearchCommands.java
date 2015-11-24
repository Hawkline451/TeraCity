package searchMode;

import java.util.ArrayList;
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
	
	@Command(shortDescription = "Searches for the className building and moves the player " +
			"towards it if it exists.",
			requiredPermission = PermissionManager.NO_PERMISSION)
    public String search(@CommandParam(value="className", required=true)  String className) {
		console.addMessage("Starting search...");
		CodeMap codeMap = CoreRegistry.get(CodeMap.class);
		List<MapObject> possibleResults = new ArrayList<>();
		Set<MapObject> mapObjects = codeMap.getMapObjects();
		int i = 0;
		for(MapObject object : mapObjects){
			//TODO: BUG! 440 objects iguales.
			if(object.containsClass(className)){
				possibleResults.add(object);
				break;
			}			
			i++;
		}
		String message = "Class not found.";
		if(possibleResults.size() == 1){
			MapObject result = possibleResults.get(0);
			EntityRef character = getLocalCharacterEntity();
			CodeScale codeScale = CoreRegistry.get(CodeScale.class);
			CodeMapFactory codeMapFactory = CoreRegistry.get(CodeMapFactory.class);
			//playerSystem.teleportCommand(character, result.getPositionX(), result.getHeight(codeScale, codeMapFactory), result.getPositionZ());
			message = "Class found, teleporting!";
		}
		else if(possibleResults.size() > 1){
			message = "Too many results, try refining your search.";
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
     * Get the current local character entity
     * 
     * @return EntityRef A reference to the local character Entity
     */
    private EntityRef getLocalCharacterEntity() {
        EntityRef clientEntity = getLocalClientEntity();
        ClientComponent clientComponent = clientEntity.getComponent(ClientComponent.class);
        return clientComponent.character;
    }
}
