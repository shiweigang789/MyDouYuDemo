package com.swg.mydouyudemo.application;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;
import com.swg.mydouyudemo.api.NetworkApi;
import com.swg.mydouyudemo.net.config.NetWorkConfiguration;
import com.swg.mydouyudemo.net.http.HttpUtils;
import com.swg.mydouyudemo.ui.pagestatemanager.PageManager;
import com.swg.mydouyudemo.utils.LogUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.smtt.sdk.WebView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by swg on 2017/11/24.
 */

public class DYApplication extends Application {

    private static Context context;
    private static final String BUGLY_APP_ID = "f0ba69311d";
    private static final String TAG = "DYApplication";
    private QbSdk.PreInitCallback preInitCallback = new QbSdk.PreInitCallback() {
        @Override
        public void onCoreInitFinished() {
        }

        @Override
        public void onViewInitFinished(boolean b) {
            LogUtil.e(TAG, " onViewInitFinished is " + b);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initX5WebView();
        initBUgly();
        // Fresco图片加载
        Fresco.initialize(context);
        // UI卡顿检测工具
        BlockCanary.install(context, new AppBlockCanaryContext()).start();
        //  网络库初始化
        initOkHttpUtils();
        // 初始化界面
        PageManager.initInApp(context);
        // 内存泄漏检测
        initLeakCanary();
    }

    private void initX5WebView() {
        // X5WebView配置
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                LogUtil.d(TAG, "onDownloadFinish is " + i);
            }

            @Override
            public void onInstallFinish(int i) {
                LogUtil.d(TAG, "onInstallFinish is " + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                LogUtil.d(TAG, "onDownloadProgress:" + i);
            }
        });
        QbSdk.initX5Environment(context, preInitCallback);
    }

    private void initBUgly() {
        // Bugly配置
        String packageName = context.getPackageName();
        String processName = getProcessName(Process.myPid());
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(context);
        userStrategy.setUploadProcess(processName == null || processName.equals(packageName));
        // X5Crash日志上报
        userStrategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
            @Override
            public synchronized Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack) {
                LinkedHashMap map = new LinkedHashMap();
                String x5CrashInfo = WebView.getCrashExtraMessage(context);
                map.put("x5crashInfo", x5CrashInfo);
                return map;
            }

            @Override
            public synchronized byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType, String errorMessage, String errorStack) {
                try {
                    return "Extra data.".getBytes("UTF-8");
                } catch (Exception e) {
                    return null;
                }
            }
        });
        CrashReport.initCrashReport(context, BUGLY_APP_ID, true, userStrategy);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private strictfp String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = null;
            processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (
                IOException e)

        {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void initOkHttpUtils() {
        NetWorkConfiguration configuration = new NetWorkConfiguration(context)
                .setBaseUrl(NetworkApi.baseUrl)
                .isCache(true)
                .isDiskCache(true)
                .isMemoryCache(true);
        HttpUtils.setConFiguration(configuration);
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

}
