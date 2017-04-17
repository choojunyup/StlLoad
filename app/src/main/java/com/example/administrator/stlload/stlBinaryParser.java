package com.example.administrator.stlload;

import android.util.Log;
import java.io.File;

import static java.lang.Math.abs;

/*
	stl_binary_format

 	byte
    80
    4      file size
    ---------------------------loop
    4        normal
    4
    4

    4        vector
    4
    4

    4        vector
    4
    4

    4        vector
    4
    4

    2         ~~~~
    ---------------------------loop
 */

public class stlBinaryParser extends paser{

	static {
		System.loadLibrary("native-lib");
	}

	float[] vectors;
	float[] normals;
	float[] vectexs_min_max;
	float[] center;
	int nor_Count=0 , ver_Count=0;

	
	public stlBinaryParser(File file){

		nor_Count = Faces(file.toString());
		ver_Count = nor_Count*3;

		normals = new float[ver_Count*3];
		vectors = new float[ver_Count*3];
		vectexs_min_max = new float[6];  // xmin,xmax,ymin,ymax,zmin,zmax
		center = new float[3];

		int i = BinaryParser(file.toString(),normals,vectors,vectexs_min_max,center);

		/*
		Log.i("stl- view-","nor_Count="+nor_Count);
		Log.i("stl- view-","faces -> "+i);

		Log.i("stl- view-","n="+normals[nor_Count*3-1]+"  v="+vectors[ver_Count*3-1]);
		Log.i("stl- view-","xs="+abs(vectexs_min_max[1]-vectexs_min_max[0]));
		Log.i("stl- view-","ys="+abs(vectexs_min_max[3]-vectexs_min_max[2]));
		Log.i("stl- view-","zs="+abs(vectexs_min_max[5]-vectexs_min_max[4]));
		//test 4.17,   3.5,  2.917
		*/
		//center[0]= (vectexs_min_max[1]+vectexs_min_max[0])/2;
		//center[1]= (vectexs_min_max[3]+vectexs_min_max[2])/2;
		//center[2]= (vectexs_min_max[5]+vectexs_min_max[4])/2;

		/*
		Log.i("stl- view-","x="+center[0]);
		Log.i("stl- view-","y="+center[1]);
		Log.i("stl- view-","z="+center[2]);
		*/
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

	private native int BinaryParser(String filename, float[] nor, float[] ver, float[] ver_min_max,float[] center);
	private native int Faces(String filename);
	
}
