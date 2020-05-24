package com.s3syntool.controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.s3syntool.App;
import com.s3syntool.manager.TaskManager;
import com.s3syntool.utils.FileTool;
import com.s3syntool.utils.MultiPartUploadInfo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class InfoController implements Initializable{

	@FXML
	private Label logo;
	
	private File[] files;
	
	private TaskManager tm;
	
	private Stage stage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		logo.setGraphic(new ImageView(new Image(App.class.getResource("picture/info.png").toString())));
	}
	
	public void setFiles(File[] files) {
		this.files = files;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setTm(TaskManager tm) {
		this.tm = tm;
	}
	
	public void handleYes() {
		for(File file:files) {
			MultiPartUploadInfo info = (MultiPartUploadInfo) FileTool.readObjectFromFile(file);
			tm.submitReUploadFile(info, App.manager);
		}
		this.stage.close();
	}
	
	public void handleNo() {
		for(File file:files) {
			file.delete();
		}
		this.stage.close();
	}

}
