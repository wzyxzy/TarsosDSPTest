package com.example.tarsosdsptest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tarsosdsptest.common.TestApplication;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
    private ReschedulableTimerTask timerTask;
    private int time_during = 0;
    private int time_max = 1000;
    private int time_min = 100;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!isMusicMode)
                        ChartUtil.showChart(MainActivity.this, lineChart, xDataList, yDataList, "频率图", "频率/时间", "Hz", isMusicMode, false);
                    break;
                case 1:
                    timer = new Timer();
                    timerTask = new ReschedulableTimerTask() {
                        @Override
                        public void run() {
                            if (isBegin) {
                                if (!isMusicMode) {

                                    time_during = (int) (time_min * Math.pow(2, count / 100 - 1));
                                    timerTask.setPeriod(time_during > time_max ? time_max : time_during);
                                    lineChart.notifyDataSetChanged();
                                    lineChart.invalidate();
//                                    Logger.d(count);
//                                    Logger.d(yDataList.toArray());
                                }

                            }
                        }
                    };
                    timer.scheduleAtFixedRate(timerTask, time_min, time_min);


                    break;
                case 2:
                    String visibleNum = "";
                    switch (msg.arg1) {
                        case 0:
                            visibleNum = "low";
                            break;
                        case 1:
                            visibleNum = "2F";
                            break;
                        case 2:
                            visibleNum = "2F#";
                            break;
                        case 3:
                            visibleNum = "2G";
                            break;
                        case 4:
                            visibleNum = "2G#";
                            break;
                        case 5:
                            visibleNum = "2A";
                            break;
                        case 6:
                            visibleNum = "2A#";
                            break;
                        case 7:
                            visibleNum = "2B";
                            break;
                        case 8:
                            visibleNum = "3C";
                            break;
                        case 9:
                            visibleNum = "3C#";
                            break;
                        case 10:
                            visibleNum = "3D";
                            break;
                        case 11:
                            visibleNum = "3D#";
                            break;
                        case 12:
                            visibleNum = "3E";
                            break;
                        case 13:
                            visibleNum = "3F";
                            break;
                        case 14:
                            visibleNum = "3F#";
                            break;
                        case 15:
                            visibleNum = "3G";
                            break;
                        case 16:
                            visibleNum = "3G#";
                            break;
                        case 17:
                            visibleNum = "3A";
                            break;
                        case 18:
                            visibleNum = "3A#";
                            break;
                        case 19:
                            visibleNum = "3B";
                            break;
                        case 20:
                            visibleNum = "4C";
                            break;
                        case 21:
                            visibleNum = "4C#";
                            break;
                        case 22:
                            visibleNum = "4D";
                            break;
                        case 23:
                            visibleNum = "4D#";
                            break;
                        case 24:
                            visibleNum = "4E";
                            break;
                        case 25:
                            visibleNum = "4F";
                            break;
                        case 26:
                            visibleNum = "4F#";
                            break;
                        case 27:
                            visibleNum = "4G";
                            break;
                        case 28:
                            visibleNum = "4G#";
                            break;
                        case 29:
                            visibleNum = "4A";
                            break;
                        case 30:
                            visibleNum = "4A#";
                            break;
                        case 31:
                            visibleNum = "4B";
                            break;
                        case 32:
                            visibleNum = "5C";
                            break;
                        case 33:
                            visibleNum = "5C#";
                            break;
                        case 34:
                            visibleNum = "5D";
                            break;
                        case 35:
                            visibleNum = "5D#";
                            break;
                        case 36:
                            visibleNum = "5E";
                            break;
                        case 37:
                            visibleNum = "5F";
                            break;
                        case 38:
                            visibleNum = "5F#";
                            break;
                        case 39:
                            visibleNum = "5G";
                            break;
                        case 40:
                            visibleNum = "5G#";
                            break;
                        case 41:
                            visibleNum = "5A";
                            break;
                        case 42:
                            visibleNum = "HIGH";
                            break;
                    }
//                    stringBuffer.append(visibleNum + "|");
                    dorimi.append(visibleNum + "|");
                    nowPitch.setText(visibleNum);

                    break;
                case 3:
//                    nowPitchWord.setText(msg.obj.toString());

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
    private TextView nowPitch;
