package com.kanon.tamarin.contracts;

public class BranchAcademyFirestoreDbContract {
    // Root collection name
    public static final String COLLECTION_NAME = "branches";

    // Document ID
    public static final String DOCUMENT_ID = "document_id";

    // Document field names
    public static final String FIELD_ACADEMY_REF = "academyRef";
    public static final String FIELD_LOCATION_REF = "locationRef";

    // To prevent someone from accidentally instantiating the contract 		class, make the constructor private
    private BranchAcademyFirestoreDbContract() {}
}
