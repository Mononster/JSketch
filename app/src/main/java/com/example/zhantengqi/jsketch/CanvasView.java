package com.example.zhantengqi.jsketch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by zhantengqi on 2016-06-22.
 */

public class CanvasView extends View implements Observer {
    private PaintModel paintModel;
    private ArrayList<Shape> shapes = new ArrayList<Shape>();

    public CanvasView(Context context, PaintModel model){
        super(context);
        this.paintModel = model;
        paintModel.setMainView(this);
        paintModel.addObserver(this);
        this.setOnTouchListener(new CanvasTouchController());
        Log.d("Paint", "CanvasView constructor");
        this.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d("Draw", "CanvasView drawMethod");
        if (shapes != null) {
            for (int i = 0; i < shapes.size(); i++) {
                Shape temp = shapes.get(i);
                temp.draw(canvas);
            }
        }
    }
    public ArrayList<Shape> getShapes(){
        return this.shapes;
    }
    public void setShapes(ArrayList<Shape> shapes){
        if (shapes == null){
            shapes = new ArrayList<Shape>();
        }
        else{
            this.shapes = shapes;
        }
    }
    public void clearCanvas(){
        shapes = new ArrayList<Shape>();
        invalidate();
    }
    public void update(Observable observable, Object data){
        Log.d("MVC", "update canvasView");
        this.invalidate();
    }
    private class CanvasTouchController implements OnTouchListener{

        Shape newShape = null;
        Shape selectShape = null;
        float lastPosX = 0;
        float lastPosY = 0;
        int fingerOne = -1;
        int fingerTwo = -1;
        float maxZoom = 4;
        float minZoom = 0.25f;
        float oldDist = 0;
        public boolean onTouch(View view, MotionEvent e){
            float posX = e.getX();
            float posY = e.getY();
            float disX = e.getX() - lastPosX;
            float disY = e.getY() - lastPosY;
            lastPosX = e.getX();
            lastPosY = e.getY();

            String toolName = paintModel.getCurrentDrawingTool();

            //Log.d(Float.toString(e.getX()), Float.toString(e.getY()));
            if (e.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                Log.d("Begin Touch", "Touch down");
                fingerOne = e.getPointerId(e.getActionIndex());
                if (toolName == "null"){
                    return false;
                }
                if (toolName == "selectTool"){
                    boolean ifExistSelectedShape = false;
                    for (Shape shape : shapes){
                        shape.setSelected(false);
                    }
                    for (Shape shape : shapes){
                        if (shape.checkIfGetSelected(posX,posY)){
                            selectShape = shape;
                            disX = 0;
                            disY = 0;
                            invalidate();
                            ifExistSelectedShape = true;
                            paintModel.adjustColorAndStroke(shape);
                            shapes.remove(shape);
                            shapes.add(shape);
                            break;
                        }
                    }
                    if (!ifExistSelectedShape){
                        selectShape = null;
                    }
                }
                if (toolName == "paintTool"){
                    Log.d("paintTool", "Active");
                    for (Shape shape : shapes){
                        if (shape.checkIfGetSelected(posX,posY)){
                            shape.setFillColor(paintModel.getCurrentDrawingColor());
                            shape.setFill(true);
                            invalidate();
                            break;
                        }
                    }
                    return false;
                }
                if (toolName == "eraserTool"){
                    for (int i = 0; i < shapes.size(); i++){
                        if (shapes.get(i).checkIfGetSelected(e.getX(), e.getY())){
                            shapes.remove(shapes.get(i));
                            invalidate();
                            break;
                        }
                    }
                    return false;
                }
                if (toolName == "rectangleTool" || toolName == "circleTool" || toolName == "lineTool"){
                    // the tool user is selecting is rectangle drawing shape tool.
                    if (toolName == "rectangleTool") {
                        newShape = new RectangleShape();
                    }
                    if (toolName == "circleTool"){
                        newShape = new CircleShape();
                    }
                    if (toolName == "lineTool"){
                        newShape = new LineShape();
                    }
                    shapes.add(newShape);
                    newShape.setBs(paintModel.getCurrentDrawingStroke());
                    newShape.setStartXY(posX,posY);
                    newShape.setBorderColor(paintModel.getCurrentDrawingColor());
                    newShape.setEndXY(posX,posY);
                    for (Shape shape : shapes){
                        shape.setSelected(false);
                    }
                }
                shapes = paintModel.sortShapes(shapes, new Point(Math.round(posX), Math.round(posY)));
                paintModel.setStartPosXY(e.getX(), e.getY());
                paintModel.setCurrPosXY(e.getX(), e.getY());
            } else if (e.getAction() == android.view.MotionEvent.ACTION_UP) {
                Log.d("End Touch", "Touch up");
                fingerOne = -1;
                invalidate();
            } else if (e.getActionMasked() == android.view.MotionEvent.ACTION_POINTER_DOWN){
                Log.d("Touch", "twoFinger");
                fingerTwo = e.getPointerId(e.getActionIndex());
                oldDist = calcDistance(e);
                Log.d("OldDist: " + oldDist, "123");
            } else if(e.getAction() == MotionEvent.ACTION_POINTER_UP) {
                Log.d("End Touch", "Touch up");
                fingerTwo = -1;
            } else if(e.getAction() == MotionEvent.ACTION_MOVE){
                if (fingerOne != -1 && fingerTwo != -1){
                    if (toolName == "selectTool" && selectShape != null){



                        float scale = calcDistance(e) / oldDist;

                        if (scale > maxZoom) {
                            scale = maxZoom;
                        }else if(scale < minZoom){
                            scale = minZoom ;
                        }
                        selectShape.resizeShape(scale);
                        invalidate();
                        Log.d("Space: " + scale,"hello");
                        return true;
                    }
                }
            }

            if (toolName == "selectTool" && selectShape != null
                    && (fingerOne != -1 && fingerTwo == -1)){
                selectShape.adjustPosition(disX,disY);
                selectShape.resetValue();
            }
            //now consider the touching stage;
            if (toolName == "rectangleTool" || toolName == "circleTool" ||toolName == "lineTool"){
                newShape.setEndXY(posX,posY);
            }

            paintModel.setCurrPosXY(posX, posY);
            return true;
        }
        public float calcDistance(MotionEvent e){
            float nfX, nfY, nsX, nsY;
            nsX = e.getX(e.findPointerIndex(fingerOne));
            nsY = e.getY(e.findPointerIndex(fingerOne));
            if (fingerTwo != -1) {
                nfX = e.getX(e.findPointerIndex(fingerTwo));
                nfY = e.getY(e.findPointerIndex(fingerTwo));
            }else{
                nfX = 0;
                nfY = 0;
            }
            float spaceX = nfX - nsX;

            float spaceY = nfY - nsY;
            return (float)Math.sqrt(spaceX * spaceX + spaceY * spaceY);
        }
    }
}
