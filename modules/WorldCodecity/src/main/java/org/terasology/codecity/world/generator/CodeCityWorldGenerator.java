package org.terasology.codecity.world.generator;

import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.loader.CodeCityDefaultLoader;
import org.terasology.codecity.world.loader.CodeCityLoader;
import org.terasology.codecity.world.loader.CodeCitySocketLoader;
import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.DrawableCode;
import org.terasology.codecity.world.map.DrawableCodeFactory;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.engine.SimpleUri;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;

/**
 * Generate a new world using information provided by JEdit
 */
@RegisterWorldGenerator(id = "codecity", displayName = "CodeCity", description = "Generates the world using a CodeCity structure")
public class CodeCityWorldGenerator extends BaseFacetedWorldGenerator {
    private final CodeScale cScale = new SquareRootCodeScale();

    public CodeCityWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    public void initialize() {
        //CodeCityLoader loader = new CodeCityDefaultLoader();
        CodeCityLoader loader = new CodeCitySocketLoader(25778);
        CodeRepresentation code = loader.loadCodeRepresentation();

        CodeMap codeMap = generateCodeMap(code);
        CoreRegistry.put(CodeMap.class, codeMap);

        super.initialize();
    }

    @Override
    protected WorldBuilder createWorld(long seed) {
        return new WorldBuilder(seed)
                .addProvider(new CodeCityGroundProvider())
        		.addProvider(new CodeCityBuildingProvider())
                .addRasterizer(new CodeCityGroundRasterizer())
                .addRasterizer(new CodeCityBuildingRasterizer())
                .setSeaLevel(0);
    }

    /**
     * Insert into the CodeRegistry the DrawableCode, gen
     * @param code
     */
    private CodeMap generateCodeMap(CodeRepresentation code) {
        List<DrawableCode> list = new ArrayList<DrawableCode>();
        list.add(DrawableCodeFactory.generateDrawableCode(code));

        CodeMapFactory factory = new CodeMapFactory(cScale);
        return factory.generateMap(list);
    }
}
