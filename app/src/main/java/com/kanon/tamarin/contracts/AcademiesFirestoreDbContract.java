package com.kanon.tamarin.contracts;

public class AcademiesFirestoreDbContract {
    // Root collection name
    public static final String COLLECTION_NAME = "academies";

    // Document ID
    public static final String DOCUMENT_ID = "document_id";

    // Document field names
    public static final String FIELD_ACADEMY_EMAIL = "email";
    public static final String FIELD_ACADEMY_PASSWORD = "password";
    public static final String FIELD_ACADEMY_STATUS = "status";
    public static final String FIELD_ACADEMY_RANK = "rank";
    public static final String FIELD_ACADEMY_SORT_ORDER = "sortOrder";
    public static final String FIELD_ACADEMY_TYPE = "type";

    // To prevent someone from accidentally instantiating the contract 		class, make the constructor private
    private AcademiesFirestoreDbContract() {}
}
