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
 * the manager of screenshot.
 * <p>
 *     Please make sure you have the permission of reading external storage.
 * <p/>
 *
 * @author zhuanghongji
 */

public class ScreenshotManager {

    private static final String TAG = "ScreenshotManager";

    private Context mContext;

    /**
     * it make the {@link OnScreenshotListener#onScreenshot(String)} execute on ui thread.
     */
    private Handler mHandler;

    /**
     * the collection of screenshot dir on most brand mobile-phone.
     */
    private List<String> mScreenshotDirectories;

    /**
     * the collection of {@link FileObserver} witch you are listened
     * and it's created by {@link #mScreenshotDirectories}.
     */
    private List<FileObserver> mFileObservers;

    private OnScreenshotListener mOnScreenshotListener;

    public ScreenshotManager(Context context) {
        mContext = context;
        mHandler = new Handler();
        mScreenshotDirectories = new ArrayList<>();
        mFileObservers = new ArrayList<>();
        initScreenshotDirectories();
    }

    private void initScreenshotDirectories() {
        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File sdCard = Environment.getExternalStorageDirectory();

        // common dir
        File screenshots = new File(pictures, "Screenshots");
        addScreenshotDirectories(screenshots.getPath());

        // common dir
        File screenCapture = new File(sdCard, "ScreenCapture");
        addScreenshotDirectories(screenCapture.getPath());

        // xiaomi
        File xiaomi = new File(dcim, "Screenshots");
        addScreenshotDirectories(xiaomi.getPath());
    }

    /**
     * if you want to listen dir witch didn't add in {@link #initScreenshotDirectories()}, you can
     * add it by this method.
     * @param screenshotDir the dir you want to listen
     */
    public void addScreenshotDirectories(final String screenshotDir) {
        mScreenshotDirectories.add(screenshotDir);
        mFileObservers.add(new FileObserver(screenshotDir, FileObserver.ALL_EVENTS) {
            @Override
            public void onEvent(int event, @Nullable final String path) {
                final String absolutePath = screenshotDir + File.separator + path;
                Log.v(TAG, "event = " + event + ", absolutePath = " + absolutePath);

                if (event == FileObserver.CREATE) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnScreenshotListener != null) {
                                mOnScreenshotListener.onScreenshot(absolutePath);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Start listening for screenshots.
     * Monitors the screenshot directories
     */
    public void startListener() {
        for (FileObserver observer : mFileObservers) {
            observer.startWatching();
        }
    }

    /**
     * Stop listening for screenshots.
     */
    public void stopListener() {
        for (FileObserver observer : mFileObservers) {
            observer.stopWatching();
        }
    }

    public List<String> getScreenshotDirectories() {
        return mScreenshotDirectories;
    }

    public void setOnScreenshotListener(OnScreenshotListener onScreenshotListener) {
        mOnScreenshotListener = onScreenshotListener;
    }

    /**
     * the listener for listen screenshot event.
     */
    public interface OnScreenshotListener {

        /**
         * it will called if screenshot event happened.
         * @param absolutePath the absolutePath of screenshot image file
         */
        void onScreenshot(@Nullable String absolutePath);
    }
}
