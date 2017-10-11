package digiwin.smartdepott100.core.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author xiemeng
 * @des 加载动画
 * @date 2017-10-11 09:25
 */

public class ThreeBallRotationProgressBar extends View {

    private final static int DEFAULT_MAX_RADIUS = 16;
    private final static int DEFAULT_MIN_RADIUS = 5;
    private final static int DEFAULT_DISTANCE = 35;

    private final static int DEFAULT_ONE_BALL_COLOR = Color
            .parseColor("#40df73");
    private final static int DEFAULT_TWO_BALL_COLOR = Color
            .parseColor("#ffdf3e");
    private final static int DEFAULT_THREE_BALL_COLOR = Color
            .parseColor("#ff733e");

    private final static int DEFAULT_ANIMATOR_DURATION = 1400;

    private Paint mOnePaint;
    private Paint mTwoPaint;
    private Paint mThreePaint;

    private float maxRadius = DEFAULT_MAX_RADIUS;
    private float minRadius = DEFAULT_MIN_RADIUS;

    private int distance = DEFAULT_DISTANCE;

    private long duration = DEFAULT_ANIMATOR_DURATION;

    private Ball mOneBall;
    private Ball mTwoBall;
    private Ball mThreeBall;

    private float mCenterX;
    private float mCenterY;

    private AnimatorSet animatorSet;

    public ThreeBallRotationProgressBar(Context context) {
        this(context, null);
    }

