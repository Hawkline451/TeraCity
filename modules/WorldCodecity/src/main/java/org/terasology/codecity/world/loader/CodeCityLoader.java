package org.terasology.codecity.world.loader;

import org.terasology.codecity.world.structure.CodeRepresentation;

/**
 * this interface represent a CodeCity loader of the base object
 */
public interface CodeCityLoader {
    
    /**
     * Load the code representation of the city
     * @return Base Code Representation 
     */
    public CodeRepresentation loadCodeRepresentation();
}
