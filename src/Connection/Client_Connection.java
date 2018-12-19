//Jamie Cotter
//R00154256

package Connection;

import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This Class starts
 * the connection with the
 * server
 * And retrieves the registry
 */
public class Client_Connection {

    //Our Server and Client objects
    private Client_Interface client;
    private Server_Interface server;

    private Registry registry;

    private int port;
    private String hostName;

    private ObservableList<String> ol;

    private String directory;

    /**
     * The constructor starts
     * the connection with the
     * server and adds the
     * client to its list
     * of clients
     * @param port - The Port the Servers operating on
     * @param hostName - The IP it is operating on
     * @param ol - The observable list held in the ListView
     * @param directory - The directory of the folder
     * @throws RemoteException
     * @throws NotBoundException
     */
    public Client_Connection(int port, String hostName, ObservableList<String> ol, String directory) throws RemoteException, NotBoundException {

        //Setting our global variables
        this.port = port;
        this.hostName = hostName;
        this.ol = ol;

        //Creating the connection
        registry = LocateRegistry.getRegistry(hostName, port);

        //Retrieving our server interface
        Remote remote = registry.lookup("rmiserver");

        if (remote instanceof Server_Interface) {
            server = (Server_Interface) remote;
            client = new Client_Receiver(ol);
        }

        //Adding client to server
        server.addClient(client);

        System.out.println(server.printHello());

        this.directory = directory;

        //Forcing server to update our listview
        firstUpdate();
    }

    /**
     * This function receives a file
     * from our gui controller class,
     *
     * Passes the name off to the server
     * which will return a byte stream
     * matching that name
     *
     * Then create a new file in our folder
     * with that name and write the byte
     * stream to it
     * @param name - Name of the file
     */
    public void Download(String name){
        try {
            byte[] contents = server.Download(name);

            File file = new File(directory + "\\" + name);

            Files.write(file.toPath(), contents);

        }
        catch (IOException e){ e.printStackTrace(); }
    }

    /**
     * Function that we call to initially update
     * Our list view when we connect
     */
    private void firstUpdate(){
        Platform.runLater(() -> {
            String[] list = null;
            try{ list = server.requestFileNames(); }
            catch(RemoteException e){ e.printStackTrace(); }

            for(String each : list)
                ol.add(each);

        });

    }

    /**
     * Invokes the upload function of the
     * server and sends it a byte stream
     * and a file name
     * @param contents - Byte stream of a file
     * @param name - Name of a file
     */
    public void Upload(byte[] contents, String name){
        try{ server.Upload(contents, name); }
        catch (RemoteException e){ e.printStackTrace(); }
    }

}
