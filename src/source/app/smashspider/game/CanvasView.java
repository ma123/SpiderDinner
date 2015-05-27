package source.app.smashspider.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import source.app.smashspider.GameOverActivity;
import source.app.smashspider.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CanvasView extends SurfaceView {
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private List<Spider> listSprite = new ArrayList<Spider>();
	private long lastClick;
	private Bitmap bmpBlood;
	private Bitmap bmpLife;
	private Bitmap bmpMouth;
	private Bitmap bmpCloseMouth;
	private Bitmap bmpBackground;
	private List<TempSprite> temps = new ArrayList<TempSprite>();
	private Context context;
	private int smashSpiderScore = 0;
	private int level = 0;
	private int LIFE = 4;
	private int numberSpider = 0;
	private int smashedSpider = 0;
	private int LIMITSPIDER = 4;
	private boolean boolCloseMouth = false;
	private Mouth mouth;
	private Mouth closeMouth;
	private Background background;
	private GameScore gameScore;
	private GameLife gameLife;
	private NewLevel newLevel;
	private int counter = 0;
	private SoundPool soundPool;
	private int soundMmm = 0;
	private int soundSmash = 0;
	private int soundWinn = 0;
	private int volume = 5;
	private boolean loadedSound = false;
	private Random rnd;

	public CanvasView(Context context) {
		super(context);
		this.context = context;
		bmpMouth = BitmapFactory.decodeResource(getResources(), R.drawable.mouth);
		bmpCloseMouth = BitmapFactory.decodeResource(getResources(), R.drawable.close_mouth);
		bmpBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood);
		bmpLife = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
		rnd = new Random();
		
		gameScore = new GameScore(this, context);
		gameLife = new GameLife(this, context, bmpLife);
		background = new Background(context, bmpBackground);
		mouth = new Mouth(context, bmpMouth);
		closeMouth = new Mouth(context, bmpCloseMouth);
		newLevel = new NewLevel(context);

		gameLoopThread = new GameLoopThread(this);
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				gameLoopThread.setRunningLoop(false);
				while (retry) {
					try {
						gameLoopThread.join();
						retry = false;
					} catch (InterruptedException e) {
					}
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				gameLoopThread.setRunningLoop(true);
				gameLoopThread.start();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});

		loadSound();
	}

	private void loadSound() {
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0); // inicializacia sound pool
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				loadedSound = true;
			}
		});
		soundMmm = soundPool.load(context, R.raw.mmm, 1);
		soundSmash = soundPool.load(context, R.raw.smashspider, 1);
		soundWinn = soundPool.load(context, R.raw.fakeaplaus, 1); 
	}

	@Override
	protected void onDraw(Canvas canvas) {
		counter++;
		canvas.drawColor(Color.argb(255, 185, 158, 116));
		background.onDraw(canvas); // nastavenie pozadia
		if (boolCloseMouth) { //
			closeMouth.onDraw(canvas); // vykreslenie objektu zatvorene usta
			if (counter % 7 == 0) {
				counter = 0;
				boolCloseMouth = false;
			}
		} else {
			mouth.onDraw(canvas); // vykreslenie objektu usta
		}
		gameScore.onDraw(canvas); // vykreslenie objektu skore
		gameLife.onDraw(canvas); // vykreslenie objektu zivot
		
		if(smashedSpider == 0) {  // vykreslenie new level popisu pokial pocet pavukov v levele je 0
			newLevel.onDraw(canvas);
		}

		createSpiders();
		checkScore();

		for (int i = temps.size() - 1; i >= 0; i--) { // vykreslenie tempov
			temps.get(i).onDraw(canvas);
		}

		for (Spider sprite : listSprite) { // vykreslenie pavukov
			sprite.onDraw(canvas);
		}
		/*
		 * Prechadzanie celym listom a kontrola kolizie s ustami v pripade
		 * kolizie znizenie zivota o 1, odstranenie pavuka, spustenie zvuku,
		 * animacie v pripade ze zivot je na 0 zastavenie slucky + napis GAME OVER
		 */
		for (int i = listSprite.size() - 1; i >= 0; i--) {
			if (LIFE == 0) {
				gameLoopThread.setRunningLoop(false);
				((Activity) context).finish(); // vyvolanie novej activity
												// GameOverActivity
				Intent intent = new Intent(context, GameOverActivity.class);
				intent.putExtra("SCORE", smashSpiderScore);
				context.startActivity(intent);
				break;
			}
			Spider sprite = listSprite.get(i);
			if (sprite.getObject().intersect(mouth.getObjekt())) {
				LIFE--; // znizenie zivota o 1
				smashedSpider--; // odobranie y poctu pavukov
				listSprite.remove(sprite); // odobranie pavuka z listu
				boolCloseMouth = true;
				if (loadedSound) {
					soundPool.play(soundMmm, volume, volume, 1, 0, 1f);
				}
			}
		}
	}

	private void checkScore() {
		if ((smashSpiderScore % 50) == 0) {
			LIFE++;
			smashSpiderScore++;
		}
	}

	/*
	 * Zabijanie pavukov dotykom
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
			if (smashedSpider > 0) {
				if (System.currentTimeMillis() - lastClick > 300) {
					lastClick = System.currentTimeMillis();
					synchronized (getHolder()) {
						float x = event.getX();
						float y = event.getY();
						for (int i = listSprite.size() - 1; i >= 0; i--) {
							Spider sprite = listSprite.get(i);
							if (sprite.isCollition(x, y)) {
								smashSpiderScore++; // pridanie bodu ku skore
								smashedSpider--; // odobranie y poctu pavukov
								if (loadedSound) {
									soundPool.play(soundSmash, volume, volume, 1, 0, 1f);
								}
      
								listSprite.remove(sprite);
								temps.add(new TempSprite(temps, this, x, y, bmpBlood));
								break;
							}
						}
					}
				}
			} else {
                 newStage();
			}
		}
		return true;
	}

	/*
	 * Vytvorenie 1 pavuka
	 */
	private Spider createOneSpider(int resouce) {
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
		return new Spider(this, bmp, context);
	}

	/*
	 * Postupne vytvorenie vsetkych sprite na zaklade velkosti counter
	 */
	private void createSpiders() {
		if (numberSpider < LIMITSPIDER) {
			for (numberSpider = 0; numberSpider < LIMITSPIDER; numberSpider++) {
				boolean value = rnd.nextBoolean();
				if(value) {
					listSprite.add(createOneSpider(R.drawable.spider));
				}
				else {
					listSprite.add(createOneSpider(R.drawable.spidertwo));
				}
				
			}
			smashedSpider = LIMITSPIDER;
			level++;
		    newLevel.setLevel(level); // nastavenie vysky levela v zobrazeni
		}
	}

	/*
	 * Nastavenie pre novy level
	 */
	public void newStage() {
		if (loadedSound) {
			soundPool.play(soundWinn, volume, volume, 1, 0, 1f);
		}
	
		smashedSpider = 0;
		numberSpider = 0;
		LIMITSPIDER += 1; // zvysenie limitu pavukov o 1
	}

	public void stopView() {
		if (gameLoopThread != null) {
			gameLoopThread.setRunningLoop(false);
		}
	}

	/*
	 * Getery a settery premennych
	 */
	public int getLIFE() {
		return LIFE;
	}

	public int getSmashSpiderScore() {
		return smashSpiderScore;
	}

	public int getLevel() {
		return level;
	}
}