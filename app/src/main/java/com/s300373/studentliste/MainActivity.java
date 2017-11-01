package com.s300373.studentliste;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by RudiAndre on 22.10.2017.
 */

public class MainActivity extends AppCompatActivity implements AddStudentFragment.AddStudentListener,
        MeldingFragment.MessageListener, RedigerStudentFragment.RedigerStudentListener{

    private static final String PROVIDER = "com.s300373.studentliste.StudentProvider";
    private static final String TABLE_STUDENTER = "Studenter";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/" + TABLE_STUDENTER);

    Toolbar toolbar;
    ListView studentListView;
    DBHandler db;
    CustomAdapter studentAdapter;
    SharedPreferences preferenceScreen;
    private List<Student> studenter;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
            changeToolbar(false);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, 3);
        }
/*
        // Contentprovider insert test
        ContentValues values = new ContentValues();
        values.put("Fornavn", "Jonny");
        values.put("Etternavn", "Bravo");
        values.put("Telefon", "19283747");
        getContentResolver().insert(CONTENT_URI, values);
*/
/*
        // Contentprovider delete test (sletter alle)
        getContentResolver().delete(CONTENT_URI, null, null);
*/


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                changeToolbar(false);
            }
        });


        db = new DBHandler(this);
        studenter = db.hentAlleStudenter();


        studentAdapter = new CustomAdapter(this, R.layout.list_row, studenter);
        studentListView = (ListView) findViewById(R.id.studentListe);
        studentListView.setAdapter(studentAdapter);

        studentListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        studentListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {

                final int checkedCount = studentListView.getCheckedItemCount();

                // Set the CAB title according to total checked items
                actionMode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                studentAdapter.toggleSelection(position);
                actionMode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                SparseBooleanArray selected = studentAdapter.getSelectedIds();
                studentAdapter.clearSparse();
                actionMode.getMenuInflater().inflate(R.menu.menu_single_selected, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                final int checkedCount = studentListView.getCheckedItemCount();
                if(checkedCount > 1) {
                    MenuItem item = menu.findItem(R.id.editStudent);
                    item.setVisible(false);
                    return true;
                } else {
                    MenuItem item = menu.findItem(R.id.editStudent);
                    item.setVisible(true);
                    return true;
                }
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        SparseBooleanArray selected = studentAdapter.getSelectedIds();
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Student selectedListItem = (Student) studentAdapter.getItem(selected.keyAt(i));
                                studentAdapter.remove(selectedListItem);
                                db.slettStudent(String.valueOf(selectedListItem._ID));
                            }
                        }
                        actionMode.finish();
                        return true;
                    case R.id.editStudent:
                        selected = studentAdapter.getSelectedIds();
                        actionMode.finish();
                        Student selectedListItem = (Student) studentAdapter.getItem(selected.keyAt(0));
                        openFragmentHandler(new RedigerStudentFragment(), selectedListItem);

                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

    }

    public void changeToolbar(Boolean b){
        getSupportActionBar().setDisplayHomeAsUpEnabled(b);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 3: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                } else {
                    finish();
                }
                finish();
            }
        }
    }

    public void startSingelService(View v) {
        Intent intent = new Intent();
        intent.setAction("com.s300373.studentliste.singelbroadcast");
        sendBroadcast(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem addStudent = menu.findItem(R.id.addStudent);
        addStudent.setVisible(true);

        MenuItem create_message = menu.findItem(R.id.create_message);
        create_message.setVisible(true);

        MenuItem settings = menu.findItem(R.id.settings);
        settings.setVisible(true);

        MenuItem lagreStudent = menu.findItem(R.id.lagreStudent);
        lagreStudent.setVisible(false);

        MenuItem lagreMelding = menu.findItem(R.id.lagreMelding);
        lagreMelding.setVisible(false);

        MenuItem redigerStudent = menu.findItem(R.id.redigerStudent);
        redigerStudent.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /*case R.id.lagreStudent:
                AddStudentFragment.AddStudentListener
                break;*/
            case R.id.create_message:

                openFragmentHandler(new MeldingFragment(), null);
                break;

            case R.id.addStudent:
                openFragmentHandler(new AddStudentFragment(), null);
                break;

            case R.id.settings:

                Intent intent = new Intent(this, SetPreferenceActivity.class);
                startActivity(intent);


            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void openFragmentHandler(Fragment fragment, Student student) {
        if(student != null) {
            Bundle bundle = new Bundle();
            bundle.putLong("studentId", student._ID);
            bundle.putString("studentFornavn", student.fornavn);
            bundle.putString("studentEtternavn", student.etternavn);
            bundle.putString("studentTlf", student.telefon);
            fragment.setArguments(bundle);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
    }

    @Override
    public void leggTilStudent(String fornavn, String etternavn, String telefon) {
        Student student = new Student(fornavn, etternavn, telefon);
        db.leggTilStudent(student);
        studenter.clear();
        studenter = db.hentAlleStudenter();
        studentAdapter.refresh(studenter);
    }

    @Override
    public void oppdaterStudent(long id, String fornavn, String etternavn, String telefon) {
        Student student = new Student(fornavn, etternavn, telefon, id);
        db.oppdaterStudent(student);
        studenter.clear();
        studenter = db.hentAlleStudenter();
        studentAdapter.refresh(studenter);
        getSupportFragmentManager().popBackStack();
        dismissKeyboard(this);
    }


    @Override
    public void lagreMelding(String melding, String dato, String tidspunkt) {
        SimpleDateFormat tidsFormat = (SimpleDateFormat) new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean meldingSwitch = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("meldingSwitch", false);

        StringBuilder sb = new StringBuilder();
        sb.append(dato);
        sb.append(" ");
        sb.append(tidspunkt);
        System.out.println(tidsFormat.toString());
        Melding dbMelding = new Melding(sb.toString(), melding);
        db.leggTilMelding(dbMelding);
        getSupportFragmentManager().popBackStack();
        dismissKeyboard(this);
        if (meldingSwitch) {
            startSingelService(this.getCurrentFocus());
        }
    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }


}
