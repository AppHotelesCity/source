package com.zebstudios.cityexpress;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by DanyCarreto on 13/12/15.
 */
public class ExtensionRecyclerView  extends RecyclerView {
    public ExtensionRecyclerView(Context context) {
        super(context);
    }

    public ExtensionRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtensionRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void stopScroll() {
        try {
            super.stopScroll();
        } catch (NullPointerException exception) {
            /**
             *  The mLayout has been disposed of before the
             *  RecyclerView and this stops the application
             *  from crashing.
             */
        }
    }
}