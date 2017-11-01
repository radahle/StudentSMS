package com.s300373.studentliste;

import android.telephony.SmsManager;

/**
 * Created by RudiAndre on 22.10.2017.
 */

public class SendSMS {
    String nummer;
    String melding;

    public SendSMS() {
    }

    public SendSMS(String nummer, String melding) {
        this.nummer = nummer;
        this.melding = melding;
    }

    public void sendSMS(String nummer, String melding){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(nummer, null, melding, null, null);
    }
}
