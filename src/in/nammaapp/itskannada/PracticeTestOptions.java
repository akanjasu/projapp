package in.nammaapp.itskannada;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PracticeTestOptions extends Activity {
	private Button btnadv;
	private Button btnpro;
	private Button btnadj;
	private Button btnver;
	private Button btncon;
	private Button btnpre;
	private Bundle bundle;
	private String key;

	private SQLiteDatabase db;
	private DBahelper myDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice_test_options);
		key="key";
		btnadv = (Button) findViewById(R.id.btnadv);//4
		btnpro = (Button) findViewById(R.id.btnpro);//1
		btnadj = (Button) findViewById(R.id.btnadj);//2
		btnver = (Button) findViewById(R.id.btnverb);//3
		btncon = (Button) findViewById(R.id.btnconj);//5
		btnpre = (Button) findViewById(R.id.btnprep);//6
		bundle = new Bundle();
		myDbHelper = new DBahelper(this);
		 try {

		     	myDbHelper.createDataBase();

			} catch (IOException ioe) {

				Toast.makeText(getApplicationContext(),
					    ioe.toString()+"  ol", Toast.LENGTH_SHORT).show();
			}
		 

				myDbHelper.openDataBase();
				db = myDbHelper.getWritableDatabase();
				

		btnpro.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Cursor c= db.rawQuery("SELECT consulted _id FROM POSTable where _id=1 and pos = 1",null);
				if(c.moveToFirst())
				{
				// TODO Auto-generated method stub
				Intent i= new Intent(PracticeTestOptions.this,PracticeTest.class);
				bundle.putInt(key,1);
				i.putExtras(bundle);
				startActivity(i);
				}
				else
					Toast.makeText(getApplicationContext(),
						    "PLEASE LEARN NEW PRONOUNS", Toast.LENGTH_SHORT).show();
				
				
			}
		});
		
		btnadj.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Cursor c= db.rawQuery("SELECT consulted _id FROM POSTable where _id=1 and pos = 2",null);
				if(c.moveToFirst())
				{
				Intent i= new Intent(PracticeTestOptions.this,PracticeTest.class);
				bundle.putInt(key,2);
				i.putExtras(bundle);
				startActivity(i);
				}
				else
					Toast.makeText(getApplicationContext(),
						    "PLEASE LEARN NEW ADJECTIVES", Toast.LENGTH_SHORT).show();
			}
		});
		
		btnver.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Cursor c= db.rawQuery("SELECT consulted _id FROM POSTable where _id=1 and pos = 3",null);
				if(c.moveToFirst())
				{
				Intent i= new Intent(PracticeTestOptions.this,PracticeTest.class);
				bundle.putInt(key,3);
				i.putExtras(bundle);
				startActivity(i);
				}
				else
					Toast.makeText(getApplicationContext(),
						    "PLEASE LEARN NEW VERBS", Toast.LENGTH_SHORT).show();
			}
		});

		btnadv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 	TODO Auto-generated method stub
				Cursor c= db.rawQuery("SELECT consulted _id FROM POSTable where _id=1 and pos = 4",null);
				if(c.moveToFirst())
				{
				Intent i= new Intent(PracticeTestOptions.this,PracticeTest.class);
				bundle.putInt(key,4);
				i.putExtras(bundle);
				startActivity(i);
				}
				else
					Toast.makeText(getApplicationContext(),
						    "PLEASE LEARN NEW ADVERBS", Toast.LENGTH_SHORT).show();
			}
		});
		
		btncon.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Cursor c= db.rawQuery("SELECT consulted _id FROM POSTable where _id=1 and pos = 5",null);
				if(c.moveToFirst())
				{
				Intent i= new Intent(PracticeTestOptions.this,PracticeTest.class);
				bundle.putInt(key,5);
				i.putExtras(bundle);
				startActivity(i);
				}
				else
					Toast.makeText(getApplicationContext(),
						    "PLEASE LEARN NEW CONJUCTIONS", Toast.LENGTH_SHORT).show();
			}
		});

		btnpre.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Cursor c= db.rawQuery("SELECT consulted _id FROM POSTable where _id=1 and pos = 6",null);
				if(c.moveToFirst())
				{
				Intent i= new Intent(PracticeTestOptions.this,PracticeTest.class);
				bundle.putInt(key,6);
				i.putExtras(bundle);
				startActivity(i);
				}
				else
					Toast.makeText(getApplicationContext(),
						    "PLEASE LEARN NEW PREPOSITIONS", Toast.LENGTH_SHORT).show();
			}
		});


	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Intent i=new Intent(this,TestOptions.class);
	        startActivity(i);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practice_test_options, menu);
		return true;
	}

}