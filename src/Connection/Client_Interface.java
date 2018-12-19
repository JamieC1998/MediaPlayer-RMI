//Jamie Cotter
//R00154256

package Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client_Interface extends Remote {

    //Called to update our list view when the servers directory changes
    public void updateListOfFiles(String[] fileNames) throws RemoteException;
}
