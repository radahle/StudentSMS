package com.s300373.studentliste;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by RudiAndre on 18.10.2017.
 */

public class AddStudentFragment extends Fragment {

    private static EditText fornavn;
    private static EditText etternavn;
    private static EditText telefon;

    AddStudentListener addStudentListener;

    public interface AddStudentListener {
        public void leggTilStudent(String fornavn, String etternavn, String telefon);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            addStudentListener = (AddStudentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + ": Må implementere AddStudentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_student_fragment, container, false);
        setHasOptionsMenu(true);

        ((MainActivity)getActivity()).changeToolbar(true);

        fornavn = view.findViewById(R.id.fNavnText);
        etternavn = view.findViewById(R.id.eNavnText);
        telefon = view.findViewById(R.id.tlfText);

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
        lagreStudent.setVisible(true);

        MenuItem lagreMelding = menu.findItem(R.id.lagreMelding);
        lagreMelding.setVisible(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lagreStudent) {
            addStudentListener.leggTilStudent(
                    fornavn.getText().toString(),
                    etternavn.getText().toString(),
                    telefon.getText().toString()
            );

            fornavn.setText("");
            etternavn.setText("");
            telefon.setText("");
        }
        return true;
    }



}
