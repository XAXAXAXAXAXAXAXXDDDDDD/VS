package socketslernen.server;

import socketslernen.general.Response;
import socketslernen.general.Request;
import socketslernen.general.HelperSimulation;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.Arrays;

/**
 *
 * @author kai bechmann
 */
public class TcpTextumwandlungServer {

    private final int port;
    private final int backlog;
    private static final String PROGNAME = "TcpTextumwandlungServer";

    public TcpTextumwandlungServer(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;
    }

    public static void main(String[] args) {
        int nbparameters = 2;
        if (args.length >= nbparameters) {
            System.out.println("Diese Loesung wurde erstellt von Kai Bechmann.");
            
            int port = Integer.parseInt(args[0]);
            int backlog = Integer.parseInt(args[1]);
            // start server
            new TcpTextumwandlungServer(port, backlog).startServer();
        } else {
            System.out.println("Anzahl gefundener Aufrufparameter: "
                    + args.length);
            System.out.println("Anzahl benötigter Aufrufparameter: "
                    + nbparameters + ". Port und Backlog");
            System.out.println("Beende Programm " + PROGNAME);
        }
    }

    public String getServerPrefix(int connectionNr, int communicationNr) {
        return "iter(" + connectionNr + ", " + communicationNr + "): ";
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(this.port, this.backlog)) {
            System.out.println(PROGNAME + " auf " + serverSocket.getLocalSocketAddress() + " gestartet.");

            this.process(serverSocket);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void process(ServerSocket serverSocket) throws IOException {
        boolean goOn = true;
        int connectionCounter = 0;
        int messageCounter = 0;
        HelperSimulation simHelper = new HelperSimulation();

        while (goOn) {
            // bereit fuer Verbindungsanfragen 
            SocketAddress socketAddress = null;
            connectionCounter++;
            System.out.println(this.getServerPrefix(connectionCounter, 0) + "bereit Verbindung entgegen zu nehmen.");

            try (
                    Socket socket = serverSocket.accept(); DataInputStream dataInput = new DataInputStream(socket.getInputStream()); DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());) {
                messageCounter = 0;

                // Verbindung aufgebaut 
                socketAddress = socket.getRemoteSocketAddress();
                System.out.println(this.getServerPrefix(connectionCounter, messageCounter) + "Verbindung zu " + socketAddress + " aufgebaut.");
                System.out.println(this.getServerPrefix(connectionCounter, messageCounter) + " bereit Nachrichten von Verbindung entgegen zu nehmen.");
                boolean holdConnection = true;

                // starte Nachrichtenaustausch mit Client
                while (holdConnection) {

                    // warte auf nachricht
                    while (dataInput.available() == 0) {

                    }

                    messageCounter++;

                    // Request Paket analysieren
                    short methodId = dataInput.readShort();
                    int simTime = dataInput.readInt();
                    System.out.println(this.getServerPrefix(connectionCounter, messageCounter) + "Empfangen: Fktnr = " + methodId);
                    int textlen = dataInput.readInt();
                    byte[] textBytes = new byte[textlen];
                    for (int i = 0; i < textlen; i++) {
                        textBytes[i] = dataInput.readByte();
                    }
                    String text = new String(textBytes);

                    Request request = new Request();
                    request.setMethodId(methodId);
                    request.setSimTime(simTime);
                    request.setText(text);
                    request.setTextlen(textlen);

                    boolean hasSimTime = request.getSimTime() > 0;
                    if (hasSimTime) {
                        System.out.println(this.getServerPrefix(connectionCounter, messageCounter) + "Verarbeitungsdauer ist: " + request.getSimTime() + "ms");
                    }

                    System.out.println(this.getServerPrefix(connectionCounter, messageCounter)
                            + "Empfangene Bytes (" + textlen + ") fuer Text: " + Arrays.toString(textBytes));
                    System.out.println(this.getServerPrefix(connectionCounter, messageCounter)
                            + "Empfangen: Text=" + request.getText() + " len=" + request.getText().length());

                    // Verarbeiten
                    Response response = new Response();
                    String result;
                    switch (methodId) {
                        case 1:
                            result = this.toLower(request.getText());
                            if (hasSimTime) {
                                try {
                                    Thread.sleep(request.getSimTime());
                                } catch (InterruptedException ex) {
                                }
                            }

                            break;

                        case 2:
                            result = this.toUpper(request.getText());
                            if (hasSimTime) {
                                try {
                                    Thread.sleep(request.getSimTime());
                                } catch (InterruptedException ex) {
                                }
                            }

                            break;
                        case 3:
                            result = "Close connection";
                            holdConnection = false;
                            break;
                        case 4:
                            result = "Server shutdown";
                            holdConnection = false;
                            goOn = false;
                            break;
                        default:
                            result = "Method not found";
                            break;
                    }
                    response.setText(result);
                    response.setTextlen(result.length());

                    System.out.println(this.getServerPrefix(connectionCounter, messageCounter)
                            + "Antwort: Resulttext=" + response.getText());

                    // Antwort schicken
                    dataOutput.writeInt(response.getTextlen());
                    dataOutput.write(response.getText().getBytes(), 0, response.getText().length());
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } finally {
                System.out.println(this.getServerPrefix(connectionCounter, messageCounter) + "Verbindung zu " + socketAddress + " abgebaut.");
            }

        }

        serverSocket.close();
        System.out.println("Schließe Socket und beende Programm " + PROGNAME + ".");
    }

    public String toUpper(String text) {
        return text.toUpperCase();
    }

    public String toLower(String text) {
        return text.toLowerCase();
    }

    public String closeConnection() {
        return "Client connection closed";
    }

    public String shutdownServer() {
        return "Server shutdown";
    }
}
