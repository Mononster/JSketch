package com.example.zhantengqi.jsketch;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by zhantengqi on 2016-06-22.
 */
public class ToolBarView extends LinearLayout implements Observer {
    private PaintModel paintModel;
    private ArrayList<Button> colorGroup = new ArrayList<Button>();
    private ArrayList<Button> lineGroup = new ArrayList<Button>();
    private ArrayList<Button> toolGroup = new ArrayList<Button>();
    private Button redButton,blackButton,blueButton,orangeButton,grayButton,greenButton,
            thick1,thick2,thick3,thick4;
    public ToolBarView(Context context,PaintModel model){
        super(context);
        this.paintModel = model;
        paintModel.addObserver(this);
        Log.d("Paint", "ToolBarView constructor");
        View.inflate(context, R.layout.toolbarview, this);
        configureToolButtons();
        configureColorButtons();
        configureLineThickness();
    }
    public void releasePress(){
        for (Button temp : colorGroup){
            temp.setSelected(false);
        }
        for (Button temp : lineGroup){
            temp.setSelected(false);
        }
    }
    public void update(Observable observable, Object data){
        int currColor = paintModel.getCurrentDrawingColor();
        int bs = paintModel.getCurrentDrawingStroke();
        releasePress();
        final int lastcolor = Color.parseColor("#ffcc99");
        final int orange = Color.parseColor("#FF6347");
        switch (currColor){
            case(Color.RED): redButton.setSelected(true); break;
            case(Color.BLUE): blueButton.setSelected(true); break;
            case(Color.GREEN): greenButton.setSelected(true); break;
        }
        if (currColor == orange){
            orangeButton.setSelected(true);
        }
        if (currColor == Color.parseColor("#C0C0C0")){
            grayButton.setSelected(true);
        }
        if (currColor == Color.parseColor("#ffcc99")){
            blackButton.setSelected(true);
        }
        if (currColor == lastcolor){
            blackButton.setSelected(true);
        }
        if (thick1 != null){
            switch(bs){
                case(2):
                    thick1.setSelected(true);
                    break;
                case(8):
                    thick2.setSelected(true);
                    break;
                case(12):
                    thick3.setSelected(true);
                    break;
                case(15):
                    thick4.setSelected(true);
                    break;
            }
        }
        invalidate();
    }

    public void configureToolButtons(){
        final Button eraseButton = (Button) findViewById(R.id.eraseButton);
        Button selectButton = (Button) findViewById(R.id.selectButton);
        Button rectButton = (Button) findViewById(R.id.rectangleButton);
        Button circleButton = (Button) findViewById(R.id.circleButton);
        Button lineButton = (Button) findViewById(R.id.lineButton);
        Button fillButton = (Button) findViewById(R.id.fillButton);
        toolGroup.add(eraseButton);
        toolGroup.add(selectButton);
        toolGroup.add(rectButton);
        toolGroup.add(circleButton);
        toolGroup.add(lineButton);
        toolGroup.add(fillButton);
        eraseButton.setOnClickListener(new ToolAdapter(paintModel, "eraserTool"));
        eraseButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deselectToolButton();
                paintModel.clearCanvas();
                eraseButton.setSelected(true);
                paintModel.setCurrentDrawingTool("eraseTool");
                return true;
            }
        });
        selectButton.setOnClickListener(new ToolAdapter(paintModel, "selectTool"));
        rectButton.setOnClickListener(new ToolAdapter(paintModel, "rectangleTool"));
        circleButton.setOnClickListener(new ToolAdapter(paintModel, "circleTool"));
        lineButton.setOnClickListener(new ToolAdapter(paintModel, "lineTool"));
        fillButton.setOnClickListener(new ToolAdapter(paintModel, "paintTool"));
    }
    public void configureColorButtons(){
        redButton = (Button) findViewById(R.id.colorRedButton);
        blackButton = (Button) findViewById(R.id.colorBlackButton);
        blueButton = (Button) findViewById(R.id.colorBlueButton);
        orangeButton = (Button) findViewById(R.id.colorOrangeButton);
        grayButton = (Button) findViewById(R.id.colorGrayButton);
        greenButton = (Button) findViewById(R.id.colorGreenButton);
        colorGroup.add(redButton);
        colorGroup.add(blackButton);
        colorGroup.add(blueButton);
        colorGroup.add(orangeButton);
        colorGroup.add(grayButton);
        colorGroup.add(greenButton);
        redButton.setOnClickListener(new ColorAdapter(paintModel, Color.RED));
        redButton.setSelected(true);
        //init red color first.
        orangeButton.setOnClickListener(new ColorAdapter(paintModel, Color.parseColor("#FF6347")));
        grayButton.setOnClickListener(new ColorAdapter(paintModel, Color.parseColor("#C0C0C0")));
        blueButton.setOnClickListener(new ColorAdapter(paintModel, Color.BLUE));
        blackButton.setOnClickListener(new ColorAdapter(paintModel, Color.parseColor("#ffcc99")));
        greenButton.setOnClickListener(new ColorAdapter(paintModel, Color.parseColor("#00ff00")));
    }
    public void configureLineThickness(){
        thick1 = (Button) findViewById(R.id.Stroke1);
        thick2 = (Button) findViewById(R.id.Stroke2);
        thick3 = (Button) findViewById(R.id.Stroke3);
        thick4 = (Button) findViewById(R.id.Stroke4);
        lineGroup.add(thick1);
        lineGroup.add(thick2);
        lineGroup.add(thick3);
        lineGroup.add(thick4);
        thick1.setOnClickListener(new StrokeAdapter(paintModel,2));
        thick1.setSelected(true);
        thick2.setOnClickListener(new StrokeAdapter(paintModel,8));
        thick3.setOnClickListener(new StrokeAdapter(paintModel,12));
        thick4.setOnClickListener(new StrokeAdapter(paintModel,15));
    }
    public void deselectColorButton(){
        for (Button colorButton : colorGroup){
            colorButton.setSelected(false);
        }
    }
    public void deselectToolButton(){
        for (Button toolButton: toolGroup){
            toolButton.setSelected(false);
        }
    }
    public void deselectLineButton(){
        for (Button lineButton : lineGroup){
            lineButton.setSelected(false);
        }
    }
    class ToolAdapter implements OnClickListener{
        PaintModel paintModel;
        String toolName;
        public ToolAdapter(PaintModel paintModel, String toolName){
            this.paintModel = paintModel;
            this.toolName = toolName;
        }
        public void onClick(View button) {
            deselectToolButton();
            button.setSelected(true);
            paintModel.setCurrentDrawingTool(toolName);
        }
    }
    class ColorAdapter implements OnClickListener{
        PaintModel paintModel;
        int color;
        public ColorAdapter(PaintModel paintModel, int color){
            this.paintModel = paintModel;
            this.color = color;
        }
        public void onClick(View button) {
            deselectColorButton();
            button.setSelected(true);
            paintModel.setCurrentDrawingColor(color);
        }
    }
    class StrokeAdapter implements  OnClickListener{
        PaintModel paintModel;
        int stroke;
        public StrokeAdapter(PaintModel paintModel, int stroke){
            this.paintModel = paintModel;
            this.stroke = stroke;
        }
        public void onClick(View button) {
            deselectLineButton();
            button.setSelected(true);
            paintModel.setCurrentDrawingStroke(stroke);
        }
    }
}
