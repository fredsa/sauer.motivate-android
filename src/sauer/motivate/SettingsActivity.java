package sauer.motivate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends Activity {
  private MotivateApplication app;
  private LinearLayout parentEmails;
  private View childLinearLayout;
  private View addEmailView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (MotivateApplication) getApplication();
    setContentView(R.layout.settings);
    parentEmails = (LinearLayout) findViewById(R.id.parent_emails);

    RadioButton radioParent = (RadioButton) findViewById(R.id.radio_parent);
    RadioButton radioChild = (RadioButton) findViewById(R.id.radio_child);
    childLinearLayout = findViewById(R.id.child_linear_layout);
    RadioGroup accountRadioGroup = (RadioGroup) findViewById(R.id.account_radio_group);
    radioChild.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        childState(isChecked);
      }
    });

    addEmailView = findViewById(R.id.add_email);

    addEmailView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        View parentEmail = getLayoutInflater().inflate(R.layout.parent_email, null);
        parentEmails.addView(parentEmail);
      }
    });
    childState(false);
  }

  private void childState(boolean isChecked) {
    int visibility = isChecked ? View.VISIBLE : View.GONE;
    childLinearLayout.setVisibility(visibility);
  }

  @Override
  protected void onPause() {
    super.onPause();
    SharedPreferences prefs = app.getSharedPreferences("motivate", MODE_PRIVATE);
    //    prefs.edit().putString("email", parentEmail.getText().toString());
  }

  @Override
  protected void onResume() {
    super.onResume();
    SharedPreferences prefs = app.getSharedPreferences("motivate", MODE_PRIVATE);
    //    parentEmail.setText(prefs.getString("email", ""));
  }
}
