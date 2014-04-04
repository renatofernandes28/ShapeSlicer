/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.a4complete.R;

 
public class MenuView extends Activity {
	public static TextView textView;
	public static Button b1;

 
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu);
		
		textView = (TextView) findViewById(R.id.textView1);
		b1 = (Button) findViewById(R.id.button1);
 
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			public void onClick(View V) {
				Log.d("DEBUG", "One Player Button Pressed!");
				Intent intent = new Intent(MenuView.this, MainActivity.class);
				intent.putExtra("gameType", true);				
				startActivityForResult(intent, 0);
			}
		});
 
		
	}
	
	
}