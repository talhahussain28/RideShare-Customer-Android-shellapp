package com.carty.riderapp.model.map_poliline.events;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Chirag Khandla on 9/9/18.
 */
public class BeginJourneyEvent {

    private String event = "BEGIN_JOURNEY";
    private LatLng beginLatLng;

    public LatLng getBeginLatLng() {
        return beginLatLng;
    }

    public void setBeginLatLng(LatLng beginLatLng) {
        this.beginLatLng = beginLatLng;
    }

}
