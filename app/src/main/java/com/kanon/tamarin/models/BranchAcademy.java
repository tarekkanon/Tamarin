package com.kanon.tamarin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

public class BranchAcademy implements Parcelable {

    @DocumentId
    private String documentId;

    private String  address, mobile, locationMap, longitude ,latitude;

    private DocumentReference academyRef, locationRef;

    @Exclude
    private Academies academy;
    @Exclude
    private Locations location;

    public BranchAcademy() {
    }

    public BranchAcademy(String address, String mobile, String locationMap, String longitude, String latitude, DocumentReference academyRef, DocumentReference locationRef , Academies academy, Locations location) {
        this.address = address;
        this.mobile = mobile;
        this.locationMap = locationMap;
        this.longitude = longitude;
        this.latitude = latitude;
        this.academyRef = academyRef;
        this.locationRef = locationRef;
        this.academy = academy;
        this.location = location;
    }

    protected BranchAcademy(Parcel in) {
        documentId = in.readString();
        address = in.readString();
        mobile = in.readString();
        locationMap = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        academy = in.readParcelable(Academies.class.getClassLoader());
        location = in.readParcelable(Locations.class.getClassLoader());
    }

    public static final Creator<BranchAcademy> CREATOR = new Creator<BranchAcademy>() {
        @Override
        public BranchAcademy createFromParcel(Parcel in) {
            return new BranchAcademy(in);
        }

        @Override
        public BranchAcademy[] newArray(int size) {
            return new BranchAcademy[size];
        }
    };

    public DocumentReference getAcademyRef() {
        return academyRef;
    }

    public void setAcademyRef(DocumentReference academyRef) {
        this.academyRef = academyRef;
    }

    public DocumentReference getLocationRef() {
        return locationRef;
    }

    public void setLocationRef(DocumentReference locationRef) {
        this.locationRef = locationRef;
    }

    @Exclude
    public Academies getAcademy() {
        return academy;
    }

    @Exclude
    public void setAcademy(Academies academy) {
        this.academy = academy;
    }

    @Exclude
    public Locations getLocation() {
        return location;
    }

    @Exclude
    public void setLocation(Locations location) {
        this.location = location;
    }

    @DocumentId
    public String getDocumentId() {
        return documentId;
    }

    @DocumentId
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(String locationMap) {
        this.locationMap = locationMap;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(documentId);
        parcel.writeString(address);
        parcel.writeString(mobile);
        parcel.writeString(locationMap);
        parcel.writeString(longitude);
        parcel.writeString(latitude);
        parcel.writeParcelable(academy, i);
        parcel.writeParcelable(location, i);
    }
}
