//Jamie Cotter
//R00154256

package Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server_Interface extends Remote {

    //Returns a string with hello
    public String printHello() throws RemoteException;

    //Adds the client to servers client list
    public void addClient(Client_Interface client) throws RemoteException;

    //Returns a list of file names
    public String[] requestFileNames() throws RemoteException;

    //Returns a byte stream matching the name passed in
    public byte[] Download(String name) throws RemoteException;

    //Receives a byte stream and name to write
    public void Upload(byte[] contents, String name) throws RemoteException;

}
