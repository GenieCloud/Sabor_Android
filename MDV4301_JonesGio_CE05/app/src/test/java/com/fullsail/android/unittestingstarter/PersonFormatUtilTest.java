package com.fullsail.android.unittestingstarter;

import com.fullsail.android.unittestingstarter.util.PersonFormatUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PersonFormatUtilTest {


    @Test
    public void testFormatName_firstLast() {
        assertEquals("John Doe", PersonFormatUtil.formatName(PersonFormatUtil.FORMAT_FIRST_LAST, "John", "Doe"));
        assertEquals("David Smith", PersonFormatUtil.formatName(PersonFormatUtil.FORMAT_FIRST_LAST, "David", "Smith"));
    }

    @Test
    public void testFormatName_lastFirst() {
        assertEquals("Doe, John", PersonFormatUtil.formatName(PersonFormatUtil.FORMAT_LAST_FIRST, "John", "Doe"));
        assertEquals("Smith, David", PersonFormatUtil.formatName(PersonFormatUtil.FORMAT_LAST_FIRST, "David", "Smith"));
    }

    @Test
    public void testFormatPhoneNumber_allDashes() {
        assertEquals("407-555-0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_ALL_DASHES, "4075550123"));
        assertEquals("407-555-0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_ALL_DASHES, "407 555 0123"));
        assertEquals("407-555-0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_ALL_DASHES, "(407)555-0123"));
        assertEquals("407-555-0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_ALL_DASHES, "407-555-0123"));
    }

    @Test
    public void testFormatPhoneNumber_withParens() {
        assertEquals("(407)555-0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_WITH_PARENS, "4075550123"));
        assertEquals("(407)555-0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_WITH_PARENS, "407 555 0123"));
        assertEquals("(407)555-0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_WITH_PARENS, "(407)555-0123"));
        assertEquals("(407)555-0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_WITH_PARENS, "407-555-0123"));
    }

    @Test
    public void testFormatPhoneNumber_withSpaces() {
        assertEquals("407 555 0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_WITH_SPACES, "4075550123"));
        assertEquals("407 555 0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_WITH_SPACES, "407 555 0123"));
        assertEquals("407 555 0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_WITH_SPACES, "(407)555-0123"));
        assertEquals("407 555 0123", PersonFormatUtil.formatPhoneNumber(PersonFormatUtil.FORMAT_WITH_SPACES, "407-555-0123"));
    }

    @Test
    public void testUnformatPhoneNumber() {
        assertEquals("4075550123", PersonFormatUtil.unformatPhoneNumber("4075550123"));
        assertEquals("4075550123", PersonFormatUtil.unformatPhoneNumber("407 555 0123"));
        assertEquals("4075550123", PersonFormatUtil.unformatPhoneNumber("(407)555-0123"));
        assertEquals("4075550123", PersonFormatUtil.unformatPhoneNumber("407-555-0123"));
    }

    @Test
    public void testIsPhoneNumberValid_valid() {
        assertTrue(PersonFormatUtil.isPhoneNumberValid("4075550123"));
        assertTrue(PersonFormatUtil.isPhoneNumberValid("407 555 0123"));
        assertTrue(PersonFormatUtil.isPhoneNumberValid("(407)555-0123"));
        assertTrue(PersonFormatUtil.isPhoneNumberValid("407-555-0123"));
    }

    @Test
    public void testIsPhoneNumberValid_invalid() {
        assertFalse(PersonFormatUtil.isPhoneNumberValid("1-407-555-0123"));
        assertFalse(PersonFormatUtil.isPhoneNumberValid("40755501234"));
        assertFalse(PersonFormatUtil.isPhoneNumberValid("407555012"));
    }
}