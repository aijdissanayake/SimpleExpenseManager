package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;

/**
 * Created by Achala PC on 11/20/2016.
 */
public class PersistantExpenseManager extends ExpenseManager {
    private Context ctx;
    public PersistantExpenseManager(Context ctx){
        //Point the constructor to the setup function or our expense manager does not get initialized
        this.ctx = ctx;
        setup();
    }
    @Override
    public void setup(){
        //database
        SQLiteDatabase mydatabase = ctx.openOrCreateDatabase("140133X", ctx.MODE_PRIVATE, null);

        //initializing database for the first time
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS accounts(" +
                "account_no VARCHAR PRIMARY KEY," +
                "bank_name VARCHAR," +
                "holder_name VARCHAR," +
                "balance REAL" +
                " );");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS transaction_details(" +
                "transaction_no INTEGER PRIMARY KEY," +
                "account_no VARCHAR," +
                "type INT," +
                "amount REAL," +
                "date DATE," +
                "FOREIGN KEY (account_no) REFERENCES Account(account_no)" +
                ");");



        //These two functions will hold our DAO instances in memory till the program exists
        PersistantAccountDAO accountDAO = new PersistantAccountDAO(mydatabase);
        //accountDAO.addAccount(new Account("Account12","Sampath bank","Manujith",500));

        setAccountsDAO(accountDAO);

        setTransactionsDAO(new PersistantTransactionDAO(mydatabase));
    }
}

