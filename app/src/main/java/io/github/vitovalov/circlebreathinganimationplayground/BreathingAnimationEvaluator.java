package io.github.vitovalov.circlebreathinganimationplayground;

import android.animation.TypeEvaluator;

import java.util.*;

/**
 * Created by vito on 13/02/2018.
 */

public class BreathingAnimationEvaluator {

  public abstract static class BaseEasingMethod implements TypeEvaluator<Number> {

    protected float mDuration;

    private ArrayList<EasingListener> mListeners = new ArrayList<EasingListener>();

    public BaseEasingMethod(float duration) {
      mDuration = duration;
    }

    public void addEasingListener(EasingListener l) {
      mListeners.add(l);
    }

    public void addEasingListeners(EasingListener... ls) {
      for (EasingListener l : ls) {
        mListeners.add(l);
      }
    }

    public void removeEasingListener(EasingListener l) {
      mListeners.remove(l);
    }

    public void clearEasingListeners() {
      mListeners.clear();
    }

    public void setDuration(float duration) {
      mDuration = duration;
    }

    @Override public final Float evaluate(float fraction, Number startValue, Number endValue) {
      float t = mDuration * fraction;
      float b = startValue.floatValue();
      float c = endValue.floatValue() - startValue.floatValue();
      float d = mDuration;
      float result = calculate(t, b, c, d);
      for (EasingListener l : mListeners) {
        l.on(t, result, b, c, d);
      }
      return result;
    }

    public abstract Float calculate(float t, float b, float c, float d);

    public interface EasingListener {

      public void on(float time, float value, float start, float end, float duration);
    }

  }

  public static class QuintEaseOut extends BaseEasingMethod {

    public QuintEaseOut(float duration) {
      super(duration);
    }

    @Override public Float calculate(float t, float b, float c, float d) {
      return c * ((t = t / d - 1) * t * t * t * t + 1) + b;
    }
  }

  public class QuintEaseIn extends BaseEasingMethod {

    public QuintEaseIn(float duration) {
      super(duration);
    }

    @Override public Float calculate(float t, float b, float c, float d) {
      return c * (t /= d) * t * t * t * t + b;
    }
  }

  public class QuintEaseInOut extends BaseEasingMethod {

    public QuintEaseInOut(float duration) {
      super(duration);
    }

    @Override public Float calculate(float t, float b, float c, float d) {
      if ((t /= d / 2) < 1) return c / 2 * t * t * t * t * t + b;
      return c / 2 * ((t -= 2) * t * t * t * t + 2) + b;
    }
  }

  public class SineEaseOut extends BaseEasingMethod {

    public SineEaseOut(float duration) {
      super(duration);
    }

    @Override public Float calculate(float t, float b, float c, float d) {
      return c * (float) Math.sin(t / d * (Math.PI / 2)) + b;
    }
  }

}
