package org.terasology.codecity.world.structure.metric;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class coverageAprox extends CoberturaMetrics {
	/*
	 * Count how many lines are hit by test vs the total line count
	 */
	@Override
	public String specificFunction(String path) {
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
			      return "Coverage: "+result;
		    	  
		      }
	      }
	      return "Aprox coverage(cobertura metric) : "+0;
	}

}

