package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.ContextObject;

/**
 * Created by Roshan on 12/5/2015.
 */

public class DatabaseOperation extends SQLiteOpenHelper {
    //Database metadata
    private static int DATABASE_VERSION = 1 ;
    private static final String DATABASE_NAME = "sem";

    //Account Table
    public static final String ACCOUNT_NO = "account_no";
    public static final String BANK_NAME = "bank_name";
    public static final String ACCOUNT_HOLDER_NAME = "account_holder_name";
    public static final String BALANCE = "balance";

    //Transaction table
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String EXPENSE_TYPE = "expense_type";
    public static final String AMOUNT = "amount";

    //Singleton object
    private static DatabaseOperation dop;

    //SQL query to create account table
    private String CREATE_ACCOUNT_TABLE = "CREATE TABLE `account`( "+ ACCOUNT_NO +" TEXT, "+
            BANK_NAME +" TEXT,"+
            ACCOUNT_HOLDER_NAME +" TEXT, "+
            BALANCE +" TEXT,PRIMARY KEY ("+ ACCOUNT_NO +"));";

    //SQL query to create transaction table
    private String CREATE_TRANSACTION_TABLE = "CREATE TABLE `transaction_info`( "+
            TRANSACTION_DATE +" TEXT, "+
            ACCOUNT_NO +" TEXT REFERENCES `account`("+ ACCOUNT_NO +"), "+
            EXPENSE_TYPE +" TEXT, "+
            AMOUNT +" TEXT NOT NULL)";

    //Create database
    private DatabaseOperation(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DB", "Database Created!");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create account table
        db.execSQL(CREATE_ACCOUNT_TABLE);
        Log.d("DB", "Account table created!");

        //Create transaction table
        db.execSQL(CREATE_TRANSACTION_TABLE);
        Log.d("DB", "Transaction table Created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static DatabaseOperation getDOP(){

        //Singleton database operation object
        if(dop == null){
            dop = new DatabaseOperation(ContextObject.getContext());
        }

        return dop;
    }
}
