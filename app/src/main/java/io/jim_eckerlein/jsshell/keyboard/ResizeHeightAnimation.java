package io.jim_eckerlein.jsshell.keyboard;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeHeightAnimation extends Animation {
    final View mView;
    final int mStartHeight;
    final float mEndHeight;
    
    public ResizeHeightAnimation(View view, float endHeight) {
        this.mView = view;
        this.mEndHeight = endHeight;
        this.mStartHeight = view.getHeight();
    }
    
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        mView.getLayoutParams().height = (int) (mStartHeight + (mEndHeight - mStartHeight) * interpolatedTime);
        mView.requestLayout();
    }
    
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }
    
    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
