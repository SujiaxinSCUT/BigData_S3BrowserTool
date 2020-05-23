package s3syntool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.s3syntool.manager.S3BrowserManager;
import com.s3syntool.manager.S3SynManager;
import com.s3syntool.manager.TaskManager;
import com.s3syntool.utils.FileTool;
import com.s3syntool.utils.MultiPartUploadInfo;
import com.sysyntool.client.Configuration;

public class ManagerTest {

	private final static String accessKey = "2D06A1DB15FCE613DC3B";
	private final static String secretKey = "WzJCQjM0NzU3REZENTU5QjAwMjQ2RjBGMzAzRTRBNzY0RUYxNkZCNURd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	
	private final static String bucketName = "sujiaxin";
	private final static String dir = "D:";
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
		S3BrowserManager test = new S3BrowserManager(new Configuration("1","1","1",""));
		System.out.println(test.getBuckets());
	}
}
