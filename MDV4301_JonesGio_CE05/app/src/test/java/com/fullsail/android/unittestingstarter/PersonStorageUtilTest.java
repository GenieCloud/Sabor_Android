package com.fullsail.android.unittestingstarter;

import com.fullsail.android.unittestingstarter.util.PersonStorageUtil;
import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.fullsail.android.unittestingstarter.object.Person;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.File;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class PersonStorageUtilTest {

    private Context context;
    private Person testPerson;

    @Before
    public void setUp() throws JSONException {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        JSONObject json = new JSONObject();
        json.put("firstName", "John");
        json.put("lastName", "Doe");
        json.put("phoneNumber", "4075550123");
        testPerson = new Person(json);

        PersonStorageUtil.deletePersonFile(context);
    }

    @After
    public void tearDown() {
        PersonStorageUtil.deletePersonFile(context);
    }

    @Test
    public void testSavePerson() {
        PersonStorageUtil.savePerson(context, testPerson);

        ArrayList<Person> people = PersonStorageUtil.loadPeople(context);
        assertEquals(1, people.size());
        assertEquals("John", people.get(0).getFirstName());
    }

    @Test
    public void testDeletePerson() {
        PersonStorageUtil.savePerson(context, testPerson);
        assertEquals(1, PersonStorageUtil.loadPeople(context).size());

        PersonStorageUtil.deletePerson(context, testPerson);
        assertEquals(0, PersonStorageUtil.loadPeople(context).size());
    }

    @Test
    public void testDeletePersonFile() {
        PersonStorageUtil.savePerson(context, testPerson);

        PersonStorageUtil.deletePersonFile(context);
        File externalStorage = context.getExternalFilesDir("json");
        File personFile = new File(externalStorage, "people.json");

        assertFalse(personFile.exists());
    }
}
