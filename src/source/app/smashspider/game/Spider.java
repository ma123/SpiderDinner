package source.app.smashspider.game;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class Spider {
	private int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
	private static final int BMP_ROWS = 4;
	private static final int BMP_COLUMNS = 3;
	private int x;
	private int y;
	private int xMax;
	private int displayConst = 40;
	private int xSpeed;
	private int ySpeed;
	private CanvasView canvasView;
	private Bitmap bmp;
	private int currentFrame = 0;
	private int width;
	private int height;

	public Spider(CanvasView canvasView, Bitmap bmp, Context context) {
		this.canvasView = canvasView;
		this.bmp = bmp;
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight() / BMP_ROWS;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		Point size = new Point();
		display.getSize(size);
		xMax = size.x;
		
		Random rnd = new Random();
		int partition = rnd.nextInt(4);
		switch(partition) {
		case 0:
			x = 0;
			y = rnd.nextInt(canvasView.getHeight() - height);
	    break;
	    
		case 1:
			x = canvasView.getWidth() - width;
			y = rnd.nextInt(canvasView.getHeight() - height);
	    break;
	    
		case 2:
			x = rnd.nextInt(canvasView.getWidth() - width);
			y = 0;
	    break;
	    
		case 3:
			x = rnd.nextInt(canvasView.getWidth() - width);
			y = canvasView.getHeight() - height;
		break;
		}
		xSpeed = rnd.nextInt(xMax / displayConst);   
		ySpeed = rnd.nextInt(xMax / displayConst);
	}

	private void update() {
		if ((x > canvasView.getWidth() - width - xSpeed) || (x + xSpeed < 0)) {
			xSpeed = -xSpeed;
		}
		x = x + xSpeed;
		
		if ((y > canvasView.getHeight() - height - ySpeed) || (y+ ySpeed < 0)) {
			ySpeed = -ySpeed;
		}
		y = y + ySpeed;
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
        int srcY = getAnimationRow() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bmp, src, dst, null);
	}
	
	private int getAnimationRow() {
        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }
	
	public Rect getObject() {
		return new Rect(x, y, x + width, y + height);
	}

	public boolean isCollition(float x2, float y2) {
		return x2 > (x - 20) && x2 < (x + width + 20) && y2 > (y - 20) && y2 < (y + height + 20);
	}
}
