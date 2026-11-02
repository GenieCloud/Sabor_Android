// Gio Jones
// MDV4301-O C202607
// ForecastFragment.java

package com.fullsail.android.mdv4301_jonesgio_ce03;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ForecastFragment extends ListFragment {

    private WeatherData mCachedData;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Read the cache data file to retrieve the 3-day forecast arrays
        mCachedData = loadDataFromFile();

        if (mCachedData != null) {
            // Set the Activity Title to match the currently selected city automatically
            if (getActivity() != null) {
                getActivity().setTitle(mCachedData.getCityName() + " 3-Day Forecast");
            }

            // Set up the custom list adapter to display the 3 forecast days
            setListAdapter(new ForecastAdapter(getActivity(), mCachedData));
        }
    }

    private WeatherData loadDataFromFile() {
        try {
            FileInputStream fis = getActivity().openFileInput(WeatherDownloadService.FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            WeatherData data = (WeatherData) ois.readObject();
            ois.close();
            fis.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Custom inner adapter to display forecast layout data
    private static class ForecastAdapter extends BaseAdapter {
        private final Context context;
        private final WeatherData data;

        public ForecastAdapter(Context context, WeatherData data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.getForecastConditions().length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.forecast_row, parent, false);
            }

            ImageView iconView = convertView.findViewById(R.id.forecast_icon);
            TextView conditionText = convertView.findViewById(R.id.forecast_text_condition);
            TextView highText = convertView.findViewById(R.id.forecast_text_high);
            TextView lowText = convertView.findViewById(R.id.forecast_text_low);

            // Populate text elements using WeatherData parallel array indices
            conditionText.setText(data.getForecastConditions()[position]);
            highText.setText(String.format(java.util.Locale.US, "Hi: %.0f°F", data.getForecastHighs()[position]));
            lowText.setText(String.format(java.util.Locale.US, "Lo: %.0f°F", data.getForecastLows()[position]));

            // Default placeholder system graphic for the weather icon
            iconView.setImageResource(android.R.drawable.ic_menu_report_image);

            return convertView;
        }
    }
}