package com.example.rok.terroristinfo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        setTabs();

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No new data is being shown because your connection is not established.", Toast.LENGTH_LONG).show();
        }

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(listener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.clear();
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_id:
                Intent settings = new Intent("android.intent.action.SETTINGS");
                startActivity(settings);
                break;

            case R.id.help_id:
                Intent help = new Intent("android.intent.action.HELP");
                startActivity(help);
                break;

            case R.id.about_us_id:
                Intent aboutUs = new Intent("android.intent.action.ABOUT");
                startActivity(aboutUs);
                break;

            case R.id.rate_id:
                launchMarket();
                break;

            case R.id.about_app_id:
                Intent aboutApp = new Intent("android.intent.action.ABOUTAPP");
                startActivity(aboutApp);
                break;

            case R.id.share_id:

                break;

            case R.id.refresh_id:
                if (isNetworkAvailable()) {
                    new RequestAsyncTask(this, this).execute("http://10.10.10.100:8888/handler");
                } else {
                    Toast.makeText(this, "Please, check your internet connection and hit refresh button again.", Toast.LENGTH_LONG).show();
                }

                break;

            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void setTabs() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new FragPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        tabLayout.setupWithViewPager(viewPager);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            // listener implementation
            if (key.equals("listEventType") || key.equals("listEventAge")) {
                recreate();
            }
        }
    };

    @Override
    protected void onDestroy() { super.onDestroy(); }

    @Override
    public void onLowMemory() { super.onLowMemory(); }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

