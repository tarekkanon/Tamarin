package com.kanon.tamarin.firestore;

import static com.kanon.tamarin.contracts.AcademyCategoryBranchFirestoreDbContract.COLLECTION_NAME;
import static com.kanon.tamarin.contracts.AcademyCategoryBranchFirestoreDbContract.FIELD_ACADEMY_REF;
import static com.kanon.tamarin.contracts.AcademyCategoryBranchFirestoreDbContract.FIELD_BRANCH_REF;
import static com.kanon.tamarin.contracts.AcademyCategoryBranchFirestoreDbContract.FIELD_CATEGORY_REF;
import static com.kanon.tamarin.contracts.AcademyCategoryBranchFirestoreDbContract.FIELD_LOCATION_REF;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.models.AcademyCategoryBranch;

public class AcademyCategoryBranchFirestoreManager extends GlobalFirestoreManager{

    private static AcademyCategoryBranchFirestoreManager academyCategoryBranchFirestoreManager;

    private CollectionReference academyCategoryBranchCollectionReference;

    public static AcademyCategoryBranchFirestoreManager academyCategoryBranchNewInstance() {
        if (academyCategoryBranchFirestoreManager == null) {
            academyCategoryBranchFirestoreManager = new AcademyCategoryBranchFirestoreManager();
        }
        return academyCategoryBranchFirestoreManager;
    }

    private AcademyCategoryBranchFirestoreManager() {
        newInstance();
        academyCategoryBranchCollectionReference = getFirebaseFirestore().collection(COLLECTION_NAME);
    }

    public void createDocument(AcademyCategoryBranch academyCategoryBranch) {
        academyCategoryBranchCollectionReference.add(academyCategoryBranch);
    }

    public void getAllAcademyCategoryBranch(OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        academyCategoryBranchCollectionReference.get().addOnCompleteListener(onCompleteListener);
    }

    public void getAllAcademyCategoriesFiltered(DocumentReference academyRef, DocumentReference locationRef, DocumentReference categoryRef, OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        academyCategoryBranchCollectionReference.whereEqualTo(FIELD_ACADEMY_REF, academyRef)
                                                .whereEqualTo(FIELD_LOCATION_REF, locationRef)
                                                .whereEqualTo(FIELD_CATEGORY_REF, categoryRef)
                .get().addOnCompleteListener(onCompleteListener);
    }

    public void updateAcademyCategoryBranch(AcademyCategoryBranch academyCategoryBranch) {
        String documentId = academyCategoryBranch.getDocumentId();
        DocumentReference documentReference = academyCategoryBranchCollectionReference.document(documentId);
        documentReference.set(academyCategoryBranch);
    }

    public void deleteAcademyCategoryBranch(String documentId) {
        DocumentReference documentReference = academyCategoryBranchCollectionReference.document(documentId);
        documentReference.delete();
    }

    public void getAllCategoriesBranch(DocumentReference branchRef, OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        academyCategoryBranchCollectionReference.whereEqualTo(FIELD_BRANCH_REF, branchRef)
                .get().addOnCompleteListener(onCompleteListener);
    }

}
