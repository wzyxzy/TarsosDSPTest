package com.example.tarsosdsptest.common;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class TestApplication extends Application {
    private static TestApplication testApplication;
    public static boolean useLocalRecongnise = true;
    public static boolean isNotMusic = true;


    @Override
    public void onCreate() {
        super.onCreate();
        testApplication = this;
        initOkgo();
        initStetho();
        Logger.addLogAdapter(new AndroidLogAdapter());

    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    private void initOkgo() {
        OkGo.getInstance().init(this);
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
////        //ʹ��sp����cookie�����cookie�����ڣ���һֱ��Ч
////        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
////        //ʹ�����ݿⱣ��cookie�����cookie�����ڣ���һֱ��Ч
////        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
////        //ʹ���ڴ汣��cookie��app�˳���cookie��ʧ
////        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
//        //ȫ�ֵĶ�ȡ��ʱʱ��
//        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//        //ȫ�ֵ�д�볬ʱʱ��
//        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//        //ȫ�ֵ����ӳ�ʱʱ��
//        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//
//        OkGo.getInstance().init(this)                             //������ó�ʼ��
//                .setOkHttpClient(builder.build())                 //��������OkHttpClient�������ý�ʹ��Ĭ�ϵ�
//                .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)                //ȫ��ͳһ����ģʽ��Ĭ�ϲ�ʹ�û��棬���Բ���
//                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)                //ȫ��ͳһ����ʱ�䣬Ĭ���������ڣ����Բ���
//                .setRetryCount(3);                               //ȫ��ͳһ��ʱ����������Ĭ��Ϊ���Σ���ô�������������4��(һ��ԭʼ����������������)������Ҫ��������Ϊ0
////                .addCommonHeaders(headers)                     //ȫ�ֹ���ͷ
////                .addCommonParams(params);                      //ȫ�ֹ�������

    }

    public static TestApplication getInstance() {
        return testApplication;
    }


}
