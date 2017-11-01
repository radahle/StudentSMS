package com.s300373.studentliste;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by RudiAndre on 27.10.2017.
 */

public class UkeSmsService extends Service {
    SendSMS sms;
    DBHandler db;
    ArrayList<Integer> nummer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new DBHandler(this);
        nummer = db.hentAlleNummer();

        String melding = intent.getExtras().getString("ukeMelding");

        sms = new SendSMS();
        for(int i = 0; i < nummer.size(); i++) {
            sms.sendSMS(Integer.toString(nummer.get(i)), melding);

        }

        return super.onStartCommand(intent, flags, startId);
    }
}
