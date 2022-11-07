import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerHandler extends Thread {
    private final Server server;
    private final Socket socket;
    private final PrintStream writer;
    private final Scanner reader;
    private final Logger logger;
    private String initLine;

    public ServerHandler(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.writer = new PrintStream(socket.getOutputStream());
        this.reader = new Scanner(socket.getInputStream());
        this.logger = Logger.getLogger(String.format("Handler-%s-%s", socket.getInetAddress().getHostName(), Thread.currentThread().getName()));
    }

    public Socket getSocket() {
        return socket;
    }

    public String getInitLine() {
        return initLine;
    }

    @Override
    public void run() {
        // 1. init line
        initLine = reader.nextLine();
        writer.println(initLine);

        // 2. concatenate 8 times
        var line2 = reader.nextLine();
        System.out.println(line2);
        writer.println(line2.repeat(8));

        // 3. send GCD
        var initNumbers = server.getInitLines()
                .stream()
                .map(BigDecimal::new)
                .toList();

        var gcdFromInits = GCD.gcd(initNumbers.toArray(BigDecimal[]::new));
        writer.println(gcdFromInits);

        // 4. recv. and remove zeros
        var line4 = reader.nextLine();
        System.out.println(line4);
        line4 = line4.replaceAll("0", "");
        writer.println(line4);

        // 5. send localPort
        writer.println(socket.getLocalPort());

        // 6. send sum of initLines
        var sum = initNumbers.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        writer.println(sum);

        // grande finale, get a flag
        String flag;
        try {
            flag = reader.nextLine();
        } catch (NoSuchElementException exception) {
            logger.log(Level.WARNING, "Flag does not have endline.");
            flag = reader.next();
        }

        logger.log(Level.INFO, String.format("Flag: %s", flag));
        System.out.println("FLAG BELOW");
        System.out.println(flag);
        System.out.println("FLAG ABOVE");

        logger.log(Level.INFO, "Closing connection!");
        try {
            socket.close();
            Runtime.getRuntime().exit(0);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
