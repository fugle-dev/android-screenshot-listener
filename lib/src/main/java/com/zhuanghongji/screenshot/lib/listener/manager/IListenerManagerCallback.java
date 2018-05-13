package com.zhuanghongji.screenshot.lib.listener.manager;

import android.support.annotation.Nullable;

/**
 * the callback for {@link IListenerManager} Impl to notify
 * {@link com.zhuanghongji.screenshot.lib.ScreenshotManager}
 * when screenshot event just happened.
 *
 * @author zhuanghongji
 */

public interface IListenerManagerCallback {

    /**
     * @param listenerManagerName the ListenManager which catch screenshot event first
     * @param absolutePath the absolutePath of screenshot image file
     */
    void notifyScreenshotEvent(String listenerManagerName, @Nullable String absolutePath);
}
