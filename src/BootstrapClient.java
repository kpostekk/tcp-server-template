import java.io.IOException;
import java.io.PrintStream;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.logging.Logger;

public class BootstrapClient extends Socket {
    private final PrintStream writer;

    public BootstrapClient(String host, int port) throws IOException {
        super(host, port);
        writer = new PrintStream(getOutputStream());
    }

    public void sendFlag() {
        writer.println("142460");
    }

    public void sendServerAddr(String host, int port) {
        writer.printf("%s:%d%n", host, port);
    }
}

class InterfaceSelector {
    static void askForInterface() throws SocketException {
        var interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
                .stream()
                .filter(networkInterface -> networkInterface.getInterfaceAddresses().size() > 0)
                .toList();
        System.out.println("Available interfaces: ");
        for (var i : interfaces) {
            for (var addr : i.getInterfaceAddresses()) {
                System.out.printf("%s: %s\n", i.getName(), addr.getAddress().getHostAddress());
            }
        }
    }

    public static void main(String[] args) throws SocketException {
        InterfaceSelector.askForInterface();
    }
}
