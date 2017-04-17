package com.example.administrator.stlload;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import static java.lang.Math.pow;

public class MyGLSurfaceView extends GLSurfaceView {

    float x[] = new float[2];
    float y[] = new float[2];
    float preX,preY;
    float pre_moveX,pre_moveY;
    //float VecterX,VecterY;
    float d_angle;
    int mode;
    float preS,length;
    float dmx,dmy;
    float now_x,now_y;

    float now_degree,pre_degree;

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context, stlPaser StlObject) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer(StlObject);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointer_count = event.getPointerCount();
        if(pointer_count > 2) pointer_count = 2;

        // mode-1: one finger
        // mode-2: two finger
        // mode-0: none
        switch(event.getAction() & MotionEvent.ACTION_MASK) {


            case MotionEvent.ACTION_DOWN: //one finger
                preX = event.getX();
                preY = event.getY();
                mode = 1;
                break;

            case MotionEvent.ACTION_POINTER_DOWN: //two finger
                for(int i=0; i < pointer_count; i++) {
                    x[i] = event.getX(i);
                    y[i] = event.getY(i);
                }
                ////////////////////////////// focus set ///////////////////////////////////
                preS = (float)Math.sqrt(pow(x[1]-x[0],2)+ pow(y[1]-y[0],2))/300;
                ////////////////////////////// focus set ///////////////////////////////////

                ////////////////////////////// move set ////////////////////////////////////
                pre_moveX = (x[0]+x[1])/2.0f;
                pre_moveY = (y[0]+y[1])/2.0f;
                ////////////////////////////// move set ////////////////////////////////////

                ////////////////////////////// angle set ////////////////////////////////////
                pre_degree = (float)((Math.atan2(y[0]-y[1],x[0]-x[1])*180)/Math.PI);
                ////////////////////////////// angle set ////////////////////////////////////

                mode = 2;
                break;

            case MotionEvent.ACTION_MOVE:

                if(mode == 2){      //two finger process
                    for(int i=0; i < pointer_count; i++) {
                        x[i] = event.getX(i);
                        y[i] = event.getY(i);
                    }
                    //////////////////////////// focus process ///////////////////////////////////
                    length = (float)Math.sqrt(pow(x[1]-x[0],2)+ pow(y[1]-y[0],2))/300;
                    float ds = length - preS;
                    preS = length;
                    //////////////////////////// focus process ///////////////////////////////////

                    ////////////////////////////// move process //////////////////////////////////
                    now_x = (x[0]+x[1])/2.0f;
                    now_y = (y[0]+y[1])/2.0f;

                    dmx = (pre_moveX - now_x)/600;
                    dmy = (pre_moveY - now_y)/600;

                    pre_moveX = now_x;
                    pre_moveY = now_y;
                    ////////////////////////////// move process //////////////////////////////////

                    ////////////////////////////// angle process ////////////////////////////////////

                    now_degree = (float)((Math.atan2(y[0]-y[1],x[0]-x[1])*180)/Math.PI);
                    d_angle = (pre_degree - now_degree);

                    //Log.i("stl-","d_angle:"+d_angle);

                    pre_degree = now_degree;
                    ////////////////////////////// angle process ////////////////////////////////////

                    mRenderer.setSize(mRenderer.getSize() - ds);

                    mRenderer.setMoveX(mRenderer.getMoveX() + dmx);
                    mRenderer.setMoveY(mRenderer.getMoveY() + dmy);

                    mRenderer.setAngleZ(mRenderer.getAngleZ() - d_angle );

                    requestRender();

                }
                else if(mode == 1){   //one finger process
                    float x1 = event.getX(0);
                    float y1 = event.getY(0);
                    float dx = (preX - x1) / 10;
                    float dy = (preY - y1) / 10;


                    mRenderer.setAngleX(mRenderer.getAngleX() + dx);
                    requestRender();
                    mRenderer.setAngleY(mRenderer.getAngleY() + dy);
                    requestRender();

                    preX =x1;
                    preY =y1;
                }
                break;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                break;
        }


        return true;
    }

    public void setObject(){

        mRenderer.ObjectRecovery();
        requestRender();
    }


}
