package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Achala PC on 11/20/2016.
 */

public class ExpenseDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Expenses.db";

    public  ExpenseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS accounts(" +
                                "account_no VARCHAR PRIMARY KEY," +
                                "bank_name VARCHAR," +
                                "holder_name VARCHAR," +
                                "balance REAL" +
                               " );");
        db.execSQL("CREATE TABLE IF NOT EXISTS transactions(" +
                "transaction_no INTEGER PRIMARY KEY," +
                "account_no VARCHAR" +
                "expense_type INT2," +
                "amount REAL," +
                "date DATE," +
                "FOREIGN KEY (account_no) REFERENCES Account(account_no)" +
                ");");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS accounts;");
        db.execSQL("DROP TABLE IF EXISTS tarnsactions;");
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}