package source.app.smashspider.game;


import android.graphics.Canvas;

public class GameLoopThread extends Thread {
	static final long FPS = 10;
    private CanvasView canvasView;
    private boolean runningLoop = false;
    
    public GameLoopThread(CanvasView canvasView) {
   	    this.canvasView = canvasView;
    }
    
    public void setRunningLoop(boolean runningLoop) {
   	    this.runningLoop = runningLoop;
    }
    
    @Override
    public void run() {
   	 long ticksPS = 1000 / FPS;
   	 long startTime;
   	 long sleepTime;
   	 while(runningLoop) {
   		 Canvas c = null;
   		 startTime = System.currentTimeMillis();
   		 try {
   			 c = canvasView.getHolder().lockCanvas();
   			 synchronized (canvasView.getHolder()) {
					canvasView.onDraw(c);
			 }
   		 }
   		 finally {
   			 if(c != null) {
   				 canvasView.getHolder().unlockCanvasAndPost(c);
   			 }
   		 }
   		 sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
   		 System.out.println();
   		 try {
   			 if(sleepTime > 0) {
   				 sleep(sleepTime);
   			 }
   			 else {
   				 sleep(10);
   			 }
   		 } catch(Exception e) {}
   	 }
    }
    
    public boolean getRunningLoop() {
    	return runningLoop;
    }
}