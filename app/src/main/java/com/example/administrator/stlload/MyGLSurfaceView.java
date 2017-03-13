package com.example.administrator.stlload;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {

    float x[] = new float[2];
    float y[] = new float[2];
    float preX,preY;
    int mode;
    float preS,length;

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
                preS = (float)Math.sqrt(Math.pow(x[1]-x[0],2)+Math.pow(y[1]-y[0],2))/300;
                mode = 2;
                break;

            case MotionEvent.ACTION_MOVE:

                if(mode == 2){
                    for(int i=0; i < pointer_count; i++) {
                        x[i] = event.getX(i);
                        y[i] = event.getY(i);
                    }

                    length = (float)Math.sqrt(Math.pow(x[1]-x[0],2)+Math.pow(y[1]-y[0],2))/300;
                    float ds = length - preS;
                    preS = length;

                    mRenderer.setSize(mRenderer.getSize() + ds);
                    requestRender();

                }
                else if(mode == 1){
                    float x1 = event.getX(0);
                    float y1 = event.getY(0);
                    float dx = (preX - x1) / 5;
                    float dy = (preY - y1) / 5;

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

}
