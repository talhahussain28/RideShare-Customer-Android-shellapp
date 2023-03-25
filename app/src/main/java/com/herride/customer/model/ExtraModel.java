package com.herride.customer.model;

import com.herride.customer.model.map_poliline.Northeast;
import com.herride.customer.model.map_poliline.Southwest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//public class Bounds {
public class ExtraModel {

    @SerializedName("northeast")
    @Expose
    private Northeast northeast;
    @SerializedName("southwest")
    @Expose
    private Southwest southwest;

    public Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

}
