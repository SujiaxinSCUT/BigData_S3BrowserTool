package s3syntool;


import org.junit.Test;

import com.s3syntool.client.Configuration;
import com.s3syntool.manager.S3BrowserManager;
import com.s3syntool.manager.S3SynManager;

public class ManagerTest {

	private final static String accessKey = "2D06A1DB15FCE613DC3B";
	private final static String secretKey = "WzJCQjM0NzU3REZENTU5QjAwMjQ2RjBGMzAzRTRBNzY0RUYxNkZCNURd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	
	private final static String bucketName = "sujiaxin";
	@Test
	public void uploadFile() throws Exception {
		S3SynManager manager = new S3SynManager(new Configuration(accessKey, secretKey, serviceEndpoint, signingRegion));
		S3BrowserManager bmanager = new S3BrowserManager(new Configuration(accessKey, secretKey, serviceEndpoint, signingRegion));
		manager.setSynBucketName(bucketName);
		manager.setSynDir("D:\\upload");
//		File file = new File("D:\\Dev-Project\\strategy.txt");
//		System.out.println(file.getAbsolutePath().substring("D:\\Dev-Project".length()+1));
//		MultiPartUploadInfo info = new MultiPartUploadInfo();
//		info.setFile(file);
//		info.setBucketName(bucketName);
//		info.setPartSize(5<<20);
//		info.setKeyName( "测试数据20190422.xls");
//		bmanager.multiPartUpload(info, bucketName, "测试数据20190422.xls", file, 5<<20);
//		File file = new File("F:\\STSWorkspace\\s3syntool\\-1905561714\\1590200371191");
//		MultiPartUploadInfo info = (MultiPartUploadInfo) FileTool.readObjectFromFile(file);
//		bmanager.restartMultiPartUpload(info);
//		TaskManager tm = new TaskManager();
//		manager.synchronizeFile(tm);
//		System.out.println("上传文件数："+tm.getUploadList().size());
//		System.out.println("卸载文件数："+tm.getDeleteList().size());
//		tm.startSyn(manager);
		System.out.println(bmanager.getBuckets());
	}
}
