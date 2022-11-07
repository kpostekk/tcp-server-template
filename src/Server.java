import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends ServerSocket {
    private final Logger logger;
    private final List<ServerHandler> handlers = new ArrayList<>();

    public Server() throws IOException {
        super(9000);
        this.logger = Logger.getLogger("Server");
    }

    public void listen() {
        logger.log(Level.INFO, "Server is listening at " + getLocalPort());
        while (!isClosed()) {
            try {
                var incomingSocket = accept();
                logger.log(Level.INFO, "Accepted connection.");
                var handler = new ServerHandler(this, incomingSocket);
                logger.log(Level.INFO, "Handler created!");
                handlers.add(handler);
                handler.start();
            } catch (IOException exception) {
                logger.log(Level.WARNING, "Server was unable to accept incoming socket or was unable to create a handler.");
                exception.printStackTrace();
            }
        }
    }

    public List<ServerHandler> getActiveHandlers() {
        return handlers.stream().filter(h -> !h.getSocket().isClosed()).toList();
    }

    public List<String> getInitLines() {
        return handlers.stream().map(ServerHandler::getInitLine).toList();
    }

    public static void main(String[] args) throws IOException {
        var bootstraper = new BootstrapClient("172.21.48.47", 24168);
        var server = new Server();
        bootstraper.sendFlag();
        new Thread(server::listen, "Server-thread").start();
        bootstraper.sendServerAddr("172.23.129.38", server.getLocalPort());
        bootstraper.close();
    }
}
