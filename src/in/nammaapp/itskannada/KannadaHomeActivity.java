package in.nammaapp.itskannada;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class KannadaHomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kannada_home_activity);
		
		Button btnResPhrase = (Button) findViewById(R.id.btnResPhrase);
		Button btnResWord = (Button) findViewById(R.id.btnResWord);
		Button btnAnsQuestion = (Button) findViewById(R.id.btnAnsQuestion);
		Button btnEval = (Button) findViewById(R.id.btnEval);
		Button btnKanQuestion = (Button) findViewById(R.id.btnKanQuestion);
		Button btnkanForum = (Button) findViewById(R.id.btnkanForum);
		
		
		
		btnResPhrase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, KanPhraseHomeActivity.class);
				startActivity(i);
			}
		});

		
		btnResWord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, ContextKanHomeActivity.class);
				startActivity(i);
			}
		});
		
		btnAnsQuestion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, ForumHomeActivity.class);
				Bundle b = new Bundle();
				b.putString("ACTION", "ANSWER");
				i.putExtras(b);
				startActivity(i);
			}
		});
		btnEval.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(KannadaHomeActivity.this, EvalActivity.class);
					startActivity(i);
				}
			});
		
		
		btnKanQuestion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, QuestionActivity.class);
				startActivity(i);
			}
		});
		
		btnkanForum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(KannadaHomeActivity.this, ForumHomeActivity.class);
				startActivity(i);
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kannada_home, menu);
		return true;
	}

}
