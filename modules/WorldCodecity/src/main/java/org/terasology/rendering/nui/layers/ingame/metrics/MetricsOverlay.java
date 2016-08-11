package org.terasology.rendering.nui.layers.ingame.metrics;

import org.terasology.codecity.world.generator.JEditExporter;
import org.terasology.codecity.world.structure.CodeClass;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.metric.CodeMetricManager;
import org.terasology.config.Config;
import org.terasology.engine.GameEngine;
import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.persistence.StorageManager;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.databinding.ReadOnlyBinding;
import org.terasology.rendering.nui.widgets.UILabel;
import org.terasology.rendering.primitives.ChunkTessellator;
import org.terasology.world.WorldProvider;
import org.terasology.world.biomes.Biome;
import org.terasology.world.biomes.BiomeManager;

public class MetricsOverlay extends CoreScreenLayer {

	@In
	private Config config;

	@In
	private GameEngine engine;

	@In
	private CameraTargetSystem cameraTarget;

	@In
	private Time time;

	@In
	private EntityManager entityManager;

	@In
	private LocalPlayer localPlayer;

	@In
	private WorldProvider worldProvider;


	@In
	private StorageManager storageManager;

	private UILabel metricsLabel;

	private String data;

	private boolean view;

	@Override
	protected void initialise() {
		view = false;
		bindVisible(new ReadOnlyBinding<Boolean>() {
			@Override
			public Boolean get() {
				return view;
			}
		});
		data = getInfo(cameraTarget);
		metricsLabel = find("metricsLabel", UILabel.class);
        if (metricsLabel != null) {
        	metricsLabel.bindText(new ReadOnlyBinding<String>() {
                @Override
                public String get() {
                    return data;
                }
            });
        }
	}

	@Override
	public void update(float delta) {
		if (metricsLabel != null) {
			data = getInfo(cameraTarget);
		}
	}

	@Override
	public boolean isModal() {
		return false;
	}

	@Override
	public boolean isEscapeToCloseAllowed() {
		return false;
	}

	public void toggle() {
		view = !view;
	}
	
	/**
	 * Get Information of the mapObject targeted by the camera;
	 * @param camera
	 * @param codemap
	 * @return
	 */
	public String getInfo(CameraTargetSystem camera) {
		if (view) {
			try {
				CodeRepresentation codeRep = CodeRepresentation.getCodeRepresentation(camera);
				CodeMetricManager men = CoreRegistry.get(CodeMetricManager.class);
				// If it's a class
				if (codeRep instanceof CodeClass) {
					CodeClass code = (CodeClass) codeRep;
					return CodeMetricManager.getInfoString(code);
				}
				
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return "Oops!";
			}
		}
		return "";
	}
}
