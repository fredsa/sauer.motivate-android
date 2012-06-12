package sauer.motivate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class MySQLiteOpenHelper extends SQLiteOpenHelper {

  private static final String TAG = MySQLiteOpenHelper.class.getName();

  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "motivate";

  MySQLiteOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    Log.d(TAG, "SQL ctor version=" + DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.d(TAG, "SQL onCreate()");
    createTable(db);
  }

  private void createTable(SQLiteDatabase sql) {
    Log.d(TAG, "CREATE TABLE...");
    sql.execSQL("CREATE TABLE chores (date TEXT, chore TEXT, reward_amount NUMBER, reward_unit TEXT, completed NUMBER);");
    String[] init = {
    "make bed",
     "laundry helper",
     "clean lego room",
     "good listening",
     "politeness, please and thank you",
     "good behavior",
     "good deed",
     "water plants|10",
     "homework no complaining|15",
     "brush teeth AM|10",
     "brush teeth PM|10",
     "clean pool|25",
     "save the earth",
     "dishes",
     "vacuum",
     "help dad",
     "help mom",
     "bathed myself",
     "nice to brother",
     "put up clothes",
     "reading 30 mins",
    };
    for (String i: init) {
      String desc = i;
      String amt = "10";
      int pos = i.indexOf('|');
      if (pos != -1) {
        desc = i.substring(0, pos);
        amt = i.substring(pos + 1);
      }
      sql.execSQL("INSERT INTO chores VALUES (null, \""+desc+"\", \""+amt+"\", \"mins\", 0);");
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.d(TAG, "SQL onUpgrade() " + oldVersion + " -> " + newVersion);
    db.execSQL("DROP TABLE chores;");
    createTable(db);
  }

}