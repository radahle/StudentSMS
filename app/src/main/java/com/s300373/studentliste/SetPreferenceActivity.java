package com.s300373.studentliste;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by RudiAndre on 24.10.2017.
 */

public class SetPreferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.prefToolbar);
        toolbar.setTitle("Innstillinger");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });


        getFragmentManager().beginTransaction().replace(R.id.prefContentFrame,
                new PrefsFragment()).commit();

    }

    public void startSingelService(View v) {
        Intent enkelIntent = new Intent();
        enkelIntent.setAction("com.s300373.studentliste.singelbroadcast");
        sendBroadcast(enkelIntent);
    }

    public void startUkeService(View v) {
        Intent ukeIntent = new Intent();
        ukeIntent.setAction("com.s300373.studentliste.periodiskbroadcast");
        sendBroadcast(ukeIntent);
    }

    public void stoppUkeSmsService(View v) {
        Intent i = new Intent(this, UkeSmsService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarm!= null) {
            alarm.cancel(pintent);
        }
    }

    public void stoppSingelSmsService(View v) {
        Intent i = new Intent(this, SingelSmsService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarm!= null) {
            alarm.cancel(pintent);
        }
    }

    public static class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            PreferenceManager.setDefaultValues(getContext(), R.xml.preferences,
                    false);
            initSummary(getPreferenceScreen());


        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePrefSummary(findPreference(key));
            if(key.equals("meldingSwitch")) {
                if(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("meldingSwitch", false)) {
                    ((SetPreferenceActivity)getActivity()).startUkeService(getView());
                    ((SetPreferenceActivity)getActivity()).startSingelService(getView());
                } else {
                    ((SetPreferenceActivity)getActivity()).stoppUkeSmsService(getView());
                    ((SetPreferenceActivity)getActivity()).stoppSingelSmsService(getView());
                }
            }
            if((key.equals("ukedag") ||
                    key.equals("timePickerPref") ||
                    key.equals("meldingPref")) &&
                    PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("meldingSwitch", false)) {
                ((SetPreferenceActivity)getActivity()).startUkeService(getView());
            }
        }

        private void initSummary(Preference p) {
            if (p instanceof PreferenceGroup) {
                PreferenceGroup pGrp = (PreferenceGroup) p;
                for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                    initSummary(pGrp.getPreference(i));
                }
            } else {
                updatePrefSummary(p);
            }
        }

        private void updatePrefSummary(Preference p) {
            if (p instanceof ListPreference) {
                ListPreference listPref = (ListPreference) p;
                p.setSummary(listPref.getEntry());
            }
            if (p instanceof EditTextPreference) {
                EditTextPreference editTextPref = (EditTextPreference) p;
                p.setSummary(editTextPref.getText());
            }
            if (p instanceof Preference) {
                Preference pref = p;
            }
        }
    }

}