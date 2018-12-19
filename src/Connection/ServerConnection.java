//Jamie Cotter
//R00154256

package Connection;

import Model.FileObservable;
import Model.FileWatcher;
import Model.FileWatcherInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

public class ServerConnection extends UnicastRemoteObject implements Server_Interface, Observer {

    //The Port we are hosting on
    private int port;

    private final long serialVersionUID = 1L;

    //Directory of the folder we're watching
    private String directory;

    //Registry we used to bind interfaces
    private Registry registry;

    //Holds our list of clients
    private HashSet<Client_Interface> clients = new HashSet<Client_Interface>();

    //Used to observe folder
    private FileObservable fileObservable;

    private FileWatcherInterface fileWatch;

    /**
     * The constructor
     * sets our globals
     * and create our folder obserable
     * @param path - The path of the folder
     * @param port - Port we are hosting on
     * @throws RemoteException
     */
    public ServerConnection(String path, int port) throws RemoteException {
        super();

        this.directory = path;
        this.port = port;

        creatingObservable();

        fileWatch = new FileWatcher(directory);


    }

    /**
     * Creates our observable object
     * that watches our folder for
     * changes and starts it
     * on a seperate thread
     */
    private void creatingObservable(){
        fileObservable = new FileObservable(directory);
        fileObservable.addObserver(this);

        new Thread(fileObservable).start();
    }

    /**
     * This function starts the
     * registry on the port
     * passed into the constructor
     * @throws RemoteException
     */
    private void StartRegistry() throws RemoteException{
        registry = LocateRegistry.createRegistry(port);
    }

    /**
     * Registering our class
     * with the Registry
     * @param name
     * @param remote
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    private void RegisterObject(String name, Remote remote) throws RemoteException, AlreadyBoundException {
        registry.bind(name, remote);
    }

    /**
     * This function starts our connection
     * when called
     * It calls the startRegistry
     * and the registerObject
     * functions
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    public void Start() throws RemoteException, AlreadyBoundException{
        StartRegistry();
        RegisterObject("rmiserver", this);

    }

    /**
     * This function is called
     * by the client
     * so that the server
     * can add it's interface
     * to it's list of clients
     * @param client
     * @throws RemoteException
     */
    @Override
    public void addClient(Client_Interface client) throws RemoteException{
        clients.add(client);
        System.out.println("Client added");
    }

    @Override
    public String printHello() throws RemoteException{
        return "Hello";
    }

    /**
     * This function is invoked
     * by our observable when
     * it notifies its observers
     * of a change to its directory
     *
     * We print out the name of each file
     * then send the list of names to
     * each client in our client pool
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        String[] fileNames = (String[]) arg;
        System.out.println("Update: ");

        for(String each : fileNames)
            System.out.println(each);

        for(Client_Interface each : clients) {
            try{ each.updateListOfFiles(fileNames);}
            catch (RemoteException e){ e.printStackTrace(); }
        }
    }

    /**
     * Returns a list of files
     * when called
     * @return Returns an array of strings
     * @throws RemoteException
     */
    public String[] requestFileNames() throws RemoteException{
        return fileWatch.ReturnFileNames();
    }

    /**
     * This function when called
     * receives a byte stream from the client
     * and a name of a file
     *
     * then it creates a new file
     * and writes the byte stream to it
     * @param contents
     * @param name
     * @throws RemoteException
     */
    @Override
    public void Upload(byte[] contents, String name) throws RemoteException {
        File file = new File(directory + "\\" + name);

        try { Files.write(file.toPath(), contents); }
        catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Receives a file name from the client
     * Uses it's file watcher to retrieve the file
     *
     * Then converts it to a byte stream and
     * returns it
     * @param name
     * @return - Byte stream of a file
     * @throws RemoteException
     */
    @Override
    public byte[] Download(String name) throws RemoteException {
        byte[] contents = null;

        try{ contents = Files.readAllBytes(fileWatch.ReturnFileReq(name).toPath()); }
        catch (IOException e) { e.printStackTrace(); }

        return contents;
    }
}
