package com.example.administrator.stlload;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class OpenGLES20Activity extends Activity {

    private stlPaser isStl;
    private String StlPath;
    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent stlLoad = getIntent();
        StlPath = stlLoad.getExtras().getString("stl_path");

        isStl = new stlPaser(StlPath);

        mGLView = new MyGLSurfaceView(this,isStl);
        setContentView(mGLView);

        Log.i("stl- view-","open-"+isStl.getFileFormat());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }
}