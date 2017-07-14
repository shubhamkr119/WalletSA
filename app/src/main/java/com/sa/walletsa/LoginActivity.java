package com.sa.walletsa;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    Boolean doubleBackToExitPressedOnce = false;
    private DatabaseLogin loginDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String _tag_ = "onCreate";
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            final EditText editTextUsername = (EditText) findViewById(R.id.editTextUsername);
            if (null == editTextUsername) {
                error_logger(_tag_, "editTextUsername is null");
                finish();
                return;
            }

            final EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
            if (null == editTextPassword) {
                error_logger(_tag_, "editTextPassword is null");
                finish();
                return;
            }

            loginDB = new DatabaseLogin(LoginActivity.this);

            Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
            if (null == buttonLogin) {
                error_logger(_tag_, "buttonLogin is null");
                finish();
                return;
            }
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextUsername.getText().toString().equals("")) {
                        editTextUsername.setText("");
                        editTextPassword.setText("");
                        editTextUsername.requestFocus();
                        Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
                    } else {
                        Cursor rs = loginDB.getDataByUsernamePassword(editTextUsername.getText().toString(), editTextPassword.getText().toString());
                        if (null == rs) {
                            error_logger(_tag_, "Cursor is null");
                            finish();
                            return;
                        }
                        rs.moveToFirst();
                        if (rs.isAfterLast()) {
                            rs.close();
                            editTextUsername.setText("");
                            editTextPassword.setText("");
                            editTextUsername.requestFocus();
                            Toast.makeText(getApplicationContext(), "Invalid user", Toast.LENGTH_SHORT).show();
                        } else {
                            String name = rs.getString(rs.getColumnIndex(DatabaseLogin.column_name));
                            rs.close();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            saveInformation(editTextUsername.getText().toString(), name);
                            loginDB.close();
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });

            Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
            if (null == buttonRegister) {
                error_logger(_tag_, "buttonRegister is null");
                finish();
                return;
            }
            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RegisterNewUserActivity.class);
                    startActivity(intent);
                }
            });

            Button buttonLoginClear = (Button) findViewById(R.id.buttonLoginClear);
            if (null == buttonLoginClear) {
                error_logger(_tag_, "buttonLoginClear is null");
                finish();
                return;
            }
            buttonLoginClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTextUsername.setText("");
                    editTextPassword.setText("");
                    editTextUsername.requestFocus();
                }
            });
        }
        catch (Exception e) {
            error_logger(_tag_, "Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        final String _tag_ = "onBackPressed";
        try {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
        catch (Exception e) {
            error_logger(_tag_, "Exception");
            e.printStackTrace();
        }
    }

    private void saveInformation(String username, String name) {
        final String _tag_ = "saveInformation";
        try {
            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
            if (null == preferences) {
                error_logger(_tag_, "preferences is null");
                finish();
                return;
            }
            SharedPreferences.Editor pEditor = preferences.edit();
            pEditor.putString("username", username);
            pEditor.putString("name", name);
            pEditor.apply();
        }
        catch (Exception e) {
            error_logger(_tag_, "Exception");
            e.printStackTrace();
        }
    }

    void error_logger(String tag, String msg) {
        Log.e(tag, msg);
        Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_SHORT).show();
    }

}
