package org.terasology.codecity.world.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.terasology.codecity.world.generator.JEditExporter;

/**
 * This class creates a file loader to load the CodeCity information from hard disc if the file does exist,
 * otherwise it creates a socket loader.
 * @author Alejandro
 *
 */
public class CodeCityLoaderManager {
    private static final int SOCKET_PORT = 25778;

    private CodeCityLoaderManager(){
        
    }
    
    public static CodeCityLoader getLoader(){
        CodeCityLoader loader = null;
        try {
            FileInputStream file = new FileInputStream(JEditExporter.getSavePath());
            loader = new CodeCityFileLoader(file);
            file.close();
        } catch (IOException e) {
            loader = new CodeCitySocketLoader(SOCKET_PORT);
        }
        return loader;
    }
    
}
