package org.terasology.codecity.world.generator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
				// Generates recursively over the directory 
				pack.addCodeContent(listFilesForFolder(fileEntry));
			} else {
				// Stores class inside CodePackage
				pack.addCodeContent(
						new CodeClass(folder.getName(), 0 /* TODO */ , (int) folder.length(), folder.getPath(), ""));
			}
		}
		// Retorna la recursion, almacenando la carpeta como package
		return pack;
	}

	
	
	/**
	 * @param file
	 * @return
	 *  	Returns the code representation of the file 
	 */
	public CodeRepresentation filesToRepresentation(final File file) {
		if (!file.isDirectory()) {
			String name = file.getName();
			if (isJava(name)){
				return new CodeClass(name,
						0 /* TODO Count Variables */,
						file.getPath(), // Path of the file
						"", /* TODO Associate github link */
						countLineLength(file.getPath()),  //Lengths of the lines //TODO each file is read twice
						transformToCodeRepresentation(file.getPath())
						);
			}
			else{
				return new NullCodeClass();
			}
		} else {
			CodePackage pack = new CodePackage(file.getName(), file.getPath(), "");
			for (final File subFile : file.listFiles()) {
				CodeRepresentation cr = filesToRepresentation(subFile);
				if (cr.validCode())
					pack.addCodeContent(cr);
			}
			if (pack.size() > 0)
				return pack;
			else
				return new NullCodeClass();
		}

	}

	public boolean isJava(String s) {
		int index = s.lastIndexOf(".java");
		if (index > 0)
			return true;
		return false;
	}
	
	/**
	 * @param filename
	 * @return
	 * 		Returns a list of the line lengths 
	 */
	public static Integer[] countLineLength(String filename) {
		List<Integer> result = new ArrayList<Integer>();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
	        int readChars = 0;
	        int count = 0;
	        while ((readChars = in.read(c)) != -1) {
	            for (int i = 0; i < readChars; ++i) {
	            	count++;
	                if (c[i] == '\n') {
	                	// Count has the total amount of bytes/chars in the line
	                	result.add(count); // Add to the list
	                	count = 0; // Reset count to 0 for new line
	                }
	            }
	        }
			return result.toArray(new Integer[result.size()]);
		}
		finally {
			in.close();
		}
		}
		catch (Exception e) {
			return result.toArray(new Integer[result.size()]);
		}
	}
	
	@Override
	public CodeRepresentation loadCodeRepresentation() {
		return filesToRepresentation(folder);
	}
	
	// -------------------Auxiliar data funtions--------------------------

	static String readFile(String path) {
		StringBuilder builder = new StringBuilder();
		File file = new File(path);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			int content;
			while ((content = fis.read()) != -1) {
				builder.append((char) content);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return builder.toString();
	}

	public static int[][] transformToCodeRepresentation(String filename) {

		String content = readFile(filename);
		int maxLengthLine = 0; //Max length of a line in the code
		int maxBlockLength = 0; /* TODO Variable not used? Always 0 */

		String lines[] = content.split("\\r?\\n");

		for (String line : lines) {
			maxLengthLine = Math.max(maxLengthLine, line.length());

		}

		int[][] result = new int[lines.length][2 * maxBlockLength]; /* TODO [2 * maxBlockLength] = [0] */

		for (int i = 0; i < lines.length; i++) {
			result[i] = transformCodeLine(lines[i], maxLengthLine);
		}


		return result;

	}


	public static int[] transformCodeLine(String codeLine, int maxLineLength) {

		// Set string line to maxChars fill with whitespace
		StringBuilder builder = new StringBuilder(codeLine);
		builder.setLength(maxLineLength);
		while (builder.length() < maxLineLength) {
			builder.append(" ");
		}
		// Builder has now the codeLine plus a fill of whitespaces 
		int[] resultRow = new int[maxLineLength];
		int step = 1;

		for (int i = 0; i < maxLineLength; i++) {
			String stepString = builder.substring(step * i, step * (i + 1));
			String replaced = stepString.replaceAll("\\s+", ""); // Removes all whitespace

			if (replaced.trim().length() > 0) {
				resultRow[i] = 1;
			} else {
				resultRow[i] = 0;
			}
		}


		return resultRow;
	}
}
