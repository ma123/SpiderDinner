package source.app.smashspider.game;

import source.app.smashspider.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;

public class GameLife {
	private int xMax;
	private int yMax;
	private Paint paint;
	private int life;
	private String lifeText= " ";
	private CanvasView canvasView;
	private Bitmap bmp;
	private Typeface typeFace;
	
    public GameLife(CanvasView canvasView, Context context, Bitmap bmp) {
    	this.canvasView = canvasView;
    	this.bmp = bmp;
    	typeFace = Typeface.createFromAsset(context.getAssets(), "architect.ttf");
    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		Point size = new Point();
		display.getSize(size);
		xMax = size.x;
		yMax = size.y;
		paint = new Paint();
		paint.setTypeface(typeFace);
		paint.setColor(Color.WHITE);
    	paint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.fontSize)); 
    }
    
    public void onDraw(Canvas canvas) {
    	life = canvasView.getLIFE();
    	lifeText = " x " + String.valueOf(life);  	
        canvas.drawText(lifeText, xMax - (xMax / 5), yMax / 15, paint);
        canvas.drawBitmap(bmp, xMax - (xMax / 3) + (xMax / 17), yMax / 32, null);
	}
}
