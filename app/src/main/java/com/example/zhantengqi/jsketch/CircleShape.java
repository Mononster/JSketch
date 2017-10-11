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
public class CircleShape extends Shape {
    float radius;
    float centerX;
    float centerY;
    float oldRadius = 0;
    float oldCenterX;
    float oldCenterY;
    public CircleShape(){
    }
    public void draw(Canvas canvas){
        //super.paintComponent(g);
        //System.out.println(startX + " " + startY + " "+ endX + " "+ endY);
        Paint paint = new Paint();
        paint.setStrokeWidth(bsLevel);
        int length = bsLevel;
        if (length == 0){
            length = 2;
        }
        radius = Math.abs(startY - endY) / 2 + length;

        if (startX - endX > 0 && startY - endY > 0){
            // if the mouse position is at north west side of start position
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(endX, endY, radius ,paint);
            if (isFilled){
                paint.setColor(fillColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(endX , endY, radius - length / 2, paint);
            }
            centerX = endX ;
            centerY = endY ;
        }
        if (startX - endX < 0 && startY - endY < 0){
            // if the cursor is at south east side of the start position
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(startX,startY, radius , paint);
            if (isFilled){
                paint.setColor(fillColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(startX, startY, radius - length / 2, paint);
            }
            centerX = startX;
            centerY = startY;
        }
        if (startX - endX > 0 && startY - endY < 0){
            // if the cursor is at the south west side of the start position.
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(endX, startY, radius, paint);
            if (isFilled){
                paint.setColor(fillColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(endX, startY, radius - length / 2, paint);
            }

            centerX = endX ;
            centerY = startY;
        }
        if (startX - endX < 0 && startY - endY > 0){
            // if the cursor is at north east side of the start.
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(startX, endY, radius, paint);
            if (isFilled){
                paint.setColor(fillColor);
                canvas.drawCircle(startX, endY,radius - length / 2, paint);
            }
            centerX = startX;
            centerY = endY;

        }

        if (isSelected){
            paint.setColor(Color.BLACK);
            // if the shape get selected, draw some rects to indicate it.
            leftSideX = centerX - (int)(radius/Math.sqrt(2));
            rightSideX = centerX + (int)(radius/Math.sqrt(2)) - 5;
            upperSideY = centerY - (int)(radius/Math.sqrt(2));
            bottomSideY = centerY + (int)(radius/Math.sqrt(2)) - 5;
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(leftSideX - 3, upperSideY - 3, leftSideX + 7, upperSideY + 6, paint);
            canvas.drawRect(leftSideX - 3, bottomSideY - 3,leftSideX + 7, bottomSideY + 6, paint);
            canvas.drawRect(rightSideX + 2, upperSideY - 3, rightSideX + 11, upperSideY + 6, paint);
            canvas.drawRect(rightSideX + 2, bottomSideY - 3, rightSideX + 11, bottomSideY + 6, paint);
        }
    }
    public Boolean checkIfGetSelected(float x, float y){
        // System.out.println(x + " " + y);
        // System.out.println(centerX + " " + centerY + " " + radius);
        if ((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) <= radius * radius){
            isSelected = true;
            return true;
        }
        isSelected = false;
        return false;
    }
    public void resizeShape(float scale){

        if (oldRadius == 0){
            oldRadius = radius;
            oldCenterX = centerX;
            oldCenterY = centerY;
        }
        if (startX > endX){
            float temp = endX;
            endX = startX;
            startX = temp;
        }
        if (startY > endY){
            float temp = endY;
            endY = startY;
            startY = temp;
        }

        startX = oldCenterX - (oldRadius * scale) / 2;
        startY = oldCenterY - (oldRadius * scale) / 2;
        endX =  oldCenterX + (oldRadius * scale) / 2;
        endY = oldCenterY + (oldRadius * scale) / 2;
        Log.d("endX: " + centerX, "endY: " + centerY);
    }
    public float getCenterX(){
        return centerX;
    }
    public float getCenterY(){
        return centerY;
    }
    public static final Parcelable.Creator<CircleShape> CREATOR = new Parcelable.Creator<CircleShape>() {
        public CircleShape createFromParcel(Parcel in) {
            return new CircleShape(in);
        }
        public CircleShape[] newArray(int size) {
            return new CircleShape[size];
        }
    };
    private CircleShape(Parcel in) {
        super(in);
        radius = in.readFloat();
        centerX = in.readFloat();
        centerY = in.readFloat();
    }
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeFloat(radius);
        out.writeFloat(centerX);
        out.writeFloat(centerY);
    }
    public void resetValue(){
        oldRadius = 0;
    }
}
