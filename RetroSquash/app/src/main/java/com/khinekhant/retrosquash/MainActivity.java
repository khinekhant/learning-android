package com.khinekhant.retrosquash;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends Activity {

    Canvas mCanvas;
    SquashCourtView mSquashCourtView;

    SoundPool mSoundPool;
    AudioAttributes audioAttributes;
    int sample1= -1;
    int sample2=-1;
    int sample3=-1;
    int sample4=-1;

    Display mDisplay;
    Point size;
    private int screenWidth;
    private int screenHeight;

    private int racketwidth;
    private int racketHight;
    Point racketPosition;

    Point ballPosition;
    private int ballWidth;

    boolean ballIsMovingLeft;
    boolean ballIsMovingRight;
    boolean ballIsMovingUp;
    boolean ballMovingDown;

    boolean racketIsMovingLeft;
    boolean racketIsMovingRight;

    long lastFrameTime;
    int fps;
    int score;
    private int lives;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSquashCourtView=new SquashCourtView(this);
        setContentView(mSquashCourtView);


       // mSoundPool=new SoundPool(10, AudioManager.STREAM_MUSIC,0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .build();
                mSoundPool=new SoundPool.Builder()
                        .setMaxStreams(10)
                        .setAudioAttributes(audioAttributes)
                        .build();
            }

        AssetManager assetManager=getAssets();
        AssetFileDescriptor fileDescriptor;

        try {
            fileDescriptor=assetManager.openFd("sample1.wav");
            sample1=mSoundPool.load(fileDescriptor,0);

            fileDescriptor=assetManager.openFd("sample2.wav");
            sample2=mSoundPool.load(fileDescriptor,0);

            fileDescriptor=assetManager.openFd("sample3.wav");
            sample3=mSoundPool.load(fileDescriptor,0);

            fileDescriptor=assetManager.openFd("sample4.wav");
            sample4=mSoundPool.load(fileDescriptor,0);

        } catch (IOException e) {
            Log.e("Sound","Sound files not found");
        }

        mDisplay=getWindowManager().getDefaultDisplay();
        size=new Point();
        mDisplay.getSize(size);
        screenWidth=size.x;
        screenHeight=size.y;


        racketPosition=new Point();
        racketPosition.x=screenWidth/2;
        //check out why -20 here?
        racketPosition.y=screenHeight-20;
        racketwidth=screenWidth/8;
        racketHight=10;

        ballWidth=screenWidth/35;
        ballPosition=new Point();
        ballPosition.x=screenWidth/2;
        ballPosition.y=1+ballWidth;

        lives=3;




    }


    private class SquashCourtView extends SurfaceView implements Runnable{
        Thread mThread=null;
        SurfaceHolder mSurfaceHolder;
        volatile boolean playingSquash;
        Paint mPaint;

        public SquashCourtView(Context context) {
            super(context);
            mSurfaceHolder=getHolder();
            mPaint=new Paint();
            ballMovingDown=true;

            Random random=new Random();
            int ballDirection=random.nextInt(3);
            switch (ballDirection){
                case 0:
                    ballIsMovingLeft=true;
                    ballIsMovingRight=false;
                    break;
                case 1:
                    ballIsMovingLeft=false;
                    ballIsMovingRight=true;
                    break;
                case 2:
                    ballIsMovingLeft=false;
                    ballIsMovingRight=false;
                    break;

            }

        }

        @Override
        public void run() {
            while(playingSquash){
                updateCourt();
                drawCourt();
                controlFPS();
            }
        }


        public void updateCourt(){
            if(racketIsMovingRight){
                racketPosition.x=racketPosition.x+10;
            }
            if(racketIsMovingLeft){
                racketPosition.x-=10;
            }

            if(ballPosition.x+ballWidth>screenWidth){
                ballIsMovingLeft=true;
                ballIsMovingRight=false;
                mSoundPool.play(sample1,1,1,0,0,1);
            }

            if(ballPosition.x<0){
                ballIsMovingRight=true;
                ballIsMovingLeft=false;
                mSoundPool.play(sample2,1,1,0,0,1);
            }
            //when the ball hit the bottom
            if(ballPosition.y>screenHeight-ballWidth){
                lives-=1;
                if(lives==0){
                    lives=3;
                    score=0;
                    mSoundPool.play(sample4,1,1,0,0,1);
                }
                //after hitting the bottom, start the position at the top
                ballPosition.y=1+ballWidth;

                //figure out the hori dir for next falling ball
                Random random=new Random();
                int startX=random.nextInt(screenWidth-ballWidth)+1;
                ballPosition.x=startX+ballWidth;

                int ballDirection=random.nextInt(3);
                switch (ballDirection){
                    case 0:
                        ballIsMovingLeft=true;
                        ballIsMovingRight=false;
                        break;
                    case 1:
                        ballIsMovingLeft=false;
                        ballIsMovingRight=true;
                        break;
                    case 2:
                        ballIsMovingLeft=false;
                        ballIsMovingRight=false;
                        break;
                }
            }

            if(ballPosition.y<=0){
                ballMovingDown=true;
                ballIsMovingUp=false;
                ballPosition.y=1;
                mSoundPool.play(sample2,1,1,0,0,1);
            }

            if(ballMovingDown){
                ballPosition.y+=6;
            }
            if(ballIsMovingUp){
                ballPosition.y-=10;
            }
            if(ballIsMovingRight){
                ballPosition.x+=10;
            }
            if(ballIsMovingLeft){
                ballPosition.x-=10;
            }

            //when ball hits the racket
            if(ballPosition.y+ballWidth >= (racketPosition.y-racketHight/2)){
                int halfRacket=racketwidth/2;
                if(ballPosition.x+ballWidth>(racketPosition.x-halfRacket)
                        && ballPosition.x-ballWidth<(racketPosition.x+halfRacket)){
                    //rebounce the ball back vertically
                    mSoundPool.play(sample3,1,1,0,0,1);
                    score++;
                    ballIsMovingUp=true;
                    ballMovingDown=false;
                    if(ballPosition.x>racketPosition.x){
                        ballIsMovingRight=true;
                        ballIsMovingLeft=false;
                    }else{
                        ballIsMovingRight=false;
                        ballIsMovingLeft=true;
                    }

                }
            }
        }

        public void drawCourt() {
            if(mSurfaceHolder.getSurface().isValid()){
                mCanvas=mSurfaceHolder.lockCanvas();
                mCanvas.drawColor(Color.BLACK);
                mPaint.setColor(Color.argb(255,255,255,255));
                mPaint.setTextSize(45);
                mCanvas.drawText("Score: "+score+"\nLives: "+lives+"FPS: "+fps,20,40,mPaint);
                mCanvas.drawRect(racketPosition.x-(racketwidth/2),racketPosition.y-(racketHight/2),
                        racketPosition.x+(racketwidth/2),racketPosition.y+racketHight,mPaint);
                //draw the ball
                mCanvas.drawRect(ballPosition.x,ballPosition.y,ballPosition.x+ballWidth,
                        ballPosition.y+ballWidth,mPaint);

                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }

        public void controlFPS() {

            long timeThisFrame=(System.currentTimeMillis()-lastFrameTime);
            long timeToSleep=15-timeThisFrame;
            if(timeThisFrame>0){
                fps=(int)(1000/timeThisFrame);
            }
            if(timeToSleep>0){
                try {
                    Thread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lastFrameTime=System.currentTimeMillis();

        }

        public void pause(){
            playingSquash=true;
            try{
                mThread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        public  void resume(){
            playingSquash=true;
            mThread=new Thread(this);
            mThread.start();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch(event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    if(event.getX()>=screenWidth/2){
                        racketIsMovingRight=true;
                        racketIsMovingLeft=false;
                    }else{
                        racketIsMovingLeft=true;
                        racketIsMovingRight=false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    racketIsMovingRight=false;
                    racketIsMovingLeft=false;
            }
            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSquashCourtView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //ensure the thread is stop
        mSquashCourtView.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        while (true){
            mSquashCourtView.pause();
            break;
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            mSquashCourtView.pause();
            finish();
            return true;
        }
        return false;
    }
}
