package com.kanon.tamarin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;

public class Locations implements Parcelable {

    @DocumentId
    private String documentId;

    private String locationName;
    private Integer sortOrder;

    public Locations() {
    }

    public Locations(String locationName, Integer sortOrder) {
        this.locationName = locationName;
        this.sortOrder = sortOrder;
    }

    protected Locations(Parcel in) {
        documentId = in.readString();
        locationName = in.readString();
        if (in.readByte() == 0) {
            sortOrder = null;
        } else {
            sortOrder = in.readInt();
        }
    }

    public static final Creator<Locations> CREATOR = new Creator<Locations>() {
        @Override
        public Locations createFromParcel(Parcel in) {
            return new Locations(in);
        }

        @Override
        public Locations[] newArray(int size) {
            return new Locations[size];
        }
    };

    @DocumentId
    public String getDocumentId() {
        return documentId;
    }

    @DocumentId
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(documentId);
        parcel.writeString(locationName);
        if (sortOrder == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(sortOrder);
        }
    }
}