//    private TextView nowPitchWord;
    private TextView dorimi;
//    private StringBuffer stringBuffer = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count = 0;
        isBegin = false;
        isMusicMode = true;
        initView();
    }


//    private void initRobot() {
//        //设置语音引擎状态更新接口
////        LeoSpeech.setViewUpdater(this);
//        ResultProcessor mResultProcessor = new ResultProcessor(this);
//        Log.v("wss", "init................");
//        LeoSpeech.init(this, mResultProcessor);
////        LeoSpeech.setViewUpdater(this);
//        LeoSpeech.makelocalGrammar();
//
//        mResultProcessor.setOnVoiceListener(new ResultProcessor.OnVoiceListener() {
//
//            @Override
//            public void onWords(String words) {
//                Message message = new Message();
//                message.obj = words;
//                message.what = 3;
//                handler.sendMessage(message);
//            }
//
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onSuccess(int nums) {
//            }
//
//            @Override
//            public void onFail() {
//
//            }
//        });
//
//
//    }

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
        score = (Button) findViewById(R.id.score);
        score.setOnClickListener(this);
        standard = (Button) findViewById(R.id.standard);
        standard.setOnClickListener(this);
        switchButton = (Switch) findViewById(R.id.switchButton);
        switchButton.setOnClickListener(this);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMusicMode = true;
                    score.setVisibility(View.VISIBLE);
                    standard.setVisibility(View.VISIBLE);
                    dorimi.setVisibility(View.VISIBLE);
                } else {
                    isMusicMode = false;
                    score.setVisibility(View.GONE);
                    standard.setVisibility(View.GONE);
                    nowPitch.setVisibility(View.GONE);
                    dorimi.setVisibility(View.GONE);
                }
                xDataList.clear();
                yDataList.clear();
                count = 0;
                isBegin = false;
                bigin.setText("开始测试");
                lineChart.clear();
                initData();
                dorimi.setText("");
            }
        });

        nowPitch = (TextView) findViewById(R.id.nowPitch);
        nowPitch.setOnClickListener(this);
//        nowPitchWord = (TextView) findViewById(R.id.nowPitchWord);
//        nowPitchWord.setOnClickListener(this);
        dorimi = (TextView) findViewById(R.id.dorimi);
        dorimi.setOnClickListener(this);
        dorimi.setMovementMethod(ScrollingMovementMethod.getInstance());
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
        if (timer != null)
            timer.cancel();
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
            Message message = new Message();
            message.what = 2;
            message.arg1 = value;
            handler.sendMessage(message);
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
                    if (xDataList.size() == 2) {
                        dateTime = new Date().getTime();
                    }
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
                dorimi.setText("");
                break;
            case R.id.score:
                String data = SPUtility.getSPString(MainActivity.this, "listStr");
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Entry>>() {
                }.getType();
                yStandardDataList = gson.fromJson(data, listType);

                if (yStandardDataList == null || yStandardDataList.size() == 0) {
                    Toast.makeText(this, "还没有上传标准唱，请先上传！", Toast.LENGTH_SHORT).show();
//                    yStandardDataList.clear();
                } else {
                    ScoreUtils2 scoreUtils2 = new ScoreUtils2(yStandardDataList, yDataList);
                    Logger.d(yStandardDataList.toArray());
                    Logger.d(yDataList.toArray());
//                    yStandardDataList.clear();
                    float[] scoreTime = scoreUtils2.scoreTime();
                    float[] scoreFrequency = scoreUtils2.scoreFrequency2();

                    int genTimeScore = 0;
                    int genzScore = (int) Math.sqrt(20 / scoreFrequency[0]);
                    genzScore = genzScore > 10 ? 10 : genzScore;
                    if (scoreTime[0] < 0.01)
                        genTimeScore = 10;
                    else if (scoreTime[0] < 0.05)
                        genTimeScore = 9;
                    else if (scoreTime[0] < 0.1)
                        genTimeScore = 8;
                    else if (scoreTime[0] < 0.4)
                        genTimeScore = (int) (1 / scoreTime[0]);
                    else
                        genTimeScore = (int) ((1 / scoreTime[0]) * (1 / scoreTime[0]));

//                    if (scoreFrequency[2]>50){
//
//                    }
                    yStandardDataList.clear();
//                    final CommonDialog commonDialog = new CommonDialog(this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(" 得 分 情 况 : ");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            xDataList.clear();
                            yDataList.clear();
                            count = 0;
                            isBegin = false;
                            bigin.setText("开始测试");
                            lineChart.clear();
                            initData();
                            dorimi.setText("");
                            dialog.dismiss();
                        }
                    });
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
                    if (TestApplication.isNotMusic)
                        builder.setMessage("您好像还没有唱歌哦！");
                    else
                        builder.setMessage("系统评分：节奏" + genTimeScore + "分，音准" + genzScore + "分\n明细如下\n节奏误差：" + scoreTime[0] + "，标准唱总帧数为：" + scoreTime[1] + "，您的总帧数为：" + scoreTime[2] + "，音准误差率为：" + scoreFrequency[0] + "，误差个数为：" + scoreFrequency[1] + "，大幅度偏差个数为：" + scoreFrequency[2]);


                    builder.create().show();
