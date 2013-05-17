package in.nammaapp.itskannada;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LearnPhraseActivity extends Activity {
Button nxt;
TextView kan,eng;
private SimpleCursorAdapter dataAdapter;
private DBahelper myDbHelper ;
private SQLiteDatabase db;
private Cursor cursor;
private Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_phrase);
		nxt=(Button) findViewById(R.id.nphrase);
		eng = (TextView) findViewById(R.id.texteng);
		kan = (TextView) findViewById(R.id.textkan);
		myDbHelper = new DBahelper(this);
		i= new Intent(this,TutorOptions.class);
		
		 try {

		     	myDbHelper.createDataBase();

			} catch (IOException ioe) {

				Toast.makeText(getApplicationContext(),
					    ioe.toString()+"  ol", Toast.LENGTH_SHORT).show();
			}
			myDbHelper.openDataBase();
			db = myDbHelper.getWritableDatabase();
			db.execSQL("UPDATE PhraseTable SET consulted = 0 WHERE done = 0");
			
			db.close();
			phrasecall();
			nxt.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					phrasecall();	
				}
			});
			
	}
	public void phrasecall()
	{
		db = myDbHelper.getWritableDatabase();
		cursor= db.rawQuery("SELECT English _id from PhraseTable where consulted = 0 ORDER BY RANDOM()", null);
		String id;
		 if(cursor.moveToFirst())
		  {
			 id=cursor.getString(cursor.getColumnIndex("_id")).toString();
			 db.execSQL("UPDATE PhraseTable SET consulted = 1 WHERE English = \""+id+"\"");
			 eng.setText(id);
			  Cursor cursor2 = db.rawQuery("SELECT Kannada _id from PhraseTable where English = \""+id+"\"",null);
			  if(cursor2.moveToFirst())
				  ;
			 kan.setText(cursor2.getString(cursor2.getColumnIndex("_id")).toString());
			 db.close();
		  }
		 else
			 
			 {
			 eng.setText("All words are done");
			 kan.setText("");
			 
			 Toast.makeText(getApplicationContext(),
					   "All words are done", Toast.LENGTH_SHORT).show();
			 
			 startActivity(i);
				
			 }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.learn_phrase, menu);
		return true;
	}

}
