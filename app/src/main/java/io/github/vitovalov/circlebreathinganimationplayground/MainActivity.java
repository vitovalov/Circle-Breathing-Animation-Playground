package io.github.vitovalov.circlebreathinganimationplayground;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  private BreathingCircleView view;
  private int i;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    view = findViewById(R.id.vieww);
    //view.setDuration(2000);

    view.init(4000);
    view.start();
  }

  public void clicked(View view) {
    if (i > 3000) {
      i = 0;
    }
    i += 1000;
    this.view.changeDuration(i);
  }
}
