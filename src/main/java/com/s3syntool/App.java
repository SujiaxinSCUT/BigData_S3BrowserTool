package com.s3syntool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.s3syntool.manager.S3BrowserManager;
import com.s3syntool.manager.S3SynManager;

/**
 * JavaFX App
 */
public class App extends Application {
	
    private static Scene scene;
    
    private static Stage stage;
    
    public static S3SynManager manager;
    
    public static S3BrowserManager bmanager;

    @Override
    public void start(Stage stage) throws IOException {
    	this.stage = stage;
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public static void setRoot(String fxml) throws IOException {
    	Parent parent = loadFXML(fxml);
        scene.setRoot(parent);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    public static void setWidth(double value) {
    	stage.setWidth(value);
    }
    
    public static void setHeight(double value) {
    	stage.setHeight(value);
    }
    
    public static Stage stage() {
    	return stage;
    }

}