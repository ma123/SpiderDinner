package source.app.smashspider;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import source.app.smashspider.R;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameOverActivity extends Activity {
	private AdView adView;
	private static final String AD_UNIT_ID = "ca-app-pub-1882232042439946/8505068315";
	private int score = 0;
	private int highScore;
	private TextView textScore;
	private TextView textScoreLabel;
	private TextView textHighScore;
	private TextView textHighScoreLabel;
	private TextView textGameOver;
	private TextView textAbsurdJoke;
	private Button btnBackToMenu;
	private SharedPreferences prefs;
	private MediaPlayer mp;
	private Typeface typeFace;
    private int xMax;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game_over);

		// Create an ad.
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(AD_UNIT_ID);

		// Add the AdView to the view hierarchy. The view will have no size
		// until the ad is loaded.
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
		layout.addView(adView);

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device.
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE").build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);

		textScore = (TextView) findViewById(R.id.text_score);
		textScoreLabel = (TextView) findViewById(R.id.text_score_label);
		textHighScore = (TextView) findViewById(R.id.text_hight_score);
		textHighScoreLabel = (TextView) findViewById(R.id.text_hight_score_label);
		textGameOver = (TextView) findViewById(R.id.text_game_over);
		textAbsurdJoke = (TextView) findViewById(R.id.text_absurd_joke);
		btnBackToMenu = (Button) findViewById(R.id.button_back);
		typeFace = Typeface.createFromAsset(getAssets(), "architect.ttf");
		
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		Point size = new Point();
		display.getSize(size);
		xMax = size.x;
		
		if(xMax >= 1000) { // osetrenie xxhdpi 
			textScore.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xStory));
			textHighScore.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xStory));
			textScoreLabel.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xStory));
			textScoreLabel.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xStory));
			textAbsurdJoke.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xBtnGame));
			textGameOver.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.gameOver));
			btnBackToMenu.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.xBtnGame));
		}
		else {
			textScore.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.story));
			textHighScore.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.story));
			textScoreLabel.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.story));
			textScoreLabel.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.story));
			textAbsurdJoke.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.btnGame));
			textGameOver.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.gameOver));
			btnBackToMenu.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.btnGame));
		}

		textScore.setTypeface(typeFace);
		textHighScore.setTypeface(typeFace);
		textScoreLabel.setTypeface(typeFace);
		textHighScoreLabel.setTypeface(typeFace);
		textAbsurdJoke.setTypeface(typeFace);
		textGameOver.setTypeface(typeFace);
		btnBackToMenu.setTypeface(typeFace);

		score = ((Activity) this).getIntent().getIntExtra("SCORE", 0);
		textScore.setText(String.valueOf(score));

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		highScore = prefs.getInt("HIGH_SCORE", 0);
		if (score > highScore) {
			prefs.edit().putInt("HIGH_SCORE", score).commit();
		}
		textHighScore.setText(String.valueOf(highScore));

		mp = MediaPlayer.create(this, R.raw.evillaugh);
		mp.start();

		ImageView wheel = (ImageView) findViewById(R.id.wheel);
		AnimatorSet wheelSet = (AnimatorSet) AnimatorInflater.loadAnimator(
				this, R.animator.wheel_spin);
		wheelSet.setTarget(wheel);
		wheelSet.start();
	}
 
	public void backToMenu(View view) {
		finish();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	/** Called before the activity is destroyed. */
	@Override
	public void onDestroy() {
		// Destroy the AdView.
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}
}