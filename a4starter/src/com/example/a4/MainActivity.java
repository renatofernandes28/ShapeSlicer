/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery
 */
package com.example.a4;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a4complete.R;

public class MainActivity extends Activity {
    private Model model;
    private MainView mainView;
    private TitleView titleView;
    
    public static Point displaySize;
    
    
   boolean b;
    
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setTitle("CS349 A4 Demo");
        
         b = getIntent().getExtras().getBoolean("gameType");
        

        // save display size
        Display display = getWindowManager().getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);

        // initialize model
        model = new Model();

        // set view
        setContentView(R.layout.main);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // create the views and add them to the main activity
        titleView = new TitleView(this.getApplicationContext(), model);
        ViewGroup v1 = (ViewGroup) findViewById(R.id.main_1);
        v1.addView(titleView);

        mainView = new MainView(this.getApplicationContext(), model);
        ViewGroup v2 = (ViewGroup) findViewById(R.id.main_2);
        v2.addView(mainView);
        

        
        
        
        // notify all views
        model.initObservers();
        
        model.timerHandler.postDelayed(timerRunnable, 0);
        
       
    }
    

   
    public Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
        	
        	 if(Fruit.numdropped >= 6){
        		Fruit.numdropped = 0;
        		model.clear(); 
        		b = false;

        		
        		MenuView.textView.setText("Your Score: " + TitleView.count);
        		MenuView.b1.setText("PLAY AGAIN");
        		
        		
             	Intent i = new Intent(MainActivity.this, MenuView.class);
             	i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
             	startActivity(i);
             	TitleView.count = 0;
             	MainView.cross = "";
             	
             }
        	
        	if (b && Fruit.numdropped < 6 ){
        		
        		
        	
        	
       
        	for (Fruit s : model.getShapes()) {
        		
        		if (s.type == "Circle"){
                s.transform.reset();
        		}
        		s.waitToDraw();
                s.move();                              
                mainView.invalidate();
            } 
//           
           model.timerHandler.postDelayed(this, 50);
        }
        }
    };    

}
