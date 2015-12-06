package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.MyAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.MyTransactionDAO;

/**
 * Created by Roshan on 12/5/2015.
 */
public class PersistentExpenseManager extends ExpenseManager {
    public PersistentExpenseManager(){
        setup();
    }
    @Override
    public void setup(){
        AccountDAO accountDAO = new MyAccountDAO();
        setAccountsDAO(accountDAO);

        TransactionDAO transactionDAO = new MyTransactionDAO();
        setTransactionsDAO(transactionDAO);
    }
}
