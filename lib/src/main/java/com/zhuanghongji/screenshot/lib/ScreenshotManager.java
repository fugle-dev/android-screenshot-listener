package com.zhuanghongji.screenshot.lib;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.zhuanghongji.screenshot.lib.listener.manager.ContentObserverListenerManager;
import com.zhuanghongji.screenshot.lib.listener.manager.FileObserverListenerManager;
import com.zhuanghongji.screenshot.lib.listener.manager.IListenerManager;

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

    private FileObserverListenerManager mFileObserverListenerManager;

    private List<IListenerManager> mListenerManagers;

    private OnScreenshotListener mOnScreenshotListener;

    private String mAbsolutePathOfLastScreenshot;

    public ScreenshotManager(Context context) {
        mListenerManagers = new ArrayList<>();

        mFileObserverListenerManager = new FileObserverListenerManager();
        addCustomListenerManager(mFileObserverListenerManager);
        addCustomListenerManager(new ContentObserverListenerManager(context));
    }

    /**
     * start the listener for listening screenshots.
     * <p>
     * you can call it when activity {@link Activity#onResume()}
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
     * you can call it when activity {@link Activity#onPause()}
     * </p>
     */
    public void stopListen() {
        for (IListenerManager manager : mListenerManagers) {
            manager.stopListen();
        }
    }

    /**
     * if you want to listen dir witch didn't add in
     * {@link FileObserverListenerManager#initScreenshotDirectories()}, you can
     * add it by this method.
     *
     * @param screenshotDir the dir you want to listen
     */
    public void addScreenshotDirectories(final String screenshotDir) {
        mFileObserverListenerManager.addScreenshotDirectories(screenshotDir);
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
        manager.setListenerManagerCallback((listenerManagerName, absolutePath) -> {
            try {
                MLog.d("listenerManagerName = %s, absolutePath = %s",
                        listenerManagerName, absolutePath);

                if (TextUtils.isEmpty(absolutePath)) {
                    MLog.d("there is something wrong because absolutePath is empty.");
                    return;
                }

                if (mOnScreenshotListener != null &&
                        absolutePath != null &&
                        !absolutePath.equals(mAbsolutePathOfLastScreenshot) &&
                        absolutePath.length() > 10
                ) {
                    // 會回傳兩個不同的 uri，但指向同一個檔案，先簡單使用字串來判斷
                    // /storage/emulated/0/Pictures/Screenshots/.pending-1637831837-Screenshot_20211118-171717.png
                    // /storage/emulated/0/Pictures/Screenshots/Screenshot_20211118-171717.png
                    String fileSubStr = absolutePath.substring(absolutePath.length() - 11, absolutePath.length() - 1);
                    if (!fileSubStr.equals(mAbsolutePathOfLastScreenshot)) {
                        mAbsolutePathOfLastScreenshot = fileSubStr;
                        mOnScreenshotListener.onScreenshot(absolutePath);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void setOnScreenshotListener(OnScreenshotListener onScreenshotListener) {
        mOnScreenshotListener = onScreenshotListener;
    }

    public static void enableLog(boolean enable) {
        MLog.enableLog(enable);
    }
}
