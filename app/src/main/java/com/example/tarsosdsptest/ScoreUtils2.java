package com.example.tarsosdsptest;

import com.github.mikephil.charting.data.Entry;
import com.orhanobut.logger.Logger;

import java.util.List;

import static com.example.tarsosdsptest.common.TestApplication.isNotMusic;

public class ScoreUtils2 {

    private List<Entry> yStandardDataList;
    private List<Entry> yDataList;
    private int[] standardFrame;
    private int[] userFrame;
    private int gap = 6;

    public ScoreUtils2(List<Entry> yStandardDataList, List<Entry> yDataList) {
        this.yStandardDataList = yStandardDataList;
        this.yDataList = yDataList;
        standardFrame = recordFrame(yStandardDataList);
        userFrame = recordFrame(yDataList);
    }

    public float[] scoreTime() {
        return new float[]{((float) Math.abs(countFrame(standardFrame) - countFrame(userFrame))) / countFrame(standardFrame), countFrame(standardFrame), countFrame(userFrame)};
    }

    public float[] scoreFrequency() {
        isNotMusic = userFrame[0] == userFrame[1];
        float score = 0;
        float highGapCount = 0;
        if (countFrame(standardFrame) > countFrame(userFrame)) {
            for (int i = userFrame[0]; i < userFrame[1]; i++) {
                try {
                    float everyScore = Math.abs(yDataList.get(i).getVal() - yStandardDataList.get((i - userFrame[0]) * countFrame(standardFrame) / countFrame(userFrame) + standardFrame[0]).getVal());
                    score += everyScore > gap ? 0 : everyScore;
                    highGapCount += everyScore > gap ? 1 : 0;
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

//                Logger.d(score);
            }
        } else {
            for (int i = standardFrame[0]; i < standardFrame[1]; i++) {
                try {
                    float everyScore = Math.abs(yStandardDataList.get(i).getVal() - yDataList.get((i - standardFrame[0]) * countFrame(userFrame) / countFrame(standardFrame) + userFrame[0]).getVal());
                    score += everyScore > gap ? 0 : everyScore;
                    highGapCount += everyScore > gap ? 1 : 0;
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
//                Logger.d(score);

            }
        }
        return new float[]{score / countFrame(userFrame), score, highGapCount};
    }

    public float[] scoreFrequency2() {
        isNotMusic = userFrame[0] == userFrame[1];
        List<Entry> yStandardNoZero = reduceZeroFrame(yStandardDataList);
        List<Entry> yNoZero = reduceZeroFrame(yDataList);
        return yStandardNoZero.size() > yNoZero.size() ? scoreCross(yStandardNoZero, yNoZero) : scoreCross(yNoZero, yStandardNoZero);
    }

    private float[] scoreCross(List<Entry> moreSize, List<Entry> lessSize) {
        float score = 0;
        float highGapCount = 0;
        int more = moreSize.size();
        int less = lessSize.size();
        for (int i = 0; i < lessSize.size(); i++) {
            try {
                float everyScore = Math.abs(lessSize.get(i).getVal() - moreSize.get(i * more / less).getVal());
                score += everyScore > gap ? 0 : everyScore;
                highGapCount += everyScore > gap ? 1 : 0;
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
        return new float[]{score / less, score, highGapCount / less};
    }

    private int[] recordFrame(List<Entry> dataList) {
        int begin = 0, end = 0;
        boolean isDoneBegin = true;
        for (int i = 0; i < dataList.size(); i++) {
            if (isDoneBegin && dataList.get(i).getVal() > 0) {
                begin = i;//记录开始唱的帧数
                isDoneBegin = false;
            }
            if (dataList.get(i).getVal() > 0) {
                end = i;//记录最后一帧
            }
        }
        return new int[]{begin, end};
    }

    private int countFrame(int[] frame) {
        return frame[1] - frame[0] + 1;
    }

    private List<Entry> reduceZeroFrame(List<Entry> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getVal() == 0) {
                dataList.remove(i);
            }
        }
        return dataList;
    }
}
