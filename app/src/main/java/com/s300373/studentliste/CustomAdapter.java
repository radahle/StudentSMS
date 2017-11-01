package com.s300373.studentliste;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ConfigurationHelper;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RudiAndre on 15.10.2017.
 */

public class CustomAdapter extends ArrayAdapter {

    List studenter;
    private SparseBooleanArray selectedListItemsIds;

    public CustomAdapter(@NonNull Context context, int resourceId, List<Student> studenter) {
        super(context, R.layout.list_row, studenter);
        selectedListItemsIds = new SparseBooleanArray();
        this.studenter = studenter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater studentInflater = LayoutInflater.from(getContext());
        View customView = studentInflater.inflate(R.layout.list_row, parent, false);

        Student student = (Student) getItem(position);

        TextView studentName = customView.findViewById(R.id.studentName);
        TextView studentPhone = customView.findViewById(R.id.studentPhone);
        ImageView studentImage = customView.findViewById(R.id.studentImage);

        studentName.setText(student.getFornavn() + " " + student.getEtternavn());
        studentPhone.setText(student.getTelefon());
        studentImage.setImageResource(R.drawable.ic_person_black_48dp);

        return customView;
    }

    public void refresh(List<Student> studentList) {
        studenter.clear();
        studenter.addAll(studentList);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedListItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            selectedListItemsIds.put(position, value);
        else
            selectedListItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedListItemsIds;
    }

    public void clearSparse() {
        selectedListItemsIds.clear();
    }
}
