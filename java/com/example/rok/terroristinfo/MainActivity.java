package com.example.rok.terroristinfo;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        setTabs();

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Please, check your internet connection and hit refresh button.", Toast.LENGTH_LONG).show();
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
                    new RequestAsyncTask(this).execute("http://10.10.10.100:8888/handler");
                } else {
                    Toast.makeText(this, "Please, check your internet connection and hit refresh button.", Toast.LENGTH_LONG).show();
                }

                break;

            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

    /*public Data[] dataGetter() {
        return data;
        //later, return data from async method. data will be stored in an array. array -> make as many textviews as needed (array size)
        //"EEEE, dd MMMM yyyy"

        Data[] data = new Data[6];
        data[0] = new Data((float)30.185969, (float)66.998882,
                "ak_black",
                "Quetta, Balochistan, Pakistan",
                "Suicide attack, Automatic Rifle and explosives used. 80+ killed, 120+ injured. Islamic state claimed responsibility.",
                "On 8 August 2016, terrorists attacked the Government Hospital of Quetta in Pakistan with a suicide bombing and shooting. They killed around 80 people (mainly lawyers). Responsibility for the attack has been initially claimed by Islamic state (ISIS). However, Jamaat-ul-Ahrar also claimed credit for thr attack.",
                "Monday",
                "August",
                8,
                "2016",
                false,
                "rifleAssault");

        data[1] = new Data((float)49.382753, (float)1.106722,
                "hostage_black",
                "Saint-Étienne-du-Rouvray, Normandy, France",
                "Two Islamist terrorists took six people captive and later slit throat of one of them (85 year old priest). The terrorist were later shot dead by police.",
                "At about 9.45, two men wielding knives, handgun and fake explosive belts entered church as Mass was being held. Priest, three nuns and two parishioners were taken hostage. The attackers forced the priest to kneel at the altar and then slit his throat while screaming 'Allahu akbar'. Parishioner was later knifed and left critically wounded (he survived). Other Hostages were unhurt. Police tried to negotiate with perpetrators - without success. Later they tried to run out of the church with hostages as human shield, but the police successfully eliminated them.",
                "Tuesday",
                "July",
                26,
                "2016",
                false,
                "hostageSituation"
                );

        data[2] = new Data(44, (float)14.1,
                "stab_black",
                "Another place",
                "Yep, here too",
                "Bad bad bad, Lalalaal lal lalala lala lallal lalalal lalalal ",
                "Wednesday",
                "September",
                7,
                "2016",
                false,
                "stabbing");

        data[3] = new Data((float) 42.1, 16,
                "ak_red",
                "Nice place",
                "What",
                "Nothing new. lalal lala lal lalal alal lala lallalala lalala",
                "Tuesday",
                "September",
                6,
                "2016",
                false,
                "rifleAssault");

        data[4] = new Data((float) 43, 17,
                "gun_yellow",
                "Very nice place",
                "Dunno what to say",
                "Lalalal lalal lalalaal lalala lalalalal lalala",
                "Monday",
                "September",
                5,
                "2016",
                false,
                "gunAssault");

        data[5] = new Data((float) 46, 17,
                "bio_red",
                "Somewhere",
                "Dunno what to say",
                "Lalalal lalal lalalaal lalala lalalalal lalala",
                "Sunday",
                "January",
                1,
                "2013",
                false,
                "biohazard");

        return data;
    }*/

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
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {

        super.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

