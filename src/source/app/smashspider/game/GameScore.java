package source.app.smashspider.game;


import source.app.smashspider.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;

public class GameScore {
	private int xMax;
	private int yMax;
	private Paint paint;
	private int spiderScore;
	private int level;
	private String scoreText = " ";
	private String levelText = " ";
	private CanvasView canvasView;
	private Typeface typeFace;
	
    public GameScore(CanvasView canvasView, Context context) {
    	this.canvasView = canvasView;
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
    	spiderScore = canvasView.getSmashSpiderScore();
    	scoreText = "Score: " + String.valueOf(spiderScore);
    	level = canvasView.getLevel();
    	levelText = "Level: " + String.valueOf(level); 	
  
        canvas.drawText(scoreText,  xMax / 17, yMax / 14, paint);
        canvas.drawText(levelText,  xMax / 3 + (xMax / 12), yMax / 14, paint);
	}
}
