package com.s3syntool.manager;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.sysyntool.client.S3BrowserClient;

public class S3SynManager {

	private S3BrowserClient client;
	
	private String synDir = "";
	
	private String synBucketName = "";
	
	public S3SynManager(String accessKey,String secretKey,String serviceEndpoint,String signingRegion) {
		client = new S3BrowserClient(accessKey, secretKey, serviceEndpoint, signingRegion);
	}
	
	public void synchronizeFile() {
		File dir_file = new File(synDir);
		AmazonS3 s3 = client.getS3();
		ObjectListing list = s3.listObjects(synBucketName);
	}
	
	public void uploadSmallFile(String fileName) {
		File file = new File(synDir+File.separator+fileName);
		client.getS3().putObject(synBucketName, fileName, file);
	}
	
	public void deleteFile(String fileName) {
		client.getS3().deleteObject(new DeleteObjectRequest(synBucketName, fileName));
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
