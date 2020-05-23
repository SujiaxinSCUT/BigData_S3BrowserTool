package com.s3syntool.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import com.s3syntool.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.DirectoryChooserBuilder;

public class SecondaryController  implements Initializable{

	@FXML
	private Label logo;
	
	@FXML
	private JFXTextField path_field;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		logo.setGraphic(new ImageView(new Image(App.class.getResource("picture/Logo.png").toString())));
	}
	
	public void browse() {
        DirectoryChooser chooser = new DirectoryChooser();
        File chosenDir = chooser.showDialog(App.stage());
        if (chosenDir != null) {
          String path = chosenDir.getAbsolutePath();
          path_field.setText(path);
          App.manager.setSynDir(path);
        }
	}
	
	public void logout() {
		try {
			App.setRoot("primary");
			App.setHeight(400);
			App.setWidth(400);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}