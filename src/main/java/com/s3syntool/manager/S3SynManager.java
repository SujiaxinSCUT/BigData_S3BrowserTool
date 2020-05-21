package com.s3syntool.manager;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		Set<S3ObjectSummary> objectSet = new HashSet<>();
		for(S3ObjectSummary sos:objectList) {
			objectSet.add(sos);
		}
	}
	
	public void uploadSmallFile(String fileName) {
		File file = new File(synDir+File.separator+fileName);
		manager.putObject(synBucketName, fileName, file);
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
