package com.sa.walletsa;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NewTransactionActivity extends AppCompatActivity {

    private DatabaseHelper userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String _tag_ = "onCreate";
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_transaction);

            ActionBar _action_bar_ = getSupportActionBar();
            if (null == _action_bar_) {
                error_logger(_tag_, "ActionBar is null");
                finish();
                return;
            }

            _action_bar_.setDisplayHomeAsUpEnabled(true);

            String _username_ = getSharedPreferences("preferences", MODE_PRIVATE).getString("username", null);
            if (null == _username_) {
                error_logger(_tag_, "_username_ is null");
                finish();
                return;
            }

            userDB = new DatabaseHelper(NewTransactionActivity.this, _username_);

            Button buttonDialogOK = (Button) findViewById(R.id.buttonDialogOK);
            Button buttonDialogCancel = (Button) findViewById(R.id.buttonDialogCancel);

            EditText editTextDialogDate = (EditText) findViewById(R.id.editTextDialogDate);
            if (null == editTextDialogDate) {
                error_logger(_tag_, "editTextDialogDate is null");
                finish();
                return;
            }

            EditText editTextDialogDescription = (EditText) findViewById(R.id.editTextDialogDescription);
            if (null == editTextDialogDescription) {
                error_logger(_tag_, "editTextDialogDescription is null");
                finish();
                return;
            }

            EditText editTextDialogAmount = (EditText) findViewById(R.id.editTextDialogAmount);
            if (null == editTextDialogAmount) {
                error_logger(_tag_, "editTextDialogAmount is null");
                finish();
                return;
            }

            editTextDialogDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final Calendar myCalendarFrom = Calendar.getInstance();

                    final DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            myCalendarFrom.set(Calendar.YEAR, year);
                            myCalendarFrom.set(Calendar.MONTH, monthOfYear);
                            myCalendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

                            EditText editTextDialogDate = (EditText) findViewById(R.id.editTextDialogDate);
                            if (null == editTextDialogDate) {
                                error_logger(_tag_, "editTextDialogDate is null");
                                finish();
                                return;
                            }

                            editTextDialogDate.setText(sdf.format(myCalendarFrom.getTime()));
                        }

                    };

                    new DatePickerDialog(NewTransactionActivity.this, dateFrom, myCalendarFrom
                            .get(Calendar.YEAR), myCalendarFrom.get(Calendar.MONTH),
                            myCalendarFrom.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            buttonDialogOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String desc, dt, amt;

                    DateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    toFormat.setLenient(false);
                    DateFormat fromFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                    fromFormat.setLenient(false);

                    EditText editTextDialogDate = (EditText) findViewById(R.id.editTextDialogDate);
                    if (null == editTextDialogDate) {
                        error_logger(_tag_, "editTextDialogDate is null");
                        finish();
                        return;
                    }

                    EditText editTextDialogDescription = (EditText) findViewById(R.id.editTextDialogDescription);
                    if (null == editTextDialogDescription) {
                        error_logger(_tag_, "editTextDialogDescription is null");
                        finish();
                        return;
                    }

                    EditText editTextDialogAmount = (EditText) findViewById(R.id.editTextDialogAmount);
                    if (null == editTextDialogAmount) {
                        error_logger(_tag_, "editTextDialogAmount is null");
                        finish();
                        return;
                    }

                    desc = editTextDialogDescription.getText().toString().trim();
                    amt = editTextDialogAmount.getText().toString().trim();
                    try {
                        dt = toFormat.format(fromFormat.parse(editTextDialogDate.getText().toString().trim()));
                    }
                    catch (ParseException pe) {
                        error_logger(_tag_, "Exception");
                        pe.printStackTrace();
                        return;
                    }

                    if(desc.length() != 0 && dt != null && dt.length() != 0 && amt.length() != 0) {
                        userDB.insertData(desc, dt, amt);
                        Toast.makeText(getApplicationContext(), desc + " added", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Incorrect Details", Toast.LENGTH_LONG).show();

                }
            });

            buttonDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editTextDialogDate = (EditText) findViewById(R.id.editTextDialogDate);
                    if (null == editTextDialogDate) {
                        error_logger(_tag_, "editTextDialogDate is null");
                        finish();
                        return;
                    }

                    EditText editTextDialogDescription = (EditText) findViewById(R.id.editTextDialogDescription);
                    if (null == editTextDialogDescription) {
                        error_logger(_tag_, "editTextDialogDescription is null");
                        finish();
                        return;
                    }

                    EditText editTextDialogAmount = (EditText) findViewById(R.id.editTextDialogAmount);
                    if (null == editTextDialogAmount) {
                        error_logger(_tag_, "editTextDialogAmount is null");
                        finish();
                        return;
                    }
                    editTextDialogDescription.setText("");
                    editTextDialogDate.setText("");
                    editTextDialogAmount.setText("");
                    editTextDialogDescription.requestFocus();
                }
            });

            DateFormat fromFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            fromFormat.setLenient(false);

            editTextDialogDate.setText(fromFormat.format(new Date()));
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
