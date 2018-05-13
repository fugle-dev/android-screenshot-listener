package com.zhuanghongji.screenshot.lib;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.zhuanghongji.screenshot.lib.listener.manager.ContentObserverListenerManager;
import com.zhuanghongji.screenshot.lib.listener.manager.FileObserverListenerManager;
import com.zhuanghongji.screenshot.lib.listener.manager.IListenerManager;
import com.zhuanghongji.screenshot.lib.listener.manager.IListenerManagerCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * the manager of screenshot.
 * <p>
 * Please make sure you have the permission of reading external storage.
 * <p/>
 *
 * @author zhuanghongji
 */

public class ScreenshotManager {

    private static final String TAG = "ScreenshotManager";

    private Context mContext;

    private List<IListenerManager> mListenerManagers;

    private OnScreenshotListener mOnScreenshotListener;

    public ScreenshotManager(Context context) {
        mContext = context;
        mListenerManagers = new ArrayList<>();

        addCustomListenerManager(new FileObserverListenerManager());
        addCustomListenerManager(new ContentObserverListenerManager(mContext));
    }

    /**
     * start the listener for listening screenshots.
     * <p>
     *     you can call it when activity {@link Activity#onResume()}
     * </p>
     */
    public void startListen() {
        for (IListenerManager manager : mListenerManagers) {
            manager.startListen();
        }
    }

    /**
     * stop the listener for listening screenshots.
     * <p>
     *     you can call it when activity {@link Activity#onPause()}
     * </p>
     */
    public void stopListen() {
        for (IListenerManager manager : mListenerManagers) {
            manager.stopListen();
        }
    }

    /**
     * if the listener implemented by {@link android.os.FileObserver} or
     * {@link android.database.ContentObserver} can not meet your requirements. you can
     * custom your own {@link IListenerManager} just like {@link FileObserverListenerManager} or
     * {@link ContentObserverListenerManager} and add to {@link ScreenshotManager} by this method.
     *
     * @param manager your own custom {@link IListenerManager}
     */
    public void addCustomListenerManager(IListenerManager manager) {
        mListenerManagers.add(manager);
        manager.setListenerManagerCallback(new IListenerManagerCallback() {
            @Override
            public void onScreenshot(@Nullable String absolutePath) {
                if (mOnScreenshotListener != null) {
                    mOnScreenshotListener.onScreenshot(absolutePath);
                }
            }
        });
    }

    public void setOnScreenshotListener(OnScreenshotListener onScreenshotListener) {
        mOnScreenshotListener = onScreenshotListener;
    }
}
