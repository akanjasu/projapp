package in.nammaapp.itskannada;

import java.io.IOException;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PhraseSearchActivity extends Activity {
ImageButton phrasebtn;
EditText phrasetxt;
TextView phrase;
private DBahelper myDbHelper ;
private SimpleCursorAdapter dataAdapter;
private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phrase_search_activity);
		phrasebtn=(ImageButton) findViewById(R.id.phrasebtn);
		phrasetxt=(EditText) findViewById(R.id.phraseText);
		phrase=(TextView) findViewById(R.id.phrase);
		 myDbHelper = new DBahelper(this);
		 try {

		     	myDbHelper.createDataBase();

			} catch (IOException ioe) {

				throw new Error("Unable to create database");

			}
		 phrasebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				   new Handler().postDelayed(new Runnable() {
		  				@Override
		  				public void run() {
		  					phrasebtn.setBackgroundColor(0xFF000000);
		  				}
		  			}, 1000);
				   phrasebtn.setBackgroundColor(0xFF000099);	
				   if(phrasetxt.getText().toString().length()!=0)
				   		{
					   try {

						   myDbHelper.openDataBase();
						   db = myDbHelper.getWritableDatabase();
						   Cursor cursor=db.rawQuery("SELECT English _id,Kannada FROM PhraseTable where _id=\""+phrasetxt.getText().toString()+"\"",null);
						   if(cursor.getCount() != 0)
							  {
							   if(cursor.moveToFirst())
							   {
							   phrase.setBackgroundColor(0xffAAAAAA);
							   phrase.setTextColor(0xff000000);
							   phrase.setText(cursor.getString(cursor.getColumnIndex("Kannada")).toString());
							   }
							  }
						   else
						   		{
							   phrase.setText("PHRASE NOT FOUND!!");
							   phrase.setTextColor(0xffFF0000);
						   		}
							  }
					   	   
					   catch(Exception e)
					   		{
						   Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
					   		}
				   		}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.phrase_search, menu);
		return true;
	}

}