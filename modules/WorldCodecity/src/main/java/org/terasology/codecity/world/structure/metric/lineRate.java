package org.terasology.codecity.world.structure.metric;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lineRate extends CoberturaMetrics {
	@Override
	public String specificFunction(String path) {
		String pattern = "filename=(.*)"+path+"(.*)line-rate=(.*)branch-rate=(.*)complexity=(.*)>";
	      Pattern r = Pattern.compile(pattern);
	      Matcher m = r.matcher(localReport);
	      if (m.find( )) {
	    	  String test = m.group(3).substring(1, m.group(3).length()-2);
	    	  return "Linerate(Cobertura metric): "+(test);
	      } else {
	         return "Linerate(Cobertura metric): "+0;
	      }
	}

}
