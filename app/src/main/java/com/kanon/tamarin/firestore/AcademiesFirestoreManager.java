package com.kanon.tamarin.firestore;

import static com.kanon.tamarin.contracts.AcademiesFirestoreDbContract.COLLECTION_NAME;
import static com.kanon.tamarin.contracts.AcademiesFirestoreDbContract.FIELD_ACADEMY_EMAIL;
import static com.kanon.tamarin.contracts.AcademiesFirestoreDbContract.FIELD_ACADEMY_PASSWORD;
import static com.kanon.tamarin.contracts.AcademiesFirestoreDbContract.FIELD_ACADEMY_RANK;
import static com.kanon.tamarin.contracts.AcademiesFirestoreDbContract.FIELD_ACADEMY_SORT_ORDER;
import static com.kanon.tamarin.contracts.AcademiesFirestoreDbContract.FIELD_ACADEMY_STATUS;
import static com.kanon.tamarin.contracts.AcademiesFirestoreDbContract.FIELD_ACADEMY_TYPE;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.models.Academies;

public class AcademiesFirestoreManager extends GlobalFirestoreManager{

    private static AcademiesFirestoreManager academiesFirestoreManager;

    private CollectionReference academiesCollectionReference;

    public static AcademiesFirestoreManager academyNewInstance() {
        if (academiesFirestoreManager == null) {
            academiesFirestoreManager = new AcademiesFirestoreManager();
        }
        return academiesFirestoreManager;
    }

    private AcademiesFirestoreManager() {
        newInstance();
        academiesCollectionReference = getFirebaseFirestore().collection(COLLECTION_NAME);
    }

    public void createDocument(Academies academies) {
        academiesCollectionReference.add(academies);
    }

    public void getAllAcademies(OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        academiesCollectionReference.get().addOnCompleteListener(onCompleteListener);
    }

    public void updateAcadmey(Academies academies) {
        String documentId = academies.getDocumentId();
        DocumentReference documentReference = academiesCollectionReference.document(documentId);
        documentReference.set(academies);
    }

    public void deleteAcademy(String documentId) {
        DocumentReference documentReference = academiesCollectionReference.document(documentId);
        documentReference.delete();
    }

    public void getAcademy(DocumentReference documentReference, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        documentReference.get().addOnCompleteListener(onCompleteListener);
    }

    public void getAcadmiesFirstLoad(String Type, OnCompleteListener<QuerySnapshot> onCompleteListener){
        academiesCollectionReference.whereEqualTo(FIELD_ACADEMY_TYPE, Type)
                                    .whereEqualTo(FIELD_ACADEMY_STATUS, 1) //active
                                    .orderBy(FIELD_ACADEMY_RANK, Query.Direction.ASCENDING)
                                    .orderBy(FIELD_ACADEMY_SORT_ORDER, Query.Direction.ASCENDING)
                .get().addOnCompleteListener(onCompleteListener);
    }

    public void getCurrentAcademy(String documentId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        DocumentReference documentReference = academiesCollectionReference.document(documentId);
        documentReference.get().addOnCompleteListener(onCompleteListener);
    }

    public DocumentReference getAcademyRef(String documentId) {
        DocumentReference documentReference = academiesCollectionReference.document(documentId);
        documentReference.get();
        return documentReference;
    }

    public void LoginAcademy(String email, String password, OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        academiesCollectionReference.whereEqualTo(FIELD_ACADEMY_EMAIL, email)
                                    .whereEqualTo(FIELD_ACADEMY_PASSWORD, password)
                .get().addOnCompleteListener(onCompleteListener);
    }
}
