package rmilernen.rmibasicandplus;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.LinkedList;
import rmilernen.rmibasic.Basic;
import rmilernen.rmibasic.LongRemoteObj;
import rmilernen.rmibasicplus.BasicPlus;

/**
 *
 * @author kai bechmann
 */
public class Server extends UnicastRemoteObject implements Basic, BasicPlus {

    private final String servername;

    public Server(String servername) throws RemoteException {
        super();
        this.servername = servername;
    }

    public String getServername() {
        return this.servername;
    }

    public static void main(String args[]) {

        System.out.println("RMI Server Aufgabe 3 started.");

        try {
            Server basicAndPlusObj = new Server("03BasicAndPlus");
            Naming.rebind(basicAndPlusObj.getServername(), basicAndPlusObj);
            System.out.println("Server " + basicAndPlusObj.servername + " ready.");

            rmilernen.rmibasicplus.Server basicPlusObj = new rmilernen.rmibasicplus.Server("03BasicPlus");
            Naming.rebind(basicPlusObj.getServername(), basicPlusObj);
            System.out.println("Server " + basicPlusObj.getServername() + " ready.");

            rmilernen.rmibasic.Server basicObj = new rmilernen.rmibasic.Server("03Basic");
            Naming.rebind(basicObj.getServername(), basicObj);
            System.out.println("Server " + basicObj.getServername() + " ready.");

            Registry registry = LocateRegistry.getRegistry();
            System.out.println("Current remote objects: ");
            System.out.println(Arrays.toString(registry.list()));
        } catch (MalformedURLException | RemoteException e) {
            System.err.println("Server exception: " + e.toString());
        }
    }

    @Override
    public long rmiSquare(long input) throws RemoteException {
        System.out.println("Called method rmiSquare with parameter=" + input);

        long result = input * input;

        System.out.println("Run method rmiSquare with result=" + result);

        return result;
    }

    @Override
    public long rmiAdd(long val1, long val2) throws RemoteException {
        System.out.println("Called method rmiAdd with parameter=" + val1 + ", " + val2);

        long result = val1 + val2;

        System.out.println("Run method rmiAdd with result=" + result);
        return result;
    }

    @Override
    public String rmiConcat(String in1, String in2) throws RemoteException {
        System.out.println("Called method rmiConcat with parameter=" + in1 + ", " + in2);

        String result = in1 + in2;

        System.out.println("Run method rmiConcat with result=" + result);
        return result;
    }

    @Override
    public String[] rmiSplit(String input) throws RemoteException {
        System.out.println("Called method rmiSplit with parameter=" + input);

        String[] result = new String[2];

        for (int i = 0; i < input.length(); i++) {
            if (Character.isWhitespace(input.charAt(i))) {
                result[0] = input.substring(0, i);
                result[1] = input.substring(i + 1, input.length() - 1);

                System.out.println("Run method rmiSplit with result=" + Arrays.toString(result));
                return result;
            }
        }

        result[0] = input;
        System.out.println("Run method rmiSplit with result=" + Arrays.toString(result));
        return result;
    }

    private final String nameOfRemoteLong = "03long_remote";
    private LongRemoteObj remoteVal = new LongRemoteObj(1);

    @Override
    public void rmiIncrement(long val) throws RemoteException {
        System.out.println("Called method rmiIncrement with parameter=" + val);

        remoteVal = new LongRemoteObj(val);
        try {
            Naming.rebind(this.nameOfRemoteLong, remoteVal);
        } catch (MalformedURLException ex) {
        }
        remoteVal.setValue(remoteVal.getValue() + 1);

        System.out.println("Run method rmiIncrement with result=" + remoteVal.getValue());
    }

    @Override
    public char rmiServernameAtWithException(int i) throws IndexOutOfBoundsException, RemoteException {
        System.out.println("Called method rmiServernameAtWithException with parameter=" + i);

        if (i >= this.servername.length()) {
            System.out.println("IndexOutOfBoundsException in method rmiServernameAtWithException");
            throw new IndexOutOfBoundsException("Index i was too big!");
        }

        char result = this.servername.charAt(i);
        System.out.println("Run method rmiIncrement with result=" + result);
        return result;
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
    public void rmiShutdown() throws RemoteException {
        System.out.println("Called method rmiShutdown.");

        try {
            System.out.println("Initiating shutdown of server " + this.getServername());

            Registry registry = LocateRegistry.getRegistry();
            System.out.println(Arrays.toString(registry.list()));

            System.out.println("Unbind server " + this.getServername() + " from registry.");
            registry.unbind(this.getServername());
            System.out.println("Unbind Long Remote Object " + this.nameOfRemoteLong + " from registry.");
            registry.unbind(this.nameOfRemoteLong);

            System.out.println("Unexport object " + this.nameOfRemoteLong);
            UnicastRemoteObject.unexportObject(this.remoteVal, true);
            System.out.println("Unexport object " + this.getServername());
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NotBoundException | RemoteException e) {
            System.err.println("Server exception: " + e.toString());
        }

    }

}
