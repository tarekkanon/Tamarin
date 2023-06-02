package com.kanon.tamarin.contracts;

public class CategoriesFirestoreDbContract {
    // Root collection name
    public static final String COLLECTION_NAME = "categories";

    // Document ID
    public static final String DOCUMENT_ID = "document_id";

    // Document field names
    public static final String FIELD_CATEGORY_NAME = "categoryName";
    public static final String FIELD_SORT_ORDER = "sortOrder";
    public static final String FIELD_THUMBNAIL_IMAGE = "thumbnailImage";

    // To prevent someone from accidentally instantiating the contract 		class, make the constructor private
    private CategoriesFirestoreDbContract() {}
}
