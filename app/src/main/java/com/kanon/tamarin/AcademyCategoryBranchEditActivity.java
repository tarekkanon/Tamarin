package com.kanon.tamarin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.adapters.AcademyCategoryBranchResultListAdapter;
import com.kanon.tamarin.firestore.AcademiesFirestoreManager;
import com.kanon.tamarin.firestore.AcademyCategoryBranchFirestoreManager;
import com.kanon.tamarin.firestore.BranchAcademyFirestoreManager;
import com.kanon.tamarin.firestore.CategoriesFirestoreManager;
import com.kanon.tamarin.firestore.LocationsFirestoreManager;
import com.kanon.tamarin.models.AcademyCategoryBranch;
import com.kanon.tamarin.models.BranchAcademy;
import com.kanon.tamarin.models.Categories;
import com.kanon.tamarin.models.Locations;

import java.util.ArrayList;
import java.util.List;

public class AcademyCategoryBranchEditActivity extends AppCompatActivity {

    private BranchAcademy currentBranch;
    private List<Locations> locationsList;
    private List<Categories> categoriesList;
    private List<AcademyCategoryBranch> currentBranchCategories;
    private AcademyCategoryBranch academyCategoryBranchForEdit;

    private Boolean isEditCategory;

    private AcademiesFirestoreManager academiesFirestoreManager;
    private BranchAcademyFirestoreManager branchAcademyFirestoreManager;
    private LocationsFirestoreManager locationsFirestoreManager;
    private AcademyCategoryBranchFirestoreManager academyCategoryBranchFirestoreManager;
    private CategoriesFirestoreManager categoriesFirestoreManager;

    private AcademyCategoryBranchResultListAdapter resultListAdapter;

    private RecyclerView categoriesListRV;
    private RecyclerView.LayoutManager layoutManager;

    private Dialog newCategoryBranchDialog;
    private Button addNewCategoryBranchDialogBTN,cancelNewCategoryBranchDialogBTN;
    private EditText categoryBranchRatePerSessionET;
    private Spinner categoryBranchLOV;

