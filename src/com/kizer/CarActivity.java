package com.kizer;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: Khalid
 * Date: 3/14/12
 * Time: 12:28 CET
 */
public class CarActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        final Button startButton = (Button) findViewById(R.id.start);
        final Button stopButton = (Button) findViewById(R.id.stop);
        final Button quitButton = (Button) findViewById(R.id.quit);

        if (isServiceRunning()) {
            Log.d("LocationService", "service is already started!");
            startButton.setVisibility(View.INVISIBLE);
            stopButton.setVisibility(View.VISIBLE);
        } else {
            stopButton.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.VISIBLE);

        }

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplication(), "button clicked!", Toast.LENGTH_LONG).show();
                startButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.VISIBLE);

                Intent intent = new Intent(getApplicationContext(), LocationService.class);
                startService(intent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopButton.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(), LocationService.class);
                stopService(intent);
                Toast.makeText(getApplication(), "service stopped!", Toast.LENGTH_SHORT).show();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplication(), "Quit Application, Service still running!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            Log.d("LocationService", service.service.getClassName());
            if ("com.kizer.LocationService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

//    protected void saveActivityPreferences() {
//        // Create or retrieve the activity preferences object.
//        SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);
//
//        // Retrieve an editor to modify the shared preferences.
//        SharedPreferences.Editor editor = activityPreferences.edit();
//
//        // Store new primitive types in the shared preferences object.
//        editor.putString("button_state", BUTTON_STATE);
//
//        // Commit changes.
//        editor.commit();
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}