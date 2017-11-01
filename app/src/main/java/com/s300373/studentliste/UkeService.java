package com.s300373.studentliste;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by RudiAndre on 25.10.2017.
 */

public class UkeService extends Service {
    DBHandler db;
    //ArrayList<Integer> nummer;
    ArrayList<Melding> meldingListe;
    Bundle periodiskBundle;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar calendar = Calendar.getInstance();
        String ukedag = PreferenceManager.getDefaultSharedPreferences(this).getString("ukedag","Mandag");
        String ukeMelding = PreferenceManager.getDefaultSharedPreferences(this).getString("meldingPref", "Default");
        long tidspunkt = PreferenceManager.getDefaultSharedPreferences(this).getLong("timePickerPref", 0);

        Date tid = new Date(tidspunkt);
        Format timeFormat = new SimpleDateFormat("HH");
        Format minuttFormat = new SimpleDateFormat("mm");

        switch (ukedag) {
            case "Søndag":
                calendar.set(Calendar.DAY_OF_WEEK, 1);
                break;
            case "Mandag":
                calendar.set(Calendar.DAY_OF_WEEK, 2);
                break;
            case "Tirsdag":
                calendar.set(Calendar.DAY_OF_WEEK, 3);
                break;
            case "Onsdag":
                calendar.set(Calendar.DAY_OF_WEEK, 4);
                break;
            case "Torsdag":
                calendar.set(Calendar.DAY_OF_WEEK, 5);
                break;
            case "Fredag":
                calendar.set(Calendar.DAY_OF_WEEK, 6);
                break;
            case "Lørdag":
                calendar.set(Calendar.DAY_OF_WEEK, 7);
                break;
            default:
                break;
        }
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeFormat.format(tid)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minuttFormat.format(tid)));
        calendar.set(Calendar.SECOND, 00);

        if(calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 7);
        }

        Intent i = new Intent(this, UkeSmsService.class);
        i.putExtra("ukeMelding", ukeMelding);

        PendingIntent pintent = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24 * 7, pintent);

        return super.onStartCommand(intent, flags, startId);
    }
}
