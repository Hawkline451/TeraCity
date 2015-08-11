package commands;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
 
 public class XMLParser {	
	 
	 public static String getResults(String path) {
		 try {
			return parseReport(getClassFiles(path));
		} catch (Exception e) {
			e.printStackTrace();
			return "Error al parsear los resultados";
		}
	 }
	 
	 public static NodeList getClassFiles(String path) throws ParserConfigurationException, SAXException, IOException{
		 File fXmlFile = new File(path);
		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		 Document doc = dBuilder.parse(fXmlFile);
		 doc.getDocumentElement().normalize();
		 return doc.getElementsByTagName("class");
	 }
	 
	 public static String parseReport(NodeList files) {
		 StringBuilder sb = new StringBuilder();
		 NamedNodeMap nnm;
		 Node n;
		 String attName, attValue;
		 sb.append("---------- Classes ----------\n");
		 for (int j = 0; j < files.getLength(); j++){
			 nnm = files.item(j).getAttributes();
			 if (nnm != null){
				 for (int i = 0; i < nnm.getLength(); i++){
					 n = nnm.item(i);
					 attName = n.getNodeName();
					 attValue= n.getNodeValue();
					 sb.append(attName + ": " + attValue + "\n");
				 }
				 sb.append("-----------------------------\n");
			 }
		 }
		 return sb.toString();
	 }
	 
	public static HashMap<String, DataNode> getDataNodes(String path) {
		NodeList files;
		Node n;
		NamedNodeMap nnm;
		String name, fname, attName, attValue;
		double line, branch, comp;
		HashMap<String, DataNode> hash = new HashMap<String, DataNode>();
		try {
			files = getClassFiles(path);
			for (int j = 0; j < files.getLength(); j++){
				 nnm = files.item(j).getAttributes();
				 if (nnm != null){
					 name = "NullNode"; fname = "NullNode";
					 line = branch = comp = 0;
					 for (int i = 0; i < nnm.getLength(); i++){
						 n = nnm.item(i);
						 attName = n.getNodeName();
						 attValue= n.getNodeValue();
						 
						 if (attName.equals("line-rate")){ line = Double.parseDouble(attValue); }
						 else if (attName.equals("branch-rate")){ branch = Double.parseDouble(attValue); }
						 else if (attName.equals("complexity")){ comp = Double.parseDouble(attValue); }
						 else if (attName.equals("filename")){ fname = attValue; }
						 else if (attName.equals("name")){ name = attValue; }
					 }
					 hash.put(fname, new DataNode(name, line, branch, comp));
				 }
			 }
		} catch (Exception e) { e.printStackTrace(); }
		return hash;
	 }
}