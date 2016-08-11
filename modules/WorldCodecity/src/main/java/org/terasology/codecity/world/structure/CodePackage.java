package org.terasology.codecity.world.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.metrics.AST;

public class CodePackage extends CodeRepresentation implements Serializable {
    private static final long serialVersionUID = -8679763624498442725L;
    // Almacena código representable, sea clases o packages
    private List<CodeRepresentation> contentList;

    /**
     * Create a new Package representation
     * @param name Name of the package
     * @param github Github link to the document
     */
    public CodePackage(String name, String path, String github) {
        super(name, path, github);
        contentList = new ArrayList<CodeRepresentation>();
    }

    /**
     * Add an object to the package
     * @param content Object to be added
     */
    public void addCodeContent(CodeRepresentation content) {
        contentList.add(content);
    }
    
    /**
     * Get the code that is contained in the package
     * @return
     */
    public List<CodeRepresentation> getContent() {
        return new ArrayList<CodeRepresentation>(contentList);
    }

    @Override
    public void accept(CodeVisitor visitor) {
        visitor.visitCodePackage(this);
    }

	@Override
	public int size() {
		int counter = 0;
		for (CodeRepresentation c: contentList)
			counter += c.size();
		return counter;
	}

}
