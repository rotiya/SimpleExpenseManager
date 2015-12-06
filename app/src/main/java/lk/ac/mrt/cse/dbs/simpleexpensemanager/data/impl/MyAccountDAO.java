package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.ContextObject;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseOperation;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Roshan on 12/5/2015.
 */
public class MyAccountDAO implements AccountDAO {
    //Database access object
    private DatabaseOperation dop;

    //Obtain database access object
    public MyAccountDAO(){
        dop = DatabaseOperation.getDOP();
    }

    @Override
    public List<String> getAccountNumbersList() {
        //Objective : Retrieve account numbers into string array

        SQLiteDatabase sqLiteDatabase = dop.getReadableDatabase();
        ArrayList<String> accountNumbers = new ArrayList<String>();

        //Required columns
        String[] columns = {DatabaseOperation.ACCOUNT_NO};
        Cursor cursor = sqLiteDatabase.query("account", columns, null, null, null, null, null);

        //Add account number to string array
        while(cursor.moveToNext()){
            accountNumbers.add(cursor.getString(0));
        }

        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        //Objective : Retrieve accounts into account array

        //Obtain database to read
        SQLiteDatabase sqLiteDatabase = dop.getReadableDatabase();

        ArrayList<Account> accounts = new ArrayList<Account>();

        //Select all columns from accounts
        String[] columns = {DatabaseOperation.ACCOUNT_NO, DatabaseOperation.BANK_NAME, DatabaseOperation.ACCOUNT_HOLDER_NAME, DatabaseOperation.BALANCE};
        Cursor cursor = sqLiteDatabase.query("account", columns, null, null, null, null, null);

        //Add accounts to account array
        while (cursor.moveToNext()){
            accounts.add(new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3))));
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        //Obtain database to read
        SQLiteDatabase sqLiteDatabase = dop.getReadableDatabase();

        //Required columns
        String[] columns = {DatabaseOperation.ACCOUNT_NO, DatabaseOperation.BANK_NAME, DatabaseOperation.ACCOUNT_HOLDER_NAME, DatabaseOperation.BALANCE};

        Cursor cursor = sqLiteDatabase.query("account", columns, DatabaseOperation.ACCOUNT_NO + " = '" + accountNo + "'", null, null, null, null);

        Account account = null;

        while (cursor.moveToNext()){
            account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3)));
            //Select one database entry : will not occur as we use primary key
            break;
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        //Objective : Save an account to database

        //Obtain database to write
        SQLiteDatabase sqLiteDatabase = dop.getWritableDatabase();

        //Set values
        ContentValues cv = new ContentValues();
        cv.put(DatabaseOperation.ACCOUNT_NO, account.getAccountNo());
        cv.put(DatabaseOperation.BANK_NAME, account.getBankName());
        cv.put(DatabaseOperation.ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        cv.put(DatabaseOperation.BALANCE, account.getBalance());

        //Execute insert query
        sqLiteDatabase.insert("account", null, cv);

        //Log
        Log.d("DB", "Row inserted to account");
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public boolean updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        //Objective : Update balance

        //Get account from the database
        Account account = getAccount(accountNo);

        //Transaction conditions
        if(expenseType == ExpenseType.INCOME){
            account.setBalance(account.getBalance() + amount);
        }else{
            if(account.getBalance() < amount){
                Toast.makeText(ContextObject.getContext(), "Insufficient balance", Toast.LENGTH_SHORT).show();

                //Log
                Log.d("DB", "Update account fail");

                //Transaction will not occur
                return false;
            }else{
                account.setBalance(account.getBalance() - amount);
            }

        }

        //Obtain database to write
        SQLiteDatabase sqLiteDatabase = dop.getWritableDatabase();

        //Set values
        ContentValues cv = new ContentValues();
        cv.put(DatabaseOperation.BALANCE, account.getBalance());

        //Execute update query
        sqLiteDatabase.update("account", cv, DatabaseOperation.ACCOUNT_NO + " = '" + accountNo + "'", null);

        //Log
        Log.d("DB", "Update account success");

        return true;
        //Transaction will occur
    }
}
