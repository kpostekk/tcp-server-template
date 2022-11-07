# templatka

*nie używaj na produkcji.*

## co to jest?

typowy projekt z templatki z serwerem TCP i klientem rozruchowym

## jak używać?

### wymagane przed uruchomieniem

1. plik BootstrapClient.java ma klasę InterfaceSelector z metodą main, która wyświetli dostępne adresy.
2. w pliku BoostrapClient musisz nadpisać flagę
3. w pliku Server.java musisz napisać adresy serwera z zadania i twojego serwera

```java
class Server {
    public static void main(String[] args) throws IOException {
        var bootstraper = new BootstrapClient("172.21.48.47", 24168); // addr serwera z zadaniem
        var server = new Server();
        bootstraper.sendFlag();
        new Thread(server::listen, "Server-thread").start();
        bootstraper.sendServerAddr("172.23.129.38", server.getLocalPort()); // addr twojego serwera/komputera
        bootstraper.close();
    }
}
```

### uruchomienie

- po nadpisaniu metody run w server handlerze, możesz uruchomić serwer
- uruchom serwer wywołując metodę main w klasie Server
