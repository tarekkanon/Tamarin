package com.kanon.tamarin.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

public class Analytics {

    @DocumentId
    private String documentId;

    private DocumentReference forRef;

    private Integer fbCount = 1, twitterCount = 1, callCount = 1, igCount = 1, mailCount = 1, mapCount = 1, youtubeCount = 1;

    public Analytics() {
    }

    public Analytics(DocumentReference forRef, Integer fbCount, Integer twitterCount, Integer callCount, Integer igCount, Integer mailCount, Integer mapCount, Integer youtubeCount) {
        this.forRef = forRef;
        this.fbCount = fbCount;
        this.twitterCount = twitterCount;
        this.callCount = callCount;
        this.igCount = igCount;
        this.mailCount = mailCount;
        this.mapCount = mapCount;
        this.youtubeCount = youtubeCount;
    }

    public DocumentReference getForRef() {
        return forRef;
    }

    public void setForRef(DocumentReference forRef) {
        this.forRef = forRef;
    }

    @DocumentId
    public String getDocumentId() {
        return documentId;
    }

    @DocumentId
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setFbCount(Integer fbCount) {
        this.fbCount = fbCount;
    }

    public void setTwitterCount(Integer twitterCount) {
        this.twitterCount = twitterCount;
    }

    public Integer getCallCount() {
        return callCount;
    }

    public void setCallCount(Integer callCount) {
        this.callCount = callCount;
    }

    public Integer getIgCount() {
        return igCount;
    }

    public void setIgCount(Integer igCount) {
        this.igCount = igCount;
    }

    public Integer getFbCount() {
        return fbCount;
    }

    public Integer getTwitterCount() {
        return twitterCount;
    }

    public Integer getMailCount() {
        return mailCount;
    }

    public void setMailCount(Integer mailCount) {
        this.mailCount = mailCount;
    }

    public Integer getMapCount() {
        return mapCount;
    }

    public void setMapCount(Integer mapCount) {
        this.mapCount = mapCount;
    }

    public Integer getYoutubeCount() {
        return youtubeCount;
    }

    public void setYoutubeCount(Integer youtubeCount) {
        this.youtubeCount = youtubeCount;
    }
}
