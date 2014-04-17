package in.nammaapp.itskannada;

import java.io.IOException;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PracticeTest extends Activity {
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
	private Integer key;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice_test);
		key=getIntent().getExtras().getInt("key");
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
		
		if(key==1)
			poscall(1);

		else if(key==2)
			poscall(2);
		
		else if(key==3)
			poscall(3);

		else if(key==4)
			poscall(4);

		else if(key==5)
			poscall(5);

		else if(key==6)
			poscall(6);
		
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int index = optgroup.indexOfChild(findViewById(optgroup.getCheckedRadioButtonId()));
				if(index == (randomno -1))
				{
					Toast.makeText(getApplicationContext(),
						    "Your answer is Correct", Toast.LENGTH_LONG).show();
				}
				else
					Toast.makeText(getApplicationContext(),
						    "Your answer is InCorrect", Toast.LENGTH_LONG).show();
					poscall(key);
			
				}
		});


	}


	public void poscall(int key)
	{
		Random r = new Random();
		randomno=r.nextInt(4) + 1;
		Cursor c1 = db.rawQuery("SELECT engword _id,kanword FROM POSTable where consulted = 1 and pos = \""+key+"\" ORDER BY RANDOM()",null );
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Intent i=new Intent(this,PracticeTestOptions.class);
	        startActivity(i);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practice_test, menu);
		return true;
	}

}