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

package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.ContextObject;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.MyAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 *
 */
public class ManageExpensesFragment extends Fragment implements View.OnClickListener {
    private Button submitButton;
    private Button btnCheckBalance;
    private EditText amount;
    private Spinner accountSelector;
    private RadioGroup expenseTypeGroup;
    private DatePicker datePicker;
    private ExpenseManager currentExpenseManager;

    private static ManageExpensesFragment manageExpensesFragment = null;

    public static ManageExpensesFragment newInstance(ExpenseManager expenseManager) {
        if( manageExpensesFragment == null){
            manageExpensesFragment = new ManageExpensesFragment();
            manageExpensesFragment.currentExpenseManager = expenseManager;
        }
        return manageExpensesFragment;
    }

    public ManageExpensesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_expenses, container, false);
        submitButton = (Button) rootView.findViewById(R.id.submit_amount);
        submitButton.setOnClickListener(this);
        btnCheckBalance = (Button) rootView.findViewById(R.id.check_balance);

        amount = (EditText) rootView.findViewById(R.id.amount);
        accountSelector = (Spinner) rootView.findViewById(R.id.account_selector);
        ArrayAdapter<String> adapter =
                null;
        if (currentExpenseManager != null) {
            adapter = new ArrayAdapter<>(this.getActivity(), R.layout.support_simple_spinner_dropdown_item,
                    currentExpenseManager.getAccountNumbersList());
        }
        accountSelector.setAdapter(adapter);

        expenseTypeGroup = (RadioGroup) rootView.findViewById(R.id.expense_type_group);
        RadioButton expenseType = (RadioButton) rootView.findViewById(R.id.expense);
        RadioButton incomeType = (RadioButton) rootView.findViewById(R.id.income);
        datePicker = (DatePicker) rootView.findViewById(R.id.date_selector);

        btnCheckBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAccountDAO myAccountDAO = new MyAccountDAO();
                try {
                    Account account = myAccountDAO.getAccount((String) accountSelector.getSelectedItem());
                    if(account != null){
                        String line1 = "Account Holder : " + account.getAccountHolderName();
                        String line2 = "Bank Name      : " + account.getBankName();
                        String line3 = "Balance        : " +account.getBalance();
                        Toast.makeText(ContextObject.getContext(), line1 + "\n" + line2 + "\n" + line3, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ContextObject.getContext(), "No available accounts", Toast.LENGTH_SHORT).show();
                    }
                } catch (InvalidAccountException e) {

                }
            }
        });
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_amount:
                String selectedAccount = (String) accountSelector.getSelectedItem();
                String amountStr = amount.getText().toString();
                RadioButton checkedType = (RadioButton) getActivity().findViewById(expenseTypeGroup
                        .getCheckedRadioButtonId());
                String type = (String) checkedType.getText();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                if (amountStr.isEmpty()) {
                    amount.setError(getActivity().getString(R.string.err_amount_required));
                }

                if (currentExpenseManager != null) {
                    try {
                        currentExpenseManager.updateAccountBalance(selectedAccount, day, month, year,
                                ExpenseType.valueOf(type.toUpperCase()), amountStr);
                    } catch (InvalidAccountException e) {
                        new AlertDialog.Builder(this.getActivity())
                                .setTitle(this.getString(R.string.msg_account_update_unable) + selectedAccount)
                                .setMessage(e.getMessage())
                                .setNeutralButton(this.getString(R.string.msg_ok),
                                        new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                    }
                }
                amount.getText().clear();
                break;
        }
    }
}
