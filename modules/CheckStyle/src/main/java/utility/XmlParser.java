package utility;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

public abstract class XmlParser {
	
	ArrayList<DataColour> data;
	
	public XmlParser() {
		data = new ArrayList<DataColour>();
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
		for (DataColour dataColour : data) {
			str.append(dataColour + "\n");
		}
		return str.toString();
	}
}
