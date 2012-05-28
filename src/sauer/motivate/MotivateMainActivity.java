package sauer.motivate;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MotivateMainActivity extends Activity {
  LinearLayout choreLinearLayout;

  Date choreDate;
  DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    choreDate = new Date();

    setContentView(R.layout.motivate_main);
    choreLinearLayout = (LinearLayout) findViewById(R.id.chore_list_linear_layout);

    TextView dayDescriptionTextView = (TextView) findViewById(R.id.day_description_text_view);
    dayDescriptionTextView.setText(dateFormat.format(choreDate));

    final TextView addChoreTextView = (TextView) findViewById(R.id.add_chore_text_view);
    addChoreTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        addChoreTextView.setText(addChoreTextView.getText() + ".");
        addChoreAlert();
      }
    });
  }

  @Override
  protected void onResume() {
    super.onStart();
    String[] chores = {"Made my bed", "Brushed my teeth", "Helped Mom (Bonus)"};
    for (String choreText : chores) {
      addChore(choreText, "25�");
    }
  }

  private void addChore(String choreText, String rewardText) {
    View choreView = View.inflate(this, R.layout.chore, null);
    TextView choreTextView = (TextView) choreView.findViewById(R.id.chore_text);
    TextView rewardTextView = (TextView) choreView.findViewById(R.id.reward_text);
    //    ToggleButton toggleButton = (ToggleButton) choreView.findViewById(R.id.toggle_button);

    choreTextView.setText(choreText);
    rewardTextView.setText(rewardText);

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
        EditText rewardEditText = (EditText) newChoreView.findViewById(R.id.rewardTextEdit);
        addChore(choreDescriptionEditText.getText().toString(), rewardEditText.getText().toString());
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

}