package app.budgetmanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import app.budgetmanager.db.DatabaseHandler;
import app.budgetmanager.model.Account;

import java.util.ArrayList;
import java.util.List;

// An activity for use others user's account
public class AccountManager extends AppCompatActivity {
    Account account;

    // Gui elements
    private EditText accountNameField;
    private ListView listView;
    Spinner accountTypeOptions;
    Button registerBtn;
    private TextView currentAccountLabel, balanceLabel;

    // Values to be inserted
    String accountName, accountType;
    private DatabaseHandler db;
    private final String IDENTITY = "ACCOUNT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_chooser);

        db = new DatabaseHandler(this);
        account = db.getAccount(db.getActiveAccountId());

        // Status >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        currentAccountLabel = findViewById(R.id.current_account_label);
        currentAccountLabel.setText("Account: " + account.getName());
        balanceLabel = findViewById(R.id.account_balance_label);
        balanceLabel.setText("$" + account.getBalance());

        // Account name field
        accountNameField = findViewById(R.id.accountNameField);

        // Account type dropdown list
        accountTypeOptions = (Spinner) findViewById(R.id.accountTypeOptions);
        String[] options = new String[]{"Credit", "Debit", "Money"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        accountTypeOptions.setAdapter(typeAdapter);

        // Register button
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountName = accountNameField.getText().toString();
                accountType = accountTypeOptions.getSelectedItem().toString();

                // Insertion in DB
                db.addAccount(new Account(accountName, accountType));
                Toast.makeText(getApplicationContext(), accountName + " registered successfully!", Toast.LENGTH_SHORT).show();
            }

        });

        db = new DatabaseHandler(this);
        List<String> accounts = db.getAllAccountsNames();

        listView = findViewById(R.id.accounts_list);
        ArrayAdapter<String> accountsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accounts);
        listView.setAdapter(accountsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId) {
                final CharSequence[] items = {"Delete", "Select as account"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountManager.this);
                builder.setTitle("Choose an option");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 1:
                                Log.d("Selection", "Item selected: " + item);
                        }
                    }
                });
                builder.show();
            }
        });
    }
}
