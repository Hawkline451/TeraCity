package org.terasology.codecity.world.generator;

import java.io.File;

import org.terasology.codecity.world.loader.CodeCityLoader;
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
	        	// Genera recursion sobre la carpeta
	            pack.addCodeContent(listFilesForFolder(fileEntry));
	        } else {
	        	// Almacena clase dentro de CodePackage
	            pack.addCodeContent(new CodeClass(folder.getName(), folder.getPath(), ""));
	        }
	    }
	    // Retorna la recursion, almacenando la carpeta como package
	    return pack;
	}
	
	public CodeRepresentation filesToRepresentation(final File file){
		if (!file.isDirectory()){
			String name = file.getName();
			if (isJava(name)){
				return new CodeClass(file.getName(), file.getPath(), "");
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

	@Override
	public CodeRepresentation loadCodeRepresentation() {
		return filesToRepresentation(folder);
	}

}
