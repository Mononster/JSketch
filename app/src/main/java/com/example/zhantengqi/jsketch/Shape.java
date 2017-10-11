package com.example.zhantengqi.jsketch;

import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhantengqi on 2016-06-30.
 */
public abstract class Shape implements Parcelable {
    protected float startX,startY,endX,endY,leftSideX,upperSideY,rightSideX,bottomSideY;
    protected int borderColor;
    protected int fillColor;
    protected Boolean isFilled = false;
    protected Boolean isSelected = false;
    protected int bsLevel = 0;

    public Shape(){
    }

    protected Shape(Parcel in) {
        startX = in.readFloat();
        startY = in.readFloat();
        endX = in.readFloat();
        endY = in.readFloat();
        leftSideX = in.readFloat();
        upperSideY = in.readFloat();
        rightSideX = in.readFloat();
        bottomSideY = in.readFloat();
        borderColor = in.readInt();
        fillColor = in.readInt();
        isFilled = in.readByte() != 0;     //myBoolean == true if byte != 0
        isSelected = in.readByte() != 0;     //myBoolean == true if byte != 0
        bsLevel = in.readInt();
    }

    public void readParcel(Parcel in){

    }
    public void setStroke(int bs){
        this.bsLevel = bs;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeFloat(startX);
        out.writeFloat(startY);
        out.writeFloat(endX);
        out.writeFloat(endY);
        out.writeFloat(leftSideX);
        out.writeFloat(upperSideY);
        out.writeFloat(rightSideX);
        out.writeFloat(bottomSideY);
        out.writeInt(borderColor);
        out.writeInt(fillColor);
        out.writeByte((byte) (isFilled ? 1 : 0));
        out.writeByte((byte) (isSelected ? 1 : 0));
        out.writeInt(bsLevel);
    }


    public void resizeShape(float scale){

    }
    public Boolean getFilled(){
        return isFilled;
    }
    public void setStartXY(float x, float y){
        this.startX = x;
        this.startY = y;
    }
    public void setEndXY(float x, float y){
        this.endX = x;
        this.endY = y;
    }
    public void setBorderColor(int color){
        this.borderColor = color;
    }
    public void setFillColor(int color){
        this.fillColor = color;
    }
    public void setFill(Boolean toset){
        isFilled = toset;
    }
    public void setSelected(Boolean var){
        this.isSelected = var;
    }
    public int getFillColor(){
        if (this instanceof LineShape) return -1;
        return this.fillColor;
    }
    public int getBorderColor(){
        return this.borderColor;
    }
    public void setBs(int bs){
        this.bsLevel = bs;
    }
    public int getBs(){
        return bsLevel;
    }
    public Boolean getIfGetSelected(){
        return isSelected;
    }
    public void setIfGestSelected(Boolean toset){
        this.isSelected = toset;
    }
    public void adjustPosition(float x, float y){
        startX = startX + x;
        startY = startY + y;
        endX = endX + x;
        endY = endY + y;
    }
    public void resetValue(){

    }
    public float getCenterX(){
        return 0;
    }
    public float getCenterY(){
        return 0;
    }
    public abstract void draw(Canvas canvas);
    public abstract Boolean checkIfGetSelected(float x, float y);
}
