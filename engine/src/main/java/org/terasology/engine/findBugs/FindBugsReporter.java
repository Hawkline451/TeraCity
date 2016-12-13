package org.terasology.engine.findBugs;
import edu.umd.cs.findbugs.PrintingBugReporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

import edu.umd.cs.findbugs.BugInstance;

public class FindBugsReporter extends PrintingBugReporter {

	private Hashtable<String, ArrayList<Integer>> fileLine;
	
	public FindBugsReporter() {
		super();
		fileLine = new Hashtable<String, ArrayList<Integer>>();
	}
	
	public static void saveTSV(Hashtable<String, ArrayList<Integer>> table, String name) throws IOException{
	    
        Writer output = null;
        File file = new File("./modules/GitHub/Metrics/" + name + ".tsv");
        output = new BufferedWriter(new FileWriter(file));
   	 	BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for(String key:table.keySet()){
        	 Path path = Paths.get(key);
   		 	 bw.write(path.getFileName().toString());
        	 for (int o : table.get(key)) {
            	 bw.write("\t");
            	 bw.write(String.valueOf(o));
        	 }
        	 bw.write("\n");
        }
        bw.close();
        output.close();
        System.out.println("File has been written");	   
	}

	@Override
    protected void doReportBug(final BugInstance bug) {
		String key = bug.getPrimarySourceLineAnnotation().getClassName()+".java";
		int value = bug.getPrimarySourceLineAnnotation().getEndLine();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		if (fileLine.containsKey(key)) {
			temp.addAll(fileLine.get(key));
		}
		temp.add(value);
		fileLine.put(key, temp);			
	}
	
	@Override
	public void finish(){
		try {
			saveTSV(fileLine, "findBugsAnalysis");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
