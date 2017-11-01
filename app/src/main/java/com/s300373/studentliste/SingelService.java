package com.s300373.studentliste;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by RudiAndre on 28.10.2017.
 */

public class SingelService extends Service {
    DBHandler db;
    ArrayList<Melding> meldingListe;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new DBHandler(this);
        meldingListe = db.hentUsendteMeldinger();
        Melding singelMelding = meldingListe.get(0);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df.parse(singelMelding.tidspunkt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(this, SingelSmsService.class);
        i.putExtra("enkelMldID", singelMelding._ID);
        i.putExtra("enkelMelding", singelMelding.melding);
        i.putExtra("enkelTidspunkt", singelMelding.tidspunkt);


        PendingIntent pintent = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pintent);

        return super.onStartCommand(intent, flags, startId);
    }
}