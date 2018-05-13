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
 * Please make sure you have the permission of reading external storage.
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

    private List<Integer> mScreenshotEvents;

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
        mScreenshotEvents = new ArrayList<>();
        mScreenshotDirectories = new ArrayList<>();
        mFileObservers = new ArrayList<>();

        initScreenshotEvents();
        initScreenshotDirectories();
    }

    /**
     * here we assume CLOSE_WRITE/CLOSE_NOWRITE event is screenshot event
     */
    private void initScreenshotEvents() {
        mScreenshotEvents.add(FileObserver.CLOSE_WRITE);
        mScreenshotEvents.add(FileObserver.CLOSE_NOWRITE);
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
     *
     * @param screenshotDir the dir you want to listen
     */
    public void addScreenshotDirectories(final String screenshotDir) {
        mScreenshotDirectories.add(screenshotDir);
        mFileObservers.add(new FileObserver(screenshotDir, FileObserver.ALL_EVENTS) {
            @Override
            public void onEvent(int event, @Nullable final String path) {
                final String desc = getEventDesc(event);
                final String absolutePath = screenshotDir + File.separator + path;
                Log.v(TAG, "event = " + event
                        + ", desc = " + desc
                        + ", absolutePath = " + absolutePath);

                if (mScreenshotEvents.contains(event)) {
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
     * get the description of FileObserver event
     * @param event the event for match description
     * @return the description string of event
     */
    private String getEventDesc(int event) {
        String desc;
        switch (event) {
            case FileObserver.ACCESS:
                desc = "ACCESS: Data was read from a file";
                break;
            case FileObserver.MODIFY:
                desc = "MODIFY: Data was written to a file";
                break;
            case FileObserver.ATTRIB:
                desc = "ATTRIB: Metadata (permissions, owner, timestamp) was changed explicitly";
                break;
            case FileObserver.CLOSE_WRITE:
                desc = "CLOSE_WRITE: Someone had a file or directory open for writing, and closed it";
                break;
            case FileObserver.CLOSE_NOWRITE:
                desc = "CLOSE_NOWRITE: Someone had a file or directory open read-only, and closed it";
                break;
            case FileObserver.OPEN:
                desc = "OPEN: A file or directory was opened";
                break;
            case FileObserver.MOVED_FROM:
                desc = "MOVED_FROM: A file or subdirectory was moved from the monitored directory";
                break;
            case FileObserver.MOVED_TO:
                desc = "MOVED_TO: A file or subdirectory was moved to the monitored directory";
                break;
            case FileObserver.CREATE:
                desc = "CREATE: A new file or subdirectory was created under the monitored directory";
                break;
            case FileObserver.DELETE:
                desc = "DELETE: A file was deleted from the monitored directory";
                break;
            case FileObserver.DELETE_SELF:
                desc = "DELETE_SELF: The monitored file or directory was deleted; monitoring effectively stops";
                break;
            case FileObserver.MOVE_SELF:
                desc = "MOVE_SELF: The monitored file or directory was moved; monitoring continues";
                break;
            default:
                desc = "No match event";
                break;

        }
        return desc;
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

    /**
     * add the event of {@link FileObserver} which you think it's screenshot event but not init
     * in {@link #initScreenshotEvents()}
     * @param event the event you want to add
     */
    public void addScreenshotEvent(int event) {
        mScreenshotEvents.add(event);
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
         *
         * @param absolutePath the absolutePath of screenshot image file
         */
        void onScreenshot(@Nullable String absolutePath);
    }
}
