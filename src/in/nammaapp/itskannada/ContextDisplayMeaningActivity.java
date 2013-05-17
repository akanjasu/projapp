package in.nammaapp.itskannada;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ContextDisplayMeaningActivity extends Activity implements
TextToSpeech.OnInitListener {
	private DBahelper myDbHelper ;
	private TextView tv1;
	private TextView tv2;
	private ListView lv1;
	private TextView word1;
	private TextView definition1;
	private SQLiteDatabase db;
	private String searchword;
	private String spelled;
	private String stemspell;
	private SimpleCursorAdapter dataAdapter;
	private TextToSpeech tts;
	Map<String,Integer> dictionary;
	private Boolean flag;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_context_display_meaning);
		tv1=(TextView) findViewById(R.id.dym);
		tv2=(TextView) findViewById(R.id.texteng);
		lv1=(ListView) findViewById(R.id.meaningslist);
		
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
					Cursor c=db.rawQuery("SELECT engword _id FROM ClientContextTable",null);
					String[] strarray = new String[c.getCount()];
					c.moveToFirst();   

			           int counter = 0;
			           while(c.moveToNext()){
			               String str = c.getString(c.getColumnIndex("_id"));
			               strarray[counter] = str;
			               counter++;
			            }
			           dictionary = new HashMap<String, Integer>();
			   		String line = "";
			   		
			   			for(int i=0;i<counter;i++)
			   				 dictionary.put(strarray[i], 1);
			   			Spelling sp = new Spelling(dictionary);
						 spelled = sp.correct(searchword);
			   			englishStemmer es = new englishStemmer();
						es.setCurrent(searchword);
						es.stem();
						stemspell = sp.correct(es.getCurrent());
					
					displaylist(searchword,spelled,stemspell);
					
					lv1.setOnItemClickListener(new OnItemClickListener() {
						   @Override
						   public void onItemClick(AdapterView<?> listView, View view, 
						     int position, long id) {
						   // Get the cursor, positioned to the corresponding row in the result set
						   Cursor cursor = (Cursor) listView.getItemAtPosition(position);
						 
						   // Get the state's capital from this row in the database.
						   String engword = 
						   tv2.getText().toString();
						   Cursor cursor3 = db.rawQuery("SELECT kanword _id,engcontext FROM ClientContextTable where engword = \""+engword+"\"", null);
						 if(cursor3.moveToFirst())
						 {
							 String ttsString = cursor3.getString(cursor3.getColumnIndex("_id"));
							 Toast.makeText(getApplicationContext(),
								    ttsString, Toast.LENGTH_SHORT).show();
							 speakOut(ttsString);
						 }
						   }
						  });
					
					
					
				}
				catch(Exception e)
				{
					
				}
			
		 
	}
	
	public void displaylist(String search,String spell,String stemspell)
	{
		
		if(call(search))
		{
			
		}
		else if(call(stemspell))
		{
			
			tv1.setText("Did you mean:");
			
		}
			
		else if(call(spell))
		{
			
			tv1.setText("Did you mean:");
		}
			 else
			  {
				  
				  tv2.setText("WORD NOT FOUND!");  
			  }			
		
	
	}

	public Boolean call(String word)
	{
		Cursor cursor2 = db.rawQuery("SELECT kanword _id,engcontext FROM ClientContextTable where engword = \""+word+"\"", null);
		  // The desired columns to be bound
		 
		 // definition1.setTypeface(Typeface.createFromAsset(getAssets(), "BRHKND.TTF"));
		  if(cursor2.moveToFirst() && cursor2.getCount()!=0)
		  {
			  tv2.setText(word);
			  String[] columns = new String[] {
					    "_id",
					  "engcontext"
					  };
			 int[] to = new int[] { 
			    R.id.texteng,
			    R.id.textcontext,
			    };
			 
			  // create the adapter using the cursor pointing to the desired data 
			  //as well as the layout information
			  dataAdapter = new SimpleCursorAdapter(
			    this, R.layout.contextmeaningsrow, 
			    cursor2, 
			    columns, 
			    to,
			    0);
			  lv1.setAdapter(dataAdapter);
			  return true;
			  
		  }
		  else return false;
		  
		  // the XML defined views which the data will be bound to
		
			

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
			tts.setSpeechRate((float) 0.7);
			tts.setPitch((float)3.5);
			
			
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.context_display_meaning, menu);
		return true;
	}

	

}
