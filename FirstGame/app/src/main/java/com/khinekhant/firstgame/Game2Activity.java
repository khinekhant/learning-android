package com.khinekhant.firstgame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Random;

public class Game2Activity extends Activity implements View.OnClickListener{

    private SoundPool mSoundPool;
    int simple1 =-1;
    int simple2=-1;
    int simple3=-1;
    int simple4=-1;

    TextView textScore;
    TextView textDiff;
    TextView textWatchGo;

    Button mButton1;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    Button replayButton;

    int diffLevel=3;
    int[] copySequence=new int[100];
    private Handler mHandler;
    boolean generateSounds=false;
    int elementToPlay=0;

    int playerResponse;
    int playerScore;
    boolean isResponding;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEdit;
    public static final String fileName="MyDataFile";
    public static  final String mKey="KeyForValue";
    int defaultValue=0;
    int highScore;

    Animation wooble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realgame);
        wooble= AnimationUtils.loadAnimation(this,R.anim.wooble);

        mPreferences=getSharedPreferences(fileName,MODE_PRIVATE);
        mEdit=mPreferences.edit();
        highScore=mPreferences.getInt(mKey,defaultValue);
        //noinspection deprecation
        mSoundPool=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        AssetManager assetManager=getAssets();
        AssetFileDescriptor fileDescriptor;

        try {
            fileDescriptor=assetManager.openFd("sample1.ogg");

        simple1=mSoundPool.load(fileDescriptor,0);

        fileDescriptor=assetManager.openFd("sample2.ogg");
        simple2=mSoundPool.load(fileDescriptor,0);

        fileDescriptor=assetManager.openFd("sample3.ogg");
        simple3=mSoundPool.load(fileDescriptor,0);

        fileDescriptor=assetManager.openFd("sample4.ogg");
        simple4=mSoundPool.load(fileDescriptor,0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        textScore=(TextView) findViewById(R.id.scoreButton);
        textScore.setText("Score: "+playerScore);
        textDiff=(TextView) findViewById(R.id.textDifficulty);
        textDiff.setText("Level: "+diffLevel);
        textWatchGo=(TextView) findViewById(R.id.textWatchGo);

        mButton1=(Button) findViewById(R.id.button);
        mButton2=(Button) findViewById(R.id.button2);
        mButton3=(Button) findViewById(R.id.button3);
        mButton4=(Button) findViewById(R.id.button4);
        replayButton=(Button) findViewById(R.id.buttonReplay);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        replayButton.setOnClickListener(this);

        mHandler=new Handler(){
            public void  handleMessage(Message msg){
                super.handleMessage(msg);

                if(generateSounds){
                  //  mButton1.setVisibility(View.VISIBLE);
                   // mButton2.setVisibility(View.VISIBLE);
                   // mButton3.setVisibility(View.VISIBLE);
                    //mButton4.setVisibility(View.VISIBLE);
                    switch (copySequence[elementToPlay]){
                        case 1:
                           // mButton1.setVisibility(View.INVISIBLE);
                            mButton1.startAnimation(wooble);
                            mSoundPool.play(simple1,1,1,0,0,1);
                            break;
                        case 2:
                            //mButton2.setVisibility(View.INVISIBLE);
                            mButton2.startAnimation(wooble);
                            mSoundPool.play(simple2,1,1,0,0,1);
                            break;
                        case 3:
                            //mButton3.setVisibility(View.INVISIBLE);
                            mButton3.startAnimation(wooble);
                            mSoundPool.play(simple3,1,1,0,0,1);
                            break;
                        case 4:
                            //mButton4.setVisibility(View.INVISIBLE);
                            mButton4.startAnimation(wooble);
                            mSoundPool.play(simple4,1,1,0,0,1);
                            break;
                    }
                    elementToPlay++;
                    if(elementToPlay==diffLevel) sequenceFinished();

                }
                mHandler.sendEmptyMessageDelayed(0,900);
            }
        };
        mHandler.sendEmptyMessage(0);


    }
    public void check(int buttonNumber ){
        if(isResponding){
            playerResponse++;
            if(copySequence[playerResponse-1]==buttonNumber){
                playerScore+=(buttonNumber+1)*2;
                textScore.setText("Score: "+playerScore);
                if(playerResponse==diffLevel){
                    isResponding=false;
                    diffLevel++;
                    playSequence();
                }
            }else{
                textWatchGo.setText("FAILED");
                isResponding=false;

                if(playerScore>highScore){
                    highScore=playerScore;
                    mEdit.putInt(mKey,highScore);
                    mEdit.commit();
                    Toast.makeText(this,"New High Score ",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(!generateSounds){
            switch (view.getId()){
                case R.id.button:
                    mSoundPool.play(simple1,1,1,0,0,1);
                    check(1);
                    break;
                case R.id.button2:
                    mSoundPool.play(simple2,1,1,0,0,1);
                    check(2);
                    break;
                case R.id.button3:
                    mSoundPool.play(simple3,1,1,0,0,1);
                    check(3);
                    break;
                case R.id.button4:
                    mSoundPool.play(simple4,1,1,0,0,1);
                    check(4);
                    break;
                case R.id.buttonReplay:
                    playerScore=0;
                    diffLevel=3;
                    textScore.setText("Score: "+playerScore);
                    playSequence();
                    break;
            }
        }


    }
    public void createSequence(){
        Random random=new Random();
        int r;
        for(int i=0;i<diffLevel;i++){
            r=random.nextInt(4);
            r++;

            copySequence[i]=r;
        }
    }

    public void playSequence(){
        createSequence();
        isResponding=false;
        elementToPlay=0;
        playerResponse=0;
        textWatchGo.setText("Watch!");
        generateSounds=true;
    }

    public void sequenceFinished(){

        generateSounds=false;
       // mButton1.setVisibility(View.VISIBLE);
       // mButton2.setVisibility(View.VISIBLE);
       // mButton3.setVisibility(View.VISIBLE);
       // mButton4.setVisibility(View.VISIBLE);
        textWatchGo.setText("GO!");
        isResponding = true;
    }
}
