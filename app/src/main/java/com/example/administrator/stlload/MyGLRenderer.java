package com.example.administrator.stlload;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import static java.lang.Math.abs;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Objectmodel ObjectModel;
    private stlPaser StlObject;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private float[] mScaleMatrix = new float[16];

    private float mAngleX = 0.0f;
    private float mAngleY = 0.0f;
    private float mSize = -6.0f;
    private float mMoveX = 0.0f;
    private float mMoveY = 0.0f;

    private float[] centerPointXYZ;    //3
    private float[] objectPointXYZ;    //6


    public MyGLRenderer(stlPaser StlObject){
        this.StlObject = StlObject;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        centerPointXYZ = StlObject.getCenterPoint();
        objectPointXYZ = StlObject.getXYZ();

        /*
        objectSizeXYZ[0]= abs(objectPointXYZ[0]-objectPointXYZ[1]);   //X_size
        objectSizeXYZ[1]= abs(objectPointXYZ[2]-objectPointXYZ[3]);   //Y_size
        objectSizeXYZ[2]= abs(objectPointXYZ[4]-objectPointXYZ[5]);   //Z_size
        */
        //averageSize =(abs(objectPointXYZ[0]-objectPointXYZ[1])+abs(objectPointXYZ[2]-objectPointXYZ[3])+abs(objectPointXYZ[4]-objectPointXYZ[5]))/3;

        ObjectModel = new Objectmodel(StlObject);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);


        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, mSize, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.rotateM(mRotationMatrix, 0, -mAngleY, 1, 0, 0); //z축
        Matrix.rotateM(mRotationMatrix, 0, -mAngleX, 0, 1, 0); //y축

        Matrix.setIdentityM(mScaleMatrix, 0);
        //Matrix.scaleM(mScaleMatrix,0,1/averageSize,1/averageSize,1/averageSize);
        //Matrix.scaleM(mScaleMatrix,0,0.2f,0.2f,0.2f);
        Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, mScaleMatrix, 0);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        //Matrix.scaleM(scratch,0,0.5f,0.5f,0.5f);
        Matrix.translateM(scratch,0,-centerPointXYZ[0],-centerPointXYZ[1],-centerPointXYZ[2]);


        ObjectModel.draw(scratch,mSize);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {

        GLES20.glViewport(0, 0, width, height);

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 300.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    public static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

/*
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
*/

    public float getAngleX() {
        return mAngleX;
    }
    public void setAngleX(float angleX) {
        mAngleX = angleX;
        if(mAngleX >=360.0f || mAngleX <= -360.0f){
            mAngleX=0.0f;
        }
        //Log.i("stl-","mAngleX:"+mAngleX);
    }

    public float getAngleY() {
        return mAngleY;
    }
    public void setAngleY(float angleY) {
        mAngleY = angleY;
        if(mAngleY >=360.0f || mAngleY <= -360.0f){
            mAngleY=0.0f;
        }
        //Log.i("stl-","mAngleX:"+mAngleY);
    }

    public float getSize() {
        return mSize;
    }
    public void setSize(float size) {
        mSize = size;
        Log.i("stl-","mSize:"+mSize);
    }

    public float getMoveX() {
        return mMoveX;
    }
    public void setMoveX(float moveX) {
        mMoveX = moveX;
    }

    public float getMoveY() {
        return mMoveY;
    }
    public void setMoveY(float moveY) {
        mMoveY = moveY;
    }

}