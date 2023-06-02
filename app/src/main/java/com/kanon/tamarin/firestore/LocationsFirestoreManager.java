package com.kanon.tamarin.firestore;

import static com.kanon.tamarin.contracts.LocationsFirestoreDbContract.COLLECTION_NAME;
import static com.kanon.tamarin.contracts.LocationsFirestoreDbContract.FIELD_SORT_ORDER;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.models.Locations;

public class LocationsFirestoreManager extends GlobalFirestoreManager{

    private static LocationsFirestoreManager locationsFirestoreManager;

    private CollectionReference locationsCollectionReference;

    public static LocationsFirestoreManager locationNewInstance() {
        if (locationsFirestoreManager == null) {
            locationsFirestoreManager = new LocationsFirestoreManager();
        }
        return locationsFirestoreManager;
    }

    private LocationsFirestoreManager() {
        newInstance();
        locationsCollectionReference = getFirebaseFirestore().collection(COLLECTION_NAME);
    }

    public void createDocument(Locations locations) {
        locationsCollectionReference.add(locations);
    }

    public void getAllLocations(OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        locationsCollectionReference.orderBy(FIELD_SORT_ORDER, Query.Direction.ASCENDING).get().addOnCompleteListener(onCompleteListener);
    }

    public void updateLocation(Locations locations) {
        String documentId = locations.getDocumentId();
        DocumentReference documentReference = locationsCollectionReference.document(documentId);
        documentReference.set(locations);
    }

    public void deleteLocation(String documentId) {
        DocumentReference documentReference = locationsCollectionReference.document(documentId);
        documentReference.delete();
    }

    public void getLocation(DocumentReference documentReference, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        documentReference.get().addOnCompleteListener(onCompleteListener);
    }

    public DocumentReference getLocationRefDocument(String documentId) {
        DocumentReference documentReference = locationsCollectionReference.document(documentId);
        documentReference.get();
        return documentReference;
    }

}
