package com.asiainfo.exporter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
	
	public static void writeToFile(String content,String path){
		ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
		write(bais,path);
		try {
			bais.close();
		} catch (IOException e) {}
	}
	
	private static void write(InputStream is,String path){
		File file = new File(path);
		
		if(file.isDirectory()){
			throw new RuntimeException(path+" is a directory");
		}
		if(!file.isFile()){
			try {
				System.out.println("create file:"+file.getAbsolutePath());
				file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}else{
			file.delete();
		}
		FileOutputStream fos = null;
		try {
			 fos = new FileOutputStream(file,true);
			int bufferSize = 1024;
			int length = 0;
			byte[] buffer = new byte[bufferSize];
			while((length=is.read(buffer, 0, bufferSize))>-1){
				fos.write(buffer,0, length);
			}
			System.out.println("write file successfully.");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {}
			}
		}
		
	}
	
	public static void removeFileIfExist(String path){
		File file = new File(path);
		
		if(file.isDirectory()){
			throw new RuntimeException(path+" is a directory");
		}
		if(file.isFile()){
			file.delete();
		}
	}
	
	public static void main(String[] args) {
		String  path= "/Users/sunguihua/Downloads/filetest.text";
		FileUtils.writeToFile("thja1231231ntent", path);
	}
	
}
