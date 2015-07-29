package org.terasology.codecity.world.structure;

import java.io.Serializable;

/**
 * This class show the size of a portion of the code.
 */
public abstract class CodeRepresentation implements Serializable {
    private static final long serialVersionUID = -6189951858152671617L;
    private String name;
    private String path;
    private String github;
    
    /**
     * @param name Name of the represented code
     * @param github Github link to the document
     */
    public CodeRepresentation(String name, String path, String github) {
        this.name = name;
        this.path = path;
        this.github = github;
    }

    /**
     * @return Github link to the document
     */
    public String getGithubDir() {
        return github;
    }
    
    public String getPath() {
        return path;
    }
    
    /**
     * @return Name of the code represented
     */
    public String getName() {
        return name;
    }
}
