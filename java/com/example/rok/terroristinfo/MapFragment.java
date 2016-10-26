package com.example.rok.terroristinfo;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;


public class MapFragment extends Fragment {
    private SharedPreferencesInit spi;
    private HashMap<Integer, Marker> hash = new HashMap<>();
    private int textViewID = -1;
    private GoogleMap googleMap;
    MapView mView;

    public static MapFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("ARG_PAGE", page);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spi = new SharedPreferencesInit(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_view, container, false);

        mView = (MapView) rootView.findViewById(R.id.mapView);
        mView.onCreate(savedInstanceState);

        mView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                onMarkerClick(mMap);
                newInfoWindow(mMap);

                setMarkers(mMap);

                changeType(spi.getPrefsString("listMapType", "normal"));

            }
        });

        return rootView;
    }

    private void setMarkers(GoogleMap mMap) {

        Data[] data = DataHolder.getData();

        if (data == null) {
            return;
        }

        //get event types from sharedpreferences
        Set<String> selections = spi.getEventTypeSet();

        Date calendarCurrentDate = calDate(new Date(), daysBeforeX());

        for (int i = 0; i < data.length; i++) {

            Date eventDate = data[i].getDate();

            Date calendarEventDate = calDate(eventDate, 0);

            /*if (data[i].getNotify()) {
                setNotification(data[i].getLocation() + " " + data[i].getStringDate(date), data[i].getBriefSummary());
            }*/


            if (selections != null && selections.contains(data[i].getEventType()) && calendarEventDate.compareTo(calendarCurrentDate) >= 0) {
                Marker marker = setMarker(
                        mMap,
                        data[i].getLocation(),
                        new LatLng(data[i].getLat(), data[i].getLng()),
                        data[i].getIconFromString(data[i].getIcon()),
                        data[i].getStringDate(eventDate) + ": " + '\n' + '\n' + data[i].getBriefSummary());
                hash.put(i, marker);
            }
        }
    }

    private Marker setMarker(GoogleMap mMap, String locality, LatLng lng, int icon, String snippet) {

        return mMap.addMarker(new MarkerOptions()
                .title(locality)
                .position(lng)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
    }


    private void onMarkerClick(final GoogleMap mMap) {
        if (!checkReady()) {
            return;
        }
        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {

                    public boolean onMarkerClick(Marker marker) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 8));
                        marker.showInfoWindow();
                        return true;
                    }
                });
    }

    private void zoomOnTextViewClick(final GoogleMap mMap) {
        textViewID = spi.getTextViewID();

        if (textViewID != -1) {
            Marker mark = hash.get(textViewID);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mark.getPosition(), 8));
            mark.showInfoWindow();
        }

        //if not used, on app restart, map will be zoomed on last used icon
        spi.setTextViewID(-1);
    }

    private void newInfoWindow(final GoogleMap mMap)    {
        if(!checkReady()) {
            return;
        }


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.info_window_view, null);
                TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                TextView snippet    = (TextView) v.findViewById(R.id.snippet);
                tvLocality.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());

                return v;
            }

        });

        //todo, open textview with appropriate ID
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.sliding_tabs);
                tabLayout.getTabAt(1).select();
            }
        });
    }


    public void changeType(String type) {

        if(type.equals("normal")) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (type.equals("satellite")) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
    }

    private void setNotification(String title, String briefSummary) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_info)
                .setContentTitle(title)
                .setContentText(briefSummary);
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, builder.build());

    }

    private int daysBeforeX() {

        String age = spi.getPrefsString("listEventAge", "oneMonth");

        switch (age) {
            case "oneDay":
                return 1;
            case "twoDays":
                return 2;
            case "threeDays":
                return 3;
            case "fourDays":
                return 4;
            case "oneWeek":
                return 7;
            case "twoWeeks":
                return 14;
            case "oneMonth":
                return 31;
            case "twoMonths":
                return 62;
            case "threeMonths":
                return 93;
            case "sixMonths":
                return 186;
            case "oneYear":
                return 365;
            case "twoYears":
                return 730;
            case "unlimited":
                return 999999;
            default:
                return 31;
        }
    }

    private Date calDate(Date date, int age) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, - age);
        return cal.getTime();
    }


    //just don't touch next two methods, and it is going to be just fine.
    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mView.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        mView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
                prefs.registerOnSharedPreferenceChangeListener(listener);

               zoomOnTextViewClick(mMap);

            }
        });
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            // listener implementation
            if (key.equals("listMapType")) {
                String type = prefs.getString("listMapType", "normal");
                changeType(type);
            }
        }
    };


    private boolean checkReady() {
        if (googleMap == null) {
            Toast.makeText(getActivity(), R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mView.onPause();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView.onDestroy();
    }
}
