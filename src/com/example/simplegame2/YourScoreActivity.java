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

import android.os.Bundle;

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
		Bundle bundle=new Bundle();
		
		score=bundle.getInt("YourScore");
		System.out.println(""+score);
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
				TEXT_SIZE, true, android.graphics.Color.GREEN);
		this.mFont.load();
	}

	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mScene=new Scene();
		float centerY=W/2-TEXT_SIZE/2;
		final Text mYourScore=new Text(50, centerY, mFont, "Your Score:"+score, this.getVertexBufferObjectManager());
		this.mScene.attachChild(mYourScore);
		return this.mScene;
	}
	
}
