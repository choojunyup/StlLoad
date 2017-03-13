package com.example.administrator.stlload;

import java.text.Collator;
import java.util.Comparator;

public class FileInfo {

	private int fileIcon = 0;
	private String fileName;
	private String fileSize;
	private String fileDate;
	private String filePath;
	
	public int getFileIcon(){
		return fileIcon;
	}
	
	public void setFileIcon(int fileIcon){
		this.fileIcon = fileIcon;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public String getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getFileDate() {
		return fileDate;
	}
	
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	
	public static final Comparator<FileInfo> ALPHA_COMPARATOR = new Comparator<FileInfo>(){
		private final Collator sCollator = Collator.getInstance();

		@Override
		public int compare(FileInfo mFileInfo1, FileInfo mFileInfo2) {
			return sCollator.compare(mFileInfo1.fileName, mFileInfo2.fileName);
		}
		
	};
	
}
