package rmilernen.rmibasicplus;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author kai bechmann
 */
public class Client {

    private Client() {
    }

    public static void main(String[] args) {

        System.out.println("Diese Loesung wurde von Kai Bechmann erstellt.");

        if (args.length < 4) {
            System.err.println("Nicht alle Konsolenargumente angegeben! Argument 1 ist host name, Argument 2 ist quad Parameter, Argument 3 ist readdir directory name, Argument 4 ist ob Server Shutdown am Ende stattfinden soll!");
        }

        String host = args[0];
        int quadParameter = Integer.parseInt(args[1]);
        String readDirParameter = args[2];
        boolean shutdownRequested = Boolean.parseBoolean(args[3]);
        try {
            BasicPlus stub = (BasicPlus) Naming.lookup(host);

            int[] resultQuad = stub.rmiQuad(quadParameter);
            System.out.println("Response rmiQuad for param=" + quadParameter + ": " + Arrays.toString(resultQuad));

            String[] resultTwice = stub.rmiTwice("Test Input");
            System.out.println("Response rmiTwice for param=Test Input: " + Arrays.toString(resultTwice));

            LinkedList<String> resultReaddir = stub.rmiReaddir(readDirParameter);
            System.out.println("Response rmiReaddir for directory=" + readDirParameter + ": " + resultReaddir.toString());

            if (shutdownRequested) {
                System.out.println("Shutdown of server: ");
                stub.rmiShutdown();
            } else {
                System.out.println("No shutdown of server requested.");
            }
            System.out.println("End of client programm.");
        } catch (NumberFormatException | MalformedURLException | NotBoundException | RemoteException e) {
            System.err.println("Client exception: " + e.toString());
        }
    }

}
