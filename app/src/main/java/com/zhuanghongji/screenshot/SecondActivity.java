package com.zhuanghongji.screenshot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SecondActivity extends BaseActivity {


    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, SecondActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }


}
