package com.s300373.studentliste;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by RudiAndre on 25.10.2017.
 */

public class MinBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DBHandler db = new DBHandler(context);
        ArrayList<Melding> meldingListe = db.hentUsendteMeldinger();

        String action = intent.getAction();
        boolean meldingSwitch = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("meldingSwitch", false);

        if(action != null) {
            if(action.equals("com.s300373.studentliste.periodiskbroadcast") && meldingSwitch) {
                Intent i = new Intent(context, UkeService.class);
                context.startService(i);
            } else if(action.equals("com.s300373.studentliste.singelbroadcast") && meldingSwitch && !meldingListe.isEmpty()) {
                Intent i = new Intent(context, SingelService.class);
                context.startService(i);
            } else if (action.equalsIgnoreCase("android.intent.action.BOOT_COMPLETED") && meldingSwitch) {
                if(!meldingListe.isEmpty()) {
                    Intent is = new Intent(context, SingelService.class);
                    context.startService(is);
                }
                Intent ip = new Intent(context, UkeService.class);
                context.startService(ip);
            }
        }
    }
}
