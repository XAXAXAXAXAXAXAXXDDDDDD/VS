/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rmilernen.rmibasic;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author kai bechmann
 */
public interface LongRemoteDataType extends Remote {

    public long getValue() throws RemoteException;

    public void setValue(long newVal) throws RemoteException;
}
