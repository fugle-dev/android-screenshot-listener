# Fork 後更新

* migrate to AndroidX
* upgrade target sdk to 31(android 12)
* update simple code
* update ScreenshotManager.setOnScreenshotListener callback
* add memo (some api Deprecated)

# android-screenshot-listener

a library that help you to listen the screenshot event on android.


## Structure

Example:

```
├ app
|  ├ BaseActivity
|  ├ MainActivity
|  ├ SecondActivity
|  └ Utils
└ ..
```

Library:

```
├ lib
|  ├ manager
|     ├ ContentObserverListenerManager
|     ├ FileObserverListenerManager
|     ├ IListenerManager
|     └ IListenerManagerCallback
|  ├ MLog
|  ├ OnScreenshotListener
|  └ ScreenshotManager
└ ..
```

# How-to-use

Add the lib module code to your project:   

```java
  // in this way you can make maximum customization for you project.
```

In most cases, you just need to use `ScreenshotManager` to manage everything, as the following code:

```java
public abstract class BaseActivity extends AppCompatActivity{

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
```

But sometimes the current code maybe not meet your requirements.  

Don't worry, you can strengthen ScreenshotManager by its public method `addScreenshotDirectories()` and `addCustomListenerManager()`.


# License

```
Copyright 2018 zhuanghongji

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
