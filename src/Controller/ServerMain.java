//Jamie Cotter
//R00154256

package Controller;

import Connection.ServerConnection;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

/*
    Main Class, starts up the server and
    passes in the path to the directory
    and the port to open the connection on
 */
public class ServerMain {

    private static final String FOLDER_PATH = "C:\\Users\\Admin\\Downloads\\RMI_Server\\src\\FileFolder";

    private static final int PORT = 11001;

    public static void main(String[] args){
        ServerConnection server;
        try{
            //Creating our connection object
            server = new ServerConnection(FOLDER_PATH, PORT);
            server.Start();
        }
        catch (RemoteException e) { e.printStackTrace(); }
        catch (AlreadyBoundException e){ e.printStackTrace(); }



    }
}
