package com.kanon.tamarin.firestore;

import static com.kanon.tamarin.contracts.CategoriesFirestoreDbContract.COLLECTION_NAME;
import static com.kanon.tamarin.contracts.CategoriesFirestoreDbContract.FIELD_SORT_ORDER;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.models.Categories;

public class CategoriesFirestoreManager extends GlobalFirestoreManager{

    private static CategoriesFirestoreManager categoriesFirestoreManager;

    private CollectionReference categoriesCollectionReference;

    public static CategoriesFirestoreManager categoryNewInstance() {
        if (categoriesFirestoreManager == null) {
            categoriesFirestoreManager = new CategoriesFirestoreManager();
        }
        return categoriesFirestoreManager;
    }

    private CategoriesFirestoreManager() {
        newInstance();
        categoriesCollectionReference = getFirebaseFirestore().collection(COLLECTION_NAME);
    }

    public void createDocument(Categories categories) {
        categoriesCollectionReference.add(categories);
    }

    public void getAllCategories(OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        categoriesCollectionReference.orderBy(FIELD_SORT_ORDER, Query.Direction.ASCENDING).get().addOnCompleteListener(onCompleteListener);
    }

    public void updateCategory(Categories categories) {
        String documentId = categories.getDocumentId();
        DocumentReference documentReference = categoriesCollectionReference.document(documentId);
        documentReference.set(categories);
    }

    public void getCategory(DocumentReference documentReference, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        documentReference.get().addOnCompleteListener(onCompleteListener);
    }

    public void deleteCategory(String documentId) {
        DocumentReference documentReference = categoriesCollectionReference.document(documentId);
        documentReference.delete();
    }

    public DocumentReference getCategoryRefDocument(String documentId) {
        DocumentReference documentReference = categoriesCollectionReference.document(documentId);
        documentReference.get();
        return documentReference;
    }
}
