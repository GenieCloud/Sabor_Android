package com.fullsail.android.unittestingstarter.object;

import org.json.JSONException;
import org.json.JSONObject;

public class Person {

    private String mFirstName;
    private String mLastName;
    private String mPhoneNumber;
    private int mAge;

    public Person() {
        mFirstName = "";
        mLastName = "";
        mPhoneNumber = "";
        mAge = 0;
    }

    public Person(String _firstName, String _lastName, String _phoneNumber, int _age) {
        mFirstName = _firstName;
        mLastName = _lastName;
        mPhoneNumber = _phoneNumber;
        mAge = _age;
    }

    public Person(JSONObject _json) {
        if (_json != null) {
            mFirstName = _json.optString("firstName", "");
            mLastName = _json.optString("lastName", "");
            mPhoneNumber = _json.optString("phoneNumber", "");
            mAge = _json.optInt("age", 0);
        }
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public int getAge() {
        return mAge;
    }

    public JSONObject getPersonAsJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("firstName", mFirstName);
            json.put("lastName", mLastName);
            json.put("phoneNumber", mPhoneNumber);
            json.put("age", mAge);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}