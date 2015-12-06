package org.terasology.engine.modes;

import org.terasology.config.Config;
import org.terasology.config.WorldGenerationConfig;
import org.terasology.engine.ComponentSystemManager;
import org.terasology.engine.GameEngine;
import org.terasology.engine.LoggingContext;
import org.terasology.engine.SimpleUri;
import org.terasology.engine.TerasologyConstants;
import org.terasology.engine.bootstrap.EntitySystemBuilder;
import org.terasology.engine.module.ModuleManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.internal.EngineEntityManager;
import org.terasology.entitySystem.event.internal.EventSystem;
import org.terasology.game.GameManifest;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.ConsoleImpl;
import org.terasology.logic.console.ConsoleSystem;
import org.terasology.logic.console.commands.CoreCommands;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.module.Module;
import org.terasology.naming.Name;
import org.terasology.network.ClientComponent;
import org.terasology.network.NetworkMode;
import org.terasology.network.NetworkSystem;
import org.terasology.reflection.copy.CopyStrategyLibrary;
import org.terasology.reflection.reflect.ReflectFactory;
import org.terasology.registry.CoreRegistry;

import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.internal.NUIManagerInternal;
import org.terasology.rendering.nui.layers.mainMenu.videoSettings.Preset;
import org.terasology.world.internal.WorldInfo;
import org.terasology.world.time.WorldTime;

public class StateInitTeracity implements GameState {
	private EngineEntityManager entityManager;
    private EventSystem eventSystem;
    private ComponentSystemManager componentSystemManager;
    private NUIManager nuiManager;

    private GameEngine gameEngine;
    
    
	@Override
	public void init(GameEngine engine) {
		//let's get the entity event system running
        entityManager = new EntitySystemBuilder().build(CoreRegistry.get(ModuleManager.class).getEnvironment(),
                CoreRegistry.get(NetworkSystem.class), CoreRegistry.get(ReflectFactory.class), CoreRegistry.get(CopyStrategyLibrary.class));

        eventSystem = CoreRegistry.get(EventSystem.class);
        CoreRegistry.put(Console.class, new ConsoleImpl());
        
        this.gameEngine = engine;
        nuiManager = CoreRegistry.get(NUIManager.class);
        ((NUIManagerInternal) nuiManager).refreshWidgetsLibrary();
        eventSystem.registerEventHandler(nuiManager);

        // TODO: Should the CSM be registered here every time or only in engine init? See Issue #1125
        componentSystemManager = new ComponentSystemManager();
        CoreRegistry.put(ComponentSystemManager.class, componentSystemManager);

        // TODO: Reduce coupling between Input system and CameraTargetSystem,
        // TODO: potentially eliminating the following lines. See Issue #1126
        CameraTargetSystem cameraTargetSystem = new CameraTargetSystem();
        CoreRegistry.put(CameraTargetSystem.class, cameraTargetSystem);

        componentSystemManager.register(cameraTargetSystem, "engine:CameraTargetSystem");
        componentSystemManager.register(new ConsoleSystem(), "engine:ConsoleSystem");
        componentSystemManager.register(new CoreCommands(), "engine:CoreCommands");


        EntityRef localPlayerEntity = entityManager.create(new ClientComponent());
        LocalPlayer localPlayer = CoreRegistry.put(LocalPlayer.class, new LocalPlayer());
        localPlayer.setClientEntity(localPlayerEntity);

        componentSystemManager.initialise();
        createGame();
	}
	
	// Creates the world of TeraCity
	private void createGame() {
		GameManifest gameManifest = new GameManifest();
		Config config = CoreRegistry.get(Config.class);
		Preset videoPreset = Preset.MINIMAL;
		videoPreset.apply(config.getRendering());
		ModuleManager moduleManager = CoreRegistry.get(ModuleManager.class);
		for (Name moduleName : config.getDefaultModSelection().listModules()) {
            Module module = moduleManager.getRegistry().getLatestModuleVersion(moduleName);
            if (module != null) {
                gameManifest.addModule(module.getId(), module.getVersion());
            }
        }

        WorldGenerationConfig worldGenConfig = config.getWorldGeneration();
		
        SimpleUri worldGeneratorUri = worldGenConfig.getDefaultGenerator();

        gameManifest.setTitle(worldGenConfig.getWorldTitle());
        gameManifest.setSeed(worldGenConfig.getDefaultSeed());
        float timeOffset = 0.5f;  // Time fixated on noon.
        WorldInfo worldInfo = new WorldInfo(TerasologyConstants.MAIN_WORLD, gameManifest.getSeed(),
                (long) (WorldTime.DAY_LENGTH * timeOffset), worldGeneratorUri);
        gameManifest.addWorld(worldInfo);
        gameEngine.changeState(new StateLoading(gameManifest, NetworkMode.NONE));
	}
	
	@Override
    public void dispose() {
        eventSystem.process();
        componentSystemManager.shutdown();
        entityManager.clear();
        CoreRegistry.clear();
    }

    @Override
    public void handleInput(float delta) {
        //inputSystem.update(delta);
    }

    @Override
    public void update(float delta) {
        eventSystem.process();
    }

    @Override
    public void render() {
    }

    @Override
    public String getLoggingPhase() {
    	return LoggingContext.INIT_PHASE;
    }

    @Override
    public boolean isHibernationAllowed() {
        return true;
    }

}
