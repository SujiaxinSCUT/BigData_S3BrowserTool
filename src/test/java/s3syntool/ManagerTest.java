package s3syntool;

import java.io.File;
import java.util.List;

import org.junit.Test;

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
	
	@Test
	public void uploadFile() {
		S3BrowserManager manager = new S3BrowserManager(new Configuration(accessKey, secretKey, serviceEndpoint, signingRegion));
//		List<S3ObjectSummary> list = manager.getObjectList(bucketName).getObjectSummaries();
//		for(S3ObjectSummary sos:list) {
//			System.out.println(sos.getKey()+" "+sos.getLastModified());
//		}
		List<String> commonPrefixes = manager.getObjectList(bucketName).getCommonPrefixes();
		System.out.println(manager.getObjectList(bucketName).getDelimiter());
	}
}
