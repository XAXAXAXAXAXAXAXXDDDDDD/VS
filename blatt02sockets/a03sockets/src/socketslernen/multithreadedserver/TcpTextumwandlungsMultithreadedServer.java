package socketslernen.multithreadedserver;

import socketslernen.general.Response;
import socketslernen.general.Request;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.Arrays;
import socketslernen.general.HelperSimulation;

/**
 *
 * @author kai bechmann
 */
public class TcpTextumwandlungsMultithreadedServer {

    private final int port;
    private final int backlog;
    private static final String PROGNAME = "TcpTextumwandlungsMultithreadedServer";

    private final int numberParallelThreads = 5;
    private int currThreads = 0;
    private final TcpServerThread[] threadArr = new TcpServerThread[numberParallelThreads];

    private boolean signalServerShutdown = false;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        int nbparameters = 2;
        if (args.length >= nbparameters) {
            System.out.println("Diese Loesung wurde erstellt von Kai Bechmann.");

            int port = Integer.parseInt(args[0]);
            int backlog = Integer.parseInt(args[1]);
            try {
                // start server
                new TcpTextumwandlungsMultithreadedServer(port, backlog).startServer();
            } catch (IOException ex) {
            }
        } else {
            System.out.println("Anzahl gefundener Aufrufparameter: "
                    + args.length);
            System.out.println("Anzahl benötigter Aufrufparameter: "
                    + nbparameters + ". Port und Backlog");
            System.out.println("Beende Programm " + PROGNAME);
        }
    }

    public TcpTextumwandlungsMultithreadedServer(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;
    }

    public boolean addNewThread(TcpServerThread t) {
        if (currThreads < numberParallelThreads - 1) {
            threadArr[currThreads++] = t;
            return true;
        } else {
            return false;
        }
    }

    public boolean removeThread(Thread t) {
        boolean foundThread = false;
        int i;
        for (i = currThreads - 1; i >= 0; i--) {
            if (threadArr[i].equals(t)) {
                foundThread = true;
                break;
            }
        }

        if (!foundThread) {
            return false;
        }

        for (int j = currThreads; j > i; j--) {
            threadArr[j - 1] = threadArr[j];
        }

        currThreads--;

        return true;
    }

    public String getServerPrefix(int threadNr, int communicationNr) {
        return "para(Thread: " + threadNr + ", " + communicationNr + "): ";
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(this.port, this.backlog);
        try {
            System.out.println(PROGNAME + " auf " + serverSocket.getLocalSocketAddress() + " lauschend...");

            while (!signalServerShutdown) {
                synchronized (serverSocket) {
                    System.out.println(this.getServerPrefix(currThreads, 0) + "bereit Verbindung entgegenzunehmen...");

                    Socket socket = serverSocket.accept();
                    DataInputStream dataInput = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());

                    TcpServerThread serverThread = new TcpServerThread(socket, dataInput, dataOutput, currThreads + 1);
                    if (!this.addNewThread(serverThread)) {
                        System.out.println(this.getServerPrefix(currThreads + 1, 0) + "No connection possible!");
                    } else {
                        serverThread.start();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println("Server Shutdown by client.");
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
        signalServerShutdown = true;
        try {
            serverSocket.close();
        } catch (IOException ex) {

        }
        for (int i = 0; i < currThreads; i++) {
            threadArr[i].signalServerShutdown();
        }

        return "Server shutdown";
    }

    class TcpServerThread extends Thread {

        private final int threadNr;
        private final Socket socket;
        private final DataInputStream dataInput;
        private final DataOutputStream dataOutput;
        private boolean serverShutdown;

        public TcpServerThread(Socket socket, DataInputStream dataInput, DataOutputStream dataOutput, int threadNr) {
            this.socket = socket;
            this.dataInput = dataInput;
            this.dataOutput = dataOutput;
            this.threadNr = threadNr;
            this.serverShutdown = false;
        }

        @Override
        public void run() {
            try {
                this.process(this.socket, this.dataInput, this.dataOutput);
            } catch (IOException ex) {
            } finally {
//                if (serverShutdown) {
//                    System.out.println("Thread shutdown by server shutdown");
//                    return;
//                }
                if (this.socket != null) {
                    try {
                        this.socket.shutdownInput();
                        this.socket.shutdownOutput();
                        this.socket.close();
                    } catch (IOException ex) {
                        System.err.println(ex.toString());
                    }
                }
            }
        }

        private void process(Socket socket, DataInputStream dataInput, DataOutputStream dataOutput) throws IOException {
            int messageCounter = 0;
            HelperSimulation simHelper = new HelperSimulation();
            SocketAddress socketAddress;
            socketAddress = socket.getRemoteSocketAddress();
            System.out.println(getServerPrefix(this.threadNr, messageCounter) + "Verbindung zu " + socketAddress + " aufgebaut.");
            System.out.println(getServerPrefix(this.threadNr, messageCounter) + "Programm " + PROGNAME + " bereit Nachrichten von Verbindung entgegen zu nehmen.");
            boolean holdConnection = true;

            while (holdConnection /*&& !serverShutdown*/) {

                // warte auf nachricht
                while (dataInput.available() == 0) {
//                    if (serverShutdown) {
//                        System.out.println("Thread shutdown by server shutdown");
//                        return;
//                    }
                }

                messageCounter++;

                // Request Paket analysieren
                short methodId = dataInput.readShort();
                int simTime = dataInput.readInt();
                System.out.println(getServerPrefix(this.threadNr, messageCounter) + "Empfangen: Fktnr = " + methodId);
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
                    System.out.println(getServerPrefix(this.threadNr, messageCounter) + "Verarbeitungsdauer ist: " + request.getSimTime() + "ms");
                }
                System.out.println(getServerPrefix(this.threadNr, messageCounter) + "Empfangene Bytes (" + textlen + ") fuer Text: " + Arrays.toString(textBytes));
                System.out.println(getServerPrefix(this.threadNr, messageCounter) + "Empfangen: Text=" + request.getText() + " len=" + request.getText().length());

                Response response = new Response();
                String result;
                switch (methodId) {
                    case 1:
                        result = toLower(request.getText());
                        if (hasSimTime) {
                            try {
                                Thread.sleep(request.getSimTime());
                            } catch (InterruptedException ex) {
                            }
                        }

                        break;

                    case 2:
                        result = toUpper(request.getText());
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
                        holdConnection = false;
                        result = shutdownServer();
                        break;
                    default:
                        result = "Method not found with fktNr=" + methodId;
                        break;
                }
                response.setText(result);
                response.setTextlen(result.length());

                System.out.println(getServerPrefix(this.threadNr, messageCounter)
                        + "Antwort: Resulttext=" + response.getText());

                dataOutput.writeInt(response.getTextlen());
                dataOutput.write(response.getText().getBytes(), 0, response.getText().length());
            }
        }

        public void signalServerShutdown() {
            this.serverShutdown = true;
        }
    }

}
