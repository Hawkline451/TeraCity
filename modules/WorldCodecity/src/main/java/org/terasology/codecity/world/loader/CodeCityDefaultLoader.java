package org.terasology.codecity.world.loader;

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
        CodePackage terasology = new CodePackage("terasology", "", "");
        
        CodeClass fac = new CodeClass("CodeCityFacet", 2, 64, "", "");
        facet.addCodeContent(fac);
        
        CodeClass bProv = new CodeClass("CodeCityBuildingProvider", 2, 74, "", "");
        CodeClass bRast = new CodeClass("CodeCityBuildingRasterizer", 1, 36, "", "");
        CodeClass gProv = new CodeClass("CodeCityGroundProvider", 0, 37, "", "");
        CodeClass gRast = new CodeClass("CodeCityGroundRasterizer", 1, 34, "", "");
        CodeClass wGen = new CodeClass("CodeCityWorldGenerator", 1, 164, "", "");
        generator.addCodeContent(bProv);
        generator.addCodeContent(bRast);
        generator.addCodeContent(gProv);
        generator.addCodeContent(gRast);
        generator.addCodeContent(wGen);
        terasology.addCodeContent(generator);
 
        CodeClass cMap = new CodeClass("CodeMap", 0, 82, "", "");
        CodeClass cMapF = new CodeClass("CodeMapFactory", 1, 102, "", "");
        CodeClass cMapH = new CodeClass("CodeMapHash", 3, 148, "", "");
        CodeClass cMapN = new CodeClass("CodeMapNull", 0, 57, "", "");
        CodeClass cMapCP = new CodeClass("CodePackage", 2, 37, "", "");
        CodeClass cMapO = new CodeClass("MapObject", 4, 67, "", "");
        CodeClass cMapDCP = new CodeClass("DrawableCodePackage", 2, 54, "", "");
        CodeClass cMapCF = new CodeClass("DrawableCodeFactory", 0, 24, "", "");
        CodeClass cMapCC = new CodeClass("DrawableCodeClass", 1, 42, "", "");
        CodeClass cMapDC = new CodeClass("DrawableCode", 0, 36, "", "");
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
 
        CodeClass cClas = new CodeClass("CodeClass", 3, 39, "", "");
        CodeClass cPac = new CodeClass("CodePackage", 2, 37, "", "");
        CodeClass cRep = new CodeClass("CodeRepresentation", 4, 42, "", "");
        structure.addCodeContent(cClas);
        structure.addCodeContent(cPac);
        structure.addCodeContent(cRep);
        terasology.addCodeContent(structure);
 
        CodeClass cSca = new CodeClass("CodeScale", 0,28, "", "");
        CodeClass cLin = new CodeClass("LinearCodeScale", 0,16, "", "");
        CodeClass cSqu = new CodeClass("SquareRootCodeScale", 0,21, "", "");
        scale.addCodeContent(cSca);
        scale.addCodeContent(cLin);
        scale.addCodeContent(cSqu);
        structure.addCodeContent(scale);
        
        return terasology;
    }
}