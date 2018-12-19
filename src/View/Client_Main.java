package View;

import Connection.Client_Connection;
import Model.FileWatcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static javafx.application.Application.launch;

public class Client_Main extends Application {
    public static void main(String[] args){ launch(args); }

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));

        Parent root = null;

        try { root = loader.load(); }
        catch (IOException e) { e.printStackTrace(); }

        primaryStage.setTitle("Media Player");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
