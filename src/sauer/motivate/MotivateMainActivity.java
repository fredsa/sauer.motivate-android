package sauer.motivate;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MotivateMainActivity extends Activity {
  @SuppressWarnings("unused")
  private static final String TAG = MotivateMainActivity.class.getName();

  private static final String DATE_FORMAT_HUMAN = "EEEE, MMMM dd, yyyy";
  private static final String DATE_FORMAT_DB = "yyyy-MM-dd";

  protected static final int WHITE = Color.rgb(255, 255, 255);
  protected static final int GRAY = Color.rgb(100, 100, 100);

  private static final int REQUEST_CODE_PLAY_SERVICES = 0;

  private LinearLayout choreLinearLayout;
  private ArrayList<Chore> chores;

  private Date choreDate;
  private MotivateApplication app;

  private String date_yyyyMMdd;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (MotivateApplication) getApplication();

    choreDate = new Date();
    new DateFormat();
    date_yyyyMMdd = (String) DateFormat.format(DATE_FORMAT_DB, choreDate);

    setContentView(R.layout.motivate_main);
    choreLinearLayout = (LinearLayout) findViewById(R.id.chore_list_linear_layout);

    TextView dayDescriptionTextView = (TextView) findViewById(R.id.day_description_text_view);
    new DateFormat();
    dayDescriptionTextView.setText(DateFormat.format(DATE_FORMAT_HUMAN, choreDate));

    final TextView addChoreTextView = (TextView) findViewById(R.id.add_chore_text_view);
    chores = app.getChores(date_yyyyMMdd);
    for (Chore chore : chores) {
      addChore(chore);
    }

    addChoreTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        addChoreAlert();
      }
    });

    Button sendButton = (Button) findViewById(R.id.send_button);
    sendButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(app, SyncingActivity.class);
        intent.putExtra("date", date_yyyyMMdd);
        startActivity(intent);
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    if (resultCode != ConnectionResult.SUCCESS) {
      resultCode = ConnectionResult.SERVICE_MISSING;
      GooglePlayServicesUtil.getErrorDialog(resultCode, this, REQUEST_CODE_PLAY_SERVICES).show();
      return;
    }

    Intent intent = new Intent(this, SettingsActivity.class);
    startActivity(intent);
   }
  }

  private void addChore(final Chore chore) {
    String choreText = chore.getDescription();
    String rewardText = "" + chore.getRewardAmount();
    String rewardUnit = chore.getRewardUnit();

    View choreView = View.inflate(this, R.layout.chore, null);
    TextView choreTextView = (TextView) choreView.findViewById(R.id.chore_text);
    final TextView rewardTextView = (TextView) choreView.findViewById(R.id.reward_text);

    ToggleButton toggleButton = (ToggleButton) choreView.findViewById(R.id.toggle_button);

    toggleButton.setChecked(chore.getCompleted() == 1);
    updateChoreView(rewardTextView, chore.getCompleted() == 1);

    toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateChoreView(rewardTextView, isChecked);
        chore.setCompleted(isChecked ? 1 : 0);
        app.setChore(chore);
      }
    });

    choreTextView.setText(choreText);
    rewardTextView.setText(rewardText + " " + rewardUnit);

    choreLinearLayout.addView(choreView);
  }

  private void addChoreAlert() {
    AlertDialog.Builder alert = new AlertDialog.Builder(this);

    final View newChoreView = View.inflate(this, R.layout.new_chore, null);
    alert.setView(newChoreView);

    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int whichButton) {
        EditText choreDescriptionEditText = (EditText) newChoreView.findViewById(R.id.choreDescriptionTextEdit);
        EditText rewardAmountEditText = (EditText) newChoreView.findViewById(R.id.reward_amount);
        EditText rewardUnitEditText = (EditText) newChoreView.findViewById(R.id.reward_unit);
        float amount = Float.parseFloat(rewardAmountEditText.getText().toString());
        Chore chore = new Chore(date_yyyyMMdd, choreDescriptionEditText.getText().toString(),
            amount, rewardUnitEditText.getText().toString(), 0);
        app.insertChore(chore);
        addChore(chore);
      }
    });

    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int whichButton) {
        // do nothing
      }
    });

    alert.show();
  }

  private void updateChoreView(final TextView rewardTextView, boolean isChecked) {
    if (isChecked) {
      rewardTextView.setTextColor(WHITE);
    } else {
      rewardTextView.setTextColor(GRAY);
    }
  }

}