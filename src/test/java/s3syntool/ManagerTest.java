package s3syntool;

import java.io.File;

import org.junit.Test;

import com.s3syntool.manager.S3SynManager;

public class ManagerTest {

	private final static String accessKey = "2D06A1DB15FCE613DC3B";
	private final static String secretKey = "WzJCQjM0NzU3REZENTU5QjAwMjQ2RjBGMzAzRTRBNzY0RUYxNkZCNURd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	
	private final static String bucketName = "sujiaxin";
	
	@Test
	public void uploadFile() {
		S3SynManager manager = new S3SynManager(accessKey, secretKey, serviceEndpoint, signingRegion);
		manager.setSynBucketName(bucketName);
		manager.setSynDir("D:");
		manager.uploadSmallFile("Dev-Project/strategy.txt");
//		manager.deleteFile("strategy.txt");
	}
}
