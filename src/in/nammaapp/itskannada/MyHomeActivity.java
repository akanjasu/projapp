package in.nammaapp.itskannada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyHomeActivity extends Activity {
	SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_home_activity);
		prefs = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		Button myquestions = (Button) findViewById(R.id.btnMyQues);
		Button myanswers = (Button) findViewById(R.id.btnMyAns);
		TextView name = (TextView) findViewById(R.id.txtMyHomeUsername);
		TextView xp = (TextView) findViewById(R.id.txtMyHomeXP);
		TextView qup = (TextView) findViewById(R.id.txtMyHomeQUp);
		TextView aup = (TextView) findViewById(R.id.txtMyHomeAUp);
		
		myquestions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MyHomeActivity.this, MyQuestionsActivity.class);
				startActivity(i);
			}
		});
		
		myanswers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MyHomeActivity.this, MyAnswersActivity.class);
				startActivity(i);
				
			}
		});
		
		name.setText(prefs.getString("name", "unknown"));
		xp.setText("XP: " + prefs.getString("xppoints", "unknown"));
		qup.setText("Q Up: " + prefs.getString("quesupvotes", "unknown"));
		aup.setText("A Up: " + prefs.getString("ansupvotes", "unknown"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_home, menu);
		return true;
	}

}
