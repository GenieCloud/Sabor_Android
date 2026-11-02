package com.fullsail.android.unittestingstarter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager; // or android.preference.PreferenceManager

import com.fullsail.android.unittestingstarter.util.PreferenceUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PreferenceUtilTest {

    @Mock
    Context mockContext;

    @Mock
    SharedPreferences mockPreferences;

    private MockedStatic<PreferenceManager> mockedPreferenceManager;

    @Before
    public void setUp() {

        mockedPreferenceManager = Mockito.mockStatic(PreferenceManager.class);
        mockedPreferenceManager
                .when(() -> PreferenceManager.getDefaultSharedPreferences(any(Context.class)))
                .thenReturn(mockPreferences);
    }

    @After
    public void tearDown() {

        if (mockedPreferenceManager != null) {
            mockedPreferenceManager.close();
        }
    }

    @Test
    public void getNameFormat_Value0_ReturnsFirstLast() {
        when(mockPreferences.getInt(anyString(), anyInt())).thenReturn(0);

        int result = PreferenceUtil.getNameFormat(mockContext);

        assertEquals(0, result);
    }

    @Test
    public void getNameFormat_Value1_ReturnsLastFirst() {
        when(mockPreferences.getInt(anyString(), anyInt())).thenReturn(1);

        int result = PreferenceUtil.getNameFormat(mockContext);

        assertEquals(1, result);
    }

    @Test
    public void getPhoneFormat_Value0_ReturnsAllDashes() {
        when(mockPreferences.getInt(anyString(), anyInt())).thenReturn(0);

        int result = PreferenceUtil.getPhoneFormat(mockContext);

        assertEquals(0, result);
    }

    @Test
    public void getPhoneFormat_Value1_ReturnsWithParens() {
        when(mockPreferences.getInt(anyString(), anyInt())).thenReturn(1);

        int result = PreferenceUtil.getPhoneFormat(mockContext);

        assertEquals(1, result);
    }

    @Test
    public void getPhoneFormat_Value2_ReturnsWithSpaces() {
        when(mockPreferences.getInt(anyString(), anyInt())).thenReturn(2);

        int result = PreferenceUtil.getPhoneFormat(mockContext);

        assertEquals(2, result);
    }
}