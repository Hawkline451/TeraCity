package org.terasology.codecity.world.metrics;

import java.util.List;

public class FileMetrics {

	private int numberOfLines;
	
	private int[] linesLength;
	
	public FileMetrics(int numberLines, List<Integer> lineslength){
		numberOfLines = numberLines;
		
		if (lineslength != null){
			linesLength = new int[lineslength.size()];
			for (int i = 0; i < lineslength.size(); i++) {
				linesLength[i] = lineslength.get(i);
			}
		}
	}
	
	public int getNumbeOfLines(){
		return numberOfLines;
	}
	
	public int[] getLinesLength(){
		return linesLength!= null ? linesLength : new int[numberOfLines];
	}
}
