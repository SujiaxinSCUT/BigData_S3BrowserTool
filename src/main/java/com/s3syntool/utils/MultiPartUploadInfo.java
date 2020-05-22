package com.s3syntool.utils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import com.amazonaws.services.s3.model.PartETag;

public class MultiPartUploadInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accessKey;
	private String secretKey;
	
	private File file;
	private String uploadId;
	private int uploaded_partNumber;
	private long filePosition;
	private long partSize;
	private String keyName;
	private String bucketName;
	private ArrayList<PartETag> partETags;
	
	
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getUploadId() {
		return uploadId;
	}
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	public int getUploaded_partNumber() {
		return uploaded_partNumber;
	}
	public void setUploaded_partNumber(int uploaded_partNumber) {
		this.uploaded_partNumber = uploaded_partNumber;
	}
	public long getFilePosition() {
		return filePosition;
	}
	public void setFilePosition(long filePosition) {
		this.filePosition = filePosition;
	}
	public long getPartSize() {
		return partSize;
	}
	public void setPartSize(long partSize) {
		this.partSize = partSize;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public ArrayList<PartETag> getPartETags() {
		return partETags;
	}
	public void setPartETags(ArrayList<PartETag> partETags) {
		this.partETags = partETags;
	}
	@Override
	public String toString() {
		return "MultiPartUploadInfo [accessKey=" + accessKey + ", secretKey=" + secretKey + ", file=" + file
				+ ", uploadId=" + uploadId + ", uploaded_partNumber=" + uploaded_partNumber + ", filePosition="
				+ filePosition + ", partSize=" + partSize + ", keyName=" + keyName + ", bucketName=" + bucketName
				+ ", partETags=" + partETags + "]";
	}
	
	
}
