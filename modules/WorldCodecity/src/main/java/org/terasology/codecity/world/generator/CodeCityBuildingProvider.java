package org.terasology.codecity.world.generator;

import org.terasology.codecity.world.facet.CodeCityFacet;
import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.math.Rect2i;
import org.terasology.math.Vector2i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

/**
 * Creates a new surface for buildings using an squared scale and a CodeMap.
 * This surface is created just above the ground.
 *
 */

@Produces(CodeCityFacet.class)
@Requires(@Facet(SurfaceHeightFacet.class))
public class CodeCityBuildingProvider implements FacetProvider {

    private final CodeScale scale = new SquareRootCodeScale();
    private final CodeMapFactory factory = new CodeMapFactory(scale);

    @Override
    public void setSeed(long seed) {

    }

    @Override
    public void process(GeneratingRegion region) {
        CodeMap codeMap = CoreRegistry.get(CodeMap.class);
        Border3D border = region.getBorderForFacet(CodeCityFacet.class);
        
        int base = (int) region.getRegionFacet(SurfaceHeightFacet.class).get(0, 0);
        CodeCityFacet facet = new CodeCityFacet(region.getRegion(), border, base);
        Rect2i processRegion = facet.getWorldRegion();

        processMap(facet, processRegion, codeMap, Vector2i.zero(), base);
        // give our newly created and populated facet to the region
        region.setRegionFacet(CodeCityFacet.class, facet);
    }

    /**
     * Update the height of the indicated position given a CodeMap.
     * @param facet Surface where the height should be updated.
     * @param region Region where the facet must be updated
     * @param map Map with the height information.
     * @param offset Offset of the current coordinates
     * @param level Current height
     */
    private void processMap(CodeCityFacet facet, Rect2i region, CodeMap map, Vector2i offset, int level) {
        for (MapObject obj : map.getMapObjects()) {
            int x = obj.getPositionX() + offset.getX();
            int y = obj.getPositionZ() + offset.getY();
            int height = obj.getHeight(scale, factory) + level;

            if (region.contains(x, y) && facet.getWorld(x, y) < height)
                for (int z = level; z < height; z++)
                    facet.setMapObject(x, z, y, obj);
            if (obj.isOrigin())
                processMap(facet, region, obj.getObject().getSubmap(scale, factory), new Vector2i(x+1, y+1), height);
        }
    }
}
