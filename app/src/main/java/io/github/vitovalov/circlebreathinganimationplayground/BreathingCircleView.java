package io.github.vitovalov.circlebreathinganimationplayground;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

/**
 * Created by vito on 13/02/2018.
 */

public class BreathingCircleView extends RelativeLayout {

  public static final int INFINITE = 0;
  private static final int DEFAULT_COLOR = Color.rgb(210, 236, 206);
  private static final int DEFAULT_REPEAT = INFINITE;
  private long disappearDuration = 400;
  private long appearDuration = 500;
  private long duration;
  private int repeat;
  private int color;
  private AnimatorSet animatorSet;
  private AnimatorSet animatorSetFade;
  private Paint paint;
  private float radius;
  private float centerX;
  private float centerY;
  private BreathingView breathingView;
  private final Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {

    @Override public void onAnimationStart(Animator animator) {

    }

    @Override public void onAnimationEnd(Animator animator) {
      animatorSet.cancel();
      animatorSet.setDuration(duration);
      animatorSet.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationStart(Animator animation) {
          ObjectAnimator appear = ObjectAnimator.ofFloat(breathingView, "alpha",  0f, 1f);
          appear.setDuration(appearDuration);
          final AnimatorSet mAnimationSet = new AnimatorSet();
          mAnimationSet.play(appear);
          mAnimationSet.start();
          animatorSet.removeListener(this);
        }
      });
      animatorSet.start();
    }

    @Override public void onAnimationCancel(Animator animator) {
    }

    @Override public void onAnimationRepeat(Animator animator) {
    }

  };

  public BreathingCircleView(Context context) {
    this(context, null, 0);
  }

  public BreathingCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

  }

  public void init(long defaultDuration) {
    // get attributes
    duration = defaultDuration;
    repeat = DEFAULT_REPEAT;
    color = DEFAULT_COLOR;

    // create paint
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(color);

    // create views
    build();
  }

  /**
   * Build breathing views and animators.
   */
  private void build() {
    // create views and animators
    LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    int repeatCount = (repeat == INFINITE) ? ObjectAnimator.INFINITE : repeat;

    // setup view
    breathingView = new BreathingView(getContext());
    setDefaultScale();
    breathingView.setAlpha(1);

    addView(breathingView, 0, layoutParams);

    // setup animators
    ObjectAnimator expandX = ObjectAnimator.ofFloat(breathingView, "ScaleX", 0.5f, 1f);
    ObjectAnimator expandY = ObjectAnimator.ofFloat(breathingView, "ScaleY", 0.5f, 1f);
    ObjectAnimator contractX = ObjectAnimator.ofFloat(breathingView, "ScaleX", 1f, 0.5f);
    ObjectAnimator contractY = ObjectAnimator.ofFloat(breathingView, "ScaleY", 1f, 0.5f);

    ValueAnimator colorAnimation =
        ValueAnimator.ofObject(new ArgbEvaluator(), ContextCompat.getColor(getContext(), R.color.breathing_exercise_first_bg),
            ContextCompat.getColor(getContext(), R.color.breathing_exercise_third_bg));
    colorAnimation.setRepeatCount(1);
    colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        setColor((Integer) valueAnimator.getAnimatedValue());
        breathingView.invalidate();
      }
    });
    ObjectAnimator disappear = ObjectAnimator.ofFloat(breathingView, "alpha", 1f, 0f);
    ObjectAnimator appear = ObjectAnimator.ofFloat(breathingView, "alpha", 0f, 1f);


    animatorSet = new AnimatorSet();
    //animatorSet.play(appear).before(expandX).with(colorAnimation);
    animatorSet.play(expandX).with(expandY).with(colorAnimation);
    animatorSet.play(contractX).after(expandY);
    animatorSet.play(contractY).after(expandY);

    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        animation.start();
      }
    });
    Interpolator customInterpolator = EasingInterpolators.getInstance().getEaseBreathInterpolator();
    animatorSet.setInterpolator(customInterpolator);
    animatorSet.setDuration(duration/2); // /2 because the whole animation is 2 cycles

    animatorSetFade = new AnimatorSet();
    animatorSetFade.play(disappear);
    //animatorSetFade.play(appear).after(disappear);
    animatorSetFade.setInterpolator(EasingInterpolators.getInstance().getDefaultInterpolator());
    animatorSetFade.setDuration(disappearDuration);
    animatorSetFade.addListener(mAnimatorListener);
  }

  public void changeDuration(long millis) {
    if (millis < 0) {
      throw new IllegalArgumentException("Duration cannot be negative");
    }

    // to avoid bad behavior. Inner cycle duration shouldn't exceed outer
    if (disappearDuration > millis - 100) {
      disappearDuration = millis - 100;
    }
    if (appearDuration > millis - 100) {
      appearDuration = millis - 100;
    }

    if (millis != duration) {
      duration = millis;

      animatorSetFade.start();
    }
  }

  private void setDefaultScale() {
    breathingView.setScaleX(0.5f);
    breathingView.setScaleY(0.5f);
  }

  /**
   * Constructor that is called when inflating a view from XML.
   *
   * @param context The Context the view is running in, through which it can access the current
   * theme, resources, etc.
   * @param attrs The attributes of the XML tag that is inflating the view.
   */
  public BreathingCircleView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public boolean isPlaying() {
    return animatorSet.isRunning();
  }

  /**
   * Stop breathing animation.
   */
  public synchronized void stop() {
    if (animatorSet == null) {
      return;
    }

    animatorSet.end();
    setDefaultScale();
    setDefaultColor();
  }

  private void setDefaultColor() {
    setColor(ContextCompat.getColor(getContext(), R.color.breathing_exercise_green));
    breathingView.invalidate();
  }

  /**
   * Get breathing duration.
   *
   * @return Duration of single breathing in milliseconds
   */
  public long getDuration() {
    return duration;
  }

  /**
   * Set single breathing duration.
   *
   * @param millis Breath duration in milliseconds
   */
  private void setDuration(long millis) {
    if (millis < 0) {
      throw new IllegalArgumentException("Duration cannot be negative");
    }

    if (millis != duration) {
      duration = millis;
      reset();
      invalidate();
    }
  }

  /**
   * Reset views and animations.
   */
  private void reset() {
    boolean isStarted = isStarted();

    // clear
    build();

    if (isStarted) {
      start();
    }
  }

  /**
   * Start breathing animation.
   */
  public synchronized void start() {
    if (animatorSet == null) {
      return;
    }

    animatorSet.start();
  }

  public synchronized boolean isStarted() {
    return (animatorSet != null);
  }

  /**
   * Gets the current color of the breathing effect in integer
   * Defaults to Color.rgb(0, 116, 193);
   *
   * @return an integer representation of color
   */
  public int getColor() {
    return color;
  }

  /**
   * Sets the current color of the breathing effect in integer
   * Takes effect immediately
   * Usage: Color.parseColor("<hex-value>") or getResources().getColor(R.color.colorAccent)
   *
   * @param color : an integer representation of color
   */
  public void setColor(@ColorInt int color) {
    if (color != this.color) {
      this.color = color;

      if (paint != null) {
        paint.setColor(color);
      }
    }
  }

  @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
    int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

    centerX = width * 0.5f;
    centerY = height * 0.5f;
    radius = Math.min(width, height) * 0.5f;

    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();

    if (animatorSet != null) {
      animatorSet.cancel();
      animatorSet = null;
    }
  }

  private class BreathingView extends View {

    public BreathingView(Context context) {
      super(context);
    }

    @Override protected void onDraw(Canvas canvas) {
      canvas.drawCircle(centerX, centerY, radius, paint);
    }

  }
}