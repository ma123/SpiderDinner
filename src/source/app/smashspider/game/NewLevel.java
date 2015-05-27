package source.app.smashspider.game;

import source.app.smashspider.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;

public class NewLevel {
	private int x;
	private int y;
	private Paint paint;
	private Paint paintRect;
	private int level = 0;
	private Typeface typeFace;
	private String nextLevel;
	private RectF frameText;
	private int sizeFont;

	public NewLevel(Context context) {
		typeFace = Typeface.createFromAsset(context.getAssets(),
				"architect.ttf");
		frameText = new RectF();

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		y = size.y / 2;
		x = size.x / 2;

		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setTypeface(typeFace);
		sizeFont = context.getResources().getDimensionPixelSize(
				R.dimen.fontSizeLevel);
		paint.setTextSize(sizeFont);

		paintRect = new Paint();
		paintRect.setColor(Color.argb(170, 255, 204, 0));
	}

	public void onDraw(Canvas canvas) {
		nextLevel = "Next level: " + (level + 1);
		float lengthStringLevel = paint.measureText(nextLevel);
		frameText.set(x - (lengthStringLevel / 2), y - sizeFont, x
				- (lengthStringLevel / 2) + lengthStringLevel, y
				+ (sizeFont / 4));
		canvas.drawRoundRect(frameText, 7, 7, paintRect); // vykreslenie ramu
															// okolo textu
		canvas.drawText(nextLevel, x - (lengthStringLevel / 2), y, paint); // vykreslenie
																			// textu
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
