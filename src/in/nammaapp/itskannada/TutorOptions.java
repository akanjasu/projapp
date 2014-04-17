package in.nammaapp.itskannada;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TutorOptions extends Activity {

	private Button btnadv;
	private Button btnpro;
	private Button btnadj;
	private Button btnver;
	private Button btncon;
	private Button btnpre;
	private Button btncmn;
	private Button btntest;
	private Bundle bundle;
	private SimpleCursorAdapter dataAdapter;
	private SQLiteDatabase db;
	private DBahelper myDbHelper ;
	private ProgressBar probar,verbar,adjbar,advbar,conbar,prebar;
	private static int pro=21,adj=26,ver=23,adv=32,con=5,pre=35,score=0,scorepref;
	
	private String key;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tutor_options);
		//this.getWindow().setBackgroundDrawableResource(R.color.black);
		key="key";
		btnadv = (Button) findViewById(R.id.btnadv);//4
		btnpro = (Button) findViewById(R.id.btnpro);//1
		btnadj = (Button) findViewById(R.id.btnadj);//2
		btnver = (Button) findViewById(R.id.btnverb);//3
		btncon = (Button) findViewById(R.id.btnconj);//5
		btnpre = (Button) findViewById(R.id.btnprep);//6
		btncmn = (Button) findViewById(R.id.btncmn);//7
		btntest = (Button) findViewById(R.id.btntest);
		bundle = new Bundle();
		
		probar = (ProgressBar)findViewById(R.id.probar);
		probar.setIndeterminate(false); // May not be necessary
		probar.setMax(pro);
		
		verbar = (ProgressBar)findViewById(R.id.progressBar1);
		verbar.setIndeterminate(false); // May not be necessary
		verbar.setMax(ver);
		
		advbar = (ProgressBar)findViewById(R.id.progressBar2);
		advbar.setIndeterminate(false); // May not be necessary
		advbar.setMax(adv);
		//advbar.setBackgroundColor(Color.RED);
		
		adjbar = (ProgressBar)findViewById(R.id.progressBar3);
		adjbar.setIndeterminate(false); // May not be necessary
		adjbar.setMax(adj);
		//adjbar.setBackgroundColor(Color.RED);
		
		conbar = (ProgressBar)findViewById(R.id.progressBar4);
		conbar.setIndeterminate(false); // May not be necessary
		conbar.setMax(con);
		//conbar.setBackgroundColor(Color.RED);
		
		prebar = (ProgressBar)findViewById(R.id.progressBar5);
		prebar.setIndeterminate(false); // May not be necessary
		prebar.setMax(pre);
		//prebar.setBackgroundColor(Color.RED);
		
		myDbHelper = new DBahelper(this);
		 try {

		     	myDbHelper.createDataBase();

			} catch (IOException ioe) {

				Toast.makeText(getApplicationContext(),
					    ioe.toString()+"  ol", Toast.LENGTH_SHORT).show();
			}
		 

				myDbHelper.openDataBase();
				db = myDbHelper.getWritableDatabase();
				
			Cursor c=db.rawQuery("SELECT * FROM POSTable WHERE pos=1 and learnt=1", null);
			probar.setProgress(c.getCount());
		
			
			c=db.rawQuery("SELECT * FROM POSTable WHERE pos=2 and learnt=1", null);
			verbar.setProgress(c.getCount());
		
			
			c=db.rawQuery("SELECT * FROM POSTable WHERE pos=3 and learnt=1", null);
			advbar.setProgress(c.getCount());
		
			
			c=db.rawQuery("SELECT * FROM POSTable WHERE pos=4 and learnt=1", null);
			adjbar.setProgress(c.getCount());
		
			
			c=db.rawQuery("SELECT * FROM POSTable WHERE pos=5 and learnt=1", null);
			conbar.setProgress(c.getCount());
		
			
			c=db.rawQuery("SELECT * FROM POSTable WHERE pos=6 and learnt=1", null);
			prebar.setProgress(c.getCount());
			
		btnpro.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				// TODO Auto-generated method stub
				Intent i= new Intent(TutorOptions.this,LearnActivity.class);
				bundle.putInt(key,1);
				i.putExtras(bundle);
				startActivity(i);
				
			}
		});
		
		btnadj.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i= new Intent(TutorOptions.this,LearnActivity.class);
				bundle.putInt(key,2);
				i.putExtras(bundle);
				startActivity(i);
			}
		});
		
		btnver.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i= new Intent(TutorOptions.this,LearnActivity.class);
				bundle.putInt(key,3);
				i.putExtras(bundle);
				startActivity(i);
			}
		});

		btnadv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 	TODO Auto-generated method stub
				Intent i= new Intent(TutorOptions.this,LearnActivity.class);
				bundle.putInt(key,4);
				i.putExtras(bundle);
				startActivity(i);
			}
		});
		
		btncon.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i= new Intent(TutorOptions.this,LearnActivity.class);
				bundle.putInt(key,5);
				i.putExtras(bundle);
				startActivity(i);
			}
		});

		btnpre.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i= new Intent(TutorOptions.this,LearnActivity.class);
				bundle.putInt(key,6);
				i.putExtras(bundle);
				startActivity(i);
			}
		});

		btncmn.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i= new Intent(TutorOptions.this,LearnActivity.class);
				bundle.putInt(key,7);
				i.putExtras(bundle);
				startActivity(i);
			}
		});

		btntest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Cursor c= db.rawQuery("SELECT consulted _id FROM POSTable where _id=1 and learnt = 0",null);
				//if(c.moveToFirst())
				//{
					Intent i= new Intent(TutorOptions.this,TestOptions.class);
					
					startActivity(i);
					
				//}
				/*else
				{
					Toast.makeText(getApplicationContext(),
						    "PLEASE LEARN THE NEW WORDS", Toast.LENGTH_SHORT).show();
				}*/
			}
		});
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.learning_module, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, EnglishHomeActivity.class);
		startActivity(i);
	}

}
