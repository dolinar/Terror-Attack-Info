package com.example.rok.terroristinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Launch extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_view);

        new RequestAsyncTask(this).execute("http://10.10.10.100:8888/handler");

        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent mapAct = new Intent("android.intent.action.MAPS");
                    mapAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mapAct);
                    finish();

                }
            }
        };
        t.start();


    }
}
