package com.fullsail.android.unittestingstarter.util;

import android.content.Context;

import com.fullsail.android.unittestingstarter.object.Person;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class PersonStorageUtil {

    private static final String FOLDER_NAME = "json";
    private static final String FILE_NAME = "people.json";

    private static File getStorageFile(Context context) {
        if (context == null) return null;
        File dir = context.getExternalFilesDir(FOLDER_NAME);
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, FILE_NAME);
    }

    public static void savePerson(Context _context, Person _person) {
        if (_context == null || _person == null) return;

        ArrayList<Person> people = loadPeople(_context);
        people.add(_person);
        savePeople(_context, people);
    }

    public static void savePeople(Context _context, ArrayList<Person> _people) {
        if (_context == null || _people == null) return;

        File file = getStorageFile(_context);
        if (file == null) return;

        JSONArray jsonArray = PersonConversionUtil.getPeopleJSONFromList(_people);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Person> loadPeople(Context _context) {
        ArrayList<Person> people = new ArrayList<>();
        if (_context == null) return people;

        File file = getStorageFile(_context);
        if (file == null || !file.exists()) return people;

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            String jsonString = new String(data, StandardCharsets.UTF_8);

            JSONArray jsonArray = new JSONArray(jsonString);
            people = PersonConversionUtil.getPeopleListFromJSON(jsonArray);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return people;
    }

    public static void deletePerson(Context _context, Person _person) {
        if (_context == null || _person == null) return;

        ArrayList<Person> people = loadPeople(_context);
        if (people.isEmpty()) return;

        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            if (p.getFirstName().equals(_person.getFirstName()) &&
                    p.getLastName().equals(_person.getLastName()) &&
                    p.getPhoneNumber().equals(_person.getPhoneNumber())) {
                people.remove(i);
                break;
            }
        }

        savePeople(_context, people);
    }

    public static void deletePersonFile(Context _context) {
        if (_context == null) return;

        File file = getStorageFile(_context);
        if (file != null && file.exists()) {
            file.delete();
        }
    }
}