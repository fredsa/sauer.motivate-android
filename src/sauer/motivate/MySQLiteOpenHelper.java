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

  private void createTable(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE chores (chore TEXT, reward_amount NUMBER, reward_unit TEXT, completed NUMBER);");
    db.execSQL("INSERT INTO chores VALUES (\"Made my bed\", \"25\", \"¢\", 0);");
    db.execSQL("INSERT INTO chores VALUES (\"Brushed my teeth\", \"0.10\", \"USD\", 0);");
    db.execSQL("INSERT INTO chores VALUES (\"Helped Mom (Bonus)\", \"3\", \"Hugs\", 0);");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.d(TAG, "SQL onUpgrade() " + oldVersion + " -> " + newVersion);
    db.execSQL("DROP TABLE chores;");
    createTable(db);
  }

}