package com.example.zhantengqi.jsketch;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by zhantengqi on 2016-06-30.
 */
public class LineShape extends Shape {
    float oldStartX;
    float oldStartY;
    float oldEndX;
    float oldEndY;
    public LineShape(){
    }
    public void draw(Canvas canvas){
        //super.paintComponent(g);
        //System.out.println(startX + " " + startY + " "+ endX + " "+ endY);
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStrokeWidth(bsLevel);
        canvas.drawLine(startX,startY,endX,endY, paint);
        if (isSelected){
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(startX - 2, startY - 3, startX + 5, startY + 3, paint);
            canvas.drawRect(endX - 2, endY - 3, endX + 5 , endY + 3, paint);
        }
    }
    public Boolean checkIfGetSelected(float x, float y){
        float EPSILON = 7f;
        float m = (startY - endY) / (startX - endX);
        float b = endY - m * endX;
        if (Math.abs(y - (m * x + b)) < EPSILON){
            isSelected = true;
            return true;
        }
        isSelected = false;
        return false;
    }
    public void resizeShape(float scale){
        if (oldStartY == 0 && oldStartX == 0) {
            oldStartY = startY;
            oldStartX = startX;
            oldEndX = endX;
            oldEndY = endY;
        }
        if (startY > endY){
            float temp = endY;
            float temp2 = endX;
            endY = startY;
            endX = startX;
            startX = temp2;
            startY = temp;
        }
        startX = oldStartX * scale;
        startY = oldStartY * scale;
        endX = oldEndX * scale;
        endY = oldEndY * scale;
        Log.d("endX: " + endX, "endY: " + endY);
    }
    public static final Parcelable.Creator<LineShape> CREATOR = new Parcelable.Creator<LineShape>() {
        public LineShape createFromParcel(Parcel in) {
            return new LineShape(in);
        }
        public LineShape[] newArray(int size) {
            return new LineShape[size];
        }
    };
    private LineShape(Parcel in) {
        super(in);
    }
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }
    public void resetValue(){
        oldStartX = 0;
        oldStartY = 0;
        oldEndX = 0;
        oldEndY = 0;
    }
}
