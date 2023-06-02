package com.kanon.tamarin.contracts;

public class EnrollRequestFirestoreDbContract {
    // Root collection name
    public static final String COLLECTION_NAME = "enrollRequests";

    // Document ID
    public static final String DOCUMENT_ID = "document_id";

    // Document field names

    // To prevent someone from accidentally instantiating the contract 		class, make the constructor private
    private EnrollRequestFirestoreDbContract() {}
}
