package org.terasology.codecity.world.structure.metric;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.terasology.codecity.world.structure.CodeClass;



/*
 * This class read the result from the cobertura result of cobertura module (see TeraCity\modules\Cobertura)
 * the results are stored as a xml in TeraCity\modules\Cobertura\cobertura-2.1.1\analysis\reports. This class uses stored results to update the report
 * run cobertura from a system console or in game run CoberturAnalysis -s "Path to project to analyse" this will overwrite the old report
 * @author andres
 *
 */
public abstract class CoberturaMetrics extends CodeMetric {

	String pathToReports = "modules"+File.separatorChar+"Cobertura"+File.separatorChar+"cobertura-2.1.1"+File.separatorChar+"analysis"+File.separatorChar+"reports"+
	File.separatorChar+"coverage.xml";
	
	String localReport = "";
	
	
	/*
	 * Is this file contained in the reported files?
	 */
	private boolean inReport(String path){
		localReport = readFile(pathToReports);
		return localReport.toLowerCase().contains(path.toLowerCase());
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
	


	
	public abstract String specificFunction(String path);
	
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

	
	/*
	 * Get all metrics for file in path (if this file is in the XML)
	 */
	@Override
	public String getMetricVal(CodeClass code) {
		String path = code.getPath();
		if (this.inReport(path)){
			return specificFunction(path)+" *this report has "+getReportAge()+" hours";
		}
		else{ //No se encontro archivo en reporte 
			return " No hay informacion para ese archivo ";
		}
	}

}

