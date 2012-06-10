package sauer.motivate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

public class SyncingActivity extends Activity {

  private static final String TAG = SyncingActivity.class.getName();

  private static final String UTF_8 = "UTF-8";
  private static final String APP_ENGINE_ORIGIN = "https://sauer-motivate.appspot.com/";
  private static final String AUTH_TOKEN_TYPE_USERINFO_EMAIL = "oauth2:https://www.googleapis.com/auth/userinfo.email";

  private MotivateApplication app;

  private TextView statusTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (MotivateApplication) getApplication();

    setContentView(R.layout.syncing);
    statusTextView = (TextView) findViewById(R.id.sync_status);

    doToken();
  }

  private void doToken() {
    AccountManager accountManager = AccountManager.get(this);
    setStatus("accountManager.getAccountsByType(\"com.google\")");
    Account[] accounts = accountManager.getAccountsByType("com.google");
    for (Account account : accounts) {
      setStatus("- account.name = " + account.name);
    }

    String token = app.getAuthToken();
    if (token != null) {
      setStatus("Invalidating previous token: " + token);
      accountManager.invalidateAuthToken(accounts[0].type, token);
      app.setAuthToken(null);
    }

    String authTokenType = AUTH_TOKEN_TYPE_USERINFO_EMAIL;

    setStatus("Get token for " + accounts[0].name + " using authTokenType " + authTokenType);
    accountManager.getAuthToken(accounts[0], authTokenType, null, this,
        new AccountManagerCallback<Bundle>() {
          @Override
          public void run(AccountManagerFuture<Bundle> future) {
            try {
              String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
              setStatus("Got KEY_AUTHTOKEN: " + token);
              app.setAuthToken(token);
              testToken(token);
            } catch (OperationCanceledException e) {
              setStatus("The user has denied you access to the API");
            } catch (Exception e) {
              setStatus(e.getMessage());
              Log.w("Exception: ", e);
            }
          }
        }, null);
  }

  protected void testToken(final String token) {
    new AsyncTask<Void, String, Void>() {

      protected void onProgressUpdate(String... values) {
        for (String v : values) {
          setStatus(v);
        }
      };

      protected void onPostExecute(Void result) {
        SyncingActivity.this.finish();
      };

      @Override
      protected Void doInBackground(Void... params) {
        try {
          String url = APP_ENGINE_ORIGIN;
          publishProgress("Testing login using " + url);
          List<Pair<String, String>> headers = new ArrayList<Pair<String, String>>();
          headers.add(new Pair<String, String>("Authorization", "Bearer " + token));
          post(url, "this-is-the-request-body", headers);
        } catch (Exception e) {
          Log.w("EXCEPTION", e);
          publishProgress("EXECPTION  " + e);
        }
        return null;
      }

      String post(String url, String body, List<Pair<String, String>> headers) {
        try {
          URL u = new URL(url);
          publishProgress("[url   ] " + u);

          HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
          urlConnection.setDoOutput(true);
          urlConnection.setDoInput(true);
          urlConnection.setChunkedStreamingMode(0);
          urlConnection.setRequestMethod("POST");

          for (Pair<String, String> header : headers) {
            publishProgress("[header] " + header.first + ": " + header.second);
            urlConnection.addRequestProperty(header.first, header.second);
          }

          OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), UTF_8);
          InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream(), UTF_8);

          publishProgress("-> " + body);
          writer.write(body);

          String result = read(reader);
          publishProgress("<- " + result);

          urlConnection.disconnect();
          return result;
        } catch (Exception e) {
          Log.w(TAG, "EXECPTION" + e);
          throw new RuntimeException(e);
        }
      }

    }.execute((Void) null);

  }

  protected void send() {
    Hashtable<String, Float> rewards = new Hashtable<String, Float>();
    for (Chore chore : app.getChores()) {
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
      setStatus(msg);
    }
  }

  private void setStatus(String msg) {
    Log.i(TAG, msg);
    statusTextView.setText(msg);
  }

  static String read(InputStreamReader reader) throws IOException {
    char[] chars = new char[1024];
    StringBuffer buf = new StringBuffer();
    int len;
    while ((len = reader.read(chars)) != -1) {
      buf.append(chars, 0, len);
    }
    return buf.toString();
  }

}