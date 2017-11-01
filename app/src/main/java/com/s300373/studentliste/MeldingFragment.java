package com.s300373.studentliste;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by RudiAndre on 18.10.2017.
 */

public class MeldingFragment extends Fragment {

    private static EditText melding;
    private static EditText dato;
    private static EditText tidspunkt;
    String datoProsess;
    Calendar kalender;
    Calendar klokke;

    MessageListener messageListener;

    public interface MessageListener {
        public void lagreMelding(String melding, String dato, String tidspunkt);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            messageListener = (MessageListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + ": Må implementere MessageListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, container, false);
        setHasOptionsMenu(true);

        ((MainActivity)getActivity()).changeToolbar(true);

        kalender = Calendar.getInstance(Locale.getDefault());
        klokke = Calendar.getInstance(Locale.getDefault());
        melding = view.findViewById(R.id.melding);
        dato = view.findViewById(R.id.datoFelt);
        tidspunkt = view.findViewById(R.id.tidsFelt);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                kalender.set(Calendar.YEAR, year);
                kalender.set(Calendar.MONTH, month);
                kalender.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat sdf = (SimpleDateFormat) new SimpleDateFormat().getDateInstance();
                SimpleDateFormat sdfProsess = new SimpleDateFormat("yyyy-MM-dd");
                dato.setText(sdf.format(kalender.getTime()));
                datoProsess = sdfProsess.format((kalender.getTime()));
            }
        };
        dato.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        new DatePickerDialog(getContext(), date, kalender.get(Calendar.YEAR),
                                kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH)).show();


                    }
                });


        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                klokke.set(Calendar.HOUR_OF_DAY, hour);
                klokke.set(Calendar.MINUTE, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                tidspunkt.setText(sdf.format(klokke.getTime()));
            }
        };
        tidspunkt.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        new TimePickerDialog(getContext(), time, klokke.get(Calendar.HOUR_OF_DAY),
                                klokke.get(Calendar.MINUTE), true).show();
                    }
                });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem addStudent = menu.findItem(R.id.addStudent);
        addStudent.setVisible(false);

        MenuItem create_message = menu.findItem(R.id.create_message);
        create_message.setVisible(false);

        MenuItem settings = menu.findItem(R.id.settings);
        settings.setVisible(false);

        MenuItem lagreStudent = menu.findItem(R.id.lagreStudent);
        lagreStudent.setVisible(false);

        MenuItem lagreMelding = menu.findItem(R.id.lagreMelding);
        lagreMelding.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lagreMelding) {
            messageListener.lagreMelding(
                    melding.getText().toString(),
                    datoProsess,
                    tidspunkt.getText().toString()
            );


        }
        return true;
    }

}
