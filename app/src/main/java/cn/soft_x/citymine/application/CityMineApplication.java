package cn.soft_x.citymine.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.soft_x.citymine.BuildConfig;

/**
 * 调货商 app
 * Created by Administrator on 2016-11-01.
 */
public class CityMineApplication extends Application {
    private static final String TAG = "CityMine";

    private static Context mApplicationContext = null;

    private static CityMineApplication instance;
    private List<Activity> activityList = new ArrayList<>();

    public CityMineApplication(){

    }
    //单例模式中获取唯一的ExitApplication 实例
    public static CityMineApplication getInstance(){
        if(null == instance){
            instance = new CityMineApplication();
        }
        return instance;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    public void exitApp() {
        try {
            for (Activity activity : activityList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        // xutils init
        x.Ext.init(this);
        // Fresco init
        Fresco.initialize(mApplicationContext);
        // Baidu Map SDK init
        SDKInitializer.initialize(mApplicationContext);
        // logger init
        if (BuildConfig.LOG_LEVEL) {
            Logger.init().logLevel(LogLevel.NONE);
        } else {
            Logger.init().methodCount(2).hideThreadInfo().logLevel(LogLevel.FULL);
        }


    }

    public static Context getAppContext() {
        return mApplicationContext;
    }
}
