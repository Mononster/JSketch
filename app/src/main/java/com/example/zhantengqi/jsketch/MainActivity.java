package com.example.zhantengqi.jsketch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class MainActivity extends Activity {
    PaintModel model;
    ArrayList<Shape> storedShapes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new PaintModel();
        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
        }
        else {
             storedShapes = savedInstanceState.getParcelableArrayList("key");
            //Log.d("here","" + storedShapes.get(0).getBs());
        }
        Log.d("Paint", "onCreate");
        //remove title bar and enter full screen.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d("Paint", "onPostCreate");

        ToolBarView toolBarView = new ToolBarView(this,model);
        ViewGroup toolBarViewGroup1 = (ViewGroup) findViewById(R.id.ToolBar);
        toolBarViewGroup1.addView(toolBarView);

        CanvasView canvasView = new CanvasView(this,model);
        canvasView.setShapes(storedShapes);
        canvasView.setWillNotDraw(false);
        ViewGroup canvasViewGroup2 = (ViewGroup) findViewById(R.id.Canvas);
        canvasViewGroup2.addView(canvasView);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", model.getShapes());
        super.onSaveInstanceState(outState);
    }
}
