package com.s3syntool.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.s3syntool.utils.FileTool;
import com.s3syntool.utils.MultiPartUploadInfo;
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
	
	public void restartMultiPartUpload(MultiPartUploadInfo info) {
	
		long partSize = info.getPartSize();
		String bucketName = info.getBucketName();
		String keyName = info.getKeyName();
		
		AmazonS3 s3 = client.getS3();
		ArrayList<PartETag> partETags = info.getPartETags();
		File file = info.getFile();
		long contentLength = file.length();
		String uploadId = info.getUploadId();
		
		File dir = new File(System.getProperty("user.dir")+File.separator+"uploadFileDir");
		if(!dir.exists()) {
			dir.mkdir();
		}
		File uploadFile = new File(dir.getAbsolutePath()+File.separator+System.currentTimeMillis());
		
		long filePosition = info.getFilePosition();
		int i = info.getUploaded_partNumber()+1;
		
		for (; filePosition < contentLength; i++) {

			partSize = Math.min(partSize, contentLength - filePosition);


			UploadPartRequest uploadRequest = new UploadPartRequest()
						.withBucketName(bucketName)
						.withKey(keyName)
						.withUploadId(uploadId)
						.withPartNumber(i)
						.withFileOffset(filePosition)
						.withFile(file)
						.withPartSize(partSize);

			System.out.format("Uploading part %d\n", i);
			partETags.add(s3.uploadPart(uploadRequest).getPartETag());
			filePosition += partSize;
			info.setFilePosition(filePosition);
			info.setUploaded_partNumber(i);

			FileTool.writeObjectToFile(info, uploadFile);

		}

		System.out.println("Completing upload");
		
		CompleteMultipartUploadRequest compRequest = 
				new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);

		s3.completeMultipartUpload(compRequest);
		
		uploadFile.delete();

		System.out.println("Done!");
	}
	
	public void multiPartUpload(MultiPartUploadInfo info,String bucketName,String keyName,File file,long partSize) {
		
		AmazonS3 s3 = client.getS3();
		ArrayList<PartETag> partETags = new ArrayList<PartETag>();
		info.setPartETags(partETags);
		long contentLength = file.length();
		String uploadId = null;
		
		File dir = new File(System.getProperty("user.dir")+File.separator+"uploadFileDir");
		if(!dir.exists()) {
			dir.mkdir();
		}
		File uploadFile = new File(dir.getAbsolutePath()+File.separator+System.currentTimeMillis());
		
		InitiateMultipartUploadRequest initRequest = 
				new InitiateMultipartUploadRequest(bucketName, keyName);
		
		uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
		
		info.setUploadId(uploadId);
		
		// Step 2: Upload parts.
		long filePosition = 0;
		for (int i = 1; filePosition < contentLength; i++) {

			partSize = Math.min(partSize, contentLength - filePosition);


			UploadPartRequest uploadRequest = new UploadPartRequest()
						.withBucketName(bucketName)
						.withKey(keyName)
						.withUploadId(uploadId)
						.withPartNumber(i)
						.withFileOffset(filePosition)
						.withFile(file)
						.withPartSize(partSize);

			System.out.format("Uploading part %d\n", i);
			partETags.add(s3.uploadPart(uploadRequest).getPartETag());
			filePosition += partSize;
			info.setFilePosition(filePosition);
			info.setUploaded_partNumber(i);

			FileTool.writeObjectToFile(info, uploadFile);

		}

		System.out.println("Completing upload");
		
		CompleteMultipartUploadRequest compRequest = 
				new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);

		s3.completeMultipartUpload(compRequest);
		
		uploadFile.delete();

		System.out.println("Done!");
	}
}
