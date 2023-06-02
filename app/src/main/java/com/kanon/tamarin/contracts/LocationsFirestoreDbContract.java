package com.kanon.tamarin.contracts;

public class LocationsFirestoreDbContract {

    // Root collection name
    public static final String COLLECTION_NAME = "locations";

    // Document ID
    public static final String DOCUMENT_ID = "document_id";

    // Document field names
    public static final String FIELD_LOCATION_NAME = "locationName";
    public static final String FIELD_SORT_ORDER = "sortOrder";

    // To prevent someone from accidentally instantiating the contract 		class, make the constructor private
    private LocationsFirestoreDbContract() {}
}
