//Jamie Cotter
//R00154256

package View;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import Connection.Client_Connection;
import Controller.MediaViewController;
import Model.Client;
import Model.FileObservable;
import Model.FileWatcher;

import Model.FileWatcherInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class HomeController{

    @FXML
    ListView<String> lvLocal;
    @FXML
    ListView<String> lvServer;
    @FXML
    Button play;
    @FXML 
    Button download;

    public static HomeController homeController = null;

    private String localDirectory = "C:\\Users\\Admin\\Downloads\\RMI_Client\\src\\File_Folder";

    ObservableList<String> observableListLocal;
    ObservableList<String> observableListServer;

    private Client_Connection clientCon;

    private final int PORT = 11001;
    private final String HOST_NAME = "localhost";

    @FXML
    public void initialize(){

        initialiseListViews();
        startConnection();

    }

    private void initialiseListViews(){

        homeController = this;

        FileObservable fileObservableLocal = new FileObservable(localDirectory);

        observableListLocal = FXCollections.observableArrayList();

        Client clientObservableLocal = new Client(observableListLocal);

        fileObservableLocal.addObserver(clientObservableLocal);

        new Thread(fileObservableLocal).start();

        observableListServer = FXCollections.observableArrayList();

        lvLocal.setItems(observableListLocal);

        lvServer.setItems(observableListServer);

    }

    /**
     * Creates our connection class
     * Which will maintain the connection
     * with the server
     */
    private void startConnection(){

        try{ clientCon = new Client_Connection(PORT, HOST_NAME, observableListServer, localDirectory); }
        catch (RemoteException e){ e.printStackTrace(); }
        catch (NotBoundException e){ e.printStackTrace(); }
    }

    /**
     * Takes in the name of the file
     * we want from the list view
     * and passes it to the client con
     * class to download it from
     * the server
     * @param e
     */
    @FXML
    public void Download(ActionEvent e){
        String s = lvServer.getSelectionModel().getSelectedItem();
        Runnable r = () -> clientCon.Download(s);

        new Thread(r).start();
    }

    @FXML
    public void PlayFile(ActionEvent e){
        FileWatcher fileWatcherLocal = new FileWatcher(localDirectory);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MediaView.fxml"));

        Parent root = null;

        try { root = loader.load(); }
        catch (IOException d) { d.printStackTrace(); }

        MediaViewController returnObject = loader.getController();

        String item = lvLocal.getSelectionModel().getSelectedItem();

        File file = (((FileWatcher) fileWatcherLocal).ReturnFileReq(item));

        System.out.println(file.getName());

        if(returnObject == null){
            System.out.println("NULL");
        }

        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        returnObject.setFile(file);


    }

    /**
     * When the upload button is clicked
     * We take the file selected
     * and convert it to a byte stream
     * and send it off to client connection
     * class
     * To upload to the server
     * @param e
     */
    @FXML
    public void UploadFile(ActionEvent e){
        String s = lvLocal.getSelectionModel().getSelectedItem();

        FileWatcherInterface fileWatch = new FileWatcher(localDirectory);

        byte[] contents = null;
        try{ contents= Files.readAllBytes(fileWatch.ReturnFileReq(s).toPath()); }
        catch (IOException d) { d.printStackTrace(); }

        byte[] finalContents = contents;
        Runnable r = () -> clientCon.Upload(finalContents, s);

        new Thread(r).start();
    }


}