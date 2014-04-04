/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery
 */
package com.example.a4;
import java.util.ArrayList;
import java.util.Collections;

import android.graphics.*;
import android.util.Log;

/**
 * Class that represents a Fruit. Can be split into two separate fruits.
 */
public class Fruit {
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public Matrix transform = new Matrix();
    public int delay =  MainView.random.nextInt(100);
    
    public int s = 0;
    
    public String type = new String();
    
    public Path parabola = randomArc();
    public int r = MainView.random.nextInt(2);
    public ArrayList<float[]> points = getPoints();
    
    private int i = 0;
    
    public static int numdropped = 0;
    
    public static int[] c = { Color.BLUE, Color.GREEN, Color.RED, Color.GRAY, Color.MAGENTA, Color.CYAN, Color.YELLOW };
    
    float [] coords = new float[2];
	
    int yloc = MainView.parentHeight;
    public boolean toDraw = false;
    public boolean isDown = false;

    /**
     * A fruit is represented as Path, typically populated 
     * by a series of points 
     */
    Fruit(float[] points, String s) {
        init();
        this.type = s;
        this.path.reset();
        this.path.moveTo(points[0], points[1]);
        for (int i = 2; i < points.length; i += 2) {
            this.path.lineTo(points[i], points[i + 1]);
        }
        this.path.moveTo(points[0], points[1]);
        
        if(r == 1){
        	Collections.reverse(this.points);
        }
    }

    Fruit(Region region) {
        init();
        this.path = region.getBoundaryPath();
    }

    Fruit(Path path) {
        init();
        this.path = path;
    }

    private void init() {
        this.paint.setColor(Color.BLUE);
        this.paint.setStrokeWidth(5);
    }

    /**
     * The color used to paint the interior of the Fruit.
     */
    public int getFillColor() { return paint.getColor(); }
    public void setFillColor(int color) { paint.setColor(color); }

    /**
     * The width of the outline stroke used when painting.
     */
    public double getOutlineWidth() { return paint.getStrokeWidth(); }
    public void setOutlineWidth(float newWidth) { paint.setStrokeWidth(newWidth); }

    /**
     * Concatenates transforms to the Fruit's affine transform
     */
    public void rotate(float theta) { transform.postRotate(theta); }
    public void scale(float x, float y) { transform.postScale(x, y); }
    public void translate(float tx, float ty) { transform.postTranslate(tx, ty); }

    /**
     * Returns the Fruit's affine transform that is used when painting
     */
    public Matrix getTransform() { return transform; }
    
    public void setTransform(Matrix t) { transform = t; }


    /**
     * The path used to describe the fruit shape.
     */
    public Path getTransformedPath() {
        Path originalPath = new Path(path);
        Path transformedPath = new Path();
        originalPath.transform(transform, transformedPath);
        return transformedPath;
    }
    
    public Path randomArc(){
        //Random variable for height, width, and starting x 
    	float h;    float w;    float x;
    	Path q = new Path();
    	//Random float
        float rx = 0.2f + 0.6f * MainView.random.nextFloat();
        float r = MainView.random.nextFloat();       

        //To make sure width is not veritcal
        while (r < 0.1f){
        r = MainView.random.nextFloat();
        }

        // Use random flaot to calculate random height

        x = (MainView.parentWidth) * rx;
        w = (MainView.parentWidth) * (Math.min(rx, 1 - rx) * r);           

        h = (MainView.parentHeight) * (0.5f + 0.5f * MainView.random.nextFloat());



        //Create new QuadCurve
        q.moveTo(x - w, MainView.parentHeight);
        q.quadTo( x, MainView.parentHeight - (2 * h), x + w, MainView.parentHeight + 60);
        return q;

       }
    
