package org.terasology.engine.findBugs;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.DetectorFactoryCollection;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.FindBugsCommandLine;
import edu.umd.cs.findbugs.IFindBugsEngine;
import edu.umd.cs.findbugs.Plugin;
import edu.umd.cs.findbugs.PluginException;
import edu.umd.cs.findbugs.PrintingBugReporter;
import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.SortedBugCollection;
import edu.umd.cs.findbugs.config.UserPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FindBugsProcessor {

	
	private String rootPath;
	private int lineBugs;
	FindBugs2 findBugs;
	
	
	public FindBugsProcessor(String rootPath) {
		findBugs = new FindBugs2();
		this.rootPath = rootPath;
		process();
	}
	
	public int getLineBugs() {
		return lineBugs;
	}
	
	private void process(){
	        try {
	            Project project = new Project();
	            System.out.println("Analyzing " + rootPath);
	            project.addFile(rootPath);
	            System.setProperty("findbugs.jaws", "true");
	            DetectorFactoryCollection.instance();

	            findBugs.setProject(project);
	            final BugReporter reporter = new PrintingBugReporter() {
	                @Override
	                protected void doReportBug(final BugInstance bug) {
	                        super.doReportBug(bug);
	                }
	            }; 

	            IFindBugsEngine findBugs = new FindBugs2();
	            findBugs.setBugReporter(reporter);
	            findBugs.setProject(project);
	            findBugs.setDetectorFactoryCollection(DetectorFactoryCollection.instance());
	            findBugs.setUserPreferences(UserPreferences.createDefaultUserPreferences());
	            
	            reporter.setPriorityThreshold(Detector.NORMAL_PRIORITY);


	            
	            System.out.print("Executing FindBugs");
	            findBugs.execute();
	            System.out.println("End of execution FindBugs");
	            this.lineBugs =  findBugs.getBugCount();
	            
	        } catch (Exception e) {
	            System.err.println("FindBugs processing error");
	            e.printStackTrace();
	        }
	}
}
