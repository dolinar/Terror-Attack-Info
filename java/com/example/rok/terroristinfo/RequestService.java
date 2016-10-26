package com.example.rok.terroristinfo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class RequestService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        timerTask();
        return null;
    }

    private void timerTask() {
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                //new RequestAsyncTask(getBaseContext()).execute("http://stackoverflow.com");
            }
        };
        timer.schedule(hourlyTask, 0L, 1000*60*60);   // 1000*10*60 every 10 minutes
    }

}
