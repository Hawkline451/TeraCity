package org.terasology.codecity.world.loader;

import java.io.File;

import org.terasology.codecity.world.structure.CodeClass;
import org.terasology.codecity.world.structure.CodePackage;
import org.terasology.codecity.world.structure.CodeRepresentation;

/**
 * This class is used to load a default CodeCity representation
 */
public class CodeCityDefaultLoader implements CodeCityLoader {

    @Override
    public CodeRepresentation loadCodeRepresentation() {
        CodePackage facet = new CodePackage("facet", "facet", "");
        CodePackage generator = new CodePackage("generator", "generator", "");
        CodePackage map = new CodePackage("map", "map", "");
        CodePackage structure = new CodePackage("structure", "structure", "");
        CodePackage scale = new CodePackage("scale", "scale", "");
        CodePackage terasology = new CodePackage("terasology", "."+ File.separator+"modules"+ File.separator+"WorldCodecity"+ File.separator+"src"
				+ File.separator+"main"+ File.separator+"java"+ File.separator+"org"+ File.separator+"terasology"
			+ File.separator+"codecity"+ File.separator+"world", "https://github.com/mperalta92/codecity.git");
        
        CodeClass fac = new CodeClass("CodeCityFacet", 2, 64, "", "");
        facet.addCodeContent(fac);
        
        CodeClass bProv = new CodeClass("CodeCityBuildingProvider", 2, 74, "CodeCityBuildingProvider.java", "");
        CodeClass bRast = new CodeClass("CodeCityBuildingRasterizer", 1, 36, "CodeCityBuildingRasterizer.java", "");
        CodeClass gProv = new CodeClass("CodeCityGroundProvider", 0, 37, "CodeCityGroundProvider.java", "");
        CodeClass gRast = new CodeClass("CodeCityGroundRasterizer", 1, 34, "CodeCityGroundRasterizer.java", "");
        CodeClass wGen = new CodeClass("CodeCityWorldGenerator", 1, 164, "CodeCityWorldGenerator.java", "");
        //CodeClass LOADER = new CodeClass("CodeCityProjectLoader", 1, 164, "CodeCityProjectLoader.java", "");

        generator.addCodeContent(bProv);
        generator.addCodeContent(bRast);
        generator.addCodeContent(gProv);
        generator.addCodeContent(gRast);
        generator.addCodeContent(wGen);
        //generator.addCodeContent(LOADER);
        terasology.addCodeContent(generator);
 
        CodeClass cMap = new CodeClass("CodeMap", 0, 82, "CodeMap.java", "");
        CodeClass cMapF = new CodeClass("CodeMapFactory", 1, 102, "CodeMapFactory.java", "");
        CodeClass cMapH = new CodeClass("CodeMapHash", 3, 148, "CodeMapHash.java", "");
        CodeClass cMapN = new CodeClass("CodeMapNull", 0, 57, "CodeMapNull.java", "");
        CodeClass cMapCP = new CodeClass("CodePackage", 2, 37, "CodePackage.java", "");
        CodeClass cMapO = new CodeClass("MapObject", 4, 67, "MapObject.java", "");
        CodeClass cMapDCP = new CodeClass("DrawableCodePackage", 2, 54, "DrawableCodePackage.java", "");
        CodeClass cMapCF = new CodeClass("DrawableCodeFactory", 0, 24, "DrawableCodeFactory.java", "");
        CodeClass cMapCC = new CodeClass("DrawableCodeClass", 1, 42, "DrawableCodeClass.java", "");
        CodeClass cMapDC = new CodeClass("DrawableCode", 0, 36, "DrawableCode.java", "");

        map.addCodeContent(cMap);
        map.addCodeContent(cMapF);
        map.addCodeContent(cMapH);
        map.addCodeContent(cMapN);
        map.addCodeContent(cMapO);
        map.addCodeContent(cMapDCP);
        map.addCodeContent(cMapCF);
        map.addCodeContent(cMapCC);
        map.addCodeContent(cMapDC);
        terasology.addCodeContent(map);
 
        CodeClass cClas = new CodeClass("CodeClass", 3, 39, "CodeClass.java", "");
        CodeClass cPac = new CodeClass("CodePackage", 2, 37, "CodePackage.java", "");
        CodeClass cRep = new CodeClass("CodeRepresentation", 4, 42, "CodeRepresentation.java", "");

        structure.addCodeContent(cClas);
        structure.addCodeContent(cPac);
        structure.addCodeContent(cRep);
        terasology.addCodeContent(structure);
 
        CodeClass cSca = new CodeClass("CodeScale", 0,28, "CodeScale.java", "");
        CodeClass cLin = new CodeClass("LinearCodeScale", 0,16, "LinearCodeScale.java", "");
        CodeClass cSqu = new CodeClass("SquareRootCodeScale", 0,21, "SquareRootCodeScale.java", "");
        
        scale.addCodeContent(cSca);
        scale.addCodeContent(cLin);
        scale.addCodeContent(cSqu);
        structure.addCodeContent(scale);
        
        return terasology;
    }
}
