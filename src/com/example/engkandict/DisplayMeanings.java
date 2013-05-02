package com.example.engkandict;

import java.io.IOException;
import java.util.Locale;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMeanings extends Activity implements
TextToSpeech.OnInitListener, OnClickListener {
	private DBahelper myDbHelper ;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private ListView lv1;
	private TextView word1;
	private TextView definition1;
	private SQLiteDatabase db;
	private String searchword;
	private SimpleCursorAdapter dataAdapter;
	private TextToSpeech tts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaymeaning);
		tv1=(TextView) findViewById(R.id.textView1);
		tv2=(TextView) findViewById(R.id.textView2);
		tv3=(TextView) findViewById(R.id.textView3);
		lv1=(ListView) findViewById(R.id.suggestionlist);
		word1=(TextView) findViewById(R.id.word1);
		 definition1=(TextView) findViewById(R.id.definition1);
		 searchword=getIntent().getStringExtra("searchword").toString();
			tts = new TextToSpeech(this, this);
		 //definition1.setTypeface(Typeface.createFromAsset(getAssets(), "BRHKND.TTF"));
			 myDbHelper = new DBahelper(this);
			 try {

			     	myDbHelper.createDataBase();

				} catch (IOException ioe) {

					throw new Error("Unable to create database");

				}

				try {

					myDbHelper.openDataBase();
					db = myDbHelper.getWritableDatabase();
					 Cursor cursor2 = db.rawQuery("SELECT engword _id,brhkameaning FROM engkacustomwords where _id = \""+searchword+"\"", null);
					  Cursor cursor = db.rawQuery("SELECT engword _id,brhkameaning FROM engkacustomwords where _id LIKE \"%"+searchword+"%\" and _id <> \""+searchword+"\"", null);
					  // The desired columns to be bound
					  String[] columns = new String[] {
					    "_id",
					  "brhkameaning"
					  };
					 // definition1.setTypeface(Typeface.createFromAsset(getAssets(), "BRHKND.TTF"));
					  if(cursor2.moveToFirst())
					  {
						  tv1.setText(cursor2.getString(cursor2.getColumnIndex("_id")).toString());
						  tv2.setTypeface(Typeface.createFromAsset(getAssets(), "BRHKND.TTF"));
					      tv2.setText(cursor2.getString(cursor2.getColumnIndex("brhkameaning")).toString());
					  }
					  else
					  {
						  tv1.setText("WORD NOT FOUND!");  
					  }
					  
					  // the XML defined views which the data will be bound to
					 int[] to = new int[] { 
					    R.id.word1,
					    R.id.definition1,
					    };
					 
					  // create the adapter using the cursor pointing to the desired data 
					  //as well as the layout information
					  dataAdapter = new SimpleCursorAdapter(
					    this, R.layout.meaningslayout, 
					    cursor, 
					    columns, 
					    to,
					    0);
					 
					 // ListView listView = (ListView) findViewById(R.id.suggestionlist);
					  // Assign adapter to ListView
					  if(cursor.getCount() != 0)
					  {
						  tv3.setText("Word Suggestions");
					  lv1.setAdapter(dataAdapter);
					  lv1.setOnItemClickListener(new OnItemClickListener() {
						   @Override
						   public void onItemClick(AdapterView<?> listView, View view, 
						     int position, long id) {
						   // Get the cursor, positioned to the corresponding row in the result set
						   Cursor cursor = (Cursor) listView.getItemAtPosition(position);
						 
						   // Get the state's capital from this row in the database.
						   String engword = 
						    cursor.getString(cursor.getColumnIndexOrThrow("_id"));
						   Cursor cursor3 = db.rawQuery("SELECT engword _id,kameaning FROM engkacustomwords where _id = \""+engword+"\"", null);
						 if(cursor3.moveToFirst())
						 {
							 String ttsString = cursor3.getString(cursor3.getColumnIndex("kameaning"));
							 Toast.makeText(getApplicationContext(),
								    ttsString, Toast.LENGTH_SHORT).show();
							 speakOut(ttsString);
						 }
						   }
						  });
					  }
						}catch(Exception sqle){

						tv1.setText(sqle.toString());

				}
				
		 
	}
	public void onClick(View arg0) {
		if(tv2.getText().length()!=0)
		{
			 Cursor cursor4 = db.rawQuery("SELECT engword _id,kameaning FROM engkacustomwords where _id = \""+tv2.getText().toString()+"\"", null);
			 if(cursor4.moveToFirst())
			 {
				 String ttsString = cursor4.getString(cursor4.getColumnIndex("kameaning"));
				 Toast.makeText(getApplicationContext(),
					    ttsString, Toast.LENGTH_SHORT).show();
				 speakOut(ttsString);
			 }
		}
	    }
	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			// tts.setPitch(5); // set pitch level

			// tts.setSpeechRate(2); // set speech speed rate

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {
				//btnSpeak.setEnabled(true);
				//speakOut();
			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}

	}

	private void speakOut(String ttsString) {

		//String text = txtText.getText().toString();

		tts.speak(ttsString, TextToSpeech.QUEUE_FLUSH, null);

			}
}