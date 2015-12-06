package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseOperation;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Roshan on 12/5/2015.
 */
public class MyTransactionDAO implements TransactionDAO {
    //Database access object
    DatabaseOperation dop;

    //Obtain database access object
    public  MyTransactionDAO(){
        dop = DatabaseOperation.getDOP();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        //Obtain database to write
        SQLiteDatabase sqLiteDatabase = dop.getWritableDatabase();

        //Convert date format
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sf.format(date);

        //Set values
        ContentValues cv = new ContentValues();
        cv.put(DatabaseOperation.TRANSACTION_DATE, formattedDate);
        cv.put(DatabaseOperation.ACCOUNT_NO, accountNo);
        cv.put(DatabaseOperation.EXPENSE_TYPE, expenseType.toString());
        cv.put(DatabaseOperation.AMOUNT, String.valueOf(amount));

        //Execute sql query
        sqLiteDatabase.insert("transaction_info", null, cv);

        //Log
        Log.d("DATE : ", formattedDate);
        Log.d("ACCN : ", accountNo);
        Log.d("EXPT : ", expenseType.toString());
        Log.d("AMNT : ", String.valueOf(amount));
        Log.d("DB", "Row inserted to transaction_info");
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        //Object database to write
        SQLiteDatabase sqLiteDatabase = dop.getReadableDatabase();

        //Required columns
        String[] columns = {DatabaseOperation.TRANSACTION_DATE, DatabaseOperation.ACCOUNT_NO, DatabaseOperation.EXPENSE_TYPE, DatabaseOperation.AMOUNT};

        Cursor cursor = sqLiteDatabase.query("transaction_info", columns, null, null, null, null, null);

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        //Add transaction to transaction array
        while (cursor.moveToNext()){
            transactions.add(new Transaction(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }

        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        //Obtain database to read
        SQLiteDatabase sqLiteDatabase = dop.getReadableDatabase();

        //Sql query
        String query = "select * from transaction_info order by "+ DatabaseOperation.TRANSACTION_DATE+" limit " + limit + "";

        //Required columns
        String[] columns = {DatabaseOperation.TRANSACTION_DATE, DatabaseOperation.ACCOUNT_NO, DatabaseOperation.EXPENSE_TYPE, DatabaseOperation.AMOUNT};

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        //Add transactions to transaction array
        while (cursor.moveToNext()){
            transactions.add(new Transaction(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }

        return transactions;
    }
}