//                    commonDialog.setTitle(" 得 分 情 况 : ");
//                    commonDialog.setRightButtonClickListener(new CommonDialog.RightButtonClickListener() {
//                        @Override
//                        public void onRightButtonClick() {
//                            xDataList.clear();
//                            yDataList.clear();
//                            count = 0;
//                            isBegin = false;
//                            bigin.setText("开始测试");
//                            lineChart.clear();
//                            initData();
//                            commonDialog.cancel();
//                        }
//                    });
////                    commonDialog.setLeftButtonClickListener();
//                    commonDialog.setMessage("系统评分：节奏" + genTimeScore + "分，音准" + genzScore + "分\n明细如下\n节奏误差：" + scoreTime[0] + "，标准唱总帧数为：" + scoreTime[1] + "，您的总帧数为：" + scoreTime[2] + "，音准误差率为：" + scoreFrequency[0] + "，误差个数为：" + scoreFrequency[1]);
//                    commonDialog.show();


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
                AudioDispatcherFactory.fromDefaultMicrophone(11025, 1024, 0);
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
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 11025, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);
        audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
        handler.sendEmptyMessage(1);

    }

    private void showStyleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温 馨 提 示 :");
        builder.setMessage("您确定要上传标准唱吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yStandardDataList.addAll(yDataList);
                Gson gson = new Gson();
                String data = gson.toJson(yStandardDataList);
                SPUtility.putSPString(MainActivity.this, "listStr", data);
                yStandardDataList.clear();
                xDataList.clear();
                yDataList.clear();
                count = 0;
                isBegin = false;
                bigin.setText("开始测试");
                lineChart.clear();
                initData();
                dorimi.setText("");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
//        final CommonDialog commonDialog = new CommonDialog(this);
//        commonDialog.setTitle("温 馨 提 示 :");
//        commonDialog.setMessage("您确定要上传标准唱吗？");
//        commonDialog.setRightButtonClickListener(new CommonDialog.RightButtonClickListener() {
//            @Override
//            public void onRightButtonClick() {
////                ChartUtil.showChart(MainActivity.this, lineChart, xDataList, yDataList, "频率图", "频率/时间", "Hz", isMusicMode, true);
//
////                yStandardDataList.clear();
//                yStandardDataList.addAll(yDataList);
//                Gson gson = new Gson();
//                String data = gson.toJson(yStandardDataList);
//                SPUtility.putSPString(MainActivity.this, "listStr", data);
//                yStandardDataList.clear();
//                xDataList.clear();
//                yDataList.clear();
//                count = 0;
//                isBegin = false;
//                bigin.setText("开始测试");
//                lineChart.clear();
//                initData();
//                commonDialog.cancel();
//
//
//            }
//        });
//        commonDialog.show();
    }

    public abstract class ReschedulableTimerTask extends TimerTask {
        public void setPeriod(long period) {
            //缩短周期，执行频率就提高
            setDeclaredField(TimerTask.class, this, "period", period);
        }

        //通过反射修改字段的值
        boolean setDeclaredField(Class<?> clazz, Object obj,
                                 String name, Object value) {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                field.set(obj, value);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }
}
