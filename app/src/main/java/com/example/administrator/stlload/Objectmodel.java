package com.example.administrator.stlload;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class Objectmodel {

    private final String vertexShaderCode =
                    "uniform mat4 uMVPMatrix;" +
                    "uniform mat4 uMVMatrix;"+
                    "uniform vec3 uLightPos;"+
                    "uniform vec4 vColor;"+

                    "attribute vec4 vPosition;" +
                    "attribute vec3 vNormal;"+

                    "varying vec4 uColor;"+
                    "void main() {" +
                            "vec3 modelViewVertex = vec3(uMVMatrix * vPosition);"+
                            "vec3 modelViewNormal = vec3(uMVMatrix * vec4(vNormal, 0.0));"+
                            "float distance = length(uLightPos - modelViewVertex);"+
                            "vec3 lightVector = normalize(uLightPos - modelViewVertex);"+
                            "float diffuse = max(dot(modelViewNormal, lightVector), 0.1);"+
                            "diffuse = diffuse * (1.0 / (1.0 + (0.06 * distance * distance)));"+
                            "uColor = vColor * (0.6 + diffuse);"+
                            "gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
                    "precision mediump float;" +
                    "varying vec4 uColor;" +
                     "void main() {" +
                            "  gl_FragColor = uColor;" +
                     "}";

    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mNormalHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;
    private int mLightPosHandle;
///////////////////////////////////////////////////
    private int vertexsBufferIdx;
    private int normalsBufferIdx;
///////////////////////////////////////////////////
    static final int COORDS_PER_VERTEX = 3;
    static final int VERTEX_DATA_SIZE = 3;
    static final int NORMAL_DATA_SIZE = 3;
    static final int FLOAT_BYTE_SIZE = 4;

    int vertexCount;
    private float color[] = { 0.6f, 0.6f, 0.6f, 0.8f };

    stlPaser stl;
    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Objectmodel(stlPaser StlObject) {
        // initialize vertex byte buffer for shape coordinates

        stl = StlObject;
        float[] vertexs = stl.getVectors();
        float[] normals = stl.getNormals();
        vertexCount = vertexs.length / COORDS_PER_VERTEX;

        vertexBuffer = floatToBuffer(vertexs);
        normalBuffer = floatToBuffer(normals);

        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        vboUseSet();

    }

    public void draw(float[] mvpMatrix,float mSize) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mProgram, "uLightPos");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "vNormal");


        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniform3f(mLightPosHandle, 0f, 0f,mSize);


        //vboUnUseRun();
        vboUseRun();


    }

    private FloatBuffer floatToBuffer(float[] floatArray){
        FloatBuffer Buffer;
        ByteBuffer bb = ByteBuffer.allocateDirect(floatArray.length * 4);
        bb.order(ByteOrder.nativeOrder());
        Buffer = bb.asFloatBuffer();
        Buffer.put(floatArray);
        Buffer.position(0);

        return  Buffer;
    }

    private void vboUseSet(){
        final int buffers[] = new int[2];
        GLES20.glGenBuffers(2, buffers, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * FLOAT_BYTE_SIZE, vertexBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, normalBuffer.capacity() * FLOAT_BYTE_SIZE, normalBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        vertexsBufferIdx = buffers[0];
        normalsBufferIdx = buffers[1];

        vertexBuffer.limit(0);
        vertexBuffer = null;
        normalBuffer.limit(0);
        normalBuffer = null;

    }

    private void vboUseRun(){

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexsBufferIdx);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, VERTEX_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, normalsBufferIdx);
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Draw the object
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,vertexCount);
    }

    private void vboUnUseRun(){
        vertexBuffer.position(0);
        normalBuffer.position(0);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glVertexAttribPointer( mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer( mNormalHandle,COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, normalBuffer);

        // Draw the object
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }

}
