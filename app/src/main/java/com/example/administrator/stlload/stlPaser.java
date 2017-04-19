package com.example.administrator.stlload;

import java.io.File;
/**
 * Created by Administrator on 2017-02-17.
 */

public class stlPaser {

    static {
        System.loadLibrary("native-lib");
    }
    paser STLpaser;
    File file;
    String format=null;

    public stlPaser(String fileRoot){
        file = new File(fileRoot);

        if(fileSize(fileRoot) == file.length()){
            STLpaser =new stlBinaryParser(file);
            format = "Binary";
        }else{
            STLpaser =new stlASCiiParser(file);
            format = "ASCii";
        }

    }

    public float[] getVectors(){ return STLpaser.getVectors(); }

    public float[] getNormals(){
        return STLpaser.getNormals();
    }

    public float[] getXYZ(){
        return STLpaser.getXYZ();
    }

    public float[] getCenterPoint() {return STLpaser.getObjectCenterPoint(); }

    public int getFaceCnt(){
        return STLpaser.getObjectFaceCnt();
    }

    public String getFileFormat(){
        return format;
    }

    private native int fileSize(String f);

}
