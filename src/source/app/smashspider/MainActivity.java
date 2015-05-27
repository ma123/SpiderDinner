package source.app.smashspider;

import source.app.smashspider.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private SharedPreferences prefs;
	private int highScore;
	private TextView textTitle;
	private TextView textStory;
	private TextView textHighScore;
	private TextView valueHighScore;
	private Button gameBtnView;
	private Button exitBtnView;
	private Typeface typeFace;
	private int xMax;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_menu);
		
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		Point size = new Point();
		display.getSize(size);
		xMax = size.x;
		
		textHighScore = (TextView) findViewById(R.id.text_hightScore_popis);
		valueHighScore = (TextView) findViewById(R.id.text_highScore);
		textTitle = (TextView) findViewById(R.id.text_title);
		textStory = (TextView) findViewById(R.id.text_story);
		gameBtnView = (Button) findViewById(R.id.button_play_game);
		exitBtnView = (Button) findViewById(R.id.button_exit);
		typeFace = Typeface.createFromAsset(getAssets(), "architect.ttf");
		
		textHighScore.setTypeface(typeFace);
		valueHighScore.setTypeface(typeFace);
		gameBtnView.setTypeface(typeFace);
		exitBtnView.setTypeface(typeFace);
		textStory.setTypeface(typeFace);
		textTitle.setTypeface(typeFace);
		
		if(xMax >= 1000) { // osetrenie xxhdpi 
			textHighScore.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xStory));
			valueHighScore.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xStory));
			gameBtnView.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xBtnGame));
			exitBtnView.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xBtnGame));
			textStory.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xStory));
			textTitle.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xTitleGame));
		}
		else {
			textHighScore.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.story));
			valueHighScore.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.story));
			gameBtnView.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.btnGame));
			exitBtnView.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.btnGame));
			textStory.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.story));
			textTitle.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.titleGame));
		}
				
		prefs = PreferenceManager.getDefaultSharedPreferences(this); 
		highScore = prefs.getInt("HIGH_SCORE", 0); // ziskanie highScore z preferencie
		valueHighScore.setText(String.valueOf(highScore)); // nastavenie highScore v mainlayoute
	}
	
	public void playGame(View view)
	{
		Intent enabler = new Intent(this, PlayGameActivity.class);
		startActivity(enabler);
	}

	public void exitGame(View view)
	{
		finish();
	}
}
