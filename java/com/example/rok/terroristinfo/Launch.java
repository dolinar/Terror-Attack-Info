package com.example.rok.terroristinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Rok on 27.7.2016.
 */
public class Launch extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_view);

        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(2500);

                } catch (InterruptedException e) {
                    System.out.println("Error: " + e.getMessage());
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
