/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/*
 * View of the main game area.
 * Displays pieces of fruit, and allows players to slice them.
 */
public class MainView extends View implements Observer {
    private final Model model;
    private final MouseDrag drag = new MouseDrag();
    public static int parentWidth = 0;
    public static int parentHeight = 0;
    public boolean addFruit = true;
    public static String cross = new String();
    
    //Canvas canvas = new Canvas();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    
    
   
    
    float downx = 0, downy = 0, upx = 0, upy = 0;
    
    public static Random random = new Random();
    
    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
    parentWidth = MeasureSpec.getSize(widthMeasureSpec);
    parentHeight = MeasureSpec.getSize(heightMeasureSpec);
    
    if(addFruit){
    	
    	Path circle = new Path(); 
        circle.addCircle(0, 0, 30, Path.Direction.CW);   	
    
    Fruit f1 = new Fruit(circle);
    f1.type = "Circle";
    f1.setFillColor(Fruit.c[random.nextInt(7)]); 
   
    model.add(f1);
    
    Fruit f2 = new Fruit(circle);
    f2.type = "Circle";
    f2.setFillColor(Fruit.c[random.nextInt(7)]); 
  
    model.add(f2);
  

    addFruit = false;
    
    }
    
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    
    
    //Handler timerHandler = new Handler();
    
    
    
   
    

    // Constructor
    MainView(Context context, Model m) {
        super(context);       
        
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //requestLayout();
        paint.setStrokeWidth(5);
        paint.setColor(Color.WHITE);    
        
     

        // register this view with the model
        model = m;
        model.addObserver(this);

        // TODO BEGIN CS349
        // test fruit, take this out before handing in!
       


       //this.setVisibility(INVISIBLE);

       
      
        
        
        // TODO END CS349

        // add controller
        // capture touch movement, and determine if we intersect a shape
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Log.d(getResources().getString(R.string.app_name), "Touch down");
                    	downx = event.getX();
                        downy = event.getY();
                        upx = event.getX();
                        upy = event.getY();
                        drag.start(event.getX(), event.getY());
                        break;
                        
                    case MotionEvent.ACTION_MOVE:
                    	upx = event.getX();
                        upy = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        // Log.d(getResources().getString(R.string.app_name), "Touch release");
                        drag.stop(event.getX(), event.getY());
                        downx = event.getX(); upx = event.getX();
                        downy = event.getY(); upy = event.getY();
                        
                       
                        
                        invalidate();

                        // find intersected shapes
                        Iterator<Fruit> i = model.getShapes().iterator();
                        while(i.hasNext()) {
                            Fruit s = i.next();
                            if (s.intersects(drag.getStart(), drag.getEnd()) && s.type == "Circle") {
                            	TitleView.count++;
                            	if (TitleView.count % 20 == 0){
                            		Path circle = new Path(); 
                            	    circle.addCircle(0, 0, 30, Path.Direction.CW);

                            	    Fruit f1 = new Fruit(circle);
                            	    f1.type = "Circle";
                            	    f1.setFillColor(Fruit.c[random.nextInt(7)]); 
                            		   
                            	    model.add(f1);
                            	}
                            	//s.setFillColor(Color.RED);
                                try {
                                    Fruit[] newFruits = s.split(drag.getStart(), drag.getEnd());

                                    // TODO BEGIN CS349
                                    // you may want to place the fruit more carefully than this
                                    //newFruits[0].translate(0, 0);
                                    //newFruits[1].translate(10, 10 );
                                    newFruits[0].toDraw = true;
                                    newFruits[1].toDraw = true;
                                    		
                                    // TODO END CS349
                                    model.add(newFruits[0]);
                                    model.add(newFruits[1]);
                                    invalidate();

                                    // TODO BEGIN CS349
                                    // delete original fruit from model
                                    // TODO END CS349

                                } catch (Exception ex) {
                                    Log.e("fruit_ninja", "Error: " + ex.getMessage());
                                }
                            } else {
                               
                            }
                            invalidate();
                        }
                        break;
                }
                return true;
            }
        });
        
        
    }

    // inner class to track mouse drag
    // a better solution *might* be to dynamically track touch movement
    // in the controller above
    class MouseDrag {
    	
        private float startx, starty;
        private float endx, endy;

        protected PointF getStart() { return new PointF(startx, starty); }
        protected PointF getEnd() { return new PointF(endx, endy); }

        protected void start(float x, float y) {
            this.startx = x;
            this.starty = y;
        }

        protected void stop(float x, float y) {
            this.endx = x;
            this.endy = y;
        }
    }
    
    
    


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw background
        setBackgroundColor(Color.BLACK);
        
        paint.setColor(Color.WHITE);
        canvas.drawLine(downx, downy, upx, upy, paint);  
        
        
        	paint.setColor(Color.RED);
        	paint.setStrokeWidth(5);
        	paint.setStyle(Paint.Style.STROKE);
        	paint.setTextSize(20); 
        	canvas.drawText(cross,10,20, paint);            
            
            // draw all pieces of fruit
        for (Fruit s : model.getShapes()) {
            s.draw(canvas);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        invalidate();
        
    }
}
