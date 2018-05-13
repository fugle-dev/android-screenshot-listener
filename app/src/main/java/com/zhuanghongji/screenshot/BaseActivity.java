package com.zhuanghongji.screenshot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zhuanghongji.screenshot.lib.OnScreenshotListener;
import com.zhuanghongji.screenshot.lib.ScreenshotManager;

/**
 * Created by zhuanghongji on 2018/5/13.
 */

public abstract class BaseActivity extends AppCompatActivity{

    public static final String BASE_TAG = "BaseActivity";

    protected final String TAG = getClass().getSimpleName();

    private ScreenshotManager mScreenshotManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenshotManager.enableLog(true);
        mScreenshotManager = new ScreenshotManager(this);
        mScreenshotManager.setOnScreenshotListener(new OnScreenshotListener() {
            @Override
            public void onScreenshot(@Nullable String absolutePath) {
                Log.d(TAG, "onScreenshot absolutePath = " + absolutePath);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScreenshotManager.startListen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScreenshotManager.stopListen();
    }
}
