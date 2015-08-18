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
        CodePackage facet = new CodePackage("facet", "", "");
        CodePackage generator = new CodePackage("generator", "", "");
        CodePackage map = new CodePackage("map", "", "");
        CodePackage structure = new CodePackage("structure", "", "");
        CodePackage scale = new CodePackage("scale", "", "");
        CodePackage terasology = new CodePackage("terasology", "."+ File.separator+"modules"+ File.separator+"WorldCodecity"+ File.separator+"src"
				+ File.separator+"main"+ File.separator+"java"+ File.separator+"org"+ File.separator+"terasology"
			+ File.separator+"codecity"+ File.separator+"world", "");
        
        CodeClass fac = new CodeClass("CodeCityFacet", 2, 64, "", "");
        facet.addCodeContent(fac);
        
        CodeClass bProv = new CodeClass("CodeCityBuildingProvider", 2, 74, "CodeCityBuildingProvider", "");
        CodeClass bRast = new CodeClass("CodeCityBuildingRasterizer", 1, 36, "CodeCityBuildingRasterizer", "");
        CodeClass gProv = new CodeClass("CodeCityGroundProvider", 0, 37, "CodeCityGroundProvider", "");
        CodeClass gRast = new CodeClass("CodeCityGroundRasterizer", 1, 34, "CodeCityGroundRasterizer", "");
        CodeClass wGen = new CodeClass("CodeCityWorldGenerator", 1, 164, "CodeCityWorldGenerator", "");
        generator.addCodeContent(bProv);
        generator.addCodeContent(bRast);
        generator.addCodeContent(gProv);
        generator.addCodeContent(gRast);
        generator.addCodeContent(wGen);
        terasology.addCodeContent(generator);
 
        CodeClass cMap = new CodeClass("CodeMap", 0, 82, "CodeMap", "");
        CodeClass cMapF = new CodeClass("CodeMapFactory", 1, 102, "CodeMapFactory", "");
        CodeClass cMapH = new CodeClass("CodeMapHash", 3, 148, "CodeMapHash", "");
        CodeClass cMapN = new CodeClass("CodeMapNull", 0, 57, "CodeMapNull", "");
        CodeClass cMapCP = new CodeClass("CodePackage", 2, 37, "CodePackage", "");
        CodeClass cMapO = new CodeClass("MapObject", 4, 67, "MapObject", "");
        CodeClass cMapDCP = new CodeClass("DrawableCodePackage", 2, 54, "DrawableCodePackage", "");
        CodeClass cMapCF = new CodeClass("DrawableCodeFactory", 0, 24, "DrawableCodeFactory", "");
        CodeClass cMapCC = new CodeClass("DrawableCodeClass", 1, 42, "DrawableCodeClass", "");
        CodeClass cMapDC = new CodeClass("DrawableCode", 0, 36, "DrawableCode", "");
        map.addCodeContent(cMap);
        map.addCodeContent(cMapF);
        map.addCodeContent(cMapH);
        map.addCodeContent(cMapN);
        map.addCodeContent(cMapCP);
        map.addCodeContent(cMapO);
        map.addCodeContent(cMapDCP);
        map.addCodeContent(cMapCF);
        map.addCodeContent(cMapCC);
        map.addCodeContent(cMapDC);
        terasology.addCodeContent(map);
 
        CodeClass cClas = new CodeClass("CodeClass", 3, 39, "CodeClass", "");
        CodeClass cPac = new CodeClass("CodePackage", 2, 37, "CodePackage", "");
        CodeClass cRep = new CodeClass("CodeRepresentation", 4, 42, "CodeRepresentation", "");
        structure.addCodeContent(cClas);
        structure.addCodeContent(cPac);
        structure.addCodeContent(cRep);
        terasology.addCodeContent(structure);
        
        CodeClass cSca = new CodeClass("CodeScale", 0,28, "CodeScale", "");
        CodeClass cLin = new CodeClass("LinearCodeScale", 0,16, "LinearCodeScale", "");
        CodeClass cSqu = new CodeClass("SquareRootCodeScale", 0,21, "SquareRootCodeScale", "");
        scale.addCodeContent(cSca);
        scale.addCodeContent(cLin);
        scale.addCodeContent(cSqu);
        structure.addCodeContent(scale);
        
        return terasology;
    }
}
