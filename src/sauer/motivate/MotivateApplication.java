package sauer.motivate;

import java.util.ArrayList;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MotivateApplication extends Application {
  private static final String TAG = MotivateApplication.class.getName();

  private SQLiteDatabase sql;

  @Override
  public void onCreate() {
    //    prefs = getSharedPreferences("motivate", Context.MODE_PRIVATE);
    sql = new MySQLiteOpenHelper(this).getWritableDatabase();
  }

  public ArrayList<Chore> getChoresNames() {
    ArrayList<Chore> list = new ArrayList<Chore>();
    Cursor query = sql.query("chores", new String[] {"chore", "reward_amount", "reward_unit"},
        null, null, "chore", null, "chore ASC");
    Log.d(TAG, "SELECT " + query);
    while (query.moveToNext()) {
      Chore chore = new Chore(query.getString(0), query.getFloat(1), query.getString(2));
      Log.d(TAG, "RESULT " + chore);
      list.add(chore);
    }
    return list;
  }

  public void insertChore(Chore chore) {
    Object[] args = new Object[] {
        chore.getDescription(), chore.getRewardAmount(), chore.getRewardUnit()};
    Log.d(TAG, "INSERT " + chore);
    sql.execSQL("INSERT INTO chores VALUES (?, ?, ?)", args);
  }

}
