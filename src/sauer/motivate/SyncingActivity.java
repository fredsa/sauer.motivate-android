package sauer.motivate;

import android.app.Activity;
import android.os.Bundle;

public class SyncingActivity extends Activity {

  private static final String TAG = SyncingActivity.class.getName();
  private MotivateApplication app;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (MotivateApplication) getApplication();

    setContentView(R.layout.syncing);
  }
}
