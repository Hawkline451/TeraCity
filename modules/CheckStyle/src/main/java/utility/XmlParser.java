package utility;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public abstract class XmlParser {
	
	HashMap<String, DataColour> data;
	
	public XmlParser() {
		data = new HashMap<String, DataColour>();
	}
	
	public void parse(String path) {
		try {
			//abrir archivo de salida
			File fXmlFile = new File(path);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			parseFiles(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public abstract void parseFiles(Document doc); 
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (DataColour dataColour : data.values()) {
			str.append(dataColour + "\n");
		}
		return str.toString();
	}
	
	public HashMap<String, DataColour> getData() {
		return data;
	}
}
