package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Achala PC on 11/20/2016.
 */
public class PersistantTransactionDAO implements TransactionDAO {
    private SQLiteDatabase database;

    public PersistantTransactionDAO(SQLiteDatabase db){
        this.database = db;
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String prepStat = "INSERT INTO transaction_details (account_no,type,amount,date) VALUES (?,?,?,?)";
        SQLiteStatement sqlStatement = database.compileStatement(prepStat);

        sqlStatement.bindString(1,accountNo);
        sqlStatement.bindLong(2,(expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        sqlStatement.bindDouble(3,amount);
        sqlStatement.bindLong(4,date.getTime());

        sqlStatement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor transSet = database.rawQuery("SELECT * FROM transaction_details",null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(transSet.moveToFirst()) {
            do{
                Transaction t = new Transaction(new Date(transSet.getLong(transSet.getColumnIndex("date"))),
                        transSet.getString(transSet.getColumnIndex("account_no")),
                        (transSet.getInt(transSet.getColumnIndex("type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        transSet.getDouble(transSet.getColumnIndex("amount")));
                transactions.add(t);
            }while (transSet.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor transSet = database.rawQuery("SELECT * FROM transaction_details LIMIT " + limit,null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(transSet.moveToFirst()) {
            do {
                Transaction t = new Transaction(new Date(transSet.getLong(transSet.getColumnIndex("date"))),
                        transSet.getString(transSet.getColumnIndex("account_no")),
                        (transSet.getInt(transSet.getColumnIndex("type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        transSet.getDouble(transSet.getColumnIndex("amount")));
                transactions.add(t);
            } while (transSet.moveToNext());
        }

        return transactions;
    }
}
