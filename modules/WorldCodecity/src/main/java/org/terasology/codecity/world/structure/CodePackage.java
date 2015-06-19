package org.terasology.codecity.world.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.map.DrawableCode;
import org.terasology.codecity.world.map.DrawableCodePackage;

public class CodePackage extends CodeRepresentation implements Serializable {
    private static final long serialVersionUID = -8679763624498442725L;
    private List<CodeRepresentation> contentList;

    /**
     * Create a new Package representation
     * @param name Name of the package
     * @param github Github link to the document
     */
    public CodePackage(String name, String github) {
        super(name, github);
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
     * {@inheritDoc}
     */
    @Override
    public DrawableCode getDrawableCode() {
        return new DrawableCodePackage(contentList);
    }
}
