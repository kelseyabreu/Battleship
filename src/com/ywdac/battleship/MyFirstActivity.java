package com.ywdac.battleship;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class MyFirstActivity 
	extends Activity
{
	public final static String EXTRA_MESSAGE = "a.random.extra";
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_my_first );
        ImageView imagePreview = ( ImageView )findViewById( R.id.preview );
        
        imagePreview.setImageResource( R.drawable.background );        
    }
    
    
    public void onDestroy(Bundle savedInstanceState) {
        super.onDestroy();
    }
    
    protected void onStop(){
    	super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.activity_my_first, menu);
        return true;
    }

	
	/** Called when the user clicks the Start button */
	public void StartGame(View view) 
	{
	    // Do something in response to button
		Intent intent = new Intent(this, BattleshipActivity.class );
		//EditText editText = (EditText) findViewById(R.id.edit_message );
		//String message = editText.getText().toString();
		//intent.putExtra(EXTRA_MESSAGE, message);
		
		startActivity(intent);
	}
	
	/** Called when the user clicks the Quit button */
	public void EndGame(View view) 
	{
	    // Do something in response to button
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do You Really Want to Quit?");
        builder.setCancelable(true);
        
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                  // do nothing, since setCancelable is true, canceling is enabled
              }
          });
        
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                 
            }
        });
        
        AlertDialog alert = builder.create();
        alert.show();
	}
}