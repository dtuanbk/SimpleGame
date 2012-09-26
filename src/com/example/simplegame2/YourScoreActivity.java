package com.example.simplegame2;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;

public class YourScoreActivity extends SimpleBaseGameActivity{
	private int W = 240;
	private int H = 320;

	private Scene mScene;

	private MenuScene mMenuScene;

	private Camera mCamera;
	
	private Font mFont;
	
	private int TEXT_SIZE=32;
	
	private int score=0;
	
	
	
	
	@Override
	public synchronized void onResumeGame() {
		// TODO Auto-generated method stub
		System.out.println("onResumeGame");
		
		super.onResumeGame();
	}

	@Override
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
	}

	@Override
	protected Scene onCreateScene() {
		Intent i=getIntent();
		Bundle bundle=i.getExtras();
		
		score=bundle.getInt("YourScore");
		System.out.println("YourScoreYourScore:"+score);
		
		System.out.println("onCreateScene");
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mScene=new Scene();
		float centerY=H/2-TEXT_SIZE/2;
		final Text mYourScore=new Text(40, centerY, mFont, "Your Score:"+score, this.getVertexBufferObjectManager());
		this.mScene.attachChild(mYourScore);
		return this.mScene;
	}

	@Override
	public boolean isGamePaused() {
		// TODO Auto-generated method stub
		
		return super.isGamePaused();
	}
	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_BACK
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			System.out.println("KEYCODE_BACK");
			finish();

			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}

	@Override
	public synchronized void onPauseGame() {
		// TODO Auto-generated method stub
		System.out.println("onPauseGame");
		finish();
		super.onPauseGame();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		System.out.println("onBackPressed");
		super.onBackPressed();
	}

	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event )
	{
		System.out.println("onKeyUp");
	    if( keyCode == KeyEvent.KEYCODE_BACK )
	    {
	    	
	    	finish();
	        return true;
	    }
	    return super.onKeyUp( keyCode, event );
	}
	
	
	
	
}
