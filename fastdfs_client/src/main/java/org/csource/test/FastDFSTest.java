package org.csource.test;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class FastDFSTest {

	@Test
	public void testFileUpload() throws Exception {
		// 1、加载配置文件，配置文件中的内容就是tracker服务的地址。
		ClientGlobal.init("G:\\积云培训\\C1708A-S\\上课资料\\day05-（后台）商品添加，FastDFS图片服务器\\资料\\fastdfs_client\\src\\main\\resources\\fdfs_client.conf");
		// 2、创建一个TrackerClient对象。直接new一个。
		TrackerClient trackerClient = new TrackerClient();
		// 3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
		TrackerServer trackerServer = trackerClient.getConnection();
		// 4、创建一个StorageServer的引用，值为null
		StorageServer storageServer = null;
		// 5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
		StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
		// 6、使用StorageClient对象上传图片。
		//扩展名不带“.”
	String strings = storageClient.upload_file1("D:/ds.jpg", "jpg", null);
		//返回一个数组
		//String[] jpgs = storageClient.upload_file("D:/ds.jpg", "jpg", null);
		// 7、返回数组。包含组名和图片的路径。
//		for (String string : strings) {
			System.out.println(strings);
//		}
	}
	@Test
	public void testFileUpload2() throws Exception {
		FastDFSClient fastDFSClient = new FastDFSClient("G:\\积云培训\\C1708A-S\\上课资料\\day05-（后台）商品添加，FastDFS图片服务器\\资料\\fastdfs_client\\src\\main\\resources\\fdfs_client.conf");
		String file = fastDFSClient.uploadFile("D:/ds.jpg");
		System.out.println(file);
	}
}