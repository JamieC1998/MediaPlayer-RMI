//Jamie Cotter
//R00154256

package Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client_Interface extends Remote {

    //Passes in a list of files to the client to update its listview
    public void updateListOfFiles(String[] fileNames) throws RemoteException;

}