    private ArrayList<float[]> getPoints() {
    	ArrayList<float[]> pointArray = new ArrayList<float[]>();
        PathMeasure pm = new PathMeasure(parabola, false);
        float length = pm.getLength();
        float distance = 0f;
        float speed = length / (MainView.random.nextInt(70 - 30 + 1) + 30);        
        float[] aCoordinates = new float[2];

        while ((distance < length)) {
            // get point from the path
            pm.getPosTan(distance, aCoordinates, null);
            pointArray.add(new float[] { aCoordinates[0], aCoordinates[1] });
            
            distance = distance + speed;
        }

        return pointArray;
    }
    
   

    /**
     * Paints the Fruit to the screen using its current affine
     * transform and paint settings (fill, outline)
     */
    public void draw(Canvas canvas) {
        // TODO BEGIN CS349
    	if (toDraw) {
    		
        int org = getFillColor();
        setOutlineWidth((float) getOutlineWidth());
        setFillColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
    	
    	canvas.drawPath(getTransformedPath(), paint);
        
    	setFillColor(org);
    	paint.setStyle(Paint.Style.FILL);
    	
    	canvas.drawPath(getTransformedPath(), paint);
    	
    	
    	
        // tell the shape to draw itself using the matrix and paint parameters
        // TODO END CS349
    	}
    }

    /**
     * Tests whether the line represented by the two points intersects
     * this Fruit.
     */
    public boolean intersects(PointF p1, PointF p2) {
        Region r1 = new Region();
        Region r2 = new Region();
        Path p = new Path();
        
        p.moveTo(p1.x, p1.y - 1);
        p.lineTo(p2.x, p2.y - 1);
        p.lineTo(p2.x, p2.y + 1);
        p.lineTo(p1.x, p1.y + 1);
        
        
        
        r1.setPath(getTransformedPath(), new Region(0,0,MainView.parentWidth,MainView.parentWidth));
        r2.setPath(p, new Region(0,0,MainView.parentWidth,MainView.parentHeight));
        
        if (contains(p1) || contains(p2)){
        	return false;
        }
        return r1.op(r2,Region.Op.INTERSECT);
    }
    
    public void move() {
    
        if (toDraw) {            
            if(i != points.size()){
            	coords = points.get(i);            	
            	i++;
            }            
            if (i == points.size() && points.size() != 0) {
            	   if(type == "Circle"){
            	    numdropped++;    
            	    MainView.cross = MainView.cross + "X ";
            	   }
            	   
            	   
            	    setFillColor(Fruit.c[MainView.random.nextInt(7)]); 
            		delay = MainView.random.nextInt(100);  
            		parabola = randomArc();
            		r = MainView.random.nextInt(2);
            		points = getPoints();
            		 if(r == 1){
            	        	Collections.reverse(this.points);
            	        }
            		i = 0;
            		
            		isDown = false; toDraw = false;
            		
            	}     
          
        
    
    if (type == "Circle"){
    	translate(coords[0], coords[1]); 
    }
    else {
    	if (s == 1) {
    	translate(0.5f,15);
    	}
    	else {
        translate(-0.5f,15);
    	}
    }
        }
    }
    
    
    public void waitToDraw(){
        if(delay <= 0 ){
            toDraw = true;
        }
        else {
            delay--;
        }
    }

    /**
     * Returns whether the given point is within the Fruit's shape.
     */
    public boolean contains(PointF p1) {
        Region region = new Region();
        region.setPath(getTransformedPath(), new Region(0,0,MainView.parentWidth,MainView.parentHeight));
        return region.contains((int) p1.x, (int) p1.y);
    }

