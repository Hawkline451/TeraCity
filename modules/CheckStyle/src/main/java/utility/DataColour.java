package utility;

import java.util.ArrayList;

public class DataColour {
	String path;
	String color;
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
	
	private void generateColor() {
		double param = 0;
		for (DataWarning dataWarning : data) {
			try {
				param += dataWarning.getFind()/dataWarning.getMax();
				param /= data.size();
			} catch (Exception e) {
				param = 4;
			}
		}
		
		if (param <= 2) color = "Coloring:amarillo";
		else if (param <= 3) color = "Coloring:naranjo";
		else if (param <= 4) color = "Coloring:rojo";
		else if (param <= 5) color = "Coloring:azul";
		else color = "Coloring:morado";
	}
	
	private void pathToClassName() {
		int ini;
		if (path.contains("\\")) {
			ini = path.lastIndexOf("\\");
		} else {
			ini = path.lastIndexOf("/");
		}
		int fin = path.lastIndexOf(".");
		path = path.substring(ini + 1, fin);
	}
	
	public String getPath() {
		return path;
	}
	
	public String getColor() {
		generateColor();
		return color;
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
