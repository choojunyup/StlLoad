package com.example.administrator.stlload;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Math.abs;

/**
 * Created by choojunyup on 2017-01-05.
 */

public class stlASCiiParser extends paser{

    static {
       System.loadLibrary("native-lib");
    }

    float[] vectors;
    float[] normals;
    float[] vectexs_min_max;
    float[] center;

    StringBuilder sb_nor,sb_ver;

    int nor_Count=0 , ver_Count=0;

    public stlASCiiParser(File file){

        sb_nor = new StringBuilder();
        sb_ver = new StringBuilder();

        nor_Count = facas(file.toString());
        ver_Count = nor_Count*3;

        //normals = new float[nor_Count*3];
        normals = new float[ver_Count*3];
        vectors = new float[ver_Count*3];
        vectexs_min_max = new float[6];  // xmin,xmax,ymin,ymax,zmin,zmax
        center = new float[3];

        ASCiiParser(file.toString(),normals,vectors,vectexs_min_max,center);
        /*
        Log.i("stl- view-","ok- "+nor_Count);
        Log.i("stl- view-","--x= "+abs(vectexs_min_max[0]-vectexs_min_max[1]));
        Log.i("stl- view-","--y= "+abs(vectexs_min_max[2]-vectexs_min_max[3]));
        Log.i("stl- view-","--z= "+abs(vectexs_min_max[4]-vectexs_min_max[5]));
        */
        //center[0]= (vectexs_min_max[1]+vectexs_min_max[0])/2;
        //center[1]= (vectexs_min_max[3]+vectexs_min_max[2])/2;
        //center[2]= (vectexs_min_max[5]+vectexs_min_max[4])/2;


    }

    public float[] getVectors(){
        return vectors;
    }

    public float[] getNormals(){
        return normals;
    }

    public float[] getXYZ(){
        return vectexs_min_max;
    }

    public float[] getObjectCenterPoint(){
        return center;
    }

    private native int facas(String s);
    private native void ASCiiParser(String s, float[] nor, float[] ver, float[] ver_min_max,float[] center);


}
