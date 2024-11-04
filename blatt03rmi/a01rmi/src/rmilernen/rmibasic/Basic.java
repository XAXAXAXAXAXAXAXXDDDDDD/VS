/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmilernen.rmibasic;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author kai bechmann
 */
public interface Basic extends Remote {

    public long rmiSquare(long input) throws RemoteException;

    public long rmiAdd(long val1, long val2) throws RemoteException;

    public String rmiConcat(String in1, String in2) throws RemoteException;

    public String[] rmiSplit(String input) throws RemoteException;

    public void rmiIncrement(long val) throws RemoteException;

    public void rmiShutdown() throws RemoteException;

    public char rmiServernameAtWithException(int i) throws RemoteException, IndexOutOfBoundsException;

}
