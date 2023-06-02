package com.kanon.tamarin.firestore;

import com.google.firebase.firestore.FirebaseFirestore;

public class GlobalFirestoreManager {

    private FirebaseFirestore firebaseFirestore;

    private static GlobalFirestoreManager globalFirestoreManager;

    public static GlobalFirestoreManager newInstance() {
        if (globalFirestoreManager == null) {
            globalFirestoreManager = new GlobalFirestoreManager();
        }
        return globalFirestoreManager;
    }

    protected GlobalFirestoreManager() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }
}
