package com.s3syntool.manager;

import java.io.File;
import java.util.List;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.sysyntool.client.Configuration;
import com.sysyntool.client.S3BrowserClient;

public class S3BrowserManager {

	private S3BrowserClient client;
	
	public S3BrowserManager(S3BrowserClient client) {
		this.client = client;
	}
	
	public S3BrowserManager(Configuration config) {
		this.client = new S3BrowserClient(config);
	}
	
	public ObjectListing getObjectList(String bucketName) {
		return client.getS3().listObjects(bucketName);
	}
	
	public boolean createBucket(String bucketName) {
		List<Bucket> list_bucket = getBuckets();
		for(Bucket b:list_bucket) {
			if(b.getName().equals(bucketName))
				return false;
		}
		client.getS3().createBucket(bucketName);
		return true;
	}
	
	public List<Bucket> getBuckets(){
		return client.getS3().listBuckets();
	}
	
	public void putObject(String bucketName,String keyName,File file) {
		client.getS3().putObject(bucketName, keyName, file);
	}
	
	public void deleteObject(String bucketName,String fileName) {
		client.getS3().deleteObject(new DeleteObjectRequest(bucketName, fileName));
	}
}
