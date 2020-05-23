package com.s3syntool.manager;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager {
	
	private ExecutorService pool;
	
	private int poolSize = 10;
	
	private Queue<String> uploadList;
	
	private Map<String,File> uploadMap;
	
	private Queue<String> deleteList;
	
	public TaskManager() {
		pool = Executors.newFixedThreadPool(poolSize);
		uploadList = new LinkedList<>();
		uploadMap = new HashMap<>();
		deleteList = new LinkedList<>();
	}
	
	public void submitUploadFile(String keyName,File file) {
		uploadMap.put(keyName, file);
		uploadList.add(keyName);
	}
	
	public void submitDeleteFile(String keyName) {
		deleteList.add(keyName);
	}
	
	public void startSyn(S3SynManager manager) {		
		while(!uploadList.isEmpty()) {
			String keyName = uploadList.poll();
			File file = uploadMap.get(keyName);
			if(file.length()>=S3SynManager.MAX_SIZE) {
				UploadTask task = new UploadTask(manager, keyName, file);
				pool.submit(task);
			}else {
				System.out.println("======开始上传"+keyName+"========");
				manager.uploadSmallFile(keyName, file);
				System.out.println("===========上传成功============");
			}
		}
		while(!deleteList.isEmpty()) {
			String keyName = deleteList.poll();
//			DeleteTask task = new DeleteTask(manager, keyName);
//			pool.submit(task);
			System.out.println("======开始删除"+keyName+"========");
			manager.deleteFile(keyName);
			System.out.println("===========删除成功============");
		}
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
	
	
}

class UploadTask implements Runnable{
	
	private S3SynManager manager;
	
	private String keyName;
	
	private File file;
	
	public UploadTask(S3SynManager manager,String keyName,File file) {
		this.manager = manager;
		this.keyName = keyName;
		this.file = file;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("========文件大于"+S3SynManager.MAX_SIZE+",进行分片传输========");
		System.out.println("======开始上传"+keyName+"========");
		manager.uploadLargeFile(keyName, file);
		System.out.println("===========上传成功============");
	}
	
}

