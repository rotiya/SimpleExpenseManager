/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This POJO holds the information regarding a single transaction.
 */
public class Transaction {
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private String accountNo;
    private ExpenseType expenseType;
    private double amount;

    public Transaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        this.date = date;
        this.accountNo = accountNo;
        this.expenseType = expenseType;
        this.amount = amount;
    }

    public Transaction(String date, String account_no, String expense_type, String amount) {

        //Call when read from the data
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        try {
            this.date = df.parse(date);
        } catch (ParseException e) {
            Log.d("DATE ERROR", "INVALID CONVERSION @ Transaction Constructor 3");
        }

        this.accountNo = account_no;
        this.expenseType = ExpenseType.valueOf(expense_type);
        this.amount = Double.parseDouble(amount);
    }
}
