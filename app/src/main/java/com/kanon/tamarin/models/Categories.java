package com.kanon.tamarin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;


public class Categories implements Parcelable {

    @DocumentId
    private String documentId;

    private String categoryName;
    private Integer sortOrder;
    private String thumbnailImage;

    public Categories()
    {

    }

    public Categories(String categoryName, Integer sortOrder, String thumbnailImage) {
        this.categoryName = categoryName;
        this.sortOrder = sortOrder;
        this.thumbnailImage = thumbnailImage;
    }

    protected Categories(Parcel in) {
        documentId = in.readString();
        categoryName = in.readString();
        if (in.readByte() == 0) {
            sortOrder = null;
        } else {
            sortOrder = in.readInt();
        }
        thumbnailImage = in.readString();
    }

    public static final Creator<Categories> CREATOR = new Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(documentId);
        parcel.writeString(categoryName);
        if (sortOrder == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(sortOrder);
        }
        parcel.writeString(thumbnailImage);
    }
}
