package com.xuanyuan.mypicture.opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * openGL 中的例子demo
 */
public class GLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        GLSurfaceView GLSurfaceView = new GLSurfaceView(this);
        GLSurfaceView.setEGLContextClientVersion(2);
        GLSurfaceView.setRenderer(new GLRenderer());
        setContentView(GLSurfaceView);
    }
}
