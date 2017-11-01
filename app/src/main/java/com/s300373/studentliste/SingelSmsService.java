package com.s300373.studentliste;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by RudiAndre on 25.10.2017.
 */

public class SingelSmsService extends Service {
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

        long mldID = intent.getExtras().getLong("enkelMldID");
        String melding = intent.getExtras().getString("enkelMelding");
        String tidspunkt = intent.getExtras().getString("enkelTidspunkt");

        sms = new SendSMS();
        for(int i = 0; i < nummer.size(); i++) {
            sms.sendSMS(Integer.toString(nummer.get(i)), melding);
            Toast.makeText(getApplicationContext(), "Sender til: " + Integer.toString(nummer.get(i)), Toast.LENGTH_SHORT).show();
        }

        Melding oppdMelding = new Melding();
        oppdMelding._ID = mldID;
        oppdMelding.tidspunkt = tidspunkt;
        oppdMelding.melding = melding;
        oppdMelding.status = "Sendt";

        db.oppdaterMelding(oppdMelding);
        return super.onStartCommand(intent, flags, startId);
    }
}
