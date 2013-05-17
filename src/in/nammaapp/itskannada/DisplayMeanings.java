package in.nammaapp.itskannada;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
TextToSpeech.OnInitListener {
	private DBahelper myDbHelper ;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3,tv4;
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
		setContentView(R.layout.displaymeaning);
		tv1=(TextView) findViewById(R.id.textView1);
		tv2=(TextView) findViewById(R.id.textView2);
		tv3=(TextView) findViewById(R.id.textView3);
		tv4=(TextView) findViewById(R.id.textView4);
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
					Cursor c=db.rawQuery("SELECT engword _id FROM engkacustomwords",null);
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
					
					flag = displaylist(searchword,spelled,stemspell);
					if(!flag)
						flag = suggestioncall(searchword);
					if(flag)
					{
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
					
					
				}
				catch(Exception e)
				{
					
				}
				tv2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						 Toast.makeText(getApplicationContext(),
								    "hello hi", Toast.LENGTH_SHORT).show();
							 
						// TODO Auto-generated method stub
						if(tv2.getText().length()!=0)
						{
							 Cursor cursor4 = db.rawQuery("SELECT engword _id,kameaning FROM engkacustomwords where _id = \""+tv1.getText().toString()+"\"", null);
							 if(cursor4.moveToFirst())
							 {
								 String ttsString = cursor4.getString(cursor4.getColumnIndex("kameaning"));
								 Toast.makeText(getApplicationContext(),
									    ttsString, Toast.LENGTH_SHORT).show();
								 speakOut(ttsString);
							 }
						}
					}
				});
		 
	}
	
	public Boolean displaylist(String search,String spell,String stemspell)
	{
		Boolean present=false;
		if(call(search))
		{
			present = suggestioncall(search);
		}
		else if(call(stemspell))
		{
			present=suggestioncall(stemspell);
			tv4.setText("Did you mean:");
			
		}
			
		else if(call(spell))
		{
			present=suggestioncall(spell);
			tv4.setText("Did you mean:");
		}
			 else
			  {
				  
				  tv1.setText("WORD NOT FOUND!");  
			  }			
		return present;
	
	}
	public Boolean suggestioncall(String query)
	{
		Cursor cursor = db.rawQuery("SELECT engword _id,brhkameaning FROM engkacustomwords where _id LIKE \""+query+"%\" and _id <> \""+query+"\"", null);
		 String[] columns = new String[] {
				    "_id",
				  "brhkameaning"
				  };
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
		  return true;
		  }
		  else
			  return false;
	}
	public Boolean call(String word)
	{
		Cursor cursor2 = db.rawQuery("SELECT engword _id,brhkameaning FROM engkacustomwords where _id = \""+word+"\"", null);
		  // The desired columns to be bound
		 
		 // definition1.setTypeface(Typeface.createFromAsset(getAssets(), "BRHKND.TTF"));
		  if(cursor2.moveToFirst() && cursor2.getCount()!=0)
		  {
			  tv1.setText(cursor2.getString(cursor2.getColumnIndex("_id")).toString());
			  tv2.setTypeface(Typeface.createFromAsset(getAssets(), "BRHKND.TTF"));
		      tv2.setText(cursor2.getString(cursor2.getColumnIndex("brhkameaning")).toString());
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

	
}