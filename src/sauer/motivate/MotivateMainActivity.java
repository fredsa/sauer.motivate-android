package sauer.motivate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MotivateMainActivity extends Activity {
  private static final String TAG = MotivateMainActivity.class.getName();

  protected static final int WHITE = Color.rgb(255, 255, 255);
  protected static final int GRAY = Color.rgb(100, 100, 100);

  LinearLayout choreLinearLayout;
  ArrayList<Chore> chores;

  Date choreDate;
  DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
  private MotivateApplication app;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    choreDate = new Date();
    app = (MotivateApplication) getApplication();

    setContentView(R.layout.motivate_main);
    choreLinearLayout = (LinearLayout) findViewById(R.id.chore_list_linear_layout);

    TextView dayDescriptionTextView = (TextView) findViewById(R.id.day_description_text_view);
    dayDescriptionTextView.setText(dateFormat.format(choreDate));

    final TextView addChoreTextView = (TextView) findViewById(R.id.add_chore_text_view);
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
        send();
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    AccountManager accountManager = AccountManager.get(this);
    Account[] accounts = accountManager.getAccountsByType("com.google");
    for (Account account : accounts) {
      Log.i(TAG, "account.name = " + account.name);
    }

    String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/appengine.admin";
    accountManager.getAuthToken(accounts[0], AUTH_TOKEN_TYPE, null, this,
        new AccountManagerCallback<Bundle>() {
          public void run(AccountManagerFuture<Bundle> future) {
            try {
              String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
              Log.i(TAG, "Got token " + token);
            } catch (OperationCanceledException e) {
              Log.i(TAG, "The user has denied you access to the API");
            } catch (Exception e) {
              Log.i(TAG, "Exception: ", e);
            }
          }
        }, null);
  }

  protected void send() {
    Hashtable<String, Float> rewards = new Hashtable<String, Float>();
    for (Chore chore : chores) {
      if (!chore.isCompleted()) {
        continue;
      }
      String unit = chore.getRewardUnit();
      Float total = rewards.get(unit);
      if (total == null) {
        total = 0f;
      }
      total += chore.getRewardAmount();
      rewards.put(unit, total);
    }
    for (Iterator<Entry<String, Float>> iterator = rewards.entrySet().iterator(); iterator.hasNext();) {
      Entry<String, Float> entry = iterator.next();
      String msg = entry.getValue() + " " + entry.getKey();
      Log.i(TAG, msg);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    chores = app.getChoresNames();
    for (Chore chore : chores) {
      addChore(chore);
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
    toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          rewardTextView.setTextColor(WHITE);
        } else {
          rewardTextView.setTextColor(GRAY);
        }
        chore.setCompleted(isChecked);
      }
    });
    //toggle color
    toggleButton.setChecked(true);
    toggleButton.setChecked(false);

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
        Chore chore = new Chore(choreDescriptionEditText.getText().toString(), amount,
            rewardUnitEditText.getText().toString());
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

}