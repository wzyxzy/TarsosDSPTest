package com.example.tarsosdsptest;

import com.github.mikephil.charting.data.Entry;
import com.orhanobut.logger.Logger;

import java.util.List;

public class ScoreUtils {

    private List<Entry> yStandardDataList;
    private List<Entry> yDataList;
    private int[] standardFrame;
    private int[] userFrame;

    public ScoreUtils(List<Entry> yStandardDataList, List<Entry> yDataList) {
        this.yStandardDataList = yStandardDataList;
        this.yDataList = yDataList;
        standardFrame = recordFrame(yStandardDataList);
        userFrame = recordFrame(yDataList);
    }

    public float[] scoreTime() {
        return new float[]{((float) Math.abs(countFrame(standardFrame) - countFrame(userFrame))) / countFrame(standardFrame), countFrame(standardFrame), countFrame(userFrame)};
    }

    public float[] scoreFrequency() {

        float score = 0;
        if (countFrame(standardFrame) > countFrame(userFrame)) {
            for (int i = userFrame[0]; i < userFrame[1]; i++) {
                float everyScore = Math.abs(yDataList.get(i).getVal() - yStandardDataList.get((i - userFrame[0]) * countFrame(standardFrame) / countFrame(userFrame) + standardFrame[0]).getVal());
                score += everyScore > 7 ? 0 : everyScore;
                Logger.d(score);
            }
        } else {
            for (int i = standardFrame[0]; i < standardFrame[1]; i++) {
                float everyScore = Math.abs(yStandardDataList.get(i).getVal() - yDataList.get((i - standardFrame[0]) * countFrame(userFrame) / countFrame(standardFrame) + userFrame[0]).getVal());
                score += everyScore > 7 ? 0 : everyScore;
                Logger.d(score);

            }
        }
        return new float[]{score / countFrame(userFrame), score};
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
}
