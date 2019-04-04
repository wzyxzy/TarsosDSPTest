package com.example.tarsosdsptest;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class MyBarDataSet extends BarDataSet {

    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);

    }

    @Override
    public int getColor(int index) {
        //根据getEntryForXIndex(index).getVal()获得Y值,然后去对比,判断
        //我这1000 4000是根据自己的需求写的,可以随便设,判断条件if根据自己需求
        float val = getEntryForXIndex(index).getVal();
        if (val > 240 && val < 278)
            return mColors.get(0);
        else if (val > 278 && val < 310)
            return mColors.get(1);
        else if (val > 310 && val < 339)
            return mColors.get(2);
        else if (val > 339 && val < 370)
            return mColors.get(3);
        else if (val > 370 && val < 416)
            return mColors.get(4);
        else if (val > 416 && val < 467)
            return mColors.get(5);
        else if (val > 467 && val < 508)
            return mColors.get(6);
        else if (val > 508 && val < 538)
            return mColors.get(7);
        else if (val < 240)
            return mColors.get(0);
        else if (val > 538)
            return mColors.get(2);
        else // greater or equal than 100 red
            return mColors.get(8);
    }
}