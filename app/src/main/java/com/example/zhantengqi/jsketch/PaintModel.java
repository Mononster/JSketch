package com.example.zhantengqi.jsketch;

import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by zhantengqi on 2016-06-22.
 */
public class PaintModel extends Observable {
    String currentDrawingTool = "null";
    CanvasView mainView;
    ArrayList<Shape> shapes = null;
    int currentDrawingColor = Color.RED;
    int currentDrawingStroke = 2;
    float startPosX, startPosY, currPosX, currPosY;
    public PaintModel(){

    }
    public String getCurrentDrawingTool(){
        return currentDrawingTool;
    }
    public int getCurrentDrawingColor(){
        return currentDrawingColor;
    }
    public void setCurrentDrawingColor(int color){
        this.currentDrawingColor = color;
//         if user selects a shape and selects the selectTool,
//         we allow user to update the border color.
        for (int i = 0; i < mainView.getShapes().size(); i++){
            if (mainView.getShapes().get(i).getIfGetSelected()
                    && this.currentDrawingTool == "selectTool"){
                mainView.getShapes().get(i).setBorderColor(color);
                break;
            }
        }
        notifyObservers();
        setChanged();
    }
    public void setCurrentDrawingTool(String toolName){
        if (toolName != "selectTool" && currentDrawingTool == "selectTool"){
            //we need to deselect all shapes since we are using different tool.
            for (Shape temp: mainView.getShapes()){
                temp.setIfGestSelected(false);
            }
        }
        currentDrawingTool = toolName;
        resetPosition();
        notifyObservers();
        setChanged();
    }
    public void setCurrPosXY(float x, float y){
        currPosY = y;
        currPosX = x;
        notifyObservers();
        setChanged();
    }
    public void setStartPosXY(float x, float y){
        startPosY = y;
        startPosX = x;
        notifyObservers();
        setChanged();
    }
    public void setCurrentDrawingStroke(int level){
        this.currentDrawingStroke = level;
        for (int i = 0; i < mainView.getShapes().size(); i++){
            if (mainView.getShapes().get(i).getIfGetSelected()
                    && this.currentDrawingTool == "selectTool"){
                mainView.getShapes().get(i).setStroke(level);
                break;
            }
        }
        notifyObservers();
        setChanged();
    }
    public int getCurrentDrawingStroke(){
        return this.currentDrawingStroke;
    }
    public void resetPosition(){
        startPosX = 0;
        startPosY = 0;
        currPosX = 0;
        currPosY = 0;
    }
    public void setMainView(CanvasView cfv){
        this.mainView = cfv;
    }
    public CanvasView getMainView(){
        return this.mainView;
    }
    public void adjustColorAndStroke(Shape shape){

        if (shape.getFilled()){
            currentDrawingColor = shape.getFillColor();
        }
        else{
            currentDrawingColor = shape.getBorderColor();
        }
        currentDrawingStroke = shape.getBs();
        notifyObservers();
        setChanged();
    }

    public ArrayList<Shape> getShapes(){
        return mainView.getShapes();
    }
    public void setShapes(ArrayList<Shape> shapes){
        this.shapes = shapes;
    }
    public void clearCanvas(){
        mainView.clearCanvas();
    }
    public ArrayList<Shape> sortShapes(ArrayList <Shape> shapes, Point p){
        //sort the shapes in order to correctly detect which shape
        //the user wants to select.
        //ALWAYS PUT Fill Graph AT THE FRONT.
        ArrayList <Shape> newListOfShapes = new ArrayList <Shape>();
        for (int i = 0; i < shapes.size(); i++){
            Shape temp = shapes.get(i);
            if (temp.getFillColor() != -1){
                newListOfShapes.add(temp);
                shapes.remove(temp);
            }
        }
        Collections.sort(shapes, new CustomComparator(p));
        for (Shape temp2 : shapes){
            newListOfShapes.add(temp2);
        }

        return newListOfShapes;
    }

    class CustomComparator implements Comparator<Shape> {
        Point p;
        public CustomComparator(Point p){
            this.p = p;
        }
        @Override
        public int compare(Shape shape1, Shape shape2) {
            float shape1X = shape1.getCenterX();
            float shape1Y = shape1.getCenterY();
            float shape2X = shape2.getCenterX();
            float shape2Y = shape2.getCenterY();

            double dist1 = Math.sqrt((p.x - shape1X) * (p.x - shape1X) +
                    (p.y - shape1Y) * (p.y - shape1Y));
            double dist2 =  Math.sqrt((p.x - shape2X) * (p.x - shape2X) +
                    (p.y - shape2Y) * (p.y - shape2Y));
            //System.out.println(shape1X + " " + shape1Y + " " + shape2X + " " + shape2Y + " " + dist1 + " " + dist2);
            int result = (dist1 < dist2) ? -1 : 1;
            return result ;
        }
    }

    @Override
    public void notifyObservers() {
        Log.d("MVC", "Model: Observers notified");
        super.notifyObservers();
    }
    @Override
    public void addObserver(Observer observer) {
        Log.d("MVC", "Model: Observer added");
        super.addObserver(observer);
    }

    @Override
    protected void setChanged() {
        super.setChanged();
    }

    @Override
    protected void clearChanged() {
        super.clearChanged();
    }
}
