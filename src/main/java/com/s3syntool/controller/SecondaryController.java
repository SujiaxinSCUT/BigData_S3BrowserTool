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
import com.s3syntool.client.Configuration;
import com.s3syntool.manager.TaskManager;
import com.s3syntool.utils.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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

		bucketName_field.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(newValue==null||newValue.length()==0) {
					syn_btn.setDisable(true);
				}else create_btn.setDisable(false);
			}
		});

		bucket_cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String path = path_field.getText();
				tm.clear();
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
				tm.clear();
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
		
//		App.stage().centerOnScreen();
		
		new Thread(new DetectFileTask()).start();;
	}
	
	public void addTask(Node task) {
		taskList.getChildren().add(task);
	}
	
	public void synFile() {
		tm.clear();
		logger.info("开始同步目录"+App.manager.getSynDir()+"与桶"+App.manager.getSynBucketName());
		try {
			App.manager.synchronizeFile(tm, logger);
			ud_btn.setDisable(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
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
			create_btn.setDisable(true);
			try {
				App.bmanager.createBucket(bucketName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("创建失败");
			}
			syn_btn.setDisable(false);
			bucketNameSet.add(bucketName);
			observable_list.add(bucketName);
			bucketName_field.clear();
			bucket_cb.getSelectionModel().select(bucketName);
		}else {
			logger.info("该桶已存在");
		}
	}
	
	public void logInfo(String mes) {
		loggerText.appendText(mes+"\r\n");
	}
	
	public class DetectFileTask extends Task<File>{

		@Override
		protected File call() throws Exception {
			// TODO Auto-generated method stub
			Configuration info = App.manager.getConfig();
			String keyStr = info.getAccessKey()+info.getSecretKey();		
			File dir = new File(System.getProperty("user.dir")+File.separator+keyStr.hashCode());
			if(dir.exists()&&dir.isDirectory()) {
				final File[] files = dir.listFiles();
				if(files.length>0) {
					Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
					        //更新JavaFX的主线程的代码放在此处
					    	try {
								Stage infoStage = new Stage();
								infoStage.initOwner(App.stage());
								infoStage.initStyle(StageStyle.TRANSPARENT);
								FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Info.fxml"));
								Scene scene = new Scene(fxmlLoader.load());
								InfoController controller = fxmlLoader.getController();
								controller.setFiles(files);
								controller.setStage(infoStage);
								controller.setTm(tm);
								infoStage.setScene(scene);
								infoStage.show();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					    }
					});
				}
			}
			return null;
		}
		
	}
	
}