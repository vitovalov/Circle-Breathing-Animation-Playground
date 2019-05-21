package io.github.vitovalov.circlebreathinganimationplayground;

import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.animation.Interpolator;

/**
 * Created by vito on 13/02/2018.
 */

public class EasingInterpolators {

  private static EasingInterpolators easingInterpolators;

  //Control points to create the cubic bezier curve.
  private float[] PRINCIPLE_DEFAULT_EASE = { 0.25f, 0.1f, 0.25f, 1.0f };
  private float[] EASE_OUT = { 0f, 0f, 0.58f, 1.0f };
  private float[] EASE_OUT_QUINT = { 0.23f, 1f, 0.32f, 1.0f };
  private float[] EASE_OUT_SINE = { 0.39f, 0.575f, 0.565f, 1.0f };
  private float[] EASE_IN = { 0.42f, 0f, 1.0f, 1.0f };
  private float[] EASE_BREATH = { 0.29f, 0f, 0.17f, 0.99f };

  public static EasingInterpolators getInstance() {
    if (easingInterpolators == null) {
      easingInterpolators = new EasingInterpolators();
    }
    return easingInterpolators;
  }

  public Interpolator getDefaultInterpolator() {
    return PathInterpolatorCompat.create(PRINCIPLE_DEFAULT_EASE[0], PRINCIPLE_DEFAULT_EASE[1], PRINCIPLE_DEFAULT_EASE[2], PRINCIPLE_DEFAULT_EASE[3]);
  }

  public Interpolator getEaseOutInterpolator() {
    return PathInterpolatorCompat.create(EASE_OUT[0], EASE_OUT[1], EASE_OUT[2], EASE_OUT[3]);
  }

  public Interpolator getEaseBreathInterpolator() {
    return PathInterpolatorCompat.create(EASE_BREATH[0], EASE_BREATH[1], EASE_BREATH[2], EASE_BREATH[3]);
  }

  public Interpolator getEaseOutQuintInterpolator() {
    return PathInterpolatorCompat.create(EASE_OUT_QUINT[0], EASE_OUT_QUINT[1], EASE_OUT_QUINT[2], EASE_OUT_QUINT[3]);
  }

  public Interpolator getEaseOutSineInterpolator() {
    return PathInterpolatorCompat.create(EASE_OUT_SINE[0], EASE_OUT_SINE[1], EASE_OUT_SINE[2], EASE_OUT_SINE[3]);
  }

  public Interpolator getEaseInInterpolator() {
    return PathInterpolatorCompat.create(EASE_IN[0], EASE_IN[1], EASE_IN[2], EASE_IN[3]);
  }
}