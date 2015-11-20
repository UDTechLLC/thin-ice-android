package com.udtech.thinice.ui.widgets.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by JOkolot on 20.11.2015.
 */
public class FlipAnimation extends Animation {
    private Camera camera;

    private View fromView;
    private View toView;

    private float centerX;
    private float centerY;

    private boolean forward = true;

    /**
     * Creates a 3D flip animation between two views.
     *
     * @param fromView First view in the transition.
     * @param toView   Second view in the transition.
     */
    public FlipAnimation(View fromView, View toView) {
        this.fromView = fromView;
        this.toView = toView;
        setDuration(700);
        setFillAfter(false);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void reverse() {
        forward = !forward;
        View switchView = toView;
        toView = fromView;
        fromView = switchView;
    }
    public boolean isReversed(View forward){
        return  !forward.equals(fromView);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        centerX = width / 2;
        centerY = height / 2;
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final double radians = Math.PI * interpolatedTime;
        float degrees = (float) (180.0 * radians / Math.PI);
        if (interpolatedTime >= 0.5f) {
            degrees -= 180.f;
            fromView.setVisibility(View.GONE);
            toView.setVisibility(View.VISIBLE);
        }
        final Matrix matrix = t.getMatrix();
        camera.save();
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
