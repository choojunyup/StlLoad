package com.example.administrator.stlload;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Objectmodel ObjectModel;
    private stlPaser StlObject;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mAccumulatedRotation = new float[16];
    private float[] mTemporaryMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    float[] scratch_1 = new float[16];
    float[] scratch_2 = new float[16];

    float mAngleX = 0.0f;
    float mAngleY = 0.0f;
    float mAngleZ = 0.0f;

    private float mMoveX = 0.0f;
    private float mMoveY = 0.0f;

    private float cameraX = 0;
    private float cameraY = 0;
    private float cameraZ = 3.0f;

    private float[] centerPointXYZ;    //3



    public MyGLRenderer(stlPaser StlObject){
        this.StlObject = StlObject;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        centerPointXYZ = StlObject.getCenterPoint();
        ObjectModel = new Objectmodel(StlObject);

        Matrix.setIdentityM(mAccumulatedRotation, 0);


    }

    @Override
    public void onDrawFrame(GL10 unused) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        cameraX = mMoveX;
        cameraY = -mMoveY;
        Matrix.setLookAtM(mViewMatrix, 0, cameraX, cameraY, cameraZ, mMoveX,-mMoveY, 0, 0.0f, 1.0f, 0.0f);
        Matrix.setIdentityM(mModelMatrix, 0);

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.rotateM(mRotationMatrix, 0, -mAngleY, 1, 0, 0); //y axis
        Matrix.rotateM(mRotationMatrix, 0, -mAngleX, 0, 1, 0); //x axis
        Matrix.rotateM(mRotationMatrix, 0, -mAngleZ, 0, 0, 1); //z axis
        mAngleY = 0.0f;
        mAngleX = 0.0f;
        mAngleZ = 0.0f;

        Matrix.multiplyMM(mTemporaryMatrix, 0, mRotationMatrix, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);

        Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16);

        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        System.arraycopy(mMVPMatrix, 0, scratch_1, 0, 16);

        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);
        System.arraycopy(mMVPMatrix, 0, scratch_2, 0, 16);

        Matrix.translateM(scratch_2, 0,-centerPointXYZ[0],-centerPointXYZ[1],-centerPointXYZ[2]);


        ObjectModel.draw(scratch_1,scratch_2,cameraZ);

        //Log.i("stl-","change");
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {

        GLES20.glViewport(0, 0, width, height);

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1;
        final float top = 1;
        final float near = 1;
        final float far = 50;

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

        //Log.i("stl-","cameraZ:"+cameraZ);
    }

    public float getAngleY() {
        return mAngleY;
    }
    public void setAngleY(float angleY) {
        mAngleY = angleY;

        if(mAngleY >=360.0f || mAngleY <= -360.0f){
            mAngleY=0.0f;
        }
        //Log.i("stl-","cameraY:"+cameraY);
    }

    public float getAngleZ() {
        return mAngleZ;
    }
    public void setAngleZ(float angleZ) {
        mAngleZ = angleZ;

        if(mAngleZ >=360.0f || mAngleZ <= -360.0f){
            mAngleZ=0.0f;
        }
        //Log.i("stl-","mAngleZ:"+mAngleZ);
    }

    public float getSize() {
        return cameraZ;
    }
    public void setSize(float size) {
        cameraZ = size;
        /*
        Log.i("stl-","mSize:"+focus);
        */
    }

    public float getMoveX() {
        return mMoveX;
    }
    public void setMoveX(float moveX) {
        mMoveX = moveX;

        //Log.i("stl-","moveX:"+mMoveX);
    }

    public float getMoveY() {
        return mMoveY;
    }
    public void setMoveY(float moveY) {
        mMoveY = moveY;

        //Log.i("stl-","moveY:"+mMoveY);
    }

    public void ObjectRecovery(){
        mMoveX = 0.0f;
        mMoveY = 0.0f;
        cameraX = 0;
        cameraY = 0;
        cameraZ = 3.0f;

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mTemporaryMatrix, 0);
        Matrix.setIdentityM(mAccumulatedRotation, 0);

        Matrix.setIdentityM(scratch_1,0);
        Matrix.setIdentityM(scratch_2,0);


    }


}