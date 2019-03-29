package com.example.tarsosdsptest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends AppCompatActivity {

    private TextView textview;
    private String[] myPermissions = new String[]{
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        textview = (TextView) findViewById(R.id.textview);
        PermissionGetting.setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                initData();
            }

            @Override
            public void onPermissionDenied() {


            }
        }, this, myPermissions);


    }

    private void initData() {

        AudioDispatcher dispatcher =
                AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e) {
                final float pitchInHz = res.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processPitch(pitchInHz);
                    }
                });
            }
        };
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);

        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    private void processPitch(float pitchInHz) {
        textview.setText(pitchInHz + "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGetting.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                initData();
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(MainActivity.this, "我们需要" + Arrays.toString(permissions) + "权限", Toast.LENGTH_SHORT).show();
                PermissionGetting.showToAppSettingDialog();

            }
        });

    }


}
