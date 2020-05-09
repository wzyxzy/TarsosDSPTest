package com.example.tarsosdsptest;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SoundPlayer {
    private static final String LOG_TAG = "SoundPlayer";
    MediaPlayer mPlayer;
    private MediaRecorder mRecorder;
    private boolean isRecording;

    public void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(newFileName());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();

    }

    public void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public void pauseRecording() {
        mRecorder.stop();
    }

    private String newFileName() {
        String mFileName = Environment.getExternalStorageDirectory()
                .getAbsolutePath();

//        String s = new SimpleDateFormat("yyyy-MM-dd hhmmss")
//                .format(new Date());
        return mFileName += "/rcd_yinyue.3gp";
    }

    public void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(newFileName());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }
}