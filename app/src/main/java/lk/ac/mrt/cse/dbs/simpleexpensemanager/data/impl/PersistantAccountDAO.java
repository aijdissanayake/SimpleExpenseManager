package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Achala PC on 11/20/2016.
 */
public class PersistantAccountDAO implements AccountDAO {
    private SQLiteDatabase database;

    //We need to store the database in the constructor to prevent reopening it everytime
    public PersistantAccountDAO(SQLiteDatabase db){
        this.database = db;
    }
    @Override
    public List<String> getAccountNumbersList() {
        //using a cursor to iterate reults.
        Cursor resultSet = database.rawQuery("SELECT account_no FROM accounts",null);
        //We point the cursor to the first record before looping

        //Initialize the list for accountNumbers and adding accountNumbers
        List<String> accountNumbers = new ArrayList<String>();
        if(resultSet.moveToFirst()) {
            do {
                accountNumbers.add(resultSet.getString(resultSet.getColumnIndex("account_no")));
            } while (resultSet.moveToNext());
        }
        //Return the list
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor resultSet = database.rawQuery("SELECT * FROM accounts",null);
        //Initialize the list for accounts and adding accounts
        List<Account> accounts = new ArrayList<Account>();

        if(resultSet.moveToFirst()) {
            //create account objects to add to the list
            do {
                Account account = new Account(resultSet.getString(resultSet.getColumnIndex("account_no")),
                        resultSet.getString(resultSet.getColumnIndex("bank_name")),
                        resultSet.getString(resultSet.getColumnIndex("holder_name")),
                        resultSet.getDouble(resultSet.getColumnIndex("balance")));
                accounts.add(account);
            } while (resultSet.moveToNext());
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = database.rawQuery("SELECT * FROM accounts WHERE account_no = " + accountNo,null);
        Account account = null;

        if(resultSet.moveToFirst()) {
            do {
                account = new Account(resultSet.getString(resultSet.getColumnIndex("account_no")),
                        resultSet.getString(resultSet.getColumnIndex("bank_name")),
                        resultSet.getString(resultSet.getColumnIndex("holder_name")),
                        resultSet.getDouble(resultSet.getColumnIndex("balance")));
            } while (resultSet.moveToNext());
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        //Using prepared statements for inserting
        String prepStat = "INSERT INTO accounts (account_no,bank_name,holder_name,balance) VALUES (?,?,?,?)";
        SQLiteStatement sqlStatement = database.compileStatement(prepStat);

        sqlStatement.bindString(1, account.getAccountNo());
        sqlStatement.bindString(2, account.getBankName());
        sqlStatement.bindString(3, account.getAccountHolderName());
        sqlStatement.bindDouble(4, account.getBalance());

        sqlStatement.executeInsert();


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String prepStat = "DELETE FROM accounts WHERE account_no = ?";
        SQLiteStatement statement = database.compileStatement(prepStat);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String prepStat = "UPDATE accounts SET balance = balance + ?";
        SQLiteStatement statement = database.compileStatement(prepStat);
        //check income or expense
        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }

        statement.executeUpdateDelete();
    }
}