package com.pennapps2019.application;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocationRecorder {

    private Map<String, Integer> record;

    public LocationRecorder() {
         record = new HashMap<>();
    }

    public void recordAt(Location location) {
        String key = locationToString(location);

        if (record.containsKey(key)) {
            record.put(key, record.get(key) + 1);
        }
        else {
            record.put(key, 1);
        }
    }

    public String dump() {
        String dump = "";

        Set<String> keys = record.keySet();
        for (String key : keys) {
            dump += "2019-09-08 08:00:00," + record.get(key) + "," + key + ",Sunday\n";
        }

        return dump;
    }

    private String locationToString(Location location) {
        return location.getLatitude() + "," + location.getLongitude();
    }

}
