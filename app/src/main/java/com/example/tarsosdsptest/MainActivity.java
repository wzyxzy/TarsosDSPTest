package com.example.tarsosdsptest;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LineChart lineChart;
    private String[] myPermissions = new String[]{
            Manifest.permission.RECORD_AUDIO
    };

    List<String> xDataList = new ArrayList<>();// x轴数据源
    List<Entry> yDataList = new ArrayList<Entry>();// y轴数据数据源
    private int count;
    private boolean isBegin;
    private boolean isMusicMode;
    private long dateTime;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ChartUtil.showChart(MainActivity.this, lineChart, xDataList, yDataList, "频率图", "频率/时间", "hz/s");
                    break;
                case 1:
                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                    break;
            }
        }
    };
    private int TIME = 5000;
    private Runnable runnable;
    private Button bigin;
    private Button clear;
    private Switch switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count = 0;
        isBegin = false;
        isMusicMode = false;
        initView();
    }

    private void initView() {
        lineChart = (LineChart) findViewById(R.id.lineChart);
        PermissionGetting.setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                initData();
            }

            @Override
            public void onPermissionDenied() {

            }
        }, this, myPermissions);
        bigin = (Button) findViewById(R.id.bigin);
        bigin.setOnClickListener(this);
        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(this);
        switchButton = (Switch) findViewById(R.id.switchButton);
        switchButton.setOnClickListener(this);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMusicMode = true;
                } else {
                    isMusicMode = false;
                }
            }
        });
    }

    private void initData() {
        xDataList.add("00:00");
        xDataList.add("00:00");
        yDataList.add(new Entry(0, ++count));
        yDataList.add(new Entry(0, ++count));
        endAndChart();
        dateTime = new Date().getTime();
    }

    private void processPitch(final float pitchInHz) {
        DateFormat df = new SimpleDateFormat("mm:ss");
        String format = df.format(new Date().getTime() - dateTime);
        xDataList.add(format);
        if (isMusicMode) {
            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setAxisMaxValue(8);
            yAxis.setAxisMinValue(1);
            if (pitchInHz > 240 && pitchInHz < 278)
                yDataList.add(new Entry(1, ++count));
            else if (pitchInHz > 278 && pitchInHz < 310)
                yDataList.add(new Entry(2, ++count));
            else if (pitchInHz > 310 && pitchInHz < 339)
                yDataList.add(new Entry(3, ++count));
            else if (pitchInHz > 339 && pitchInHz < 370)
                yDataList.add(new Entry(4, ++count));
            else if (pitchInHz > 370 && pitchInHz < 416)
                yDataList.add(new Entry(5, ++count));
            else if (pitchInHz > 416 && pitchInHz < 467)
                yDataList.add(new Entry(6, ++count));
            else if (pitchInHz > 467 && pitchInHz < 508)
                yDataList.add(new Entry(7, ++count));
            else if (pitchInHz > 508 && pitchInHz < 538)
                yDataList.add(new Entry(8, ++count));
            else if (pitchInHz < 240)
                yDataList.add(new Entry(0, ++count));
            else
                yDataList.add(new Entry(0, ++count));
        } else {
            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setAxisMaxValue(600);
            yAxis.setAxisMinValue(100);
            yDataList.add(new Entry(pitchInHz, ++count));

        }
        handler.sendEmptyMessage(1);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bigin:
                if (isBegin) {
                    isBegin = false;
                    bigin.setText("开始测试");
                } else {
                    isBegin = true;
                    bigin.setText("暂停测试");
                    startRecord();
                }
                break;
            case R.id.clear:
                xDataList.clear();
                yDataList.clear();
                count = 0;
                isBegin = false;
                bigin.setText("开始测试");
                lineChart.clear();
                initData();
                break;
        }
    }

    Thread audioThread;

    private void endAndChart() {
        handler.sendEmptyMessage(0);
    }

    private void startRecord() {
        AudioDispatcher dispatcher =
                AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e) {
                final float pitchInHz = res.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isBegin)
                            processPitch(pitchInHz);
                    }
                });
            }
        };
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);
        audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();

    }
}
