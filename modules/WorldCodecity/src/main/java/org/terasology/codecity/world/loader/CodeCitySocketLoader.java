package org.terasology.codecity.world.loader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.terasology.codecity.world.structure.CodePackage;
import org.terasology.codecity.world.structure.CodeRepresentation;

/**
 * This class is used to load a Code Representation from a socket
 */
public class CodeCitySocketLoader implements CodeCityLoader {
    
    private int port;
    
    public CodeCitySocketLoader(int port) {
        this.port = port;
    }

    @Override
    public CodeRepresentation loadCodeRepresentation() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            return getCodeRepresentation(serverSocket);
        } catch (IOException | ClassNotFoundException e) {
            return new CodePackage("", "", "");
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) { }
        }
    }

    /**
     * Get the Code Representation from the given ServerSocket
     * @param serverSocket Socket from where the client will be connected
     * @return The loaded Code Representation
     */
    private CodeRepresentation getCodeRepresentation(ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        Socket clientSocket = serverSocket.accept();
        ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
        return (CodeRepresentation)input.readObject();
    }
}
