package org.terasology.codecity.world.generator;

import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.loader.CodeCityLoader;
import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.DrawableCode;
import org.terasology.codecity.world.map.DrawableCodeFactory;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.engine.SimpleUri;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;

/**
 * Generate a new world using information provided by JEdit
 */
@RegisterWorldGenerator(id = "codecity", displayName = "CodeCity",
		description = "Generates the world using a CodeCity structure")
public class CodeCityWorldGenerator extends BaseFacetedWorldGenerator {
	private String path = "";

	public CodeCityWorldGenerator(SimpleUri uri) {
		super(uri);
	}

	@Override
	public void initialize(String s) {
		this.path = s;
		CodeCityLoader loader;
		if (!this.path.equals("")) {
			this.path = "C:/Users/Franco/Documents/GitHub/TeraCity/modules/WorldCodecity/src";
		}
		loader = new CodeCityProjectLoader(this.path);
		CodeRepresentation code = loader.loadCodeRepresentation();
		CodeMap codeMap = generateCodeMap(code);
		CoreRegistry.put(CodeMap.class, codeMap);
		CoreRegistry.put(CodeRepresentation.class, code);
		super.initialize(s);
		// storeCodeRepresentation(code);
	}

	private void storeCodeRepresentation(CodeRepresentation code) {
		JEditExporter.export(code);

	}

	@Override
	protected WorldBuilder createWorld(long seed) {
		return new WorldBuilder(seed).addProvider(new CodeCityGroundProvider())
				.addProvider(new CodeCityBuildingProvider()).addRasterizer(new CodeCityGroundRasterizer())
				.addRasterizer(new CodeCityBuildingRasterizer()).setSeaLevel(0);
	}

	/**
	 * Insert into the CodeRegistry the DrawableCode, gen
	 * 
	 * @param code
	 */
	private CodeMap generateCodeMap(CodeRepresentation code) {
		DrawableCodeFactory drawableFactory = new DrawableCodeFactory();
		List<DrawableCode> list = new ArrayList<DrawableCode>();
		list.add(drawableFactory.generateDrawableCode(code));

		CodeMapFactory factory = new CodeMapFactory();
		return factory.generateMap(list);
	}

}
