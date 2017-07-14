package com.sa.walletsa;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterNewUserActivity extends AppCompatActivity {

    private DatabaseLogin loginDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String _tag_ = "onCreate";
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_new_user);

            ActionBar _action_bar_ = getSupportActionBar();
            if (null == _action_bar_) {
                error_logger(_tag_, "ActionBar is null");
                finish();
                return;
            }

            _action_bar_.setDisplayHomeAsUpEnabled(true);

            loginDB = new DatabaseLogin(RegisterNewUserActivity.this);

            final EditText editTextRegisterName = (EditText) findViewById(R.id.editTextRegisterName);
            if (null == editTextRegisterName) {
                error_logger(_tag_, "editTextRegisterName is null");
                finish();
                return;
            }

            final EditText editTextRegisterUsername = (EditText) findViewById(R.id.editTextRegisterUsername);
            if (null == editTextRegisterUsername) {
                error_logger(_tag_, "editTextRegisterUsername is null");
                finish();
                return;
            }

            final EditText editTextRegisterPassword = (EditText) findViewById(R.id.editTextRegisterPassword);
            if (null == editTextRegisterPassword) {
                error_logger(_tag_, "editTextRegisterPassword is null");
                finish();
                return;
            }

            Button buttonRegisterDone = (Button) findViewById(R.id.buttonRegisterDone);
            if (null == buttonRegisterDone) {
                error_logger(_tag_, "buttonRegisterDone is null");
                finish();
                return;
            }
            buttonRegisterDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = editTextRegisterUsername.getText().toString();
                    if (username.equals("")) {
                        editTextRegisterUsername.setText("");
                        editTextRegisterPassword.setText("");
                        editTextRegisterUsername.requestFocus();
                        Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
                    } else {
                        Cursor rs = loginDB.getDataByUsername(username);
                        if (null == rs) {
                            error_logger(_tag_, "Cursor is null");
                            finish();
                            return;
                        }
                        rs.moveToFirst();
                        if (rs.isAfterLast()) {
                            loginDB.insertData(editTextRegisterName.getText().toString(), editTextRegisterUsername.getText().toString(), editTextRegisterPassword.getText().toString());
                            Toast.makeText(getApplicationContext(), editTextRegisterUsername.getText().toString() + " added", Toast.LENGTH_LONG).show();
                            rs.close();
                            finish();
                        } else {
                            rs.close();
                            editTextRegisterUsername.setText("");
                            editTextRegisterPassword.setText("");
                            editTextRegisterUsername.requestFocus();
                            Toast.makeText(getApplicationContext(), "Username not available", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            Button buttonRegisterClear = (Button) findViewById(R.id.buttonRegisterClear);
            if (null == buttonRegisterClear) {
                error_logger(_tag_, "buttonRegisterClear is null");
                finish();
                return;
            }
            buttonRegisterClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTextRegisterName.setText("");
                    editTextRegisterUsername.setText("");
                    editTextRegisterPassword.setText("");
                    editTextRegisterName.requestFocus();
                }
            });
        }
        catch (Exception e) {
            error_logger(_tag_, "Exception");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final String _tag_ = "onOptionsItemSelected";
        try {
            int id = item.getItemId();

            if (id == android.R.id.home) {
                finish();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
        catch (Exception e) {
            error_logger(_tag_, "Exception");
            e.printStackTrace();
            return false;
        }
    }

    void error_logger(String tag, String msg) {
        Log.e(tag, msg);
        Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_SHORT).show();
    }
}
