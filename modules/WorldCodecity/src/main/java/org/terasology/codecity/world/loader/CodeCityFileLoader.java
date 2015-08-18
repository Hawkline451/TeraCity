package org.terasology.codecity.world.loader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.terasology.codecity.world.generator.JEditExporter;
import org.terasology.codecity.world.structure.CodePackage;
import org.terasology.codecity.world.structure.CodeRepresentation;

public class CodeCityFileLoader implements CodeCityLoader{
private CodeRepresentation code;

public CodeCityFileLoader(FileInputStream file) {
    try {
        ObjectInputStream input = new ObjectInputStream (file);
        Object object = input.readObject();
        input.close();
        code = (CodeRepresentation) object;
    } catch (IOException | ClassNotFoundException e) {
        code = new CodePackage("", "", "");
    }
}

    @Override
    public CodeRepresentation loadCodeRepresentation() {
        return code;
    }
}
