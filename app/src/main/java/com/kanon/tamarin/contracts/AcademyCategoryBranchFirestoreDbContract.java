package com.kanon.tamarin.contracts;

public class AcademyCategoryBranchFirestoreDbContract {
    // Root collection name
    public static final String COLLECTION_NAME = "academyCategoryBranch";

    // Document ID
    public static final String DOCUMENT_ID = "document_id";

    // Document field names
    public static final String FIELD_BRANCH_REF = "branchRef";
    public static final String FIELD_ACADEMY_REF = "academyRef";
    public static final String FIELD_CATEGORY_REF = "categoryRef";
    public static final String FIELD_LOCATION_REF = "locationRef";

    // To prevent someone from accidentally instantiating the contract 		class, make the constructor private
    private AcademyCategoryBranchFirestoreDbContract() {}
}
