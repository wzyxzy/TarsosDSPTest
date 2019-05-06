package com.example.tarsosdsptest.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.leo.api.abstracts.IResultProcessor;
import com.leo.api.nlp.NLPResult;
import com.leo.api.util.Logs;

/**
 * 识别结果/出错 处理
 *
 * @author ydshu create 2013-02-20
 */
public class ResultProcessor implements IResultProcessor {

    private final static String TAG = "ResultProcessor";
    //some commands
    private Context mContext;
    private boolean mIsBusy;
    // 休息功能
    //private PreBusiness mPreBusiness;
    // 阻塞性且非必须的business，有了这个，就不能执行后面的business了，比如自由点播，复位后要重启语音识别
//    private IBusiness mBlockBusiness;
//    // business列表，都是固定的功能，初始化以后不能增删，也不能无效化
//    private ArrayList<IBusinessNLP> mBusinesses = new ArrayList<IBusinessNLP>();
    private OnVoiceListener onVoiceListener;

    public void setOnVoiceListener(OnVoiceListener onVoiceListener) {
        this.onVoiceListener = onVoiceListener;
    }


    //定义回调接口
    public interface OnVoiceListener {
        void onWords(String words);

        void onSuccess(int nums);

        void onFail();
    }


    /**
     * 构造函数
     *
     * @param context
     */
    public ResultProcessor(Context context) {
        mContext = context;
        //mPreBusiness = new PreBusiness(mContext);
        //mBusinesses.add(new FunctionBusiness(mContext));
        //mBusinesses.add(new SimpleAnswerBusiness(mContext)); //动作库+本地问答
        //mBusinesses.add(new CustomBusiness(mContext));
        //mBusinesses.add(new DataChatBusiness(mContext)); //达闼语义理解
        //mBusinesses.add(new ChatBusiness(mContext));
//        mBusinesses.add(new ChatTuringBusiness(mContext));
    }

    @Override
    public void onInit() {
        Logs.d(TAG, "onInit");
    }


    @Override
    public void onResult(NLPResult result) {
        Logs.d(TAG, "onResult");
        mIsBusy = true;
        handleResult(result);

    }

    @Override
    public void onError(int errorCode) {
//        FileUtils.core("errorCode:" + errorCode);


        Log.v(TAG, "ResultProcessor.onError errorCode=" + errorCode);
        reset();

    }

    @Override
    public String onLoaclRecResult(String resultStr) { //离线语音识别
        Log.d("leo", TAG + ": onLoaclRecResult");
        if (null == resultStr) { //这里不判断"", ""是有用的。
            return null;
        }
        handleResult(new NLPResult(resultStr));
        return null;
    }

    @Override
    public void onSwitchOK() {
        Logs.d(TAG, "onSwitchOK");
        // 播放提示语
    }

    private void handleResult(NLPResult nlp) {
        String result = trimString(nlp.getRawtext());
        String response = nlp.getAnswer();
        handleResult(result, response);
    }

    @Override
    public void handleResult(String result, String response) {
        mIsBusy = true;

        Log.d(TAG, "result =" + result);
        if (TextUtils.isEmpty(result)) {
            return;
        }
        onVoiceListener.onWords(result);



    }




    public boolean isBusy() {
        return mIsBusy;
    }

    @Override
    public void reset() {

    }

    @Override
    public void clearTask() {

    }

    private String trimString(String s) {
        return s.replaceAll("[。|!|\\?|！|？]$", "").replaceAll("&apos;", "'").replaceAll("\\.", "");
    }

    @Override
    public void handleCmd(String cmd) {

    }

}
