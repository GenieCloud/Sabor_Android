package com.fullsail.android.unittestingstarter.util;

import com.fullsail.android.unittestingstarter.object.Person;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PersonConversionUtil {

    public static JSONArray getPeopleJSONFromList(ArrayList<Person> _people) {
        JSONArray jsonArray = new JSONArray();
        if (_people == null) return jsonArray;

        for (Person person : _people) {
            if (person != null) {

                JSONObject json = person.getPersonAsJSON();
                jsonArray.put(json);
            }
        }

        return jsonArray;
    }

    public static ArrayList<Person> getPeopleListFromJSON(JSONArray _jsonArray) {
        ArrayList<Person> people = new ArrayList<>();
        if (_jsonArray == null) return people;

        for (int i = 0; i < _jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = _jsonArray.getJSONObject(i);
                if (jsonObject != null) {
                    // Pass the JSONObject directly into Person's constructor
                    Person person = new Person(jsonObject);
                    people.add(person);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return people;
    }
}