    private Dialog deleteConfirmationDialog;
    private Button deleteConfirmationYesBTN, deleteConfirmationNoBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_category_branch_edit);

        currentBranch = getIntent().getParcelableExtra("currentBranch");
        locationsList = getIntent().getParcelableArrayListExtra("allLocations");
        categoriesList = getIntent().getParcelableArrayListExtra("allCategories");

        categoriesListRV = findViewById(R.id.categories_profile_list);

        layoutManager = new LinearLayoutManager(this);
        categoriesListRV.setLayoutManager(layoutManager);
        categoriesListRV.setItemAnimator(new DefaultItemAnimator());

        Log.w("branch success", currentBranch.getAddress());

        newCategoryBranchDialog = new Dialog(AcademyCategoryBranchEditActivity.this);
        newCategoryBranchDialog.setContentView(R.layout.dialog_new_academy_branch_category);
        newCategoryBranchDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addNewCategoryBranchDialogBTN = newCategoryBranchDialog.findViewById(R.id.dialog_category_branch_add_btn);
        cancelNewCategoryBranchDialogBTN = newCategoryBranchDialog.findViewById(R.id.dialog_category_branch_cancel_btn);
        categoryBranchRatePerSessionET = newCategoryBranchDialog.findViewById(R.id.categoryBranchRateET);
        categoryBranchLOV = newCategoryBranchDialog.findViewById(R.id.dialog_LOVCategory);

        deleteConfirmationDialog = new Dialog(AcademyCategoryBranchEditActivity.this);
        deleteConfirmationDialog.setContentView(R.layout.dialog_confirmation_box);
        deleteConfirmationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        deleteConfirmationYesBTN = deleteConfirmationDialog.findViewById(R.id.dialog_confirm_yes);
        deleteConfirmationNoBTN = deleteConfirmationDialog.findViewById(R.id.dialog_confirm_no);
    }

    @Override
    protected void onStart() {
        super.onStart();

        academiesFirestoreManager = AcademiesFirestoreManager.academyNewInstance();
        branchAcademyFirestoreManager = BranchAcademyFirestoreManager.branchAcademyNewInstance();
        locationsFirestoreManager = LocationsFirestoreManager.locationNewInstance();
        categoriesFirestoreManager = CategoriesFirestoreManager.categoryNewInstance();
        academyCategoryBranchFirestoreManager = AcademyCategoryBranchFirestoreManager.academyCategoryBranchNewInstance();

        RefreshCategoryBranchList();

        ArrayList<String> cats = new ArrayList<>();

        for (Categories cat :
                categoriesList) {
            cats.add(cat.getCategoryName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AcademyCategoryBranchEditActivity.this, android.R.layout.simple_spinner_dropdown_item, cats);

        categoryBranchLOV.setAdapter(arrayAdapter);

        addNewCategoryBranchDialogBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryBranchRatePerSessionET.getText().toString().isEmpty() && categoryBranchRatePerSessionET.getText().toString().equals("0"))
                {
                    Toast.makeText(AcademyCategoryBranchEditActivity.this, "Missing Data", Toast.LENGTH_LONG).show();
                    return;
                }

                if (isEditCategory)
                {
                    academyCategoryBranchForEdit.setCategory(categoriesList.get(categoryBranchLOV.getSelectedItemPosition()));
                    academyCategoryBranchForEdit.setCategoryRef(categoriesFirestoreManager.getCategoryRefDocument(categoriesList.get(categoryBranchLOV.getSelectedItemPosition()).getDocumentId()));

                    academyCategoryBranchForEdit.setRatePerSession(Integer.parseInt(categoryBranchRatePerSessionET.getText().toString()));

                    academyCategoryBranchFirestoreManager.updateAcademyCategoryBranch(academyCategoryBranchForEdit);

                    Toast.makeText(AcademyCategoryBranchEditActivity.this, "Updated!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    AcademyCategoryBranch academyCategoryBranch = new AcademyCategoryBranch();

                    academyCategoryBranch.setCategory(categoriesList.get(categoryBranchLOV.getSelectedItemPosition()));
                    academyCategoryBranch.setCategoryRef(categoriesFirestoreManager.getCategoryRefDocument(categoriesList.get(categoryBranchLOV.getSelectedItemPosition()).getDocumentId()));
                    academyCategoryBranch.setAcademy(currentBranch.getAcademy());
                    academyCategoryBranch.setAcademyRef(academiesFirestoreManager.getAcademyRef(currentBranch.getAcademy().getDocumentId()));
                    academyCategoryBranch.setBranch(currentBranch);
                    academyCategoryBranch.setBranchRef(branchAcademyFirestoreManager.getBranchRefDocument(currentBranch.getDocumentId()));
                    academyCategoryBranch.setLocation(currentBranch.getLocation());
                    academyCategoryBranch.setLocationRef(locationsFirestoreManager.getLocationRefDocument(currentBranch.getLocation().getDocumentId()));

                    academyCategoryBranch.setRatePerSession(Integer.parseInt(categoryBranchRatePerSessionET.getText().toString()));

                    academyCategoryBranchFirestoreManager.createDocument(academyCategoryBranch);

                    Toast.makeText(AcademyCategoryBranchEditActivity.this, "New activity Added!", Toast.LENGTH_LONG).show();
                }

                newCategoryBranchDialog.dismiss();

                RefreshCategoryBranchList();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category_branch_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.ProfileNewCategoryBranchMenuBTN:

                isEditCategory = false;

                addNewCategoryBranchDialogBTN.setText(R.string.Add);

                newCategoryBranchDialog.show();

                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }

    private void RefreshCategoryBranchList(){
        academyCategoryBranchFirestoreManager.getAllCategoriesBranch(branchAcademyFirestoreManager.getBranchRefDocument(currentBranch.getDocumentId()), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshot = task.getResult();

                    if (querySnapshot != null) {
                        currentBranchCategories = querySnapshot.toObjects(AcademyCategoryBranch.class);

                        if (!currentBranchCategories.isEmpty()) {
                                for (int i = 0; i < currentBranchCategories.size(); i++) {
                                    currentBranchCategories.get(i).setLocation(SearchLocationInList(locationsList, currentBranchCategories.get(i).getLocationRef()));
                                    currentBranchCategories.get(i).setCategory(SearchCategoryInList(categoriesList, currentBranchCategories.get(i).getCategoryRef()));
                                }

                                resultListAdapter = new AcademyCategoryBranchResultListAdapter(currentBranchCategories, locationsList, categoriesList);

                                categoriesListRV.setAdapter(resultListAdapter);
                            }
                    }

                } else {
                    Log.w("Error App", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private Locations SearchLocationInList(List<Locations> searchLocationsList, DocumentReference searchLocationRef)
    {
        String searchSTR = searchLocationRef.getPath().replace("locations/", "");
        for (Locations loc : searchLocationsList) {
            if (loc.getDocumentId().contentEquals(searchSTR)) {
                return loc;
            }
        }
        return null;
    }

    private Categories SearchCategoryInList(List<Categories> searchCategoriesList, DocumentReference searchCategoryRef)
    {
        String searchSTR = searchCategoryRef.getPath().replace("categories/", "");
        for (Categories cat : searchCategoriesList) {
            if (cat.getDocumentId().contentEquals(searchSTR)) {
                return cat;
            }
        }
        return null;
    }

    public void OpenEditDialogForCategory(AcademyCategoryBranch academyCategoryBranch)
    {
        isEditCategory = true;

        addNewCategoryBranchDialogBTN.setText(R.string.Update);

        categoryBranchRatePerSessionET.setText(academyCategoryBranch.getRatePerSession().toString());

        int selectedCategoryPosition = 0;

        for (int i = 0 ; i < categoryBranchLOV.getAdapter().getCount(); i++)
        {
            if (academyCategoryBranch.getCategory().getCategoryName() == categoryBranchLOV.getAdapter().getItem(i).toString())
                selectedCategoryPosition = i;
        }
        categoryBranchLOV.setSelection(selectedCategoryPosition);

        academyCategoryBranchForEdit = academyCategoryBranch;

        newCategoryBranchDialog.show();

        cancelNewCategoryBranchDialogBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCategoryBranchDialog.dismiss();
            }
        });

    }

    public void OpenDeleteDialogForCategory(AcademyCategoryBranch academyCategoryBranch)
    {
        deleteConfirmationDialog.show();

        deleteConfirmationYesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                academyCategoryBranchFirestoreManager.deleteAcademyCategoryBranch(academyCategoryBranch.getDocumentId());

                RefreshCategoryBranchList();

                deleteConfirmationDialog.dismiss();
            }
        });

        deleteConfirmationNoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteConfirmationDialog.dismiss();
            }
        });
    }
}