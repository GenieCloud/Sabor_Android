// Gio Jones
// MDV4301-O C202607
// WeatherData.java

package com.fullsail.android.mdv4301_jonesgio_ce03;

import java.io.Serializable;

public class WeatherData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String cityName;
    private final String conditionText;
    private final double currentTemp;
    private final String timestamp;

    // Arrays to display the 3-day forecast activity
    private final String[] forecastConditions;
    private final double[] forecastHighs;
    private final double[] forecastLows;

    public WeatherData(String cityName, String conditionText, double currentTemp, String timestamp,
                       String[] forecastConditions, double[] forecastHighs, double[] forecastLows) {
        this.cityName = cityName;
        this.conditionText = conditionText;
        this.currentTemp = currentTemp;
        this.timestamp = timestamp;
        this.forecastConditions = forecastConditions;
        this.forecastHighs = forecastHighs;
        this.forecastLows = forecastLows;
    }

    // Getters
    public String getCityName() { return cityName; }
    public String getConditionText() { return conditionText; }
    public double getCurrentTemp() { return currentTemp; }
    public String getTimestamp() { return timestamp; }
    public String[] getForecastConditions() { return forecastConditions; }
    public double[] getForecastHighs() { return forecastHighs; }
    public double[] getForecastLows() { return forecastLows; }
}