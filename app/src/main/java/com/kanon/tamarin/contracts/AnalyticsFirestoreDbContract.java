package com.kanon.tamarin.contracts;

public class AnalyticsFirestoreDbContract {
    // Root collection name
    public static final String COLLECTION_NAME = "analytics";

    // Document ID
    public static final String DOCUMENT_ID = "document_id";

    // Document field names
    public static final String FIELD_FOR_REF = "forRef";

    // To prevent someone from accidentally instantiating the contract 		class, make the constructor private
    private AnalyticsFirestoreDbContract() {}
}
