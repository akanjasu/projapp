package in.nammaapp.itskannada;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TestOptions extends Activity {
	Button practice,newtest;

	private SQLiteDatabase db;
	private DBahelper myDbHelper ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_options);
		practice=(Button) findViewById(R.id.practice);
		newtest=(Button) findViewById(R.id.newtest);
		myDbHelper = new DBahelper(this);
		 try {

		     	myDbHelper.createDataBase();

			} catch (IOException ioe) {

				Toast.makeText(getApplicationContext(),
					    ioe.toString()+"  ol", Toast.LENGTH_SHORT).show();
			}
		 

				myDbHelper.openDataBase();
				db = myDbHelper.getWritableDatabase();
				

		practice.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent i= new Intent(TestOptions.this,PracticeTestOptions.class);
			startActivity(i);
			}
		});
		newtest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Cursor c= db.rawQuery("SELECT consulted _id FROM POSTable where _id=1 and learnt = 0",null);
				if(c.moveToFirst())
				{
			Intent i= new Intent(TestOptions.this,KanWordsTestActivity.class);
			startActivity(i);
				}
				else
					Toast.makeText(getApplicationContext(),
						    "PLEASE LEARN THE NEW WORDS", Toast.LENGTH_SHORT).show();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Intent i=new Intent(this,EnglishHomeActivity.class);
	        startActivity(i);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_options, menu);
		return true;
	}

}