    /**
     * This method assumes that the line represented by the two points
     * intersects the fruit. If not, unpredictable results will occur.
     * Returns two new Fruits, split by the line represented by the
     * two points given.
     */
    public Fruit[] split(PointF p1, PointF p2) {
    	//Path topPath = new Path();
    	//Path bottomPath = new Path();
    	Region top = new Region();
    	Region bottom = new Region();
        Region rt = new Region();
        Region rb = new Region();
        Path p = new Path();
        Path pr = new Path();
        
      
    	
    	 	   
    	top.setPath(getTransformedPath(), new Region(0,0,MainView.parentWidth,MainView.parentHeight));
    	bottom.setPath(getTransformedPath(), new Region(0,0,MainView.parentWidth,MainView.parentHeight));
    	Rect bounds = top.getBounds();
    	
       if((p1.y >= bounds.top && p1.y <= bounds.bottom) || (p2.y >= bounds.top && p2.y <= bounds.bottom)){
    	  if (p1.x <= p2.x){
    	   p.moveTo(p1.x, p1.y);
           p.lineTo(bounds.left, bounds.top);
           p.lineTo(bounds.right, bounds.top);
           p.lineTo(p2.x, p2.y);
           p.moveTo(p1.x, p1.y);
           
           pr.moveTo(p1.x, p1.y);
           pr.lineTo(bounds.left, bounds.bottom);
           pr.lineTo(bounds.right, bounds.bottom);
           pr.lineTo(p2.x, p2.y);
           pr.moveTo(p1.x, p1.y);
    	  }
    	  else {
    		  p.moveTo(p1.x, p1.y);
    		  p.lineTo(bounds.right, bounds.top);
              p.lineTo(bounds.left, bounds.top);              
              p.lineTo(p2.x, p2.y);
              p.moveTo(p1.x, p1.y); 
              
              pr.moveTo(p1.x, p1.y);
              pr.lineTo(bounds.right, bounds.bottom);
              pr.lineTo(bounds.left, bounds.bottom);              
              pr.lineTo(p2.x, p2.y);
              pr.moveTo(p1.x, p1.y);
    	  }
    	}
    	
       else if (p1.y <= p2.y){
    	 p.moveTo(p1.x, p1.y);
         p.lineTo(bounds.left, bounds.top);
         p.lineTo(bounds.left, bounds.bottom);
         p.lineTo(p2.x, p2.y);
         p.moveTo(p1.x, p1.y);
         
         pr.moveTo(p1.x, p1.y);
         pr.lineTo(bounds.right, bounds.top);
         pr.lineTo(bounds.right, bounds.bottom);
         pr.lineTo(p2.x, p2.y);
         pr.moveTo(p1.x, p1.y);
    	}
    	
    	else if (p1.y >= p2.y){
       	    p.moveTo(p1.x, p1.y);
         	p.lineTo(bounds.left, bounds.bottom);
            p.lineTo(bounds.left, bounds.top);            
            p.lineTo(p2.x, p2.y);
            p.moveTo(p1.x, p1.y);
            
            pr.moveTo(p1.x, p1.y);
            pr.lineTo(bounds.right, bounds.bottom);
            pr.lineTo(bounds.right, bounds.top);            
            pr.lineTo(p2.x, p2.y);
            pr.moveTo(p1.x, p1.y);
       	}
    	
    	
        rt.setPath(p, new Region(0,0,MainView.parentWidth,MainView.parentHeight));
        rb.setPath(pr, new Region(0,0,MainView.parentWidth,MainView.parentHeight));
        
        top.op(rt,Region.Op.INTERSECT);
        bottom.op(rb,Region.Op.INTERSECT);
      
    	
    	
    	    	
        toDraw = false;
	    delay = MainView.random.nextInt(100);
	    
		
		parabola = randomArc();
		r = MainView.random.nextInt(2);
		points = getPoints();
		 if(r == 1){
	        	Collections.reverse(this.points);
	        }
		i = 0;
		 
        
    	

		
		Fruit f1 = new Fruit(top);
		f1.s = 0;
		f1.setFillColor(getFillColor());
		Fruit f2 = new Fruit(bottom);
		f2.s = 1;
		f2.setFillColor(getFillColor());
		setFillColor(Fruit.c[MainView.random.nextInt(7)]); 
        return new Fruit[] { f1, f2 };
    	
    }
}
