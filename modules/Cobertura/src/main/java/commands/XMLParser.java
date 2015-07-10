package commands;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
 
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
}