package com.fullsail.android.unittestingstarter;

import com.fullsail.android.unittestingstarter.object.Person;
import com.fullsail.android.unittestingstarter.util.PersonConversionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PersonConversionUtilTest {

    private Person mPerson1;
    private Person mPerson2;
    private Person mPerson3;

    @Before
    public void setUp() {
        mPerson1 = new Person("John", "Doe", "555-0100", 25);
        mPerson2 = new Person("Jane", "Smith", "555-0101", 30);
        mPerson3 = new Person("Bob", "Johnson", "555-0102", 35);
    }

    @Test
    public void testGetPeopleJSONFromList_onePerson() throws JSONException {
        ArrayList<Person> people = new ArrayList<>();
        people.add(mPerson1);

        JSONArray jsonArray = PersonConversionUtil.getPeopleJSONFromList(people);

        assertNotNull(jsonArray);
        assertEquals(1, jsonArray.length());
    }

    @Test
    public void testGetPeopleJSONFromList_threePeople() throws JSONException {
        ArrayList<Person> people = new ArrayList<>();
        people.add(mPerson1);
        people.add(mPerson2);
        people.add(mPerson3);

        JSONArray jsonArray = PersonConversionUtil.getPeopleJSONFromList(people);

        assertNotNull(jsonArray);
        assertEquals(3, jsonArray.length());
    }

    @Test
    public void testGetPeopleListFromJSON_onePerson() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(mPerson1.getPersonAsJSON());

        ArrayList<Person> people = PersonConversionUtil.getPeopleListFromJSON(jsonArray);

        assertNotNull(people);
        assertEquals(1, people.size());
    }

    @Test
    public void testGetPeopleListFromJSON_threePeople() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(mPerson1.getPersonAsJSON());
        jsonArray.put(mPerson2.getPersonAsJSON());
        jsonArray.put(mPerson3.getPersonAsJSON());

        ArrayList<Person> people = PersonConversionUtil.getPeopleListFromJSON(jsonArray);

        assertNotNull(people);
        assertEquals(3, people.size());
    }
}