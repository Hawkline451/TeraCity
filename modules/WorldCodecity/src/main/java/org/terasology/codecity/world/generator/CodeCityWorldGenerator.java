package org.terasology.codecity.world.generator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.DrawableCode;
import org.terasology.codecity.world.map.DrawableCodeFactory;
import org.terasology.codecity.world.structure.CodeClass;
import org.terasology.codecity.world.structure.CodePackage;
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
        CodeRepresentation code = loadCodeRepresentationDefault();
        //CodeRepresentation code = loadCodeRepresentationFromSocket();

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
     * Load the Code Representation object from a socket connection
     * @return The loaded Code Representation
     */
    private CodeRepresentation loadCodeRepresentationFromSocket() {
        int portNumber = 25778;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
            return getCodeRepresentation(serverSocket);
        } catch (IOException | ClassNotFoundException e) {
            return new CodePackage("", "");
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) { }
        }
    }
    
    /**
     * Get the Code Representation from the given ServerSocket
     * @param serverSocket Socket from where the client will be connected
     * @return The loaded Code Representation
     */
    private CodeRepresentation getCodeRepresentation(ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        Socket clientSocket = serverSocket.accept();
        ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
        return (CodeRepresentation)input.readObject();
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

    /**
     * Load the default code representation
     */
    private CodeRepresentation loadCodeRepresentationDefault() {
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
