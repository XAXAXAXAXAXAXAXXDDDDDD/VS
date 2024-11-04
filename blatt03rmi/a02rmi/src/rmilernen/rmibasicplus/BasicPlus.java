package rmilernen.rmibasicplus;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 *
 * @author kai
 */
public interface BasicPlus extends Remote {

    public int[] rmiQuad(int input) throws RemoteException;

    public String[] rmiTwice(String name) throws RemoteException;

    public LinkedList<String> rmiReaddir(String dirName) throws RemoteException;

    public void rmiShutdown() throws RemoteException;
}
