package com.kanon.tamarin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;

public class EnrollRequest implements Parcelable {

    @DocumentId
    private String documentId;

    private String name, contactPersonName, contactPersonMobile, email;

    public EnrollRequest(String name, String contactPersonName, String contactPersonMobile, String email) {
        this.name = name;
        this.contactPersonName = contactPersonName;
        this.contactPersonMobile = contactPersonMobile;
        this.email = email;
    }

    public EnrollRequest() {
    }

    protected EnrollRequest(Parcel in) {
        documentId = in.readString();
        name = in.readString();
        contactPersonName = in.readString();
        contactPersonMobile = in.readString();
        email = in.readString();
    }

    public static final Creator<EnrollRequest> CREATOR = new Creator<EnrollRequest>() {
        @Override
        public EnrollRequest createFromParcel(Parcel in) {
            return new EnrollRequest(in);
        }

        @Override
        public EnrollRequest[] newArray(int size) {
            return new EnrollRequest[size];
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonMobile() {
        return contactPersonMobile;
    }

    public void setContactPersonMobile(String contactPersonMobile) {
        this.contactPersonMobile = contactPersonMobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(documentId);
        parcel.writeString(name);
        parcel.writeString(contactPersonName);
        parcel.writeString(contactPersonMobile);
        parcel.writeString(email);
    }
}
