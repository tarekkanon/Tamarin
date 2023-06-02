package com.kanon.tamarin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;

import java.util.List;

public class Academies implements Parcelable {

    @DocumentId
    private String documentId;

    private String academyName, academyDescription, type,contactPersonName, contactPersonMobile, email, password, image1, image2, image3, image4, profilePic, fbLink, instaLink, youtubeLink, twitterLink;
    private Integer rank, sortOrder, status;

    private List<String> images;

    public Academies(){

    }

    public Academies(String academyName, String academyDescription, String type ,String contactPersonName, String contactPersonMobile, String email, String password, List<String> images, String image1, String image2, String image3, String image4, String profilePic, String fbLink, String instaLink, String youtubeLink, String twitterLink, Integer rank, Integer sortOrder, Integer status) {
        this.academyName = academyName;
        this.academyDescription = academyDescription;
        this.type = type;
        this.contactPersonName = contactPersonName;
        this.contactPersonMobile = contactPersonMobile;
        this.email = email;
        this.password = password;
        this.images = images;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.profilePic = profilePic;
        this.fbLink = fbLink;
        this.instaLink = instaLink;
        this.youtubeLink = youtubeLink;
        this.twitterLink = twitterLink;
        this.rank = rank;
        this.sortOrder = sortOrder;
        this.status = status;
    }

    protected Academies(Parcel in) {
        documentId = in.readString();
        academyName = in.readString();
        academyDescription = in.readString();
        type = in.readString();
        contactPersonName = in.readString();
        contactPersonMobile = in.readString();
        email = in.readString();
        password = in.readString();
        images = in.readArrayList(String.class.getClassLoader());
        image1 = in.readString();
        image2 = in.readString();
        image3 = in.readString();
        image4 = in.readString();
        profilePic = in.readString();
        fbLink = in.readString();
        instaLink = in.readString();
        youtubeLink = in.readString();
        twitterLink = in.readString();
        if (in.readByte() == 0) {
            rank = null;
        } else {
            rank = in.readInt();
        }
        if (in.readByte() == 0) {
            sortOrder = null;
        } else {
            sortOrder = in.readInt();
        }
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
    }

    public static final Creator<Academies> CREATOR = new Creator<Academies>() {
        @Override
        public Academies createFromParcel(Parcel in) {
            return new Academies(in);
        }

        @Override
        public Academies[] newArray(int size) {
            return new Academies[size];
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

    public String getAcademyName() {
        return academyName;
    }

    public void setAcademyName(String academyName) {
        this.academyName = academyName;
    }

    public String getAcademyDescription() {
        return academyDescription;
    }

    public void setAcademyDescription(String academyDescription) {
        this.academyDescription = academyDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getInstaLink() {
        return instaLink;
    }

    public void setInstaLink(String instaLink) {
        this.instaLink = instaLink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(documentId);
        parcel.writeString(academyName);
        parcel.writeString(academyDescription);
        parcel.writeString(type);
        parcel.writeString(contactPersonName);
        parcel.writeString(contactPersonMobile);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeList(images);
        parcel.writeString(image1);
        parcel.writeString(image2);
        parcel.writeString(image3);
        parcel.writeString(image4);
        parcel.writeString(profilePic);
        parcel.writeString(fbLink);
        parcel.writeString(instaLink);
        parcel.writeString(youtubeLink);
        parcel.writeString(twitterLink);
        if (rank == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(rank);
        }
        if (sortOrder == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(sortOrder);
        }
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(status);
        }
    }
}
