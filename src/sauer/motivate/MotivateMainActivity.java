package sauer.motivate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class MotivateMainActivity extends Activity {
  LinearLayout linearLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.motivate_main);
    linearLayout = (LinearLayout) findViewById(R.id.chore_list_linear_layout);

    View choreView = View.inflate(this, R.layout.chore, null);
    ToggleButton toggleButton = (ToggleButton) choreView.findViewById(R.id.toggle_button);
    toggleButton.setTextOff("Not yet");
    toggleButton.setTextOn("Done!");
    linearLayout.addView(choreView);
  }
}