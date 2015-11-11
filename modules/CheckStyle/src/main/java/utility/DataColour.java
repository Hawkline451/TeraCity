package utility;

import java.util.ArrayList;

public class DataColour {
	String path;
	ArrayList<DataWarning> data;
	
	public DataColour(String path) {
		path = path.replace("./", "");
		path = path.replace(".\\", "");
		this.path = path;
		//pathToClassName();
		data = new ArrayList<DataWarning>();
	}
	
	public void add(DataWarning dataWarning) {
		data.add(dataWarning);
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	/*
	private void pathToClassName() {
		int ini;
		if (path.contains("\\")) {
			ini = path.lastIndexOf("\\");
		} else {
			ini = path.lastIndexOf("/");
		}
		int fin = path.lastIndexOf(".");
		path = path.substring(ini + 1, fin);
	}*/
	
	public String getPath() {
		//String def = System.getProperty("user.dir");
		//def = def.substring(0, def.lastIndexOf("\\"));
		return path;
	}
	
	public double getMetricValue() {
		
		double metric = 0;
		for (DataWarning dataWarning : data) {
			try {
				// que intenta hacer este calculo?, esta correcto?
				metric += dataWarning.getFind()/dataWarning.getMax();
				metric /= data.size();
			} catch (Exception e) {
				return -1;
			}
		}
		return metric;
	}
	
	public ArrayList<DataWarning> getDataWarnings(){
		return data;
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
