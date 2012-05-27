package sauer.motivate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MotivateMainActivity extends Activity {
  LinearLayout linearLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.motivate_main);
    linearLayout = (LinearLayout) findViewById(R.id.chore_list_linear_layout);


    String[] chores = {"Made my bed", "Brushed my teeth", "Helped Mom (Bonus)"};
    for (String c : chores) {
      View choreView = View.inflate(this, R.layout.chore, null);

      TextView choreText = (TextView) choreView.findViewById(R.id.chore_text);
      choreText.setText(c);
      linearLayout.addView(choreView);
    }
  }
}