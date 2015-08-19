package flyMode;

import org.terasology.HUDToggleButtons.systems.HUDToggleButtonsClientSystem;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.CharacterMovementComponent;
import org.terasology.logic.characters.MovementMode;
import org.terasology.logic.characters.events.SetMovementModeEvent;
import org.terasology.logic.console.Console;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.network.ClientComponent;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.generator.WorldGenerator;

import speedAlgorithm.CodeMapSpeed;
import speedAlgorithm.Speed;

/**
 * Class that toggles the Flying mode in Terasology when clicking a button
 */
@RegisterSystem
public class FlyMode extends BaseComponentSystem implements HUDToggleButtonsClientSystem.HUDToggleButtonState {
    @In
    private HUDToggleButtonsClientSystem toggleButtonsClientSystem;
    @In
    private EntityManager entityManager;
    @In
    private Console console;
    @In
    private WorldGenerator worldGenerator;
    @In
    private BlockManager blockManager;
    
    private EntityRef localClientEntity;
    private Vector3f oldLocation;

    /* 
     * @see org.terasology.entitySystem.systems.BaseComponentSystem#initialise()
     * We do override of this method for register the button into the GUI.
     */
    @Override
    public void initialise() {
        toggleButtonsClientSystem.registerToggleButton(this);
    }

    /**
     * @return localClientEntity A reference to the local client Entity
     * Get the current client Entity (The local one)
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
     * @return EntityRef A reference to the local character Entity
     * Get the current character Entity (The local one)
     */
    private EntityRef getLocalCharacterEntity() {
        EntityRef clientEntity = getLocalClientEntity();
        ClientComponent clientComponent = clientEntity.getComponent(ClientComponent.class);
        return clientComponent.character;
    }

    /* 
     * Overrides the toggle method of the Button State Interface for the flying button
     */
    @Override
    public void toggle() {
        MovementMode nextMode = MovementMode.WALKING;
        if (getMovementMode() != MovementMode.FLYING) {
            nextMode = MovementMode.FLYING;
        }
        getLocalCharacterEntity().send(new SetMovementModeEvent(nextMode));
        setNewSpeed();
    }
    
    
    /**
     * This private method sets the new speed when the flying mode is toggled by clicking the button
     */
    private void setNewSpeed(){
    	MovementMode move = getMovementMode();
    	// We create a new Speed object, according to the CodeMap city
    	Speed newSpeed = new CodeMapSpeed();
        ClientComponent clientComp = getLocalClientEntity().getComponent(ClientComponent.class);
        CharacterMovementComponent newMove = clientComp.character.getComponent(CharacterMovementComponent.class);
        if (move == MovementMode.FLYING) {
        	// We must save the position before the fly mode was toggled
        	saveOldPosition(clientComp);
        	// We place a mark, in the place we start flying
        	placeTorchMarks(clientComp.character.getComponent(LocationComponent.class));
        	// Calling of the method that calculates the speed
            newMove.speedMultiplier = newSpeed.getCalculatedSpeed();
            clientComp.character.saveComponent(newMove);
            console.addMessage("Speed multiplier set to " + newMove.speedMultiplier + " (was 1.0f)");
            console.addMessage("Max height of the map: " + newSpeed.getMaxHeight());
        }
        else{
        	// In this case we're toggling the walking mode. We need to get back to the old position.
        	goBack(clientComp);
        	newMove.speedMultiplier = 1.0f;
        	clientComp.character.saveComponent(newMove);
        	console.addMessage("Setting the speed multiplier to the original value (1.0f)");
        }
    }
    
	/**
	 * @param clientComp Instance of the current ClientComponent
	 * Helper method that changes back the position of the character to the old position (Before flying).
	 */
	private void goBack(ClientComponent clientComp) {
        // Deactivate the character to reset the CharacterPredictionSystem,
        // which would overwrite the character location
        clientComp.character.send(BeforeDeactivateComponent.newInstance());
		LocationComponent newLocation = clientComp.character.getComponent(LocationComponent.class);
		console.addMessage("Current position is: " + newLocation.getWorldPosition().toString());
        newLocation.setWorldPosition(this.oldLocation);
        clientComp.character.saveComponent(newLocation);

        // We must reactive the character
        clientComp.character.send(OnActivatedComponent.newInstance());
        console.addMessage("You're back to the initial position. You're in: "+ this.oldLocation.toString());
	}
	
    /** 
     * @param locationComponent Component of the location of the character. Received when the button is clicked, for getting back to the surface.
     * Method that places a torch at the position that the character was when the button is clicked for going back.
     */
    private void placeTorchMarks(LocationComponent locationComponent) {
    	BlockFamily blockFamily = blockManager.getBlockFamily("core:Torch");
        WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
            world.setBlock(new Vector3i((int) locationComponent.getWorldPosition().x, (int) locationComponent.getWorldPosition().y, (int) locationComponent.getWorldPosition().z), blockFamily.getArchetypeBlock());
        }
    }

	/**
	 * @param clientComp Instance of the current ClientComponent
	 * Method that saves the position of the character before flying.
	 */
	private void saveOldPosition(ClientComponent clientComp) {
		LocationComponent oldLocation = clientComp.character.getComponent(LocationComponent.class);
		this.oldLocation = oldLocation.getWorldPosition();
		console.addMessage("Saved the initial position. It was: "+ this.oldLocation.toString());
	}

    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * @return An instance of the current movement mode of the character
     * Method that returns the current movement mode of the character
     */
    private MovementMode getMovementMode() {
        EntityRef character = getLocalCharacterEntity();
        CharacterMovementComponent movementComponent = character.getComponent(CharacterMovementComponent.class);
        return movementComponent.mode;
    }

/*
 * Set the right text into the button label
 */
    @Override
    public String getText() {
        switch (getMovementMode()) {
            case CLIMBING:
                return "Climbing";
            case FLYING:
                return "Flying";
            case SWIMMING:
                return "Swimming";
            case WALKING:
                return "Walking";
            case GHOSTING:
                return "Ghosting";
            default:
                return "Unknown";
        }
    }
}
