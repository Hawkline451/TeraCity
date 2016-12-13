package org.terasology.engine.findBugs;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.DetectorFactoryCollection;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.IFindBugsEngine;
import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.config.UserPreferences;


public class FindBugsProcessor {

	
	private String rootPath;
	FindBugs2 findBugs;
	
	
	public FindBugsProcessor(String rootPath) {
		findBugs = new FindBugs2();
		this.rootPath = rootPath;
		process();
	}
	
	public FindBugsReporter getReporter() {
		 return new FindBugsReporter();
	}
	
	private void process(){
	        try {
	            Project project = new Project();
	            System.out.println("Analyzing " + rootPath);
	            project.addFile(rootPath);
	            System.setProperty("findbugs.jaws", "true");
	            DetectorFactoryCollection.instance();
	            
	            findBugs.setProject(project);
	            final FindBugsReporter reporter = this.getReporter();
	            IFindBugsEngine findBugs = new FindBugs2();
	            
	            findBugs.setBugReporter(reporter);
	            findBugs.setProject(project);
	            findBugs.setDetectorFactoryCollection(DetectorFactoryCollection.instance());
	            findBugs.setUserPreferences(UserPreferences.createDefaultUserPreferences());
	            reporter.setPriorityThreshold(Detector.NORMAL_PRIORITY);
	            
	            findBugs.execute();	            
	        } catch (Exception e) {
	            System.err.println("FindBugs processing error");
	            e.printStackTrace();
	        }
	}
}
