package com.zhuanghongji.screenshot;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.zhuanghongji.screenshot.lib.OnScreenshotListener;
import com.zhuanghongji.screenshot.lib.ScreenshotManager;

public class MainActivity extends BaseActivity {

    final static String TAG = "MainActivity";
    ScreenshotManager screenshotManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenshotManager = new ScreenshotManager(this);
        screenshotManager.setOnScreenshotListener(new OnScreenshotListener() {
            @Override
            public void onScreenshot(@Nullable String absolutePath) {
                log("absolutePath: " + absolutePath);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        screenshotManager.startListen();

    }

    @Override
    protected void onPause() {
        super.onPause();
        screenshotManager.stopListen();
    }

    private void log(String msg) {
        Log.d(TAG, "#### 🤖 " + msg);
    }
}