    public ThreeBallRotationProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThreeBallRotationProgressBar(Context context, AttributeSet attrs,
                                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mOneBall = new Ball();
        mTwoBall = new Ball();
        mThreeBall = new Ball();

        mOneBall.setColor(DEFAULT_ONE_BALL_COLOR);
        mTwoBall.setColor(DEFAULT_TWO_BALL_COLOR);
        mThreeBall.setColor(DEFAULT_THREE_BALL_COLOR);

        mOnePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOnePaint.setColor(DEFAULT_ONE_BALL_COLOR);
        mTwoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTwoPaint.setColor(DEFAULT_TWO_BALL_COLOR);
        mThreePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThreePaint.setColor(DEFAULT_THREE_BALL_COLOR);

        configAnimator();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mOneBall.getRadius() >= mTwoBall.getRadius()) {
            if (mThreeBall.getRadius() >= mOneBall.getRadius()) {
                canvas.drawCircle(mTwoBall.getCenterX(), mCenterY,
                        mTwoBall.getRadius(), mTwoPaint);
                canvas.drawCircle(mOneBall.getCenterX(), mCenterY,
                        mOneBall.getRadius(), mOnePaint);
                canvas.drawCircle(mThreeBall.getCenterX(), mCenterY,
                        mThreeBall.getRadius(), mThreePaint);
            } else {
                if (mTwoBall.getRadius() <= mThreeBall.getRadius()) {
                    canvas.drawCircle(mTwoBall.getCenterX(), mCenterY,
                            mTwoBall.getRadius(), mTwoPaint);
                    canvas.drawCircle(mThreeBall.getCenterX(), mCenterY,
                            mThreeBall.getRadius(), mThreePaint);
                    canvas.drawCircle(mOneBall.getCenterX(), mCenterY,
                            mOneBall.getRadius(), mOnePaint);
                } else {
                    canvas.drawCircle(mThreeBall.getCenterX(), mCenterY,
                            mThreeBall.getRadius(), mThreePaint);
                    canvas.drawCircle(mTwoBall.getCenterX(), mCenterY,
                            mTwoBall.getRadius(), mTwoPaint);
                    canvas.drawCircle(mOneBall.getCenterX(), mCenterY,
                            mOneBall.getRadius(), mOnePaint);

                }
            }
        } else {
            if (mThreeBall.getRadius() >= mTwoBall.getRadius()) {
                canvas.drawCircle(mOneBall.getCenterX(), mCenterY,
                        mOneBall.getRadius(), mOnePaint);
                canvas.drawCircle(mTwoBall.getCenterX(), mCenterY,
                        mTwoBall.getRadius(), mTwoPaint);
                canvas.drawCircle(mThreeBall.getCenterX(), mCenterY,
                        mThreeBall.getRadius(), mThreePaint);
            } else {
                if (mOneBall.getRadius() <= mThreeBall.getRadius()) {
                    canvas.drawCircle(mOneBall.getCenterX(), mCenterY,
                            mOneBall.getRadius(), mOnePaint);
                    canvas.drawCircle(mThreeBall.getCenterX(), mCenterY,
                            mThreeBall.getRadius(), mThreePaint);
                    canvas.drawCircle(mTwoBall.getCenterX(), mCenterY,
                            mTwoBall.getRadius(), mTwoPaint);
                } else {
                    canvas.drawCircle(mThreeBall.getCenterX(), mCenterY,
                            mThreeBall.getRadius(), mThreePaint);
                    canvas.drawCircle(mOneBall.getCenterX(), mCenterY,
                            mOneBall.getRadius(), mOnePaint);
                    canvas.drawCircle(mTwoBall.getCenterX(), mCenterY,
                            mTwoBall.getRadius(), mTwoPaint);

                }
            }
        }

    }

    private void configAnimator() {
        float centerRadius = (maxRadius + minRadius) * 0.5f;

        ObjectAnimator oneScaleAnimator = ObjectAnimator.ofFloat(mOneBall,
                "radius", centerRadius, maxRadius, centerRadius, minRadius,
                centerRadius);
        oneScaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        ValueAnimator oneCenterAnimator = ValueAnimator
                .ofFloat(-1, 0, 1, 0, -1);
        oneCenterAnimator.setRepeatCount(ValueAnimator.INFINITE);
        oneCenterAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        float x = mCenterX + (distance) * value;
                        mOneBall.setCenterX(x);
                        invalidate();
                    }
                });
        ValueAnimator oneAlphaAnimator = ValueAnimator.ofFloat(0.8f, 1, 0.8f,
                0, 0.8f);
        oneAlphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        oneAlphaAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        int alpha = (int) (255 * value);
                        mOnePaint.setAlpha(alpha);
                    }
                });

        ObjectAnimator twoScaleAnimator = ObjectAnimator.ofFloat(mTwoBall,
                "radius", maxRadius, centerRadius, minRadius, centerRadius,
                maxRadius);
        twoScaleAnimator.setRepeatCount(ValueAnimator.INFINITE);

        ValueAnimator twoCenterAnimator = ValueAnimator.ofFloat(0, 1, 0, -1, 0);
        twoCenterAnimator.setRepeatCount(ValueAnimator.INFINITE);
        twoCenterAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        float x = mCenterX + (distance) * value;
                        mTwoBall.setCenterX(x);
                    }
                });
        ValueAnimator twoAlphaAnimator = ValueAnimator.ofFloat(1, 0.8f, 0,
                0.8f, 1);
        twoAlphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        twoAlphaAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        int alpha = (int) (255 * value);
                        mTwoPaint.setAlpha(alpha);
                    }
                });

        ObjectAnimator threeScaleAnimator = ObjectAnimator.ofFloat(mThreeBall,
                "radius", centerRadius, minRadius, centerRadius, maxRadius,
                centerRadius);
        threeScaleAnimator.setRepeatCount(ValueAnimator.INFINITE);

        ValueAnimator threeCenterAnimator = ValueAnimator.ofFloat(1, 0, -1, 0,
                1);
        threeCenterAnimator.setRepeatCount(ValueAnimator.INFINITE);
        threeCenterAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        float x = mCenterX + (distance) * value;
                        mThreeBall.setCenterX(x);
                    }
                });
        ValueAnimator threeAlphaAnimator = ValueAnimator.ofFloat(0.8f, 0, 0.8f,
                1, 0.8f);
        threeAlphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        threeAlphaAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        int alpha = (int) (255 * value);
                        mThreePaint.setAlpha(alpha);
                    }
                });

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(oneScaleAnimator, oneCenterAnimator,
                twoScaleAnimator, twoCenterAnimator, threeScaleAnimator,
                threeCenterAnimator);
        animatorSet.setDuration(DEFAULT_ANIMATOR_DURATION);
        animatorSet.setInterpolator(new LinearInterpolator());
    }

    public class Ball {
        private float radius;
        private float centerX;
        private int color;

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public float getCenterX() {
            return centerX;
        }

        public void setCenterX(float centerX) {
            this.centerX = centerX;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                stopAnimator();
            } else {
                startAnimator();
            }
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int v) {
        super.onVisibilityChanged(changedView, v);
        if (v == GONE || v == INVISIBLE) {
            stopAnimator();
        } else {
            startAnimator();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimator();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimator();
    }

    public ThreeBallRotationProgressBar setOneBallColor(int color) {
        mOneBall.setColor(color);
        return this;
    }

    public ThreeBallRotationProgressBar setmTwoBallColor(int color) {
        mTwoBall.setColor(color);
        return this;
    }

    /**
     * 小球最大半径
     * @param maxRadius
     * @return
     */
    public ThreeBallRotationProgressBar setMaxRadius(float maxRadius) {
        this.maxRadius = maxRadius;
        configAnimator();
        return this;
    }

    /**
     * 小球最小半径
     * @param minRadius
     * @return
     */
    public ThreeBallRotationProgressBar setMinRadius(float minRadius) {
        this.minRadius = minRadius;
        configAnimator();
        return this;
    }

    /**
     * 小球之间距离
     * @param distance
     * @return
     */
    public ThreeBallRotationProgressBar setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public ThreeBallRotationProgressBar setDuration(long duration) {
        this.duration = duration;
        if (animatorSet != null) {
            animatorSet.setDuration(duration);
        }
        return  this;
    }

    public void startAnimator() {
        if (getVisibility() != VISIBLE)
            return;

        if (animatorSet.isRunning())
            return;

        if (animatorSet != null) {
            animatorSet.start();
        }
    }

    public void stopAnimator() {
        if (animatorSet != null) {
            animatorSet.end();
        }
    }
}
