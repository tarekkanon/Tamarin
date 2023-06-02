package com.kanon.tamarin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.adapters.AcademiesResultListAdapter;
import com.kanon.tamarin.firestore.AcademiesFirestoreManager;
import com.kanon.tamarin.firestore.AcademyCategoryBranchFirestoreManager;
import com.kanon.tamarin.firestore.BranchAcademyFirestoreManager;
import com.kanon.tamarin.firestore.CategoriesFirestoreManager;
import com.kanon.tamarin.firestore.LocationsFirestoreManager;
import com.kanon.tamarin.models.Academies;
import com.kanon.tamarin.models.AcademyCategoryBranch;
import com.kanon.tamarin.models.BranchAcademy;
import com.kanon.tamarin.models.Categories;
import com.kanon.tamarin.models.Locations;

import java.util.ArrayList;
import java.util.List;

public class MainActivityBinding extends AppCompatActivity {

    private CategoriesFirestoreManager categoriesFirestoreManager;
    private LocationsFirestoreManager locationsFirestoreManager;
    private AcademiesFirestoreManager academiesFirestoreManager;
    private BranchAcademyFirestoreManager branchAcademyFirestoreManager;
    private AcademyCategoryBranchFirestoreManager academyCategoryBranchFirestoreManager;

    List<Locations> locationsList;
    List<Categories> categoriesList;
    List<AcademyCategoryBranch> AcademiesList;

    private RecyclerView academiesListRV;
    private RecyclerView.LayoutManager layoutManager;
    private AcademiesResultListAdapter resultListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        academiesListRV = findViewById(R.id.AcademyiesResultRV);
        layoutManager = new LinearLayoutManager(this);
        academiesListRV.setLayoutManager(layoutManager);
        academiesListRV.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onStart() {
        super.onStart();

        locationsFirestoreManager = LocationsFirestoreManager.locationNewInstance();
        categoriesFirestoreManager = CategoriesFirestoreManager.categoryNewInstance();
        academiesFirestoreManager = AcademiesFirestoreManager.academyNewInstance();
        branchAcademyFirestoreManager = BranchAcademyFirestoreManager.branchAcademyNewInstance();
        academyCategoryBranchFirestoreManager = AcademyCategoryBranchFirestoreManager.academyCategoryBranchNewInstance();

        locationsFirestoreManager.getAllLocations(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshot = task.getResult();

                    if (querySnapshot != null) {

                        locationsList = querySnapshot.toObjects(Locations.class);
                        Spinner LOVLocation = findViewById(R.id.LOVLocation);
                        ArrayList<String> locs = new ArrayList<>();
                        locs.add(locationsList.get(0).getLocationName());
                        locs.add(locationsList.get(1).getLocationName());
                        locs.add(locationsList.get(2).getLocationName());
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivityBinding.this, android.R.layout.simple_spinner_dropdown_item, locs);

                        LOVLocation.setAdapter(arrayAdapter);

                    }

                } else {
                    Log.w("Error App", "Error getting documents: ", task.getException());
                }
            }
        });

        categoriesFirestoreManager.getAllCategories(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshot = task.getResult();

                    if (querySnapshot != null) {

                        categoriesList = querySnapshot.toObjects(Categories.class);
                        Spinner LOVCategory = findViewById(R.id.LOVCategory);
                        ArrayList<String> cats = new ArrayList<>();
                        cats.add(categoriesList.get(0).getCategoryName());
                        cats.add(categoriesList.get(1).getCategoryName());
                        cats.add(categoriesList.get(2).getCategoryName());
                        ArrayAdapter<String> arrayAdapterCat = new ArrayAdapter<>(MainActivityBinding.this, android.R.layout.simple_spinner_dropdown_item, cats);

                        LOVCategory.setAdapter(arrayAdapterCat);

                    }

                } else {
                    Log.w("Error App", "Error getting documents: ", task.getException());
                }
            }
        });

        academyCategoryBranchFirestoreManager.getAllAcademyCategoryBranch(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshot = task.getResult();

                    if (querySnapshot != null) {

                        AcademiesList = querySnapshot.toObjects(AcademyCategoryBranch.class);

                        for (int i = 0 ; i < AcademiesList.size(); i++)
                        {
                            int finalI = i;
                            categoriesFirestoreManager.getCategory(AcademiesList.get(i).getCategoryRef(), new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        DocumentSnapshot documentSnapshotCategory = task.getResult();

                                        if (documentSnapshotCategory != null) {
                                            academiesFirestoreManager.getAcademy(AcademiesList.get(finalI).getAcademyRef(), new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        DocumentSnapshot documentSnapshotAcademy = task.getResult();

                                                        if (documentSnapshotAcademy != null) {
                                                            locationsFirestoreManager.getLocation(AcademiesList.get(finalI).getLocationRef(), new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {

                                                                        DocumentSnapshot documentSnapshotLocation = task.getResult();

                                                                        if (documentSnapshotLocation != null) {
                                                                            branchAcademyFirestoreManager.getBranch(AcademiesList.get(finalI).getBranchRef(), new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if (task.isSuccessful()) {

                                                                                        DocumentSnapshot documentSnapshotBranch = task.getResult();

                                                                                        if (documentSnapshotBranch != null) {
                                                                                            AcademiesList.get(finalI).setLocation(documentSnapshotLocation.toObject(Locations.class));
                                                                                            AcademiesList.get(finalI).setAcademy(documentSnapshotAcademy.toObject(Academies.class));
                                                                                            AcademiesList.get(finalI).setCategory(documentSnapshotCategory.toObject(Categories.class));
                                                                                            AcademiesList.get(finalI).setBranch(documentSnapshotBranch.toObject(BranchAcademy.class));

                                                                                            if (finalI == (AcademiesList.size() - 1 ))
                                                                                            {
                                                                                                resultListAdapter = new AcademiesResultListAdapter(AcademiesList);
                                                                                                academiesListRV.setAdapter(resultListAdapter);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    }

                } else {
                    Log.w("Error App", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.home_menu, menu);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.EnrollRequestMenuBTN:

                startActivity(new Intent(MainActivityBinding.this, EnrollmentActivity.class));
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }
}