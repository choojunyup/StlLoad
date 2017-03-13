package com.example.administrator.stlload;

public class FileUtil {

	public static String fileTypeName(String file){
		String fileTypeName = null;
		int start = file.lastIndexOf(".")+1;
		int end = file.length();
		
		fileTypeName = file.substring(start, end).toLowerCase();
		return fileTypeName;
	}

	/*
	public static boolean isImage(String file){
		boolean result = true;
		String fileTypeName = fileTypeName(file);
		if(!(fileTypeName.equals("gif") || fileTypeName.equals("jpeg") || fileTypeName.equals("jpg") || fileTypeName.equals("bmp") || fileTypeName.equals("png") || fileTypeName.equals("bm"))){
			result = false;
		}
		return result;
	}
	*/

	public static boolean isStlFile(String file){
		boolean result = true;
		String fileTypeName = fileTypeName(file);
		if(!(fileTypeName.equals("stl") || fileTypeName.equals("STL"))){
			result = false;
		}
		return result;
	}

	/*
	public static boolean isDocument(String file){
		boolean result = true;
		String fileTypeName = fileTypeName(file);
		if(!(fileTypeName.contains("hwp") || fileTypeName.contains("doc") || fileTypeName.contains("xls") || fileTypeName.contains("txt") || fileTypeName.contains("ppt") || fileTypeName.contains("pdf"))){
			result = false;
		}
		return result;
	}
	*/
	
}
