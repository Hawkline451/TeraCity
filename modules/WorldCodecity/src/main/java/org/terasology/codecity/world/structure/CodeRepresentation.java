package org.terasology.codecity.world.structure;

import java.io.Serializable;

/**
 * This class show the size of a portion of the code.
 */
public abstract class CodeRepresentation implements Serializable {
    private static final long serialVersionUID = -6189951858152671617L;
    private String name;
    private String github;
    
    /**
     * @param name Name of the represented code
     * @param github Github link to the document
     */
    public CodeRepresentation(String name, String github) {
        this.name = name;
        this.github = github;
    }

    /**
     * @return Github link to the document
     */
    public String getGithubDir() {
        return github;
    }
    
    /**
     * @return Name of the code represented
     */
    public String getName() {
        return name;
    }
}
