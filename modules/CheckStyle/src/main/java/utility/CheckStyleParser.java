package utility;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CheckStyleParser extends XmlParser {

	public static void main(String[] args) {
		String path = "Project/out.xml";
		CheckStyleParser cp = new CheckStyleParser();
		cp.parse(path);
		System.out.println(cp);
	}
	
	@Override
	public void parseFiles(Document doc) {
		NodeList files = doc.getElementsByTagName("file");
		for (int i = 0; i < files.getLength(); i++) {
			Node fileNode = files.item(i);
			if (fileNode.getNodeType() == Node.ELEMENT_NODE) {
				Element actFile = (Element) fileNode;
				String path = actFile.getAttribute("name");
				if (!path.matches(".*.java")) continue;
				DataColour dataColour = new DataColour(path); 
				NodeList attList = actFile.getChildNodes();
				for (int j = 0; j < attList.getLength(); j++) {
					Node attNode = attList.item(j);
					if (attNode.getNodeType() == Node.ELEMENT_NODE) {
						Element attributes = (Element) attNode;
						int line = Integer.parseInt(attributes.getAttribute("line"));
						String message = attributes.getAttribute("message");
						DataWarning dataWarning = new DataWarning(line, message);
						dataColour.add(dataWarning);
					}
				}
				if (!dataColour.isEmpty()) data.add(dataColour);
			}
		}
	}
}
