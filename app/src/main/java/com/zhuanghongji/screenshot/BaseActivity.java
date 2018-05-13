package com.zhuanghongji.screenshot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zhuanghongji.screenshot.lib.ContentObserverManager;
import com.zhuanghongji.screenshot.lib.ScreenshotManager;

/**
 * Created by zhuanghongji on 2018/5/13.
 */

public abstract class BaseActivity extends AppCompatActivity{

    public static final String BASE_TAG = "BaseActivity";

    protected final String TAG = getClass().getSimpleName();

    private ScreenshotManager mScreenshotManager;

    private ContentObserverManager mContentObserverManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScreenshotManager = new ScreenshotManager(this);
        mScreenshotManager.setOnScreenshotListener(new ScreenshotManager.OnScreenshotListener() {
            @Override
            public void onScreenshot(@Nullable String path) {
                Log.d(TAG, "onScreenshot path = " + path);
            }
        });

        mContentObserverManager = new ContentObserverManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScreenshotManager.startListener();
        mContentObserverManager.startListen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScreenshotManager.stopListener();
        mContentObserverManager.stopListen();
    }
}
