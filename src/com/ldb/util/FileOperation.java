package com.ldb.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 文件操作
 * @author lsp
 *
 */
public class FileOperation {

	public static String fileMove(String from, String to, boolean isOverrid) throws IOException {
		File source = new File(from); 
        if(source.isDirectory()){
        	throw new FileNotFoundException(from + "是目录，请传入文件路径");
        }
        File dest = new File(to);
        File destParent = dest.getParentFile();
        if(!destParent.exists()){
        	destParent.mkdirs();
        }
        String filename = dest.getName();
        if(!isOverrid){
        	int count = 1;
        	String destPath = dest.getCanonicalPath();
        	// 判断文件是否存在，如果存在就对文件名加上序号
	        while(dest.exists()){
	        	String ext = "";
	        	int index = filename.lastIndexOf(".");
	        	if(index > 0){
	        		ext = filename.substring(index);
	        		filename = filename.substring(0, index);
	        	}
	        	filename = destPath.substring(0, destPath.lastIndexOf("\\")) + "\\" +  filename + "_" + count + ext;
	        	count++;
	        	dest = new File(filename);
	        }
        }
        source.renameTo(dest);
       
        return filename;
    }  
	
//	public static void main(String[] args) {
//		try {
//			fileMove(Constants.UPLOAD_IMAGE_DIR_TEMP + "\\search.png",  Constants.UPLOAD_IMAGE_DIR + "\\1\\search.png", false);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("出错: " + e.getMessage());
//		}
//	}
  
}
