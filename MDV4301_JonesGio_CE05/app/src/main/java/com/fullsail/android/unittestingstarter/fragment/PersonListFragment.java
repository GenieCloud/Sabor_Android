package com.fullsail.android.unittestingstarter.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fullsail.android.unittestingstarter.R;
import com.fullsail.android.unittestingstarter.object.Person;
import com.fullsail.android.unittestingstarter.util.PersonFormatUtil;
import com.fullsail.android.unittestingstarter.util.PersonStorageUtil;
import com.fullsail.android.unittestingstarter.util.PreferenceUtil;

import java.util.ArrayList;

public class PersonListFragment extends ListFragment {
    public static final String TAG = "PersonListFragment.TAG";

    public static PersonListFragment newInstance() {
        return new PersonListFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refresh();
        setEmptyText("No People Saved");
    }

    public void refresh() {
        ArrayList<Person> people = PersonStorageUtil.loadPeople(getActivity());
        setListAdapter(new PersonAdapter(getActivity(), people));
    }

    private class PersonAdapter extends BaseAdapter {

        private ArrayList<Person> mPeople;
        private Context mContext;

        public PersonAdapter(Context _context, ArrayList<Person> _people) {
            mPeople = _people;
            mContext = _context;
        }

        @Override
        public int getCount() {
            return mPeople.size();
        }

        @Override
        public Person getItem(int i) {
            return mPeople.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null);
            }

            Person person = getItem(i);

            @PersonFormatUtil.NameFormat
            int nameFormat = PreferenceUtil.getNameFormat(mContext);

            @PersonFormatUtil.PhoneFormat
            int numberFormat = PreferenceUtil.getPhoneFormat(mContext);

            TextView tv = (TextView)view.findViewById(R.id.text_name);
            tv.setText(PersonFormatUtil.formatName(nameFormat,
                    person.getFirstName(), person.getLastName()));

            tv = (TextView)view.findViewById(R.id.text_phone);
            tv.setText(PersonFormatUtil.formatPhoneNumber(numberFormat,
                    person.getPhoneNumber()));

            tv = (TextView)view.findViewById(R.id.text_age);
            tv.setText("" + person.getAge());

            return view;
        }
    }
}
