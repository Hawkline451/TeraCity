package utility;

import java.util.ArrayList;

public class DataColour {
	String path;
	ArrayList<DataWarning> data;
	
	public DataColour(String path) {
		this.path = path;
		pathToClassName();
		data = new ArrayList<DataWarning>();
	}
	
	public void add(DataWarning dataWarning) {
		data.add(dataWarning);
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	private void pathToClassName() {
		int ini = path.lastIndexOf("/");
		int fin = path.lastIndexOf(".");
		path = path.substring(ini + 1, fin);
	}
	
	public String getPath() {
		return path;
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
