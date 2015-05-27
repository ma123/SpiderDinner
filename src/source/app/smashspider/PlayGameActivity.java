package source.app.smashspider;

import source.app.smashspider.game.CanvasView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class PlayGameActivity extends Activity {
	private CanvasView canvasView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		canvasView = new CanvasView(this);
		setContentView(canvasView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		canvasView.stopView();
	}
}
