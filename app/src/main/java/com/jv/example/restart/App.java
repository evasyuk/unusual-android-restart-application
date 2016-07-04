package com.jv.example.restart;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by jv on 03.07.16.
 */
public class App extends Application {

    public static final String CRASHED_BOOLEAN_FLAG_KEY = "CRASHED_BOOLEAN_FLAG_KEY";

    static Intent in;

    public static void setIntent(Intent intent) {
        in = intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        in = new Intent(App.this, StarterActivity.class);
        ComponentName componentName = new ComponentName("com.jv.example.restart", "StarterActivity");
        in.setComponent(componentName);
        in.setAction(Intent.ACTION_MAIN);
        in.addCategory(Intent.CATEGORY_LAUNCHER);

        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            in.putExtra(CRASHED_BOOLEAN_FLAG_KEY, true);

            Crashlytics.logException(throwable);

            Toast.makeText(App.this, "Oops! Something terrible has happened", Toast.LENGTH_SHORT).show();

            long WAIT_TIME = 2000;

            PendingIntent pin = PendingIntent.getActivity(App.this, 0, in, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, WAIT_TIME, pin);

            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            android.os.Process.killProcess(android.os.Process.myPid());

        }
    };
}
