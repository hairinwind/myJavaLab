package my.javalab.filecopy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileCopy {
	
	private static final int DEFAULT_BUFFER_SIZE = 5120;
	private static String SRC_FILE_NAME = "src/main/resources/test.csv";
	private static String SRC_LARGE_FILE_NAME = "src/main/resources/testLarge.csv";
	private static String TARGET_FILE_NAME = "src/main/resources/testResult.csv";
		
	public static void main(String[] args) throws IOException {
//		generateLargeFile();
		
//		deleteTargetFileIfExist();
//		long start1 = System.currentTimeMillis();
//		FileCopy.copy1();
//		long end1 = System.currentTimeMillis();
//		System.out.println("time for copy1: " + (end1-start1));

		deleteTargetFileIfExist();
		long start2 = System.currentTimeMillis();
		FileCopy.copy2();
		long end2 = System.currentTimeMillis();
		System.out.println("time for copy2: " + (end2-start2));

		deleteTargetFileIfExist();
		long start3 = System.currentTimeMillis();
		FileCopy.copy3();
		long end3 = System.currentTimeMillis();
		System.out.println("time for copy3: " + (end3-start3));
		
		deleteTargetFileIfExist();
	}
	
	public static void generateLargeFile() throws IOException {
		FileInputStream in = new FileInputStream(SRC_FILE_NAME);
		FileOutputStream out = new FileOutputStream(SRC_LARGE_FILE_NAME);
		FileChannel inChannel = in.getChannel();        
        FileChannel outChannel = out.getChannel();
        for (int i=0;i<10;i++) {
	        long bytesTransferred = 0;
	        while(bytesTransferred < inChannel.size()){
	          bytesTransferred += inChannel.transferTo(bytesTransferred, 51200, outChannel);
	        }
        }
        inChannel.close();
        outChannel.close();
		in.close();
		out.close();
	}
	
	public static void deleteTargetFileIfExist() {
		File targetFile = new File(TARGET_FILE_NAME);
		if(targetFile.exists()) {
			targetFile.delete();
		}
	}

	public static void copy1() throws IOException {
		InputStream in = new FileInputStream(SRC_FILE_NAME);
		OutputStream out = new FileOutputStream(TARGET_FILE_NAME);
		byte[] b = new byte[512];
		while (true) {
			int i = in.read(b);
			if (i == -1) {
				break;
			}
			out.write(b);
		}
		in.close();
		out.close();
	}
	
	public static void copy2() throws IOException {
		InputStream in = new FileInputStream(SRC_FILE_NAME);
		BufferedInputStream bin = new BufferedInputStream(in);
		OutputStream out = new FileOutputStream(TARGET_FILE_NAME);
		BufferedOutputStream bout = new BufferedOutputStream(out);
		byte[] b = new byte[DEFAULT_BUFFER_SIZE];
		while (true) {
			int i = bin.read(b);
			if (i == -1) {
				break;
			}
			bout.write(b);
		}
		bin.close();
		bout.close();
		in.close();
		out.close();
	}
	
	public static void copy3() throws IOException {
		FileInputStream in = new FileInputStream(SRC_FILE_NAME);
		FileOutputStream out = new FileOutputStream(TARGET_FILE_NAME);
		FileChannel inChannel = in.getChannel();        
        FileChannel outChannel = out.getChannel();
        System.out.println("inChannel class:" + inChannel.getClass().getName());
        System.out.println("outChannel class:" + outChannel.getClass().getName());
//        long bytesTransferred = 0;
//        while(bytesTransferred < inChannel.size()){
//          bytesTransferred += inChannel.transferTo(bytesTransferred, 51200, outChannel);
//        }
        ByteBuffer buf = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);
        while((inChannel.read(buf)) != -1) {
                buf.flip();
                outChannel.write(buf);
                buf.clear();
        }
        inChannel.close();
        outChannel.close();
		in.close();
		out.close();
	}
	
}
