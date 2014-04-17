package in.nammaapp.itskannada;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class PointsActivity extends Activity {
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.points_activity);
		pref = getApplicationContext().getSharedPreferences("preferencesFile", Context.MODE_PRIVATE);
		TextView points = (TextView) findViewById(R.id.pointsTotalScore);
		TextView badges = (TextView) findViewById(R.id.badgestext);
		
		points.setText("POINTS: " + pref.getInt("tscore", 0));
		badges.setText("BADGES EARNED:" + pref.getInt("badgeno", 0));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.points, menu);
		return true;
	}

}
