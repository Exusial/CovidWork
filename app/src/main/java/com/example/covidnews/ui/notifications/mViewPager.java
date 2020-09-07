package com.example.covidnews.ui.notifications;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class mViewPager extends ViewPager {

    public mViewPager(@NonNull Context context, AttributeSet attr) {
        super(context,attr);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev){
        return false;
    }

    public boolean onTouchEvent(MotionEvent ev){
        return true;
    }
}
