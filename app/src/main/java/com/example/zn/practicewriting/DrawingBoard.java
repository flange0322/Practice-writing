package com.example.zn.practicewriting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class DrawingBoard extends AppCompatActivity {

    public ImageView mImageView;
    private GifImageView mGifImageView;
    private GifDrawable mGifDrawable;
    private Button btn_Multi_Status;
    private Button btn_NextWord;
    private Timer mTimer;
    private DrawTool mDrawTool;
    private DataHandler mSender;
    private ArrayList<DrawableDataType> DataSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_board);

        init();
        DataSize = mSender.getDrawableData(getBundleData("wordCode"),DataSize);

        try{
            int resId = getResId();
            mGifDrawable = new GifDrawable(getResources(),resId);
            mGifImageView.setImageDrawable(mGifDrawable);
            mGifDrawable.setLoopCount(1);
            mGifDrawable.stop();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        mDrawTool = new DrawTool();
        mDrawTool.initPaint();
        mDrawTool.setOwner(mImageView);
        mImageView.setOnTouchListener(mDrawTool.touch);
    }

    public void init(){
        mGifImageView = findViewById(R.id.iv_Gif);
        mImageView = findViewById(R.id.iv_Canvas);
        btn_Multi_Status = findViewById(R.id.anime_Start);
        btn_NextWord = findViewById(R.id.word_Next);
        DataSize = new ArrayList<>();
        mSender = new DataHandler(this);
    }

    public int getResId(){
        int resId = 0;
        String wordName = getBundleData("wordName");
        for(int i=0;i<DataSize.size();i++){
            String tmp = DataSize.get(i).word_Name;
            if(tmp.equals(wordName)){
                resId = getResources().getIdentifier(DataSize.get(i).drawable_Name,"drawable",getPackageName());
                break;
            }
        }
        return resId;
    }

    public String getBundleData(String key){
        Bundle bundle_title = this.getIntent().getExtras();
        String temp = bundle_title.getString(key);
        return temp;
    }

    public void resetClick(View view) {
        mGifDrawable.reset();
        btn_Multi_Status.setText(R.string.btn_Starting);
        btn_Multi_Status.setEnabled(false);
        btn_NextWord.setEnabled(false);

        int exec_Time = mGifDrawable.getDuration();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mGifDrawable.stop();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!mGifDrawable.isPlaying()){
                            btn_Multi_Status.setText(R.string.btn_Restart);
                            btn_Multi_Status.setEnabled(true);
                            btn_NextWord.setEnabled(true);
                            cancel();
                        }
                    }
                });
                cancel();
            }
        },exec_Time);
    }

    public void resumeCanvas(View view) {
        switch (view.getId()){
            case R.id.canvas_Clear:
                mDrawTool.resumeCanvas();
                Toast.makeText(DrawingBoard.this,
                        "清除畫板成功，可以重新開始繪圖",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void wordNext(View view) {

    }
}
