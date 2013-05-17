package in.nammaapp.itskannada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class ChooseStartActivity extends Activity {
	SharedPreferences pref;
	String querySelectedUserType;
	Intent startIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_start);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		querySelectedUserType = pref.getString("kannada", "notSet");
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(querySelectedUserType != "notSet") {
					String name = pref.getString("name", "notSet");
					String username = pref.getString("username", "notSet");
					String password = pref.getString("password", "notSet");
					String email = pref.getString("email", "notSet");
					String region = pref.getString("region", "notSet");
					if(name == "notSet" || username == "notSet" || password == "notSet" || email == "notSet" || region == "notSet") {
						startIntent = new Intent(ChooseStartActivity.this, SignUpActivity.class);
						startActivity(startIntent);
					} else {
						if(querySelectedUserType == "true") {
							startIntent = new Intent(ChooseStartActivity.this, KannadaHomeActivity.class);
							startActivity(startIntent);
						} else {
							startIntent = new Intent(ChooseStartActivity.this, EnglishHomeActivity.class);
							startActivity(startIntent);
						}
					}
				} else {
					startIntent = new Intent(ChooseStartActivity.this, EngOrKanUser.class);
					startActivity(startIntent);
				}
			}
		}, 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_start, menu);
		return true;
	}

}
