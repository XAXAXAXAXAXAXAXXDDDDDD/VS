/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmilernen.rmibasic;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author kai bechmann
 */
public class LongRemoteObj extends UnicastRemoteObject implements LongRemoteDataType {

    private long value;

    public LongRemoteObj(long value) throws RemoteException {
        super();
        this.value = value;
    }

    public long getValue() throws RemoteException {
        return this.value;
    }

    public void setValue(long newVal) throws RemoteException {
        this.value = newVal;
    }

    @Override
    public String toString() {
        return Long.toString(this.value);
    }

}
