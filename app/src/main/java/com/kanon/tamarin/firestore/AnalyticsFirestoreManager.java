package com.kanon.tamarin.firestore;


import static com.kanon.tamarin.contracts.AnalyticsFirestoreDbContract.COLLECTION_NAME;
import static com.kanon.tamarin.contracts.AnalyticsFirestoreDbContract.FIELD_FOR_REF;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.models.Analytics;

public class AnalyticsFirestoreManager extends GlobalFirestoreManager{

    private static AnalyticsFirestoreManager analyticsFirestoreManager;

    private CollectionReference analyticsCollectionReference;

    public static AnalyticsFirestoreManager analyticsNewInstance() {
        if (analyticsFirestoreManager == null) {
            analyticsFirestoreManager = new AnalyticsFirestoreManager();
        }
        return analyticsFirestoreManager;
    }

    private AnalyticsFirestoreManager() {
        newInstance();
        analyticsCollectionReference = getFirebaseFirestore().collection(COLLECTION_NAME);
    }

    public void createDocument(Analytics analytics) {
        analyticsCollectionReference.add(analytics);
    }

    public void getAllAnalytics(OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        analyticsCollectionReference.get().addOnCompleteListener(onCompleteListener);
    }

    public void updateAnalytics(Analytics analytics) {
        String documentId = analytics.getDocumentId();
        DocumentReference documentReference = analyticsCollectionReference.document(documentId);
        documentReference.set(analytics);
    }

    public void getAnalytics(DocumentReference documentReference, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        documentReference.get().addOnCompleteListener(onCompleteListener);
    }
    public void getAnalyticsFor(DocumentReference forRef, OnCompleteListener<QuerySnapshot> onCompleteListener){
        analyticsCollectionReference.whereEqualTo(FIELD_FOR_REF, forRef)
                .get().addOnCompleteListener(onCompleteListener);
    }
}
