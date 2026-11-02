package com.fullsail.android.unittestingstarter.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fullsail.android.unittestingstarter.R;
import com.fullsail.android.unittestingstarter.object.Person;
import com.fullsail.android.unittestingstarter.util.PersonFormatUtil;

public class FormFragment extends Fragment {
    public static final String TAG = "FormFragment.TAG";

    public static FormFragment newInstance() {
        return new FormFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_fragment, container, false);
    }

    public Person getPersonFromLayout() {

        View view = getView();
        if(view == null) {
            return null;
        }


        EditText editFirst = (EditText)view.findViewById(R.id.edit_first_name);
        EditText editLast = (EditText)view.findViewById(R.id.edit_last_name);
        EditText editAge = (EditText)view.findViewById(R.id.edit_age);
        EditText editPhone = (EditText)view.findViewById(R.id.edit_phone_number);

        String firstName = editFirst.getText().toString();
        String lastName = editLast.getText().toString();
        String ageString = editAge.getText().toString();
        String phone = editPhone.getText().toString();
        phone = PersonFormatUtil.unformatPhoneNumber(phone);

        int age = -1;
        try { age = Integer.parseInt(ageString); }
        catch(Exception e) { }

        if(firstName.trim().length() == 0 ||
                lastName.trim().length() == 0 ||
                !PersonFormatUtil.isPhoneNumberValid(phone) ||
                age == -1) {
            return null;
        }

        return new Person(firstName, lastName, phone, age);
    }
}
