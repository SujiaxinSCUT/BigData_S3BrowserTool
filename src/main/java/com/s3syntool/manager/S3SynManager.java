package com.s3syntool.manager;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sysyntool.client.Configuration;
import com.sysyntool.client.S3BrowserClient;

public class S3SynManager {
	
	private S3BrowserManager manager;
	
	private String synDir = "";
	
	private String synBucketName = "";
	
	public S3SynManager(Configuration config) {
		manager = new S3BrowserManager(config);
	}
	
	public S3SynManager(S3BrowserClient client) {
		manager = new S3BrowserManager(client);
	}
	
	public void synchronizeFile() {
		File dir_file = new File(synDir);
		ObjectListing listing = manager.getObjectList(synBucketName);
		List<S3ObjectSummary> objectList = listing.getObjectSummaries();
		Map<String,S3ObjectSummary> objectMap = new HashMap<>();
		for(S3ObjectSummary s3o:objectList) {
			objectMap.put(s3o.getKey(), s3o);
		}
		
		LinkedList<File> fileList = new LinkedList<>();
		fileList.addAll(Arrays.asList(dir_file.listFiles()));
		while(!fileList.isEmpty()) {
			File file = fileList.removeFirst();
			if(file.isDirectory()) {
				fileList.addAll(Arrays.asList(file.listFiles()));
			}else {
				String path = file.getAbsolutePath().substring(dir_file.getAbsolutePath().length());
				path.replace('\\','/');
				if(objectMap.containsKey(path)) {
					S3ObjectSummary os = objectMap.get(path);
					Date fileDate = new Date(file.lastModified());
					Date objectDate = os.getLastModified();
					if(objectDate.before(fileDate)) {
//						upload file
					}
				}else {
//					upload file
				}
			}
		}
	}
	
	public void uploadSmallFile(String keyName,File file) {
		manager.putObject(synBucketName, keyName, file);
	}
	
	public void uploadLargeFile(String keyName,File file) {
		
	}
	
	public void deleteFile(String fileName) {
		manager.deleteObject(synBucketName, fileName);
	}

	public String getSynDir() {
		return synDir;
	}

	public void setSynDir(String synDir) {
		this.synDir = synDir;
	}

	public String getSynBucketName() {
		return synBucketName;
	}

	public void setSynBucketName(String synBucketName) {
		this.synBucketName = synBucketName;
	}
	
	
}
