// Gio Jones
// MDV4301-O - C202607
// MapFragment.java

package com.fullsail.android.mdv4301_jonesgio_ce01;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 0x0100;
    private GoogleMap mGoogleMap;

    public MapFragment() {

    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tell the fragment framework that this screen has an action bar menu to display
        setHasOptionsMenu(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Triggers every time the user navigates back to the Map screen.
     * Refreshes and draws any new saved entries.
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshMapMarkers();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate form_menu resource file into the Action Bar
        inflater.inflate(R.menu.form_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Track clicks on the action bar items
        if (item.getItemId() == R.id.action_camera) {

            // Check if parent activity is MainActivity
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();

                // Establish standard backup defualt location fallback coordinates
                double lat = 28.5966;
                double lng = -81.3014;

                // If the map tracking blue dot layer is enabled, grab the live coordinates
                if (mGoogleMap != null && mGoogleMap.isMyLocationEnabled() && mGoogleMap.getMyLocation() != null) {
                    lat = mGoogleMap.getMyLocation().getLatitude();
                    lng = mGoogleMap.getMyLocation().getLongitude();
                }

                // Pass these dynamic coordinates straight into the fragment's factory constructor
                mainActivity.navigateToFragment(FormFragment.newInstance(lat, lng));
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Configure the custom pop-up window adapter layout
        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {

                return null;
            }

            @Nullable
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                // Inflate a custom pop-up bubble view layout framework
                View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                ImageView ivPreview = view.findViewById(R.id.macroImageWindowPreview);
                TextView tvNotes = view.findViewById(R.id.textViewWindowNotes);

                // Handle safety styling constraints for standard default map position marker
                if ("Default Location".equals(marker.getTitle())) {
                    ivPreview.setVisibility(View.GONE);
                    tvNotes.setText(marker.getTitle());
                    return view;
                }

                // Bind text field values safely
                tvNotes.setText(marker.getTitle());

                // Read out the image URI string stashed inside the snippet block element
                if (marker.getSnippet() != null) {
                    ivPreview.setVisibility(View.VISIBLE);
                    ivPreview.setImageURI(android.net.Uri.parse(marker.getSnippet()));
                }

                return view;
            }
        });

        // Default layout view zoom
        LatLng initialView = new LatLng(28.5966, -81.3014);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialView, 12.0f));

        // Initial paint of the markers
        refreshMapMarkers();

        enableMyLocation();
    }

    /**
     * Clears the map canvas and loops through the central collection to plot all items.
     */
    private void refreshMapMarkers() {
        if (mGoogleMap == null) return;

        // Clear existing markers to prevent stacking duplicates
        mGoogleMap.clear();

        // Standard backup position marker
        LatLng initialView = new LatLng(28.5966, -81.3014);
        mGoogleMap.addMarker(new MarkerOptions().position(initialView).title("Default Location"));

        // Pull saved items from MainActivity and map them out
        if (getActivity() instanceof MainActivity) {
            ArrayList<MapMarkerItem> markers = ((MainActivity) getActivity()).getSavedMarkers();

            for (MapMarkerItem item : markers) {
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(item.getCoordinates())
                        .title(item.getNotes()) // Puts notes as the title
                        .snippet(item.getImageUriString())); // Stashes image URI in the snippet
            }
        }
    }

    private void enableMyLocation() {
        if (mGoogleMap == null) return;

        // Check if permissions are already granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Blue dot and center on map button enabled
            mGoogleMap.setMyLocationEnabled(true);

        } else {
            // Request permissions at runtime
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mGoogleMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(requireContext(), "Location permission denied. Cannot show live location.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}