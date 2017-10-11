package com.example.zhantengqi.jsketch;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by zhantengqi on 2016-06-30.
 */
public class RectangleShape extends Shape{
    Rect rect;
    float centerX;
    float centerY;
    float width;
    float height;
    int rectX;
    int rectY;
    int rectEndX;
    int rectEndY;
    float oldWidth;
    float oldHeight;
    float oldCenterX;
    float oldCenterY;
    public RectangleShape(){
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(bsLevel);
        int length = bsLevel;
        if (length == 0){
            length = 2;
        }
        if (startX - endX > 0 && startY - endY > 0){
            // if the mouse position is at north west side of start position
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(endX, endY, startX ,startY, paint);
            width = startX - endX;
            height = startY - endY;
            if (isFilled){
                paint.setColor(fillColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(endX + length / 2, endY + length / 2, startX
                        - length / 2 , startY - length / 2, paint);
            }
            rect = new Rect(Math.round(endX - length) , Math.round(endY - length),
                    Math.round(startX + 2 * length) ,Math.round(startY + 2 * length));
            rectX = Math.round(endX - length);
            rectY = Math.round(endY - length);
            rectEndX = Math.round(startX + 2 * length);
            rectEndY = Math.round(startY + 2 * length);

            centerX = endX + (startX - endX) / 2;
            centerY = endY + (startY - endY) / 2;
        }
        if (startX - endX < 0 && startY - endY < 0){
            // if the cursor is at south east side of the start position
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(startX, startY, endX, endY, paint);
            width = endX - startX;
            height = endY - startY;
            if (isFilled){
                paint.setColor(fillColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(startX + length / 2, startY + length / 2,
                        endX - length / 2, endY - length / 2, paint);
            }
            rect = new Rect(Math.round(startX - length) ,Math.round(startY - length)
                    , Math.round(endX + 2 * length) , Math.round(endY + 2 * length));
            rectX = Math.round(startX - length);
            rectY = Math.round(startY - length);
            rectEndX = Math.round(endX + 2 * length);
            rectEndY = Math.round(endY + 2 * length);
            centerX = startX + (endX - startX) / 2;
            centerY = startY + (endY - startY) / 2;
        }
        if (startX - endX > 0 && startY - endY < 0){
            // if the cursor is at the south west side of the start position.
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(endX, startY, startX , endY, paint);
            width = startX - endX;
            height = endY - startY;
            if (isFilled){
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(fillColor);
                canvas.drawRect(endX + length / 2, startY + length / 2,
                        startX - length / 2, endY - length / 2,paint);
            }
            rect = new Rect(Math.round(endX - length), Math.round(startY - length),
                    Math.round(startX + 2 * length), Math.round(endY + 2 * length));
            rectX = Math.round(endX - length);
            rectY = Math.round(startY - length);
            rectEndX = Math.round(startX + 2 * length);
            rectEndY = Math.round(endY + 2 * length);
            centerX = endX + (startX - endX) / 2;
            centerY = startY + (endY - startY) / 2;
        }
        if (startX - endX < 0 && startY - endY > 0){
            // if the cursor is at north east side of the start.
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(startX, endY, endX, startY, paint);
            width = endX - startX;
            height = startY - endY;
            if (isFilled){
                paint.setColor(fillColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(startX + length / 2, endY + length / 2,
                        endX - length / 2 ,startY - length / 2, paint);
            }
            rect = new Rect(Math.round(startX - length), Math.round(endY - length),
                    Math.round(endX + 2 * length), Math.round(startY + 2 * length));
            rectX = Math.round(startX - length);
            rectY = Math.round(endY - length);
            rectEndX = Math.round(endX + 2 * length);
            rectEndY = Math.round(startY + 2 * length);
            centerX = startX + (endX - startX) / 2;
            centerY = endY + (startY - endY) / 2;
        }
        if (isSelected){
            paint.setColor(Color.BLACK);
            // if the shape get selected, draw some rects to indicate it.
            leftSideX = rect.left - 3 + length;
            upperSideY = rect.top - 3 + length;
            rightSideX = rect.right - 2 * length;
            bottomSideY = rect.bottom - 2 * length;
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(leftSideX - 1, upperSideY - 3, leftSideX + 8,upperSideY + 6, paint);
            canvas.drawRect(leftSideX - 1, bottomSideY - 3, leftSideX + 8,bottomSideY + 6, paint);
            canvas.drawRect(rightSideX - 3, upperSideY - 3, rightSideX + 6, upperSideY + 6, paint);
            canvas.drawRect(rightSideX - 3, bottomSideY - 3, rightSideX + 6, bottomSideY + 6, paint);
        }
    }
    public Boolean checkIfGetSelected(float x, float y){
        if (rect != null && rect.contains(Math.round(x),Math.round(y))){
            isSelected = true;
            return true;
        }else{
            isSelected = false;
            return false;
        }
    }
    public void resizeShape(float scale){

        if (oldWidth == 0 && oldHeight == 0){
            Log.d("hrere","Reset");
            oldWidth = width;
            oldHeight = height;
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
        startX = oldCenterX - (oldWidth * scale) / 2;
        startY = oldCenterY - (oldHeight * scale) / 2;
        endX =  oldCenterX + (oldWidth * scale) / 2;
        endY = oldCenterY + (oldHeight * scale) / 2;
        Log.d("endX: " + endX, "endY: " + endY);
    }

    public float getCenterX(){
        return centerX;
    }
    public float getCenterY(){
        return centerY;
    }
    public static final Parcelable.Creator<RectangleShape> CREATOR = new Parcelable.Creator<RectangleShape>() {
        public RectangleShape createFromParcel(Parcel in) {
            return new RectangleShape(in);
        }
        public RectangleShape[] newArray(int size) {
            return new RectangleShape[size];
        }
    };
    private RectangleShape(Parcel in) {
        super(in);
        centerX = in.readFloat();
        centerY = in.readFloat();
        width = in.readFloat();
        height = in.readFloat();
        rect = new Rect(in.readInt(),in.readInt(),in.readInt(),in.readInt());
    }
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeFloat(centerX);
        out.writeFloat(centerY);
        out.writeFloat(width);
        out.writeFloat(height);
        out.writeInt(rectX);
        out.writeInt(rectY);
        out.writeInt(rectEndX);
        out.writeInt(rectEndY);
    }
    public void resetValue(){
        oldWidth = 0;
        oldHeight = 0;
    }
}
