package org.terasology.codecity.world.generator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.loader.CodeCityLoader;
import org.terasology.codecity.world.metrics.FileMetrics;
import org.terasology.codecity.world.structure.CodeClass;
import org.terasology.codecity.world.structure.CodePackage;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.NullCodeClass;

public class CodeCityProjectLoader implements CodeCityLoader {
	final File folder;
	
	public CodeCityProjectLoader(String path) {
		folder = new File(path);

	}
	
	public CodeRepresentation listFilesForFolder(final File folder) {
		CodePackage pack = new CodePackage(folder.getName(), folder.getPath(), "");
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	// Genera recursión sobre la carpeta
	            pack.addCodeContent(listFilesForFolder(fileEntry));
	        } else {
	        	// Almacena clase dentro de CodePackage
	            pack.addCodeContent(new CodeClass(folder.getName(), 0 /* TODO */  , (int) folder.length(), folder.getPath(), ""));
	        }
	    }
	    // Retorna la recursión, almacenando la carpeta como package
	    return pack;
	}
	
	public CodeRepresentation filesToRepresentation(final File file){
		if (!file.isDirectory()){
			String name = file.getName();
			if (isJava(name)){
				FileMetrics metrics = getMetrics(file.getPath()); 
				return new CodeClass(name, 0 /* TODO Ver variables*/, metrics.getNumbeOfLines() /*TODO ver largo archivo*/, file.getPath(), "", metrics.getLinesLength());
			}
			else
				return new NullCodeClass();
		}
		else{
			CodePackage pack = new CodePackage(file.getName(), file.getPath(), "");
			for (final File subFile: file.listFiles()){
				CodeRepresentation cr = filesToRepresentation(subFile);
				if (cr.validCode())
					pack.addCodeContent(cr);
			}
			if(pack.size() > 0)
				return pack;
			else
				return new NullCodeClass();
		}
			
	}
	
	public boolean isJava(String s){
		int index = s.lastIndexOf(".java");
		if(index > 0)
			return true;
		return false;
	}

	
	/* From stackoverflow <3*/
	public static FileMetrics getMetrics(String filename) {
		try{
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        List<Integer> lengths = new ArrayList<Integer>();
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                	lengths.add(i);
	                    ++count;
	                }
	            }
	        }
	        if (count == 0 && !empty) 
	        	return new FileMetrics(1, null);
	        else
	        	return new FileMetrics(count, lengths);
	    } finally {
	        is.close();
	    }
		}
		catch(Exception e){
			return new FileMetrics(0, null);
		}
	}

	@Override
	public CodeRepresentation loadCodeRepresentation() {
		return filesToRepresentation(folder);
	}

}
