/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmilernen.rmibasic;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

/**
 *
 * @author kai bechmann
 */
public class Client {

    private Client() {
    }

    public static void main(String[] args) {

        System.out.println("Diese Loesung wurde von Kai Bechmann erstellt.");

        if (args.length < 2) {
            System.err.println("2 Aufrufparameter erwartet! 1. Name des RMI-Server (String), 2. Shutdown des Servers nach Durchlauf (boolean).");
        }

        String host = args[0];
        boolean shutdownRequested = Boolean.parseBoolean(args[1]);

        try {
            Basic stub = (Basic) Naming.lookup(host);

            System.out.println("Call method rmiSquare with parameter=2");
            long val = stub.rmiSquare(2);
            System.out.println("Result: " + val);

            System.out.println("Call method rmiSquare with parameter=18");
            val = stub.rmiSquare(18);
            System.out.println("Result: " + val);

            System.out.println("Call method rmiAdd with parameters=2, 17");
            val = stub.rmiAdd(2, 17);
            System.out.println("Result: " + val);

            System.out.println("Call method rmiAdd with parameters=1832742834, 17");
            val = stub.rmiAdd(1832742834, 17);
            System.out.println("Result: " + val);

            System.out.println("Call method rmiConcat with parameter=\"Schnitzel\", \"Broetchen\"");
            String res = stub.rmiConcat("Schnitzel", "Broetchen");
            System.out.println("Result: " + res);

            System.out.println("Call method rmiConcat with parameter=\"Wiener\", \"   Toast\"");
            res = stub.rmiConcat("Wiener", "   Toast");
            System.out.println("Result: " + res);

            System.out.println("Call method rmiSplit with parameter=\"Wiener   Wurst\"");
            String[] resArr = stub.rmiSplit("Wiener   Wurst");
            System.out.println("Result: " + Arrays.toString(resArr));

            System.out.println("Call method rmiSplit with parameter=\"Toast mit viel Butter\"");
            resArr = stub.rmiSplit("Toast mit viel Butter");
            System.out.println("Result: " + Arrays.toString(resArr));

            long valueIncrement = 37;
            System.out.println("Call method rmiIncrement with parameter=" + valueIncrement);
            stub.rmiIncrement(valueIncrement);
            LongRemoteDataType longStub = (LongRemoteDataType) Naming.lookup("01long_remote");
            System.out.println("Result: " + longStub.getValue());

            valueIncrement = Long.MAX_VALUE;
            System.out.println("Call method rmiIncrement with parameter=" + valueIncrement);
            stub.rmiIncrement(valueIncrement);
            longStub = (LongRemoteDataType) Naming.lookup("01long_remote");
            System.out.println("Result: " + longStub.getValue());
            
            int rmiServerNameParameter = 1;
            System.out.println("Call method rmiServernameAtWithException with parameter=" + rmiServerNameParameter);
            try {
                var result = stub.rmiServernameAtWithException(rmiServerNameParameter);
                System.out.println("Result: " + result);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Result: " + e);
            }

            rmiServerNameParameter = 100;
            System.out.println("Call method rmiServernameAtWithException with parameter=" + rmiServerNameParameter);
            try {
                var result = stub.rmiServernameAtWithException(rmiServerNameParameter);
                System.out.println("Result: " + result);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Result: " + e);
            }

            if (shutdownRequested) {
                System.out.println("Shutdown of server: ");
                stub.rmiShutdown();
            } else {
                System.out.println("No shutdown of server requested.");
            }
            System.out.println("End of client programm.");
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.err.println("Client exception: " + e.toString());
        }
    }

}
