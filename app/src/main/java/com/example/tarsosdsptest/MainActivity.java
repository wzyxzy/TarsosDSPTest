package com.example.tarsosdsptest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    List<Entry> yStandardDataList = new ArrayList<Entry>();// y轴数据数据源
    private int count;
    private boolean isBegin;
    private boolean isMusicMode;
    private long dateTime;
    private Timer timer;
    private TimerTask timerTask;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ChartUtil.showChart(MainActivity.this, lineChart, xDataList, yDataList, "频率图", "频率/时间", "Hz", isMusicMode, false);
                    break;
                case 1:
                    timer = new Timer();
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (isBegin) {
                                lineChart.notifyDataSetChanged();
                                lineChart.invalidate();
//                                Logger.d(count);
//                                Logger.d(yDataList.toArray());
                            }
                        }
                    };
                    timer.schedule(timerTask, 500, 500);


                    break;
            }
        }
    };
    private int TIME = 5000;
    private Runnable runnable;
    private Button bigin;
    private Button clear;
    private Switch switchButton;
    private Button score;
    private Button standard;

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
                xDataList.clear();
                yDataList.clear();
                count = 0;
                isBegin = false;
                bigin.setText("开始测试");
                lineChart.clear();
                initData();
            }
        });
        score = (Button) findViewById(R.id.score);
        score.setOnClickListener(this);
        standard = (Button) findViewById(R.id.standard);
        standard.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//软件在后台屏幕不需要常亮
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
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
//            yAxis.setDrawGridLines(true);
            int value = 0;
            if (pitchInHz < 85) {
                value = 0;
            } else if (pitchInHz < 90) {
                value = 1;
            } else if (pitchInHz < 95) {
                value = 2;
            } else if (pitchInHz < 100) {
                value = 3;
            } else if (pitchInHz < 106) {
                value = 4;
            } else if (pitchInHz < 113) {
                value = 5;
            } else if (pitchInHz < 120) {
                value = 6;
                ;
            } else if (pitchInHz < 127) {
                value = 7;
            } else if (pitchInHz < 135) {
                value = 8;
            } else if (pitchInHz < 142) {
                value = 9;
            } else if (pitchInHz < 151) {
                value = 10;
            } else if (pitchInHz < 160) {
                value = 11;
            } else if (pitchInHz < 170) {
                value = 12;
            } else if (pitchInHz < 180) {
                value = 13;
            } else if (pitchInHz < 191) {
                value = 14;
            } else if (pitchInHz < 202) {
                value = 15;
            } else if (pitchInHz < 214) {
                value = 16;
            } else if (pitchInHz < 226) {
                value = 17;
            } else if (pitchInHz < 240) {
                value = 18;
            } else if (pitchInHz < 254) {
                value = 19;
            } else if (pitchInHz < 269) {
                value = 20;
            } else if (pitchInHz < 285) {
                value = 21;
            } else if (pitchInHz < 302) {
                value = 22;
            } else if (pitchInHz < 320) {
                value = 23;
            } else if (pitchInHz < 339) {
                value = 24;
            } else if (pitchInHz < 359) {
                value = 25;
            } else if (pitchInHz < 381) {
                value = 26;
            } else if (pitchInHz < 404) {
                value = 27;
            } else if (pitchInHz < 428) {
                value = 28;
            } else if (pitchInHz < 453) {
                value = 29;
            } else if (pitchInHz < 480) {
                value = 30;
            } else if (pitchInHz < 508) {
                value = 31;
            } else if (pitchInHz < 539) {
                value = 32;
            } else if (pitchInHz < 571) {
                value = 33;
            } else if (pitchInHz < 604) {
                value = 34;
            } else if (pitchInHz < 641) {
                value = 35;
            } else if (pitchInHz < 679) {
                value = 36;
            } else if (pitchInHz < 719) {
                value = 37;
            } else if (pitchInHz < 762) {
                value = 38;
            } else if (pitchInHz < 807) {
                value = 39;
            } else if (pitchInHz < 855) {
                value = 40;
            } else if (pitchInHz < 906) {
                value = 41;
            } else if (pitchInHz >= 906) {
                value = 42;
            } else {
                value = 0;
            }
            yDataList.add(new Entry(value, ++count));
        } else {

            yDataList.add(new Entry(pitchInHz, ++count));

        }
//        handler.sendEmptyMessage(1);
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
            case R.id.score:

                if (yStandardDataList == null || yStandardDataList.size() == 0) {
                    Toast.makeText(this, "还没有上传标准唱，请先上传！", Toast.LENGTH_SHORT).show();
                } else {
                    ScoreUtils scoreUtils = new ScoreUtils(yStandardDataList, yDataList);
                    Logger.d(yStandardDataList.toArray());
                    Logger.d(yDataList.toArray());
                    float[] scoreTime = scoreUtils.scoreTime();
                    float[] scoreFrequency = scoreUtils.scoreFrequency();
                    final CommonDialog commonDialog = new CommonDialog(this);
                    commonDialog.setTitle(" 得 分 情 况 : ");
                    commonDialog.setMessage("节奏误差：" + scoreTime[0] + "，标准唱总帧数为：" + scoreTime[1] + "，您的总帧数为：" + scoreTime[2] + "，音准误差率为：" + scoreFrequency[0] + "，误差个数为：" + scoreFrequency[1]);
                    commonDialog.show();

                }


                break;
            case R.id.standard:
                showStyleDialog();
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
                        if (isBegin) {
                            processPitch(pitchInHz);
                        }

                    }
                });
            }
        };
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);
        audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
        handler.sendEmptyMessage(1);

    }

    private void showStyleDialog() {

        final CommonDialog commonDialog = new CommonDialog(this);
        commonDialog.setTitle("温 馨 提 示 :");
        commonDialog.setMessage("您确定要上传标准唱吗？");
        commonDialog.setRightButtonClickListener(new CommonDialog.RightButtonClickListener() {
            @Override
            public void onRightButtonClick() {
//                ChartUtil.showChart(MainActivity.this, lineChart, xDataList, yDataList, "频率图", "频率/时间", "Hz", isMusicMode, true);
                yStandardDataList.clear();
                yStandardDataList.addAll(yDataList);
                xDataList.clear();
                yDataList.clear();
                count = 0;
                isBegin = false;
                bigin.setText("开始测试");
                lineChart.clear();
                initData();
                commonDialog.cancel();


            }
        });
        commonDialog.show();
    }
}
