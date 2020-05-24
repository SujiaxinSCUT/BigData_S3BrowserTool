package com.s3syntool.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.amazonaws.services.s3.model.Bucket;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.s3syntool.App;
import com.s3syntool.manager.TaskManager;
import com.s3syntool.utils.Logger;
import com.sysyntool.client.Configuration;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class SecondaryController  implements Initializable{

	@FXML
	private Label logo;
	
	@FXML
	private JFXTextField path_field;
	
	@FXML
	private JFXTextField bucketName_field;
	
	@FXML
	private JFXComboBox<String>	bucket_cb;
	
	@FXML
	private JFXButton create_btn;
	
	@FXML
	private JFXButton syn_btn;
	
	@FXML
	private JFXButton ud_btn;
	
	@FXML
	private TextArea loggerText;
	
	@FXML
	private VBox taskList;
	
	private ObservableList<String> observable_list = FXCollections.observableArrayList();
	
	private Set<String> bucketNameSet = new HashSet<>();
	
	private Logger logger;
	
	private TaskManager tm;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		App.setHeight(480);
		App.setWidth(620);
		App.controller = this;
		logo.setGraphic(new ImageView(new Image(App.class.getResource("picture/Logo.png").toString())));
		
		loggerText.setFont(Font.font(10));
		
		bucket_cb.setItems(observable_list);
		
		tm = new TaskManager(this);
		
		logger = new Logger(this);
		
		getBucketList();
//		桶名文本框监听文本改变
		bucketName_field.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue==null||newValue.length()==0) {
					syn_btn.setDisable(true);
				}else create_btn.setDisable(false);
			}
		});
//	复选列表框选中监听	
		bucket_cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String path = path_field.getText();
				if(path!=null&&path.length()>0) {
					syn_btn.setDisable(false);
				}
				App.manager.setSynBucketName(newValue);
				ud_btn.setDisable(true);
			}
			
		});
		
		path_field.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue==null||newValue.length()==0) {
					syn_btn.setDisable(true);
				}else {
					App.manager.setSynDir(newValue);
					String selectItem = bucket_cb.getSelectionModel().getSelectedItem();
					if(selectItem==null||selectItem.length()==0)
						syn_btn.setDisable(true);
					else syn_btn.setDisable(false);
				}
				ud_btn.setDisable(true);
			}
		});
		
		App.stage().centerOnScreen();
		
		new Thread(new DetectFileTask()).start();
	}
	
	public void addTask(Node task) {
		taskList.getChildren().add(task);
	}
	
	public void synFile() {
		tm.clear();
		logger.info("开始同步目录"+App.manager.getSynDir()+"与桶"+App.manager.getSynBucketName());
		App.manager.synchronizeFile(tm, logger);
		ud_btn.setDisable(false);
	}
	
	public void updateAndDelete() {
		logger.info("开始上传与删除文件");
		tm.startSyn(App.manager,logger);
		logger.info("同步结束");
		ud_btn.setDisable(true);
	}
	
	public void getBucketList() {
		List<Bucket> buckets = App.bmanager.getBuckets();
		for(Bucket b:buckets) {
			observable_list.add(b.getName());
			bucketNameSet.add(b.getName());
		}
	}
	
	public void setBucketList(List<Bucket> buckets) {
		for(Bucket b:buckets) {
			observable_list.add(b.getName());
			bucketNameSet.add(b.getName());
		}
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
	
	public void createBucket() {
		String bucketName = bucketName_field.getText();
		if(!bucketNameSet.contains(bucketName)) {
			syn_btn.setDisable(true);
			try {
				App.bmanager.createBucket(bucketName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				syn_btn.setDisable(false);
			}
			syn_btn.setDisable(false);
			bucketNameSet.add(bucketName);
			observable_list.add(bucketName);
			bucketName_field.clear();
			bucket_cb.getSelectionModel().select(bucketName);
		}
	}
	
	public void logInfo(String mes) {
		loggerText.appendText(mes+"\r\n");
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
	
	public class DetectFileTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Configuration info = App.manager.getConfig();
			String keyStr = info.getAccessKey()+info.getSecretKey();		
			File dir = new File(System.getProperty("user.dir")+File.separator+keyStr.hashCode());
			if(dir.exists()&&dir.isDirectory()) {
				File[] files = dir.listFiles();
				if(files.length>0) {
					Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
					        //更新JavaFX组件的代码
					    	Stage infoStage = new Stage();
					    	infoStage.setAlwaysOnTop(true);
							infoStage.initOwner(App.stage());
							infoStage.initStyle(StageStyle.TRANSPARENT);
							FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Info.fxml"));
							try {
								Scene scene = new Scene(fxmlLoader.load());
								InfoController controller = fxmlLoader.getController();
								controller.setFiles(files);
								controller.setStage(infoStage);
								controller.setTm(tm);
								infoStage.setScene(scene);
								infoStage.show();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					    }
					});
				}
			}
		}
		
	}
	
}