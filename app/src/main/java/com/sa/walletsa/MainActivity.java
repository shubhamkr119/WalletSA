package com.sa.walletsa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper userDB;
    String current_view;
    Boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String _tag_ = "onCreate";
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (null == toolbar) {
                error_logger(_tag_, "toolbar is null");
                finish();
                return;
            }
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (null == drawer) {
                error_logger(_tag_, "drawer is null");
                finish();
                return;
            }
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            if (null == navigationView) {
                error_logger(_tag_, "navigationView is null");
                finish();
                return;
            }
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_all);

            String _name_ = getSharedPreferences("preferences", MODE_PRIVATE).getString("name", null);
            if (null == _name_) {
                error_logger(_tag_, "_name_ is null");
                finish();
                return;
            }
            String _username_ = getSharedPreferences("preferences", MODE_PRIVATE).getString("username", null);
            if (null == _username_) {
                error_logger(_tag_, "_username_ is null");
                finish();
                return;
            }

            View _navHeaderView_ = navigationView.getHeaderView(0);
            if (null == _navHeaderView_) {
                error_logger(_tag_, "_navHeaderView_ is null");
                finish();
                return;
            }

            TextView _textViewNavUsername_ = _navHeaderView_.findViewById(R.id.textViewNavUsername);
            if (null == _textViewNavUsername_) {
                error_logger(_tag_, "_textViewNavUsername_ is null");
                finish();
                return;
            }
            _textViewNavUsername_.setText(_username_);

            TextView _textViewNavName_ = _navHeaderView_.findViewById(R.id.textViewNavName);
            if (null == _textViewNavName_) {
                error_logger(_tag_, "_textViewNavName_ is null");
                finish();
                return;
            }
            String _greeting_msg_ = "Hi " + _name_ + " !";
            _textViewNavName_.setText(_greeting_msg_);

            userDB = new DatabaseHelper(MainActivity.this, _username_);

            Button buttonLogout = (Button) findViewById(R.id.nav_btn_logout);
            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = getSharedPreferences("preferences", 0);
                    preferences.edit().remove("username").apply();
                    userDB.close();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            ListView _listViewMainActivity_ = (ListView) findViewById(R.id.listViewMainActivity);
            if (null == _listViewMainActivity_) {
                error_logger(_tag_, "_listViewMainActivity_ is null");
                finish();
                return;
            }
            _listViewMainActivity_.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemLongClickListViewMainActivity(position);
                    return true;
                }
            });

            current_view = "All";
            doubleBackToExitPressedOnce = false;
        }
        catch (Exception e) {
            error_logger(_tag_, "Exception");
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        final String _tag_ = "onPostResume";
        try {
            super.onPostResume();
            update_main_view();
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
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
        }
        catch (Exception e) {
            error_logger(_tag_, "Exception");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final String _tag_ = "onCreateOptionsMenu";
        try {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        catch (Exception e) {
            error_logger(_tag_, "Exception");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final String _tag_ = "onOptionsItemSelected";
        try {
            int id = item.getItemId();

            if (id == R.id.action_add_item) {
                Intent intent = new Intent(getApplicationContext(), NewTransactionActivity.class);
                startActivity(intent);
                return true;
            }
            else if (id == R.id.action_help) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("HELP");
                alertDialog.setIcon(android.R.drawable.ic_menu_help);
                alertDialog.setMessage(R.string.help_main_activity);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final String _tag_ = "onNavigationItemSelected";
        try {
            int id = item.getItemId();

            if (id == R.id.nav_all) {
                current_view = "All";
                update_main_view();
            }
            else if (id == R.id.nav_today) {
                current_view = "Today";
                update_main_view();
            }
            else if (id == R.id.nav_date_range) {
                current_view = "TBD";
                update_main_view();
            }
            else if (id == R.id.nav_change_password) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
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

    private void onItemLongClickListViewMainActivity(int position) {
        final String _tag_ = "onBackPressed";
        try {
            ListView _listViewMainActivity_ = (ListView) findViewById(R.id.listViewMainActivity);
            if (null == _listViewMainActivity_) {
                error_logger(_tag_, "_listViewMainActivity_ is null");
                finish();
                return;
            }
            final CustomObject item = (CustomObject) _listViewMainActivity_.getItemAtPosition(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to delete item \"" + item.getDescription() + "\"");

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    String desc = null, dt = null, amt = null;

                    DateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    toFormat.setLenient(false);
                    DateFormat fromFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                    fromFormat.setLenient(false);
                    try {
                        desc = item.getDescription();
                        dt = toFormat.format(fromFormat.parse(item.getDate()));
                        amt = item.getAmount();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    userDB.deleteData(desc, dt, amt);

                    update_main_view();

                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), desc + " deleted", Toast.LENGTH_SHORT).show();
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        catch (Exception e) {
            error_logger(_tag_, "Exception");
            e.printStackTrace();
        }
    }

    void update_main_view() {
        final String _tag_ = "update_main_view";
        try {
            TextView _textViewCurrentView_ = (TextView) findViewById(R.id.textViewCurrentView);
            if (null == _textViewCurrentView_) {
                error_logger(_tag_, "_textViewCurrentView_ is null");
                finish();
                return;
            }
            if (null == current_view || (!current_view.equals("All") && !current_view.equals("Today") && !current_view.equals("TBD"))) {
                error_logger(_tag_, "Unexpected current_view");
                finish();
                return;
            }
            _textViewCurrentView_.setText(current_view);

            int sum = 0;

            DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            toFormat.setLenient(false);

            ArrayList<CustomObject> objects = new ArrayList<>();

            Cursor rs;
            switch (current_view) {
                case "Today":
                    rs = userDB.getData();
                    break;
                case "All":
                    rs = userDB.getDataAll();
                    break;
                default:
                    rs = userDB.getData();
                    break;
            }
            if (null == rs) {
                error_logger(_tag_, "Cursor is null");
                finish();
                return;
            }
            rs.moveToFirst();

            View _contents_main_ = findViewById(R.id.contents_main);
            if (null == _contents_main_) {
                error_logger(_tag_, "_contents_main_ is null");
                finish();
                return;
            }
            if (rs.isAfterLast()) {
                Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();
                _contents_main_.setVisibility(View.GONE);
                return;
            }
            _contents_main_.setVisibility(View.VISIBLE);

            while (!rs.isAfterLast()) {
                try {
                    objects.add(new CustomObject(rs.getString(rs.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)), toFormat.format(fromFormat.parse(rs.getString(rs.getColumnIndex(DatabaseHelper.COLUMN_DATE)))), rs.getString(rs.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT))));
                    sum = sum + Integer.parseInt(rs.getString(rs.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                rs.moveToNext();
            }
            rs.close();

            TextView _textViewSum_ = (TextView) findViewById(R.id.textViewSum);
            if (null == _textViewSum_) {
                error_logger(_tag_, "_textViewSum_ is null");
                finish();
                return;
            }
            if (0 > sum) {
                sum = Math.abs(sum);
                _textViewSum_.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorMaterialRedA700));
            } else {
                _textViewSum_.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorMaterialLightGreen800));
            }
            String _sum_string_ = "\u20B9 " + sum;
            _textViewSum_.setText(_sum_string_);

            CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, objects);

            ListView _listViewMainActivity_ = (ListView) findViewById(R.id.listViewMainActivity);
            if (null == _listViewMainActivity_) {
                error_logger(_tag_, "_listViewMainActivity_ is null");
                finish();
                return;
            }

            _listViewMainActivity_.setAdapter(customAdapter);
        }
        catch (Exception e) {
            error_logger(_tag_, "Exception");
            e.printStackTrace();
        }
    }

    private class CustomObject {

        private String description;
        private String date;
        private String amount;

        CustomObject(String description, String date, String amount) {
            this.description = description;
            this.date = date;
            this.amount = amount;
        }

        String getDescription() {
            return description;
        }

        String getDate() {
            return date;
        }

        String getAmount() {
            return amount;
        }
    }

    private class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<CustomObject> objects;

        CustomAdapter(Context context, ArrayList<CustomObject> objects) {
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }

        public CustomObject getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_listview_main_activity, parent, false);
                holder.textViewDescription = convertView.findViewById(R.id.textviewListviewMainActivityDescription);
                holder.textViewDate = convertView.findViewById(R.id.textviewListviewMainActivityDate);
                holder.textViewAmount = convertView.findViewById(R.id.textviewListviewMainActivityAmount);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textViewDescription.setText(objects.get(position).getDescription());
            holder.textViewDate.setText(objects.get(position).getDate());
            int _amount_ = Integer.parseInt(objects.get(position).getAmount());
            if (0 > _amount_) {
                _amount_ = Math.abs(_amount_);
                holder.textViewAmount.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorMaterialRedA700));
            } else {
                holder.textViewAmount.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorMaterialLightGreen800));
            }
            String _amount_string_ = "\u20B9 " + _amount_;
            holder.textViewAmount.setText(_amount_string_);
            return convertView;
        }

        private class ViewHolder {
            TextView textViewDescription;
            TextView textViewDate;
            TextView textViewAmount;
        }
    }

}
