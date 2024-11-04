package rmilernen.rmibasicplus;

/**
 *
 * @author kai bechmann
 */
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.LinkedList;

public class Server extends UnicastRemoteObject implements BasicPlus {

    private final String servername;

    public Server(String servername) throws RemoteException {
        super();
        this.servername = servername;
    }

    public String getServername() {
        return this.servername;
    }

    public static void main(String args[]) {
        try {
            System.out.println("Diese Loesung wurde von Kai Bechmann erstellt.");
            
            Server obj = new Server("02BasicPlus");

            Registry registry = LocateRegistry.getRegistry();

            Naming.rebind(obj.getServername(), obj);

            System.out.println("Server " + obj.servername + " ready.");

            System.out.println("Current remote objects: ");
            System.out.println(Arrays.toString(registry.list()));
        } catch (MalformedURLException | RemoteException e) {
            System.err.println("Server exception: " + e.toString());
        }
    }

    @Override
    public int[] rmiQuad(int input) throws RemoteException {
        System.out.println("Called method rmiQuad with parameter=" + input);

        int[] result = new int[input];

        for (int i = 0; i < input; i++) {
            result[i] = (i + 1) * (i + 1);
        }

        System.out.println("Run method rmiQuad with result=" + Arrays.toString(result));
        return result;
    }

    @Override
    public String[] rmiTwice(String name) throws RemoteException {
        System.out.println("Called method rmiTwice with parameter=" + name);
        String[] result = new String[2];
        result[0] = name;
        result[1] = name.concat(name);

        System.out.println("Run method rmiTwice with result=" + Arrays.toString(result));

        return result;
    }

    @Override
    public LinkedList<String> rmiReaddir(String dirName) throws RemoteException {
        System.out.println("Called method rmiReaddir with parameter=" + dirName);
        File directory = new File(dirName);

        if (directory.isDirectory()) {
            var result = new LinkedList<>(Arrays.asList(directory.list()));
            System.out.println("Run method rmiReaddir with result=" + Arrays.toString(directory.list()));
            return result;
        } else {
            // Fehlerkennung
            //return new LinkedList<String>(Arrays.asList(new String[]{dirName}));
            System.out.println("Run method rmiReaddir with Exception: " + "Verzeichnis " + dirName + " konnte nicht gelesen oder gefunden werden!");
            return new LinkedList<>(Arrays.asList(new String[]{"Verzeichnis " + dirName + " konnte nicht gelesen oder gefunden werden!"}));
        }
    }

    @Override
    public void rmiShutdown() {
        try {
            System.out.println("Initiating shutdown of server " + this.getServername());

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            System.out.println(Arrays.toString(registry.list()));

            System.out.println("Unbind object " + this.getServername());
            registry.unbind(this.getServername());

            System.out.println("Unexport object " + this.getServername());
            UnicastRemoteObject.unexportObject(this, true);

        } catch (NotBoundException | RemoteException e) {
            System.err.println("Server exception: " + e.toString());
        }

    }
}
