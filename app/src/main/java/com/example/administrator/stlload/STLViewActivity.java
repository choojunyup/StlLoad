package com.example.administrator.stlload;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.abs;

public class STLViewActivity extends Activity {

    private stlPaser isStl;
    private String StlPath;
    //private GLSurfaceView mGLView;
    private MyGLSurfaceView mGLView;
    private long process = 0;
    private float complete = 0;
    private float xyz[];
    private float xSize = 0, ySize = 0, zSize = 0;

    int faceCnt =0;
    //private String row_path = null;

    static RelativeLayout mainLayout;
    TextView objectInfo_left;
    TextView objectInfo_right;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stlview);

        mainLayout = (RelativeLayout)findViewById(R.id.layout_view);
        objectInfo_left = (TextView)findViewById(R.id.objectInfo_left);
        objectInfo_right = (TextView)findViewById(R.id.objectInfo_right);

        Intent intent_data = getIntent();

        if(intent_data.getDataString() != null){
            StlPath = intent_data.getDataString().substring(7);   // "file://" --> delete
        }else if(intent_data.getExtras() != null){
            StlPath = intent_data.getExtras().getString("stl_path");
        }else{
            Toast.makeText(STLViewActivity.this,"path error!!", Toast.LENGTH_LONG).show();
            finish();
        }

        //Log.i("stl- view-","StlPath-"+intent_data.getDataString());

        process = System.currentTimeMillis();

        isStl = new stlPaser(StlPath);  //pasering start
        xyz = isStl.getXYZ();          // object max,min coordinate
        faceCnt = isStl.getFaceCnt();  //triangle count

        xSize = abs(xyz[0]-xyz[1]);
        ySize = abs(xyz[2]-xyz[3]);
        zSize = abs(xyz[4]-xyz[5]);

        mGLView = new MyGLSurfaceView(this,isStl);

        objectInfo_left.setText(xSize+" x "+ySize+" x "+zSize+" ");
        objectInfo_right.setText(faceCnt+" faces");

        mainLayout.addView(mGLView,0);  //gl view add

        complete = ( System.currentTimeMillis()-process)/1000.0f;

        if(faceCnt==0){
            Toast.makeText(STLViewActivity.this,"nothing faces", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(STLViewActivity.this, "parse complete! \n" + complete + " seconds", Toast.LENGTH_LONG).show();
        }
        //Log.i("stl- view-","open-"+isStl.getFileFormat());
    }

    public void backButton(View v){
        finish();
    }

    public void recover(View v){
        mGLView.setObject();
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