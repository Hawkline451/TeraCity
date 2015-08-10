package org.terasology.persistence.internal;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.persistence.internal.SaveTransactionHelper;
import org.terasology.persistence.internal.StoragePathProvider;
/**
 * This class allows us export the JEdit information to the hard disc.
 * @author alstrat
 *
 */
public class JEditExporter {
    private static final Logger logger = LoggerFactory.getLogger(SaveTransactionHelper.class);
     
    private JEditExporter(){
    }
     
    public static void export(StoragePathProvider storagePathProvider){
        try (
          OutputStream file = new FileOutputStream(storagePathProvider.getJEditMapInfoPath().toString());
          OutputStream buffer = new BufferedOutputStream(file);
          ObjectOutput output = new ObjectOutputStream(buffer);
        ){
            output.writeObject("hola mundo");
        }catch(IOException ex){
            logger.error("JEdit information couldn't be saved.");
        }
    }
     
     
 
}