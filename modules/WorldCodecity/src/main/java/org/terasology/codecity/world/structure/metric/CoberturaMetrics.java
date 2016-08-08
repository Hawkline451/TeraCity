package org.terasology.codecity.world.structure.metric;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/*
 * This class read the result from the cobertura result of cobertura module (see TeraCity\modules\Cobertura)
 * the results are stored as a xml in TeraCity\modules\Cobertura\cobertura-2.1.1\analysis\reports. This class uses stored results to update the report
 * run cobertura from a system console or in game run CoberturAnalysis -s "Path to project to analyse" this will overwrite the old report
 * @author andres
 *
 */
public class CoberturaMetrics extends CodeMetric {

	String pathToReports = "modules"+File.separatorChar+"Cobertura"+File.separatorChar+"cobertura-2.1.1"+File.separatorChar+"analysis"+File.separatorChar+"reports"+
	File.separatorChar+"coverage.xml";
	
	String localReport = "";
	
	
	/*
	 * A little test function 
	 */
	private void test(){
		
		System.out.println(inReport("GameAgentPacman"));
		System.out.println(inReport("GameAgentPacman23"));

		
		System.out.println(getReportAge());
		getLineRate("GameAgentPacman");
		getBranchRate("GameEventLivesUpdated");
		getCoverageAprox("GameInstance");
		getCoverageAprox("GameEventLivesUpdated");
	}
	
	/*
	 * Is this file contained in the reported files?
	 */
	private boolean inReport(String path){
		localReport = readFile(pathToReports);
		return localReport.toLowerCase().contains(path.toLowerCase());
	}
	
	/*
	 * Get line rate metric from the entry in the XML
	 */
	private float getLineRate(String path){
		
		String pattern = "filename=(.*)"+path+"(.*)line-rate=(.*)branch-rate=(.*)complexity=(.*)>";
	      Pattern r = Pattern.compile(pattern);
	      Matcher m = r.matcher(localReport);
	      if (m.find( )) {
	    	  String test = m.group(3).substring(1, m.group(3).length()-2);
	    	  return Float.parseFloat(test);

	      } else {
	         System.out.println("NO MATCH FOR PATH");
	         return (Float)null;
	      }
		
	}
	/*
	 * Get branch rate metric for file in the XML
	 */
	private float getBranchRate(String path){
		String pattern = "filename=(.*)"+path+"(.*)line-rate=(.*)branch-rate=(.*)complexity=(.*)>";
	      Pattern r = Pattern.compile(pattern);
	      Matcher m = r.matcher(localReport);
	      if (m.find( )) {
	    	  String test = m.group(4).substring(1, m.group(4).length()-2);
	    	  return Float.parseFloat(test);
	      } else {
	         System.out.println("NO MATCH FOR PATH");
	         return (Float)null;
	      }

	}

	/*
	 * Count how many lines are hit by test vs the total line count
	 */
	private float getCoverageAprox(String path){
		String pattern = "<class name=(.*)"+path+"(.*)>";
		String patterEnd = "(.*)</class>(.*)";
	      Pattern r = Pattern.compile(pattern);
	      Pattern r2 = Pattern.compile(patterEnd);
	      Matcher m = r.matcher(localReport);
	      
	      
	      if (m.find()){
	      int start = m.start();
	      
	      String rest = localReport.substring(start);
	      Matcher m2 = r2.matcher(rest);
		      if (m2.find( )) {
		    	  int end = m2.start();
		    	  String classString = localReport.substring(start,start+end);
			      Pattern r3 = Pattern.compile("<line number=(.*) hits=(.*) branch=(.*)/>");
			      Matcher m3 = r3.matcher(classString);
			      int lineas = 0;
			      int hasHits = 0;
			      
			      while (m3.find()){
			    	  lineas++;
			    	  int hitOfLine = Integer.parseInt(m3.group(2).substring(1, m3.group(2).length()-1));
			    	  if (hitOfLine > 0){
			    		  hasHits++;
			    	  }
			      }
			      float result = ((float)hasHits) / ((float)lineas);
			      return result;
		    	  
		      }
	      }
	    	  return (Float)null;


	}
	
	/*
	 * Get how many hours have pass since last report 
	 */
	private String getReportAge(){
		
		 // String to be scanned to find the pattern.
	      String pattern = "timestamp=.(\\d+).>";
	      Pattern r = Pattern.compile(pattern);
	      Matcher m = r.matcher(localReport);
	      if (m.find( )) {
	    	  long oldReportTimestamp = Long.parseLong(m.group(1));
	    	  float millis = System.currentTimeMillis();
	    	  float diffHours = (millis - oldReportTimestamp)/3600000;
	    	  return Float.toString(diffHours);
	      } else {
	         return "No hay informacion de ultimo reporte";
	      }
		
	}
	
	/*
	 * Get all metrics for file in path (if this file is in the XML)
	 */
	public String getRepresentation(String path){
		String head = "Reporte para archivo : "+path+'\n';
		String ageReport = "El ultimo reporte fue generado hace "+getReportAge()+" horas \n"+'\n';
		
		if (this.inReport(path)){
			ageReport = "El ultimo reporte fue generado hace "+getReportAge()+" horas \n"+'\n';
			
			String reportBody = "LineRate : "+String.valueOf(getLineRate(path))+'\n'+"BranchRate : "+String.valueOf(getBranchRate(path))+'\n'
					+"CoverageAprox : "+String.valueOf(getCoverageAprox(path))+'\n';
			return head+ageReport+reportBody;
		}
		else{ //No se encontro archivo en reporte 
			return head+ageReport+" No hay informacion para ese archivo ";
		}
	}
	
	//------------------------------------------------------------Funciones auxiliares ---------------------------------------------------------------------------------
	
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
	
	public static void main(String[] args){		
		CoberturaMetrics c1 = new CoberturaMetrics();
		System.out.println(c1.getRepresentation("GameActionDown"));
		System.out.println(c1.getRepresentation("GameAgentPacman"));
		System.out.println(c1.getRepresentation("GameEventLivesUpdated"));
		System.out.println(c1.getRepresentation("GameInstance"));
		
		System.out.println(c1.getRepresentation("afdsdas"));

	}

}

