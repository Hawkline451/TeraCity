package commands;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashMap;
 
 public abstract class XMLParser {	
	 
	 public static String parse(String path) {
		 try {
			 File fXmlFile = new File(path);
			 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			 Document doc = dBuilder.parse(fXmlFile);
			 doc.getDocumentElement().normalize();
	 
			 NodeList files = doc.getElementsByTagName("class");
			 return parseFiles(files);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return "Error";
		 }
	 }
	
	 public static String parseFiles(NodeList files) {
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
	 
	 public static HashMap<String, DataNode> makeNodes(NodeList files) {
		 NamedNodeMap nnm;
		 Node n;
		 
		 HashMap<String, DataNode> hash = new HashMap<String, DataNode>();
		 String name, fname, attName, attValue;
		 double line, branch, comp;
		 name = "NullNode";
		 line = branch = comp = 0;
		 
		 for (int j = 0; j < files.getLength(); j++){
			 nnm = files.item(j).getAttributes();
			 if (nnm != null){
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
				 hash.put(name, new DataNode(name, line, branch, comp));
			 }
		 }
		 
		 return hash;
	 }
}