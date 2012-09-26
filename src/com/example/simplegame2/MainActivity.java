package com.example.simplegame2;

import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.content.Intent;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;

public class MainActivity extends SimpleBaseGameActivity implements
		IOnMenuItemClickListener {
	private int W = 240;
	private int H = 320;

	private Scene mScene;

	private MenuScene mMenuScene;

	private Camera mCamera;

	// Sprite Spider
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITiledTextureRegion mTTR_spider;
	private AnimatedSprite spider;

	private Font mFont;

	private int TEXT_SIZE = 24;

	private Text mScore;
	private Text mTime;

	private int score = 0;
	private int times = 60;
	// Xac dinh thoi gian pause
	private long timePause = 0;
	private long startPause= 0;

	private long start;

	private int ls = 0;
	private int ms = 0;
	private int hs = 0;

	// Tao menu Item
	private static final int MENU_PAUSE = 0;

	// Xac dinh khi diem duoc thay doi
	private boolean isChange = false;
	
	private boolean isEnd=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		mCamera = new Camera(0, 0, W, H);

		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
				new RatioResolutionPolicy(W, H), mCamera);
	}

	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
		FontFactory.setAssetBasePath("font/");
		final ITexture fontITexture = new BitmapTextureAtlas(
				this.getTextureManager(), W, H);
		mFont = FontFactory.createFromAsset(this.getFontManager(),
				fontITexture, this.getAssets(), "VNI-Disney Normal.TTF",
				TEXT_SIZE, true, android.graphics.Color.WHITE);
		this.mFont.load();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				128, 32);
		mTTR_spider = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas,
						this.getAssets(), "spider.png", 0, 0, 4, 1);
		mBitmapTextureAtlas.load();

	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mMenuScene = this.createMenuScene();

		this.mScene = new Scene();
		this.mScene.setOnSceneTouchListenerBindingOnActionDownEnabled(true);

		VertexBufferObjectManager pVertexBufferObjectManager = this
				.getVertexBufferObjectManager();
		
		// Phai them thuoc tinh length neu ko se loi Buffer
		mScore = new Text(10, 10, mFont, "Score:" + score,
				"Score: XXXXX".length(), pVertexBufferObjectManager);
		this.mScene.attachChild(mScore);

		this.mTime = new Text(W - 90, 10, mFont, "Time: " + 60 + "s",
				pVertexBufferObjectManager);
		
		
		this.mScene.attachChild(mTime);

		start = SystemClock.elapsedRealtime();

		this.mScene.registerUpdateHandler(pUpdateHandler);
		this.mScene.setOnAreaTouchListener(pOnAreaTouchListener);

		return this.mScene;
	}

	// Tao menu khi pause

	private IUpdateHandler pUpdateHandler = new IUpdateHandler() {

		public void reset() {
			// TODO Auto-generated method stub

		}

		public void onUpdate(float pSecondsElapsed) {
			// TODO Auto-generated method stub
			long time = SystemClock.elapsedRealtime() - start;
			if(timePause!=0){
				time=time-timePause;
			}
			if (time < 65000) {

				long coutdown = 60000 - time;
				mTime.setText("Time: " + coutdown / 1000 + "s");
				

				if (time < 20000) {
					if (time > (1000 * ls)) {
						addSpider(1);
						ls++;
						System.out.println("" + ls);
						
					}
					mTime.setColor(Color.GREEN);
				} else if (time < 40000) {
					if (time > (20000 + 800 * ms)) {
						addSpider(2);
						ms++;
					}
					mTime.setColor(Color.PINK);
					
				} else {
					if (time > (40000 + 500 * hs)) {
						addSpider(3);
						hs++;
					}
					mTime.setColor(Color.RED);
				}
				if(time>60000){
					isEnd=true;
				}
			}

			if (isChange) {
				score = score + 10;
				mScore.setText("Score:" + score);
				isChange = false;
				System.out.println("AAAAA"+score);
			}
			if(isEnd){
				
				final int a=score;
				System.out.println("BBBB"+a);
				MainActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						// TODO Auto-generated method stub
						Intent intent=new Intent(getBaseContext(), YourScoreActivity.class);
						Bundle bundle=new Bundle();
						bundle.putInt("YourScore", a);
						System.out.println(""+a);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
			}
		}
	};

	private IOnAreaTouchListener pOnAreaTouchListener = new IOnAreaTouchListener() {

		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				ITouchArea pTouchArea, float pTouchAreaLocalX,
				float pTouchAreaLocalY) {

			if (pSceneTouchEvent.isActionDown()) {
				AnimatedSprite sprite = (AnimatedSprite) pTouchArea;
				if (sprite.equals(sprite)) {
					removeSpider(sprite);
					isChange = true;
				}
			}
			return true;
		}
	};

	// Ham remove sprite
	private void removeSpider(final AnimatedSprite spider) {
		mScene.unregisterTouchArea(spider);
		mScene.detachChild(spider);
	}

	// Ham add sprite
	private void addSpider(int addspeed) {
		int x = new Random().nextInt(W - 32);

		final float speed = (float) (new Random().nextFloat() + (addspeed + 1) * 2 / 3);

		final AnimatedSprite addspider = new AnimatedSprite(x, -32,
				mTTR_spider, this.getVertexBufferObjectManager());
		mScene.attachChild(addspider);
		mScene.registerTouchArea(addspider);
		int ani = 200 - addspeed * 50;
		addspider.animate(ani);
		addspider.registerUpdateHandler(new IUpdateHandler() {

			public void reset() {

			}

			public void onUpdate(float pSecondsElapsed) {
				// TODO Auto-generated method stub
				if (addspider.getY() < H) {
					addspider.setPosition(addspider.getX(), addspider.getY()
							+ speed);
				} else {

				}
			}
		});

	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			

			if (this.mScene.hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mMenuScene.back();
				timePause+=SystemClock.elapsedRealtime()-startPause;
			} else {
				/* Attach the menu. */
				startPause=SystemClock.elapsedRealtime();
				this.mScene.setChildScene(this.mMenuScene, false, true, true);
			}

			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}

	protected MenuScene createMenuScene() {
		final MenuScene menuScene = new MenuScene(this.mCamera);

		final IMenuItem pauseMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_PAUSE, this.mFont, "PAUSE",
						this.getVertexBufferObjectManager()), Color.PINK,
				Color.YELLOW);
		pauseMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA,
				GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(pauseMenuItem);

		menuScene.buildAnimations();
		menuScene.setBackgroundEnabled(false);
		menuScene.setOnMenuItemClickListener(this);
		return menuScene;
	}

	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		switch (pMenuItem.getID()) {
		case MENU_PAUSE:
			timePause+=SystemClock.elapsedRealtime()-startPause;
			System.out.println("Unpaused");
			this.mScene.back();
			break;

		default:
			break;
		}
		return false;
	}

}
