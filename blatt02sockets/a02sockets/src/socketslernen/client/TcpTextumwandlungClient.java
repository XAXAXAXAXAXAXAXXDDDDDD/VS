package socketslernen.client;

import socketslernen.general.Request;
import java.io.IOException;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;
import socketslernen.general.HelperSimulation;

/**
 *
 * @author kai bechmann
 */
public class TcpTextumwandlungClient {

    private static final String PROGNAME = "TcpTextumwandlungClient";

    public static void main(String[] args) {
        System.out.println("Diese Loesung wurde erstellt von Kai Bechmann.");

        TcpTextumwandlungClient client = new TcpTextumwandlungClient();
        client.runInstance(args);

        System.out.println(PROGNAME + " beendet.");
    }

    private void runInstance(String[] args) {
        String host = args[0]; // Zielhost
        int port = Integer.parseInt(args[1]); // Zielport

        HelperSimulation simHelper = new HelperSimulation();

        System.out.println("Contacting Server=" + host + " at Port=" + port);
        try (Socket socket = new Socket(host, port); DataInputStream datainput
                = new DataInputStream(socket.getInputStream()); DataOutputStream dataoutput
                = new DataOutputStream(socket.getOutputStream());) // try-with-resource-statement
        {
            String inputParameter = "Empty";
            short fnkNr;
            Scanner sc = new Scanner(System.in);
            boolean hasServerClosedConnection = false;

            while (!hasServerClosedConnection || !inputParameter.equals("quit")) {

                System.out.println("Fktnr (1 to-lower, 2 to-upper, 3 close-connection, 4 server-shutdown)");
                fnkNr = sc.nextShort();
                System.out.println("Text eingeben ('quit' fuer beenden des Client)");
                System.out.println("Bis zu drei Ziffern fuer Simulationszeit: ");
                sc.nextLine();
                inputParameter = sc.nextLine();

                if (inputParameter.equals("quit")) {
                    if (hasServerClosedConnection) {
                        System.out.println("Ende Kommando fuer Client gefunden.");
                        break;
                    } else {
                        System.out.println("Quit nicht moeglich: Erst die Verbindung beim Server beenden (Fktnr = 3).");
                    }
                }

                // Erstelle Request
                Request request = new Request();
                request.setMethodId(fnkNr);
                request.setSimTime(simHelper.computeOperationTime(inputParameter));
                inputParameter = simHelper.stringWithoutOperationTime(inputParameter);
                request.setTextlen(inputParameter.length());
                request.setText(inputParameter);

                dataoutput.writeShort(request.getMethodId());
                dataoutput.writeInt(request.getSimTime());
                dataoutput.writeInt(request.getTextlen());
                dataoutput.write(request.getText().getBytes(), 0,
                        request.getText().length());
                System.out.println("Sende Anfragenachricht mit Fktnr=" + fnkNr + " und Text=" + inputParameter);

                // Erhalte Antwort
                int textlen = datainput.readInt();
                byte[] textbytes = new byte[textlen];
                for (int i = 0; i < textlen; i++) {
                    textbytes[i] = datainput.readByte();
                }
                int offset = 0;
                String responseText = new String(textbytes, offset, textlen);
                System.out.println("Lesen der Antwortnachricht: Ergebnistext=" + responseText);
                if ((fnkNr == 4 || fnkNr == 3) && responseText != null) {
                    hasServerClosedConnection = true;
                }
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("Trenne Verbindung zu Server.");
        }
    }
}
