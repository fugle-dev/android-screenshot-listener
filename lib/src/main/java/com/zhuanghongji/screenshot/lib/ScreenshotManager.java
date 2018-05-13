package com.zhuanghongji.screenshot.lib;

import android.content.Context;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuanghongji on 2018/5/13.
 */

public class ScreenshotManager {

    private static final String TAG = "ScreenshotManager";

    private Context mContext;

    private Handler mHandler;

    private List<String> mScreenshotDirectories;

    private List<FileObserver> mFileObservers;

    private OnScreenshotListener mOnScreenshotListener;

    public ScreenshotManager(Context context) {
        mContext = context;
        mHandler = new Handler();
        initScreenshotDirectories();
        initFileObservers();
    }

    private void initFileObservers() {
        mFileObservers = new ArrayList<>();
        for (String dir : mScreenshotDirectories) {
            mFileObservers.add(new FileObserver(dir, FileObserver.ALL_EVENTS) {
                @Override
                public void onEvent(int event, @Nullable final String path) {
                    Log.v(TAG, "event = " + event + ", path = " + path);
                    if (event == FileObserver.CREATE) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mOnScreenshotListener != null) {
                                    mOnScreenshotListener.onScreenshot(path);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void initScreenshotDirectories() {
        mScreenshotDirectories = new ArrayList<>();
        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File sdCard = Environment.getExternalStorageDirectory();

        // common dir
        File screenshots = new File(pictures, "Screenshots");
        mScreenshotDirectories.add(screenshots.getPath());

        // common dir
        File screenCapture = new File(sdCard, "ScreenCapture");
        mScreenshotDirectories.add(screenCapture.getPath());

        // xiaomi
        File xiaomi = new File(dcim, "Screenshots");
        mScreenshotDirectories.add(xiaomi.getPath());
    }

    public void startListener() {
        for (FileObserver observer : mFileObservers) {
            observer.startWatching();
        }
    }

    /**
     * Start listening for screenshots.
     * Monitors the screenshot directories
     */
    public void stopListener() {
        for (FileObserver observer : mFileObservers) {
            observer.stopWatching();
        }
    }

    /**
     * Stop listening for screenshots.
     */
    public void addScreenshotDirectories(String path) {

    }

    public List<String> getScreenshotDirectories() {
        return mScreenshotDirectories;
    }

    public void setOnScreenshotListener(OnScreenshotListener onScreenshotListener) {
        mOnScreenshotListener = onScreenshotListener;
    }

    public interface OnScreenshotListener {
        void onScreenshot(@Nullable String path);
    }
}
