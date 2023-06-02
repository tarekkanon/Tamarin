package com.kanon.tamarin.firestore;

import static com.kanon.tamarin.contracts.BranchAcademyFirestoreDbContract.COLLECTION_NAME;
import static com.kanon.tamarin.contracts.BranchAcademyFirestoreDbContract.FIELD_ACADEMY_REF;
import static com.kanon.tamarin.contracts.BranchAcademyFirestoreDbContract.FIELD_LOCATION_REF;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.models.BranchAcademy;

public class BranchAcademyFirestoreManager extends GlobalFirestoreManager{

    private static BranchAcademyFirestoreManager branchAcademyFirestoreManager;

    private CollectionReference branchAcademyCollectionReference;

    public static BranchAcademyFirestoreManager branchAcademyNewInstance() {
        if (branchAcademyFirestoreManager == null) {
            branchAcademyFirestoreManager = new BranchAcademyFirestoreManager();
        }
        return branchAcademyFirestoreManager;
    }

    private BranchAcademyFirestoreManager() {
        newInstance();
        branchAcademyCollectionReference = getFirebaseFirestore().collection(COLLECTION_NAME);
    }

    public void createDocument(BranchAcademy branchAcademy) {
        branchAcademyCollectionReference.add(branchAcademy);
    }

    public void getAllAcademyBranch(DocumentReference academyRef, OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        branchAcademyCollectionReference.whereEqualTo(FIELD_ACADEMY_REF, academyRef)
                                        .get().addOnCompleteListener(onCompleteListener);
    }

    public void updateBranch(BranchAcademy branchAcademy) {
        String documentId = branchAcademy.getDocumentId();
        DocumentReference documentReference = branchAcademyCollectionReference.document(documentId);
        documentReference.set(branchAcademy);
    }

    public void deleteBranch(String documentId) {
        DocumentReference documentReference = branchAcademyCollectionReference.document(documentId);
        documentReference.delete();
    }

    public void getBranch(DocumentReference documentReference, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        documentReference.get().addOnCompleteListener(onCompleteListener);
    }

    public DocumentReference getBranchRefDocument(String documentId) {
        DocumentReference documentReference = branchAcademyCollectionReference.document(documentId);
        documentReference.get();
        return documentReference;
    }
}
