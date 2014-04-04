/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import com.example.a4complete.R;

import java.util.Observable;
import java.util.Observer;

/*
 * View to display the Title, and Score
 * Score currently just increments every time we get an update
 * from the model (i.e. a new fruit is added).
 */
public class TitleView extends TextView implements Observer {
    public static int count = 0;
    private final Model m;

    // Constructor requires model reference
    public TitleView(Context context, Model model) {
        super(context);

        // set width, height of this view
        this.setHeight(235);
        this.setWidth(MainActivity.displaySize.x);
        
        //this.setVisibility(INVISIBLE);

        // register with model so that we get updates
        m = model;
        m.addObserver(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO BEGIN CS349
        // add high score, anything else you want to display in the title
        // TODO END CS349
        setBackgroundColor(Color.BLACK);
        setGravity(Gravity.CENTER);
        setTextSize(20);
        setText(getResources().getString(R.string.app_title));        
        append("\n" + "\n" + "                                                          " + count);
    }

    // Update from model
    // ONLY useful for testing that the view notifications work
    @Override
    public void update(Observable observable, Object data) {
        // TODO BEGIN CS349
        // do something more meaningful here
        // TODO END CS349
        //count++;
        invalidate();
    }
}
