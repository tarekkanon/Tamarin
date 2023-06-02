package com.kanon.tamarin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

public class AcademyCategoryBranch implements Parcelable {

    @DocumentId
    private String documentId;

    private DocumentReference academyRef, categoryRef, locationRef, branchRef;

    private Integer ratePerSession, sessions;


    private Academies academy;
    @Exclude
    private Categories category;
    @Exclude
    private Locations location;
    @Exclude
    private BranchAcademy branch;

    public AcademyCategoryBranch() {
    }

    public AcademyCategoryBranch(DocumentReference academyRef,DocumentReference branchRef, DocumentReference categoryRef, DocumentReference locationRef , Integer ratePerSession, Integer sessions, Academies academy, Categories category, Locations location, BranchAcademy branch) {
        this.academyRef = academyRef;
        this.categoryRef = categoryRef;
        this.locationRef = locationRef;
        this.ratePerSession = ratePerSession;
        this.academy = academy;
        this.category = category;
        this.location = location;
        this.branch = branch;
        this.branchRef = branchRef;
        this.sessions = sessions;
    }

    protected AcademyCategoryBranch(Parcel in) {
        documentId = in.readString();
        if (in.readByte() == 0) {
            ratePerSession = null;
        } else {
            ratePerSession = in.readInt();
        }
        if (in.readByte() == 0) {
            sessions = null;
        } else {
            sessions = in.readInt();
        }
        academy = in.readParcelable(Academies.class.getClassLoader());
        category = in.readParcelable(Categories.class.getClassLoader());
        location = in.readParcelable(Locations.class.getClassLoader());
        branch = in.readParcelable(Locations.class.getClassLoader());
    }

    public static final Creator<AcademyCategoryBranch> CREATOR = new Creator<AcademyCategoryBranch>() {
        @Override
        public AcademyCategoryBranch createFromParcel(Parcel in) {
            return new AcademyCategoryBranch(in);
        }

        @Override
        public AcademyCategoryBranch[] newArray(int size) {
            return new AcademyCategoryBranch[size];
        }
    };

    public DocumentReference getAcademyRef() {
        return academyRef;
    }

    public void setAcademyRef(DocumentReference academyRef) {
        this.academyRef = academyRef;
    }

    public DocumentReference getCategoryRef() {
        return categoryRef;
    }

    public void setCategoryRef(DocumentReference categoryRef) {
        this.categoryRef = categoryRef;
    }

    public DocumentReference getLocationRef() {
        return locationRef;
    }

    public void setLocationRef(DocumentReference locationRef) {
        this.locationRef = locationRef;
    }

    public DocumentReference getBranchRef() {
        return branchRef;
    }

    public void setBranchRef(DocumentReference branchRef) {
        this.branchRef = branchRef;
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
    public Categories getCategory() {
        return category;
    }

    @Exclude
    public void setCategory(Categories category) {
        this.category = category;
    }

    @Exclude
    public Locations getLocation() {
        return location;
    }

    @Exclude
    public void setLocation(Locations location) {
        this.location = location;
    }

    @Exclude
    public BranchAcademy getBranch() {
        return branch;
    }

    @Exclude
    public void setBranch(BranchAcademy branch) {
        this.branch = branch;
    }

    @DocumentId
    public String getDocumentId() {
        return documentId;
    }
    @DocumentId
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Integer getRatePerSession() {
        return ratePerSession;
    }

    public void setRatePerSession(Integer ratePerSession) {
        this.ratePerSession = ratePerSession;
    }

    public Integer getSessions() {
        return sessions;
    }

    public void setSessions(Integer sessions) {
        this.sessions = sessions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(documentId);
        if (ratePerSession == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(ratePerSession);
        }
        if (sessions == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(sessions);
        }
        parcel.writeParcelable(academy, i);
        parcel.writeParcelable(category, i);
        parcel.writeParcelable(location, i);
        parcel.writeParcelable(branch, i);
    }
}
