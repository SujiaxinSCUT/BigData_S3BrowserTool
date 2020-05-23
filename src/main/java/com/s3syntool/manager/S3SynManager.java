package com.s3syntool.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.s3syntool.utils.FileTool;
import com.s3syntool.utils.MultiPartUploadInfo;
import com.sysyntool.client.Configuration;
import com.sysyntool.client.S3BrowserClient;

public class S3SynManager {
	
	private S3BrowserManager manager;
	
	private String synDir = "";
	
	private String synBucketName = "";
	
	private Configuration config;
	
	private long partSize = 5<<20;
	
	public static final long MAX_SIZE = 20<<20;
	
	public S3SynManager(Configuration config) throws Exception {
		this.config = config;
		manager = new S3BrowserManager(config);
	}
	
	public S3SynManager(S3BrowserClient client) {
		this.config = client.getConfig();
		manager = new S3BrowserManager(client);
	}
	
	public void synchronizeFile(TaskManager tm) {
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
				String path = file.getAbsolutePath().substring(synDir.length()+1);
				path = path.replaceAll("\\\\", "/");
				System.out.println(path);
				if(objectMap.containsKey(path)) {
					S3ObjectSummary os = objectMap.get(path);
					Date fileDate = new Date(file.lastModified());
					Date objectDate = os.getLastModified();
					if(objectDate.before(fileDate)) {
//						upload file
						tm.submitUploadFile(path, file);
						System.out.println(path+"加入上传组");
					}
					objectMap.remove(path);
				}else {
//					upload file
					tm.submitUploadFile(path, file);
					System.out.println(path+"加入上传组");
				}
			}
		}
//		delete the rest file
		Set<String> keySet = objectMap.keySet();
		for(String key:keySet) {
			System.out.println(key+"加入删除组");
			tm.submitDeleteFile(key);
		}
	}
	
	public void uploadSmallFile(String keyName,File file) {
		manager.putObject(synBucketName, keyName, file);
	}
	
	public void uploadLargeFile(String keyName,File file) {
		MultiPartUploadInfo info = new MultiPartUploadInfo();
		info.setAccessKey(config.getAccessKey());
		info.setSecretKey(config.getSecretKey());
		info.setBucketName(synBucketName);
		info.setFile(file);
		info.setKeyName(keyName);
		info.setPartSize(partSize);
		manager.multiPartUpload(info);
	}
	
	public List<MultiPartUploadInfo> searchUploadingFile() {
		List<MultiPartUploadInfo> list = new ArrayList<>();
		String key = config.getAccessKey()+config.getSecretKey();
		File dir = new File(System.getProperty("user.dir")+File.separator+key.hashCode());
		if(!dir.exists()||dir.listFiles().length==0) return null;
		else {
			File[] files = dir.listFiles();
			for(File f:files) {
				MultiPartUploadInfo info = (MultiPartUploadInfo) FileTool.readObjectFromFile(f);
				if(info!=null)
					list.add(info);
			}
		}
		return list;
	}
	
	public void deleteFile(String keyName) {
		manager.deleteObject(synBucketName, keyName);
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
