package source.app.smashspider.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class Mouth {
	private int x;
	private int y;
	private Bitmap bmp;
	private int width;
	private int height;
	
      public Mouth(Context context, Bitmap bmp) {
    	this.bmp = bmp;
    	this.width = bmp.getWidth();
    	this.height = bmp.getHeight();
    	
    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
  		Display display = wm.getDefaultDisplay();
  		Point size = new Point();

  		if (  Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13 ) {
  	         y = display.getHeight();
  	       } else {
  	    		display.getSize(size);
  	    		y = size.y;
  	       }  		
  	
  		if(y <= 450) {
  			y = 177;//ldpi
  		}
  		else { if(y <= 600) {
  			      y = 236;//mdpi
  		       }
  		       else { if(y <= 900) {
  		    	        y = 353;//hdpi
  		              }
  		              else {
  		            	  if(y <= 1300) {
  		            		  y = 530;//xhdpi
  		            	  }
  		            	  else {
  		            		  y = 795;//xxhdpi
  		            	  }
  		              }
  		       }
  		} 
    	
  		x = size.x / 2 - (width / 3) + (width / 8);
  		
  		System.out.println(y);
  	}

  	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(bmp, x, y, null);
  	}
  	
  	public Rect getObjekt() {
		return new Rect(x, y, x + width, y + height);
	}
}

