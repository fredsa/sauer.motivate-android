package sauer.motivate;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class MotivateApplication extends Application {
  private static final String PREF_OAUTH2_TOKEN = "OAUTH2_TOKEN";

  private static final String TAG = MotivateApplication.class.getName();

  private SQLiteDatabase sql;

  private SharedPreferences prefs;

  @Override
  public void onCreate() {
    prefs = getSharedPreferences("motivate", Context.MODE_PRIVATE);
    sql = new MySQLiteOpenHelper(this).getWritableDatabase();
  }

  public ArrayList<Chore> getChores(String date_yyyymmdd) {
    ArrayList<Chore> list = new ArrayList<Chore>();
    Cursor query = sql.query("chores", new String[] {
        "date", "chore", "reward_amount", "reward_unit", "completed"}, "date = ?",
        new String[] {date_yyyymmdd}, "chore", null, "chore ASC");
    Log.d(TAG, "SELECT * FROM chores WHERE date = " + date_yyyymmdd);
    while (query.moveToNext()) {
      Chore chore = new Chore(query.getString(0), query.getString(1), query.getFloat(2),
          query.getString(3), query.getInt(4));
      Log.d(TAG, "RESULT " + chore);
      list.add(chore);
    }
    return list;
  }

  public void insertChore(Chore chore) {
    Object[] args = new Object[] {
        chore.getDate(), chore.getDescription(), chore.getRewardAmount(), chore.getRewardUnit(),
        chore.getCompleted()};
    Log.d(TAG, "INSERT " + chore);
    sql.execSQL("INSERT INTO chores VALUES (?, ?, ?, ?, ?)", args);
  }

  public String getAuthToken() {
    return prefs.getString(PREF_OAUTH2_TOKEN, null);
  }

  public void setAuthToken(String authToken) {
    prefs.edit().putString(PREF_OAUTH2_TOKEN, authToken).commit();
  }

  public void setChore(Chore chore) {
    deleteChore(chore);
    insertChore(chore);
  }

  private void deleteChore(Chore chore) {
    Object[] args = new Object[] {chore.getDescription()};
    Log.d(TAG, "DELETE " + chore.getDescription());
    sql.execSQL("DELETE FROM chores WHERE chore = ?", args);
  }

}
