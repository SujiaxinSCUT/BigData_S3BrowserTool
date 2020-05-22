package s3syntool;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.s3syntool.manager.S3BrowserManager;
import com.s3syntool.manager.S3SynManager;
import com.sysyntool.client.Configuration;

public class ManagerTest {

	private final static String accessKey = "2D06A1DB15FCE613DC3B";
	private final static String secretKey = "WzJCQjM0NzU3REZENTU5QjAwMjQ2RjBGMzAzRTRBNzY0RUYxNkZCNURd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	
	private final static String bucketName = "sujiaxin";
	private final static String dir = "D:";
	@Test
	public void uploadFile() {
		S3SynManager manager = new S3SynManager(new Configuration(accessKey, secretKey, serviceEndpoint, signingRegion));
		S3BrowserManager bmanager = new S3BrowserManager(new Configuration(accessKey, secretKey, serviceEndpoint, signingRegion));
		manager.setSynBucketName(bucketName);
		manager.setSynDir("D:");
//		ObjectListing listing = bmanager.getObjectList(bucketName);
//		for(S3ObjectSummary s3o:listing.getObjectSummaries()) {
//			System.out.println(s3o.getKey());
//		}
		File file = new File("D://dev-project//strategy.txt");
//		File dir_file = new File(dir);
		System.out.println(new Date(file.lastModified()).toGMTString());
//		System.out.println(file.getAbsolutePath().substring(dir_file.getAbsolutePath().length()).replace('\\','/'));
//		Date date1 = new Date();
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Date date2 = new Date();
//		System.out.println(date1.before(date2));
	}
}
