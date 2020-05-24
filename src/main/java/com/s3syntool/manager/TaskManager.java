package com.s3syntool.manager;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXSpinner;
import com.s3syntool.App;
import com.s3syntool.controller.SecondaryController;
import com.s3syntool.utils.Logger;
import com.s3syntool.utils.MultiPartUploadInfo;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class TaskManager {
	
	private ExecutorService pool;
	
	private int poolSize = 5;
	
	private Queue<String> uploadList;
	
	private Map<String,File> uploadMap;
	
	private Queue<String> deleteList;
	
	private SecondaryController controller;
	
	public TaskManager(SecondaryController controller) {
		pool = Executors.newFixedThreadPool(poolSize);
		uploadList = new LinkedList<>();
		uploadMap = new HashMap<>();
		deleteList = new LinkedList<>();
		this.controller = controller;
	}
	
	public void submitReUploadFile(MultiPartUploadInfo info,S3SynManager manager) {
		ReUploadTask task = new ReUploadTask(manager, info.getKeyName(), info.getFile(), controller, info);
		pool.submit(task);
	}
	
	public void submitUploadFile(String keyName,File file) {
		uploadMap.put(keyName, file);
		uploadList.add(keyName);
	}
	
	public void submitDeleteFile(String keyName) {
		deleteList.add(keyName);
	}
	
	public void startSyn(S3SynManager manager,Logger logger) {		
		while(!uploadList.isEmpty()) {
			String keyName = uploadList.poll();
			File file = uploadMap.get(keyName);
			if(file.length()>=S3SynManager.MAX_SIZE) {
				logger.info(keyName+"大于限定文件大小，加入大文件上传任务栏");
				UploadTask task = new UploadTask(manager, keyName, file, controller);
				pool.submit(task);
			}else {
				logger.info("开始上传"+keyName);
				manager.uploadSmallFile(keyName, file);
				logger.info(keyName+"上传成功");
			}
		}
		while(!deleteList.isEmpty()) {
			String keyName = deleteList.poll();
			logger.info("开始删除"+keyName);
			manager.deleteFile(keyName);
			logger.info(keyName+"删除成功");
		}
	}
	
	public void shutDown() {
		pool.shutdownNow();
	}

	public Queue<String> getUploadList() {
		return uploadList;
	}

	public void setUploadList(Queue<String> uploadList) {
		this.uploadList = uploadList;
	}

	public Queue<String> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(Queue<String> deleteList) {
		this.deleteList = deleteList;
	}
	
	public void clear() {
		this.deleteList.clear();
		this.uploadList.clear();
		this.uploadMap.clear();
	}
	
	public class UploadTask implements Runnable{
		
		protected S3SynManager manager;
		
		protected String keyName;
		
		protected File file;
		
		private SecondaryController controller;
		
		protected HBox taskLabel;

		
		public UploadTask(S3SynManager manager,String keyName,File file,SecondaryController controller) {
			this.manager = manager;
			this.keyName = keyName;
			this.file = file;
			this.controller = controller;
		}
		
		public void update(int partNum) {
	        Platform.runLater(new Runnable() {
	            @Override public void run() {
	            	JFXSpinner spinner = new JFXSpinner();
	    			spinner.setMaxSize(15, 15);
	    			ImageView imageView = new ImageView(new Image(App.class.getResource("picture/success.png").toString()));
	    			imageView.setFitWidth(15);
	    			imageView.setFitHeight(15);
	    			
	            	taskLabel.getChildren().remove(2);
	            	taskLabel.getChildren()
	            		.add(imageView);
	            	taskLabel = new HBox();
	    			taskLabel.setSpacing(10);
	            	Label fileName = new Label(keyName);
	    			fileName.setPrefWidth(120);
	    			fileName.setFont(Font.font(12));
	    			Label num = new Label(String.valueOf(partNum));
	    			num.setFont(Font.font(12));
	    			num.setPrefWidth(41);
	    			taskLabel.getChildren().add(fileName);
	    			taskLabel.getChildren().add(num);
	    			taskLabel.getChildren().add(spinner);
	    			controller.addTask(taskLabel);
	            }
	        });
		}
		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			taskLabel = new HBox();
			taskLabel.setSpacing(10);
			Label fileName = new Label(keyName);
			fileName.setPrefWidth(120);
			fileName.setFont(Font.font(12));
			Label num = new Label("1");
			num.setFont(Font.font(12));
			num.setPrefWidth(41);
			taskLabel.getChildren().add(fileName);
			taskLabel.getChildren().add(num);
			JFXSpinner spinner = new JFXSpinner();
			spinner.setPrefHeight(15);
			spinner.setPrefWidth(15);
			taskLabel.getChildren().add(spinner);
	        Platform.runLater(new Runnable() {
	            @Override public void run() {
	            	controller.addTask(taskLabel);
	            }
	        });
	        MultiPartUploadInfo info = new MultiPartUploadInfo();
	        info.setUt(this);
			manager.uploadLargeFile(keyName, file,info);
		}
		
	}
	
	public class ReUploadTask extends UploadTask{

		private MultiPartUploadInfo info;
		
		public ReUploadTask(S3SynManager manager, String keyName, File file, SecondaryController controller,MultiPartUploadInfo info) {
			super(manager, keyName, file, controller);
			// TODO Auto-generated constructor stub
			this.info = info;
		}
		
		@Override
		public void run() {
			taskLabel = new HBox();
			taskLabel.setSpacing(10);
			Label fileName = new Label(keyName);
			fileName.setPrefWidth(120);
			fileName.setFont(Font.font(12));
			Label num = new Label(String.valueOf(info.getUploaded_partNumber()+1));
			num.setFont(Font.font(12));
			num.setPrefWidth(41);
			taskLabel.getChildren().add(fileName);
			taskLabel.getChildren().add(num);
			JFXSpinner spinner = new JFXSpinner();
			spinner.setPrefHeight(15);
			spinner.setPrefWidth(15);
			taskLabel.getChildren().add(spinner);
	        Platform.runLater(new Runnable() {
	            @Override public void run() {
	            	controller.addTask(taskLabel);
	            }
	        });
	        info.setUt(this);
			manager.reupload(info);
		}
		
	}
}


