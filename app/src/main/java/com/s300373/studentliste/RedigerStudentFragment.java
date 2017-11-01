package com.s300373.studentliste;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by RudiAndre on 28.10.2017.
 */

public class RedigerStudentFragment extends Fragment {

    private static EditText fornavn;
    private static EditText etternavn;
    private static EditText telefon;
    long _ID;
    RedigerStudentListener redigerStudentListener;

    public interface RedigerStudentListener {
        public void oppdaterStudent(long id, String fornavn, String etternavn, String telefon);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            redigerStudentListener = (RedigerStudentFragment.RedigerStudentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + ": Må implementere redigerStudentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rediger_student_fragment, container, false);
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).changeToolbar(true);
        fornavn = view.findViewById(R.id.editFNavnText);
        etternavn = view.findViewById(R.id.editENavnText);
        telefon = view.findViewById(R.id.editTlfText);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            _ID = bundle.getLong("studentId", 0);
            fornavn.setText(bundle.getString("studentFornavn", null));
            etternavn.setText(bundle.getString("studentEtternavn", null));
            telefon.setText(bundle.getString("studentTlf", null));
        }

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
        lagreMelding.setVisible(false);

        MenuItem redigerStudent = menu.findItem(R.id.redigerStudent);
        redigerStudent.setVisible(true);
    }

   /* @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.addStudent); // You can change the state of the menu item here if you call getActivity().supportInvalidateOptionsMenu(); somewhere in your code
        getActivity().supportInvalidateOptionsMenu();
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.redigerStudent) {
            redigerStudentListener.oppdaterStudent(_ID, fornavn.getText().toString(), etternavn.getText().toString(), telefon.getText().toString());
        }
        return true;
    }


}