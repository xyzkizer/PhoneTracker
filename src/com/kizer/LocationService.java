package com.kizer;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Kizer
 * Date: 3/15/12
 * Time: 04:19 CET
 */
public class LocationService extends Service {

    private static String TAG = "LocationService";
    private LocationManager locationManager;
    private Timer timer = new Timer();
    private static final int INTERVAL = 1000 * 60 * 5;
    private final IBinder myBinder = new MyBinder();
    private static String imsi;


    @Override
    public void onCreate() {
        super.onCreate();

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imsi = telephonyManager.getSubscriberId();
        Log.d("IMSI", telephonyManager.getSubscriberId());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        Log.d(TAG, "best provider is " + provider);

        locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "location!");
                String provider = locationManager.getBestProvider(criteria, true);
                Log.d(TAG, "best provider is " + provider);

                Location location = locationManager.getLastKnownLocation(provider);

                locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
                updateLocation(location);
            }
        }, 0, INTERVAL);
        Log.d(TAG, "timer start!");
    }


    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "location changed!");
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "service destroy");

        if (timer != null) timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    private void updateLocation(Location location) {

        if (location != null) {
            Log.d(TAG, "Latitude=" + location.getLatitude());
            Log.d(TAG, "Longitude=" + location.getLongitude());
            Log.d(TAG, "Altitude=" + location.getAltitude());


            Socket socket = null;
            try {
                socket = new Socket(InetAddress.getByName("113.108.186.143"), 7541);


                ByteArrayOutputStream header = new ByteArrayOutputStream();
                DataOutputStream headers = new DataOutputStream(header);

                ByteArrayOutputStream data = new ByteArrayOutputStream();
                DataOutputStream datas = new DataOutputStream(data);

                Exchanger ex = new Exchanger((byte) 1, Exchanger.REQUEST, (byte) 1, (byte) 1, 1);

                ex.setValue("identity", imsi);
                ex.setValue("latitude", String.valueOf(location.getLatitude()));
                ex.setValue("longitude", String.valueOf(location.getLongitude()));
                ex.setValue("altitude", String.valueOf(location.getAltitude()));
                ex.setValue("time", (new Timestamp(location.getTime())).toString());


                headers.writeByte(ex.getVersion());
                headers.writeByte(ex.getType());
                headers.writeByte(ex.getEncode());
                headers.writeByte(ex.getExtend());
                headers.writeInt(ex.getCommand());

                Map<String, String> values = ex.getValues();

                if (!values.isEmpty()) {

                    for (Map.Entry<String, String> entry : values.entrySet()) {
                        datas.writeInt(entry.getKey().length());
                        datas.writeBytes(entry.getKey());

                        datas.writeInt(entry.getValue().length());
                        datas.writeBytes(entry.getValue());
                    }

                    headers.writeInt(data.toByteArray().length);
                }

                header.write(data.toByteArray());

                socket.getOutputStream().write(header.toByteArray());

                socket.close();

            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
        } else {
            Log.d(TAG, "cant access location!");
        }
    }


    public class MyBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }
}
