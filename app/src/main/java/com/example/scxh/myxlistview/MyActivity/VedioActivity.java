package com.example.scxh.myxlistview.MyActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.R;

public class VedioActivity extends AppCompatActivity {
    private static final String TAG = "MediaPlayer";
    private TextView mToastTxt;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private static final String strVideoPath = "/mnt/sdcard/voide/test30m.3gp";
    private String mVideoHttpUrl = "http://flv2.bn.netease.com/videolib3/1503/06/KHDxR7409/SD/KHDxR7409-mobile.mp4";

    private int mVideoWidth;
    private int mVideoHeight;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_layout);

        mToastTxt = (TextView) findViewById(R.id.myTextView1);
        mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView1);

        Bundle bundle = getIntent().getBundleExtra("BUNDLE");
        mVideoHttpUrl= bundle.getString("URL");
        Logs.e("mVideoHttpUrl>>>>"+mVideoHttpUrl);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Logs.e("surface Created");
                inintVideo();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Logs.e("Surface Changed");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Logs.e("Surface Destroyed");
                if (mMediaPlayer != null) {
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
            }
        });


//		playerVideoByIntent();
    }

    private void inintVideo() {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mVideoHttpUrl);
        } catch (Exception e) {
            mToastTxt.setText("setDataSource Exception:" + e.toString());
            e.printStackTrace();
        }
        mMediaPlayer.setDisplay(mSurfaceHolder);
        try {
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            mToastTxt.setText("prepare Exceeption:" + e.toString());
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Logs.e("onPrepared called");
                mVideoWidth = mMediaPlayer.getVideoWidth();
                mVideoHeight = mMediaPlayer.getVideoHeight();
                if (mVideoWidth != 0 && mVideoHeight != 0) {
            /* 设置视频的宽度和高度 */
                    mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
			/* 开始播放 */
                    mMediaPlayer.start();
                    mToastTxt.setText("播放");
                }
            }
        });//预处理结束监听

        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                mToastTxt.setText("播放进度:" + percent);
            }
        }); //播放进度监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mToastTxt.setText("播放结束");
            }
        });//播放完成监听

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }


    private boolean checkSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


}

