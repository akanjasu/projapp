package in.nammaapp.itskannada;

import java.io.IOException;
import java.util.Random;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class KanWordsTestActivity extends Activity {
	SharedPreferences pref;
	private RadioButton opt1;
	private RadioButton opt2;
	private RadioButton opt3;
	private RadioButton opt4;
	private Button submit;
	private RadioGroup optgroup;
	private DBahelper myDbHelper ;
	private SQLiteDatabase db;
	private TextView tv;
	private String[] a = {"a","b","c","d"};
	public static int randomno;
	public static String engword;
	public static int quesno=0;
	private static int score=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kan_words_test);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		opt1=(RadioButton) findViewById(R.id.opt1);
		opt2=(RadioButton) findViewById(R.id.opt2);
		opt3=(RadioButton) findViewById(R.id.opt3);
		opt4=(RadioButton) findViewById(R.id.opt4);
		submit=(Button) findViewById(R.id.submit);
		tv =(TextView) findViewById(R.id.wordtv);
		optgroup = (RadioGroup) findViewById(R.id.radioGroup1);
		
		myDbHelper = new DBahelper(this);
		 try {

		     	myDbHelper.createDataBase();

			} catch (IOException ioe) {

				Toast.makeText(getApplicationContext(),
					    ioe.toString()+"  ol", Toast.LENGTH_SHORT).show();
			}
		 

				myDbHelper.openDataBase();
				db = myDbHelper.getWritableDatabase();
				score=0;
				nextques();
				
			submit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int index = optgroup.indexOfChild(findViewById(optgroup.getCheckedRadioButtonId()));
					Boolean bool=false;
					
					if(index == (randomno -1))
					{
						db.execSQL("UPDATE POSTable set learnt = 1 where engword=\""+engword+"\"");
						score++;
					}
					else
						db.execSQL("UPDATE POSTable set consulted = 2 where engword=\""+engword+"\"");
					Cursor cur = db.rawQuery("SELECT consulted _id FROM POSTable where consulted=1 and learnt=0",null);
					if(cur.moveToFirst())
						bool=true;
					else
						bool=false;
					quesno++;
					if(quesno <= 10 && bool )
					nextques();
					else
					{
						Toast.makeText(getApplicationContext(),
							    "Your score is "+score+" out of "+quesno+".... Take up next Test", Toast.LENGTH_LONG).show();
						int xp = Integer.parseInt(pref.getString("xppoints", "0"));
						xp += 1;
						SharedPreferences.Editor editor = pref.edit();
						editor.putString("xppoints", Integer.toString(xp));
						editor.commit();
						quesno=0;
						db.execSQL("UPDATE POSTable set consulted = 1 where consulted = 2");
						Intent i= new Intent(KanWordsTestActivity.this,TutorOptions.class);
						startActivity(i);
					}
				}
			});
	}
	
	public void nextques()
	{
		Random r = new Random();
		randomno=r.nextInt(4) + 1;
		Cursor c1 = db.rawQuery("SELECT engword _id,kanword FROM POSTable where consulted = 1 and learnt = 0 ORDER BY RANDOM()",null );
		if(c1.moveToFirst())
		{
			engword=c1.getString(c1.getColumnIndex("_id")).toString();
			a[0]=c1.getString(c1.getColumnIndex("kanword")).toString();
			tv.setText(engword);
		}
		Cursor c2= db.rawQuery("SELECT kanword _id FROM POSTable where engword <> \""+engword+"\" ORDER BY RANDOM()",null);
		if(c2!=null)
		{
			Integer i=1;
			while(c2.moveToNext() && i<4)
			{
				a[i]=c2.getString(c2.getColumnIndex("_id")).toString();
				i++;
			}
		}
		switch(randomno)
		{
		case 1:
			opt1.setText(a[0]);
			opt2.setText(a[1]);
			opt3.setText(a[2]);
			opt4.setText(a[3]);
			break;
		case 2:
			opt2.setText(a[0]);
			opt1.setText(a[1]);
			opt3.setText(a[2]);
			opt4.setText(a[3]);
			break;
		case 3:
			opt3.setText(a[0]);
			opt1.setText(a[1]);
			opt2.setText(a[2]);
			opt4.setText(a[3]);
			break;
		case 4:
			opt4.setText(a[0]);
			opt2.setText(a[1]);
			opt3.setText(a[2]);
			opt1.setText(a[3]);
			break;
		default:
				opt4.setText(a[0]);
				opt2.setText(a[1]);
				opt3.setText(a[2]);
				opt1.setText(a[3]);
		break;		
		}
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kan_words_test, menu);
		return true;
	}

}

