package com.kanon.tamarin.firestore;

import static com.kanon.tamarin.contracts.EnrollRequestFirestoreDbContract.COLLECTION_NAME;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.models.EnrollRequest;

public class EnrollRequestFirestoreManager extends GlobalFirestoreManager{

    private static EnrollRequestFirestoreManager enrollRequestFirestoreManager;

    private CollectionReference enrollRequestCollectionReference;

    public static EnrollRequestFirestoreManager enrollRequestNewInstance() {
        if (enrollRequestFirestoreManager == null) {
            enrollRequestFirestoreManager = new EnrollRequestFirestoreManager();
        }
        return enrollRequestFirestoreManager;
    }

    private EnrollRequestFirestoreManager() {
        newInstance();
        enrollRequestCollectionReference = getFirebaseFirestore().collection(COLLECTION_NAME);
    }

    public void createDocument(EnrollRequest enrollRequest) {
        enrollRequestCollectionReference.add(enrollRequest);
    }

    public void getAllEnrollRequests(OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        enrollRequestCollectionReference.get().addOnCompleteListener(onCompleteListener);
    }

    public void updateEnrollRequest(EnrollRequest enrollRequest) {
        String documentId = enrollRequest.getDocumentId();
        DocumentReference documentReference = enrollRequestCollectionReference.document(documentId);
        documentReference.set(enrollRequest);
    }

    public void deleteEnrollRequest(String documentId) {
        DocumentReference documentReference = enrollRequestCollectionReference.document(documentId);
        documentReference.delete();
    }

    public void getEnrollRequest(DocumentReference documentReference, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        documentReference.get().addOnCompleteListener(onCompleteListener);
    }
}
