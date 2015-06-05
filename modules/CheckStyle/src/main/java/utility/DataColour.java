package utility;

import java.util.ArrayList;

public class DataColour {
	String path;
	ArrayList<DataWarning> data;
	
	public DataColour(String path) {
		this.path = path;
		data = new ArrayList<DataWarning>();
	}
	
	public void add(DataWarning dataWarning) {
		data.add(dataWarning);
	}
	
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("ruta archivo: " + path + "\n" + "warnings:\n");
		for (DataWarning dataWarning : data) {
			strBuilder.append("\t" + dataWarning + "\n");
		}
		return strBuilder.toString();
	}
}
