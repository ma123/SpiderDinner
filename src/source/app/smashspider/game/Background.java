package source.app.smashspider.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
	private int x = 0;
	private int y = 0;
	private Bitmap bmp;

	public Background(Context context, Bitmap bmp) {
		this.bmp = bmp;
	}

	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(bmp, x, y, null);
	}
}
