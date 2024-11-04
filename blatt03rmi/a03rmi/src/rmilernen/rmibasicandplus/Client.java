package rmilernen.rmibasicandplus;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedList;
import rmilernen.rmibasic.Basic;
import rmilernen.rmibasic.LongRemoteDataType;
import rmilernen.rmibasicplus.BasicPlus;

/**
 *
 * @author kai bechmann
 */
public class Client {

    public static void main(String[] args) {

        System.out.println("Diese Loesung wurde von Kai Bechmann erstellt.");

        if (args.length < 1) {
            System.err.println("1 Aufrufparameter erwartet! 1. Parameter: Shutdown des Servers nach Durchlauf (boolean).");
        }

        boolean shutdownRequested = Boolean.parseBoolean(args[0]);

        try {
            Basic stub = (Basic) Naming.lookup("03Basic");

            System.out.println("Call method rmiSquare with parameter=2, Remote Objekt: " + stub);
            long val = stub.rmiSquare(2);
            System.out.println("Result: " + val);

            System.out.println("Call method rmiAdd with parameters=2, 17, Remote Objekt: " + stub);
            val = stub.rmiAdd(2, 17);
            System.out.println("Result: " + val);

            System.out.println("Call method rmiConcat with parameter=\"Wiener\", \"   Toast\", Remote Objekt: " + stub);
            String res = stub.rmiConcat("Wiener", "   Toast");
            System.out.println("Result: " + res);

            System.out.println("Call method rmiSplit with parameter=\"Wiener   Wurst\", Remote Objekt: " + stub);
            String[] resArr = stub.rmiSplit("Wiener   Wurst");
            System.out.println("Result: " + Arrays.toString(resArr));

            long valueIncrement = 37;
            System.out.println("Call method rmiIncrement with parameter=" + valueIncrement + ",  Remote Objekt: " + stub);
            stub.rmiIncrement(valueIncrement);
            LongRemoteDataType longStub = (LongRemoteDataType) Naming.lookup("01long_remote");
            System.out.println("Result: " + longStub.getValue());

            int rmiServerNameParameter = 1;
            System.out.println("Call method rmiServernameAtWithException with parameter=" + rmiServerNameParameter + ", Remote Objekt: " + stub);
            try {
                var result = stub.rmiServernameAtWithException(rmiServerNameParameter);
                System.out.println("Result: " + result);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Result: " + e);
            }

            if (shutdownRequested) {
                System.out.println("Remote Objekt: " + stub);
                System.out.println("Shutdown of server.");
                stub.rmiShutdown();
            } else {
                System.out.println("No shutdown of server requested.");
            }

            BasicPlus basicPlusStub = (BasicPlus) Naming.lookup("03BasicPlus");
            int quadParameter = 3;
            int[] resultQuad = basicPlusStub.rmiQuad(quadParameter);
            System.out.println("Remote Objekt: " + basicPlusStub);
            System.out.println("Response rmiQuad for param=" + quadParameter + ": " + Arrays.toString(resultQuad));

            String[] resultTwice = basicPlusStub.rmiTwice("Test Input");
            System.out.println("Remote Objekt: " + basicPlusStub);
            System.out.println("Response rmiTwice for param=Test Input: " + Arrays.toString(resultTwice));

            String readDirParameter = "abc";
            LinkedList<String> resultReaddir = basicPlusStub.rmiReaddir(readDirParameter);
            System.out.println("Remote Objekt: " + basicPlusStub);
            System.out.println("Response rmiReaddir for directory=" + readDirParameter + ": " + resultReaddir.toString());

            if (shutdownRequested) {
                System.out.println("Remote Objekt: " + basicPlusStub);
                System.out.println("Shutdown of server.");
                basicPlusStub.rmiShutdown();
            } else {
                System.out.println("No shutdown of server requested.");
            }

            Basic basicAndPlusStubBasic = (Basic) Naming.lookup("03BasicAndPlus");

            System.out.println("Call method rmiSquare with parameter=4, Remote Objekt: " + basicAndPlusStubBasic);
            val = basicAndPlusStubBasic.rmiSquare(4);
            System.out.println("Result: " + val);

            System.out.println("Call method rmiAdd with parameters=3, 17, Remote Objekt: " + basicAndPlusStubBasic);
            val = basicAndPlusStubBasic.rmiAdd(3, 17);
            System.out.println("Result: " + val);

            System.out.println("Call method rmiConcat with parameter=\"Wienerle mit\", \"   Toast\", Remote Objekt: " + basicAndPlusStubBasic);
            res = basicAndPlusStubBasic.rmiConcat("Wienerle mit", "   Toast");
            System.out.println("Result: " + res);

            System.out.println("Call method rmiSplit with parameter=\"Wienerle mit  Wurst\", Remote Objekt: " + basicAndPlusStubBasic);
            resArr = basicAndPlusStubBasic.rmiSplit("Wienerle mit   Wurst");
            System.out.println("Result: " + Arrays.toString(resArr));

            valueIncrement = 800;
            System.out.println("Call method rmiIncrement with parameter=" + valueIncrement + ", Remote Objekt: " + basicAndPlusStubBasic);
            basicAndPlusStubBasic.rmiIncrement(valueIncrement);
            longStub = (LongRemoteDataType) Naming.lookup("03long_remote");
            System.out.println("Result: " + longStub.getValue());

            rmiServerNameParameter = 70;
            System.out.println("Call method rmiServernameAtWithException with parameter=" + rmiServerNameParameter + ", Remote Objekt: " + basicAndPlusStubBasic);
            try {
                var result = basicAndPlusStubBasic.rmiServernameAtWithException(rmiServerNameParameter);
                System.out.println("Result: " + result);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Result: " + e);
            }

            BasicPlus basicAndPlusStubBasicPlus = (BasicPlus) Naming.lookup("03BasicAndPlus");
            quadParameter = 7;
            resultQuad = basicAndPlusStubBasicPlus.rmiQuad(quadParameter);
            System.out.println("Remote Objekt: " + basicAndPlusStubBasicPlus);
            System.out.println("Response rmiQuad for param=" + quadParameter + ": " + Arrays.toString(resultQuad));

            resultTwice = basicAndPlusStubBasicPlus.rmiTwice("Das ist ein Test");
            System.out.println("Remote Objekt: " + basicAndPlusStubBasicPlus);
            System.out.println("Response rmiTwice for param=Das ist ein Test: " + Arrays.toString(resultTwice));

            readDirParameter = "Ein neuer Wert";
            resultReaddir = basicAndPlusStubBasicPlus.rmiReaddir(readDirParameter);
            System.out.println("Remote Objekt: " + basicAndPlusStubBasicPlus);
            System.out.println("Response rmiReaddir for directory=" + readDirParameter + ": " + resultReaddir.toString());

            if (shutdownRequested) {
                basicAndPlusStubBasic = (Basic) Naming.lookup("03BasicAndPlus");
                System.out.println("Remote Objekt: " + basicAndPlusStubBasic);
                System.out.println("Shutdown of server.");
                basicAndPlusStubBasic.rmiShutdown();
            } else {
                System.out.println("No shutdown of server requested.");
            }
            System.out.println("End of client programm.");
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.err.println("Client exception: " + e.toString());
        }
    }

}
