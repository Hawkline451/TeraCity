package org.terasology.codecity.world.generator;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.engine.modes.StateLoading;
import org.terasology.engine.paths.PathManager;
import org.terasology.game.GameManifest;
import org.terasology.persistence.internal.StoragePathProvider;
import org.terasology.registry.CoreRegistry;
/**
 * This class allows us export the JEdit information to the hard disc.
 * @author alstrat
 *
 */
public class JEditExporter {
    private static final Logger logger = LoggerFactory.getLogger(StateLoading.class);
     
    private JEditExporter(){
    }
     
    public static void export(CodeRepresentation code){
        try (
        	    OutputStream file = new FileOutputStream(getSavePath());
        	    OutputStream buffer = new BufferedOutputStream(file);
        	    ObjectOutput output = new ObjectOutputStream(buffer);
        ){
            output.writeObject(code);
        }catch(IOException ex){
            logger.error("JEdit information couldn't be saved.");
        }
    } 
    
    public static String getSavePath(){
        //Path savePath = PathManager.getInstance().getSavePath(gameManifest.getTitle());
        Path savePath = CoreRegistry.get(Path.class);
        StoragePathProvider storagePathProvider = new StoragePathProvider(savePath);
        return storagePathProvider.getJEditMapInfoPath().toString();
    }
     
 
}