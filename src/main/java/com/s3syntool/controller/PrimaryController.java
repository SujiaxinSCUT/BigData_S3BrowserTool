package com.s3syntool.controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.amazonaws.services.s3.model.Bucket;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.s3syntool.App;
import com.s3syntool.client.Configuration;
import com.s3syntool.manager.S3BrowserManager;
import com.s3syntool.manager.S3SynManager;
import com.s3syntool.utils.FileTool;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PrimaryController implements Initializable{

	@FXML
	private Label logo;
	
	@FXML
	private JFXTextField accessKey_field;
	
	@FXML
	private JFXTextField secretKey_field;
	
	@FXML
	private JFXTextField serviceEndpoint_field;
	
	@FXML
	private JFXButton connect_btn;
	
	@FXML
	private VBox container;
	
	private Text mesText;
	
	private JFXSpinner spinner;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		logo.setGraphic(new ImageView(new Image(App.class.getResource("picture/Logo.png").toString())));
		mesText = new Text();
		mesText.setFill(Color.RED);
		mesText.setFont(Font.font(12));
		spinner = new JFXSpinner();
		spinner.setPrefWidth(15);
		spinner.setPrefHeight(15);
		detectLoginCache();
	}
	
	public void detectLoginCache() {
		File cacheFile = new File(System.getProperty("user.dir")+File.separator+"logincache.dat");
		if(cacheFile.exists()) {
			Configuration config = (Configuration) FileTool.readObjectFromFile(cacheFile);
			if(config!=null) {
				accessKey_field.setText(config.getAccessKey());
				secretKey_field.setText(config.getSecretKey());
				serviceEndpoint_field.setText(config.getServiceEndpoint());
			}
		}
	}
	
	public void message(String mes) {
		mesText.setText("*"+mes);
		container.getChildren().add(4, mesText);
	}
	
	public void clearContainer() {
		if(container.getChildren().size()==6) {
			container.getChildren().remove(4);
		}
	}
	
	public void addSpinner() {
		container.getChildren().add(4, spinner);
	}
	
	public void changeStatus() {
		connect_btn.setDisable(!connect_btn.isDisable());
		accessKey_field.setDisable(!accessKey_field.isDisable());
		secretKey_field.setDisable(!secretKey_field.isDisable());
		serviceEndpoint_field.setDisable(!serviceEndpoint_field.isDisable());
	}
	
	public void connect() {
		clearContainer();
		String access_key = accessKey_field.getText();
		if(access_key==null||access_key.length()==0) {
			message("access_key不能为空");
			return;
		}
		String secret_key = secretKey_field.getText();
		if(secret_key==null||secret_key.length()==0) {
			message("secret_key不能为空");
			return;
		}
		String serviceEndpoint = serviceEndpoint_field.getText();
		if(serviceEndpoint==null||serviceEndpoint.length()==0) {
			message("serviceEndpoint不能为空");
			return;
		}
    	changeStatus();
    	addSpinner();
		Configuration config = new Configuration(access_key, secret_key, serviceEndpoint, "");
		final LoginTask task = new LoginTask(config);
		Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
        task.runningProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> ov, Boolean wasRunning, Boolean isRunning) {
                if (!isRunning) {
                	changeStatus();
                	if(!task.succeed) {
            			clearContainer();
            			message("连接失败");
                	}
                }
            }
        });
	}
}

class LoginTask extends Task<List<Bucket>>{
	
	private Configuration config;
	
	public boolean succeed = false;
	
	public LoginTask(Configuration config) {
		this.config = config;
	}

	@Override
	protected List<Bucket> call() throws Exception {
		// TODO Auto-generated method stub
        		try {
        			App.manager = new S3SynManager(config);
        			App.bmanager = new S3BrowserManager(config);
        			App.bmanager.getBuckets();
	        		App.setRoot("secondary");
	        		File cacheFile = new File(System.getProperty("user.dir")+File.separator+"logincache.dat");
	        		FileTool.writeObjectToFile(config, cacheFile);
        		} catch (Exception e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
		return null;
	}
	
}

