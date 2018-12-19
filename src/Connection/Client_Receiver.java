//Jamie Cotter
//R00154256

package Connection;

import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client_Receiver extends UnicastRemoteObject implements Client_Interface{

    private final long serialVersionUID = 1L;

    private ObservableList<String> ol;

    /**
     * Server calls this when there
     * is a change in the contents of its
     * directory.
     *
     * When received we clear the
     * observable list and add our
     * new list of names to it
     * @param fileNames - Our new list of names
     * @throws RemoteException
     */
    @Override
    public void updateListOfFiles(String[] fileNames) throws RemoteException {
        Platform.runLater(() -> {
            ol.clear();

            for(String each : fileNames)
                ol.add(each);
        });
    }

    public Client_Receiver(ObservableList<String> ol) throws RemoteException {
        this.ol = ol;
    }
}
