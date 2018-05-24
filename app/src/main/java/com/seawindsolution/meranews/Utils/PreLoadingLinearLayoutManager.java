package com.seawindsolution.meranews.Utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

/**
 * Created by admin on 23-May-18.
 */

public class PreLoadingLinearLayoutManager extends LinearLayoutManager {
    private int mPages = 1;
    private OrientationHelper mOrientationHelper;
    public PreLoadingLinearLayoutManager(Context context) {
        super(context);
    }
    public PreLoadingLinearLayoutManager(final Context context, final int pages) {
        super(context);
        this.mPages = pages;
    }
    public PreLoadingLinearLayoutManager(final Context context, final int orientation, final boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        mOrientationHelper = null;
    }
    public void setPages(final int pages) {
        this.mPages = pages;
    }

    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        if (mOrientationHelper == null) {
            mOrientationHelper = OrientationHelper.createOrientationHelper(this, getOrientation());
        }
        return mOrientationHelper.getTotalSpace() * mPages;
    }
}
