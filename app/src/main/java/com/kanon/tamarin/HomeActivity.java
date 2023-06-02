package com.kanon.tamarin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;

import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.adapters.AcademiesResultListAdapter;
import com.kanon.tamarin.adapters.CategorySpinnerAdapter;
import com.kanon.tamarin.databinding.ActivityHomeBinding;
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
import com.kanon.tamarin.sharedpref.TamarinSharedPrefHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    private CategoriesFirestoreManager categoriesFirestoreManager;
    private LocationsFirestoreManager locationsFirestoreManager;
    private AcademiesFirestoreManager academiesFirestoreManager;
    private BranchAcademyFirestoreManager branchAcademyFirestoreManager;
    private AcademyCategoryBranchFirestoreManager academyCategoryBranchFirestoreManager;

    private List<Locations> locationsList;
    private List<Categories> categoriesList;
    private List<AcademyCategoryBranch> AcademiesList;
    private List<Academies> academiesLoaded;
    private int totalLoadedObjects;

    private Academies currentAcademyProfile;
    private List<BranchAcademy> currentAcademyBranches;
    private List<AcademyCategoryBranch> currentBranchCategories;


    private RecyclerView academiesListRV;
    private RecyclerView.LayoutManager layoutManager;
    private AcademiesResultListAdapter resultListAdapter;

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private Spinner LOVLocation, LOVCategory;
    private MaterialButtonToggleGroup searchTypeSW;

    private ImageView searchIMGBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_academy_profile, R.id.nav_enroll, R.id.nav_signout)
                .setOpenableLayout(drawer)
                .build();

        academiesListRV = findViewById(R.id.AcademyiesResultRV);
        layoutManager = new LinearLayoutManager(this);
        academiesListRV.setLayoutManager(layoutManager);
        academiesListRV.setItemAnimator(new DefaultItemAnimator());

        LOVLocation = findViewById(R.id.LOVLocation);
        LOVCategory = findViewById(R.id.LOVCategory);

        searchTypeSW = findViewById(R.id.SearchSwitch);

        searchIMGBTN = findViewById(R.id.IMGBTNSearch);

        searchIMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //GetAcademiesSearchResult();
                if (searchTypeSW.getCheckedButtonId() == R.id.SearchSWAcademy)
                    GetAllAcademies("academy");
                else if (searchTypeSW.getCheckedButtonId() == R.id.SearchSWTrainer)
                    GetAllAcademies("trainer");
            }
        });

        RefreshLocationCategory();
    }

    @Override
    protected void onStart() {

        //RefreshLocationCategory();

        //GetAcademiesSearchResult();

        CheckIsLoggedAccount();

        //SeedDataAcademies();
        //SeedAcademyBranches();
        //SeedCategoryBranchAcademy();

        super.onStart();
    }

    private void CheckIsLoggedAccount()
    {
        if (new TamarinSharedPrefHandler(this).GetValue("currentAcademy") == null)
        {
            navigationView.getMenu().findItem(R.id.nav_academy_profile).setTitle(R.string.Login);
            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(false);
        }
        else
        {
            navigationView.getMenu().findItem(R.id.nav_academy_profile).setTitle(R.string.menu_nav_academy_profile);
            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(true);
        }
    }

    private void GetAcademiesSearchResult()
    {
        totalLoadedObjects = 0;

        academiesFirestoreManager = AcademiesFirestoreManager.academyNewInstance();
        branchAcademyFirestoreManager = BranchAcademyFirestoreManager.branchAcademyNewInstance();
        academyCategoryBranchFirestoreManager = AcademyCategoryBranchFirestoreManager.academyCategoryBranchNewInstance();
        locationsFirestoreManager = LocationsFirestoreManager.locationNewInstance();
        categoriesFirestoreManager = CategoriesFirestoreManager.categoryNewInstance();

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
                            categoriesFirestoreManager.getCategory(AcademiesList.get(finalI).getCategoryRef(), new OnCompleteListener<DocumentSnapshot>() {
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

                                                                                            CheckAllObjectsLoaded();
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

    private void GetAllAcademies(String type){
        academiesFirestoreManager = AcademiesFirestoreManager.academyNewInstance();

        academiesFirestoreManager.getAcadmiesFirstLoad(type, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshotAcademy = task.getResult();

                    academiesLoaded = querySnapshotAcademy.toObjects(Academies.class);

                    if (academiesLoaded.isEmpty())
                        return;

                    GetAllActivites(academiesLoaded);
                }
            }
        });
    }

    private void GetAllActivites(List<Academies> allLoadedAcademies)
    {
        academyCategoryBranchFirestoreManager = AcademyCategoryBranchFirestoreManager.academyCategoryBranchNewInstance();
        categoriesFirestoreManager = CategoriesFirestoreManager.categoryNewInstance();
        locationsFirestoreManager = LocationsFirestoreManager.locationNewInstance();

        AcademiesList = new ArrayList<>();

        resultListAdapter = new AcademiesResultListAdapter(AcademiesList);
        academiesListRV.setAdapter(resultListAdapter);

        for (int i = 0 ; i < allLoadedAcademies.size(); i++) {
            int finalI = i;
            academyCategoryBranchFirestoreManager.getAllAcademyCategoriesFiltered(academiesFirestoreManager.getAcademyRef(allLoadedAcademies.get(i).getDocumentId()),
                                                    locationsFirestoreManager.getLocationRefDocument(locationsList.get(LOVLocation.getSelectedItemPosition()).getDocumentId()),
                                                    categoriesFirestoreManager.getCategoryRefDocument(categoriesList.get(LOVCategory.getSelectedItemPosition()).getDocumentId()),
                    new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshotActivites = task.getResult();

                        querySnapshotActivites.forEach(new Consumer<QueryDocumentSnapshot>() {
                            @Override
                            public void accept(QueryDocumentSnapshot queryDocumentSnapshot) {
                                AcademyCategoryBranch academyCategoryBranch = queryDocumentSnapshot.toObject(AcademyCategoryBranch.class);
                                academyCategoryBranch.setCategory(categoriesList.get(LOVCategory.getSelectedItemPosition()));
                                academyCategoryBranch.setLocation(locationsList.get(LOVLocation.getSelectedItemPosition()));
                                academyCategoryBranch.setAcademy(allLoadedAcademies.get(finalI));

                                AcademiesList.add(academyCategoryBranch);

                                GetAllActivitesBranches();
                            }
                        });
                    }
                }
            });
        }
    }

    private void GetAllActivitesBranches()
    {
        branchAcademyFirestoreManager = BranchAcademyFirestoreManager.branchAcademyNewInstance();

        //totalLoadedObjects = 0;

        for (int i = 0; i < AcademiesList.size(); i++){
            int finalI = i;

            if (AcademiesList.get(i).getBranch() == null)
                branchAcademyFirestoreManager.getBranch(AcademiesList.get(i).getBranchRef(), new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot documentSnapshotBranch = task.getResult();

                            if (documentSnapshotBranch != null) {
                                AcademiesList.get(finalI).setBranch(documentSnapshotBranch.toObject(BranchAcademy.class));

                                //CheckAllObjectsLoaded();

                                resultListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
        }
    }

    private void CheckAllObjectsLoaded() {
        totalLoadedObjects++;

        if (totalLoadedObjects == AcademiesList.size())
        {
            resultListAdapter = new AcademiesResultListAdapter(AcademiesList);
            academiesListRV.setAdapter(resultListAdapter);
        }

    }

    private void RefreshLocationCategory()
    {
        locationsFirestoreManager = LocationsFirestoreManager.locationNewInstance();
        categoriesFirestoreManager = CategoriesFirestoreManager.categoryNewInstance();

        locationsFirestoreManager.getAllLocations(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshot = task.getResult();

                    if (querySnapshot != null) {

                        locationsList = querySnapshot.toObjects(Locations.class);

                        ArrayList<String> locs = new ArrayList<>();

                        for (Locations loc :
                                locationsList) {
                            locs.add(loc.getLocationName());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_spinner_dropdown_item, locs);

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

                        ArrayAdapter<Categories> categoriesArrayAdapter = new CategorySpinnerAdapter(HomeActivity.this, R.layout.category_custom_spinner_item, categoriesList);

                        LOVCategory.setAdapter(categoriesArrayAdapter);

                    }

                } else {
                    Log.w("Error App", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        drawer.openDrawer(GravityCompat.START);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_enroll:
                startActivity(new Intent(HomeActivity.this, EnrollmentActivity.class));
                //drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_academy_profile:

                if (new TamarinSharedPrefHandler(this).GetValue("currentAcademy") == null)
                {
                    startActivity(new Intent(this,LoginAcademyActivity.class));
                }
                else
                {
                    academiesFirestoreManager = AcademiesFirestoreManager.academyNewInstance();
                    Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
                    //intent.putExtra()
                    academiesFirestoreManager.getCurrentAcademy(new TamarinSharedPrefHandler(this).GetValue("currentAcademy"), new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshotAcademy = task.getResult();

                                if (documentSnapshotAcademy != null) {
                                    intent.putExtra("currentProfile", documentSnapshotAcademy.toObject(Academies.class));
                                    intent.putParcelableArrayListExtra("allLocations", (ArrayList<? extends Parcelable>) locationsList);
                                    intent.putParcelableArrayListExtra("allCategories", (ArrayList<? extends Parcelable>) categoriesList);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                }

                return true;

            case R.id.nav_signout:
                new TamarinSharedPrefHandler(this).RemoveValue("currentAcademy");
                Toast.makeText(this, "Signed off!", Toast.LENGTH_LONG).show();
                CheckIsLoggedAccount();
                return true;
            default:
                return false;
        }
    }

    private void SeedCategoryBranchAcademy() {
        AcademyCategoryBranch act1 = new AcademyCategoryBranch();
        act1.setRatePerSession(500);
        act1.setAcademyRef(academiesFirestoreManager.getAcademyRef("IPWZNdGQtHcoElY2uMjl"));
        act1.setBranchRef(branchAcademyFirestoreManager.getBranchRefDocument("irYSW1G5vzLAWtJ6SLhb"));
        act1.setLocationRef(locationsFirestoreManager.getLocationRefDocument("FgCfWHoiXgK9fG10xXUI"));
        act1.setCategoryRef(categoriesFirestoreManager.getCategoryRefDocument("FWz47YyZgtlTYLJq2y9m"));

        AcademyCategoryBranch act2 = new AcademyCategoryBranch();
        act2.setRatePerSession(400);
        act2.setAcademyRef(academiesFirestoreManager.getAcademyRef("IPWZNdGQtHcoElY2uMjl"));
        act2.setBranchRef(branchAcademyFirestoreManager.getBranchRefDocument("irYSW1G5vzLAWtJ6SLhb"));
        act2.setLocationRef(locationsFirestoreManager.getLocationRefDocument("FgCfWHoiXgK9fG10xXUI"));
        act2.setCategoryRef(categoriesFirestoreManager.getCategoryRefDocument("btWt6Wot3RDerPqRQqUd"));

        AcademyCategoryBranch act3 = new AcademyCategoryBranch();
        act3.setRatePerSession(300);
        act3.setAcademyRef(academiesFirestoreManager.getAcademyRef("IPWZNdGQtHcoElY2uMjl"));
        act3.setBranchRef(branchAcademyFirestoreManager.getBranchRefDocument("irYSW1G5vzLAWtJ6SLhb"));
        act3.setLocationRef(locationsFirestoreManager.getLocationRefDocument("FgCfWHoiXgK9fG10xXUI"));
        act3.setCategoryRef(categoriesFirestoreManager.getCategoryRefDocument("ikV9Hej0PkzzXWc291LX"));

        AcademyCategoryBranch act4 = new AcademyCategoryBranch();
        act4.setRatePerSession(100);
        act4.setAcademyRef(academiesFirestoreManager.getAcademyRef("IPWZNdGQtHcoElY2uMjl"));
        act4.setBranchRef(branchAcademyFirestoreManager.getBranchRefDocument("XX7A5Dyhit7KgqcKDVGO"));
        act4.setLocationRef(locationsFirestoreManager.getLocationRefDocument("yJRdiU28IFgpT66OOvrF"));
        act4.setCategoryRef(categoriesFirestoreManager.getCategoryRefDocument("FWz47YyZgtlTYLJq2y9m"));

        academyCategoryBranchFirestoreManager.createDocument(act1);
        academyCategoryBranchFirestoreManager.createDocument(act2);
        academyCategoryBranchFirestoreManager.createDocument(act3);
        academyCategoryBranchFirestoreManager.createDocument(act4);
    }

    private void SeedDataAcademies() {

        String imgLink = "https://firebasestorage.googleapis.com/v0/b/tamarin-dev-4f3ed.appspot.com/o/mockup_apple_iphone_12_pro_max_644201348e.png?alt=media&token=4c5610e0-1d78-40b1-9bd7-23eab72f9c9c";

        Academies academy1 = new Academies();
        academy1.setAcademyName("Hoba");
        academy1.setAcademyDescription("hovaaaa");
        academy1.setEmail("hobaaa");
        academy1.setContactPersonMobile("01111111");
        academy1.setContactPersonName("hobaas");
        academy1.setImage1(imgLink);
        academy1.setProfilePic(imgLink);
        academy1.setRank(1);
        academy1.setSortOrder(1);
        academy1.setStatus(1);

        Academies academy2 = new Academies();
        academy2.setAcademyName("ASDASD");
        academy2.setAcademyDescription("AAAA");
        academy2.setEmail("hoDADAbaaa");
        academy2.setContactPersonMobile("02222222");
        academy2.setContactPersonName("ASASA");
        academy2.setImage1(imgLink);
        academy2.setProfilePic(imgLink);
        academy2.setRank(2);
        academy2.setSortOrder(2);
        academy2.setStatus(1);

        Academies academy3 = new Academies();
        academy3.setAcademyName("EEEEE");
        academy3.setAcademyDescription("EEEEDDD");
        academy3.setEmail("DEDEEEDED");
        academy3.setContactPersonMobile("033333");
        academy3.setContactPersonName("ededededed");
        academy3.setImage1(imgLink);
        academy3.setProfilePic(imgLink);
        academy3.setRank(3);
        academy3.setSortOrder(3);
        academy3.setStatus(1);

        academiesFirestoreManager.createDocument(academy1);
        academiesFirestoreManager.createDocument(academy2);
        academiesFirestoreManager.createDocument(academy3);

    }

    private void SeedAcademyBranches()
    {
        String mapLink = "https://goo.gl/maps/jYbpTJtmLku891Jf9";

        BranchAcademy branch1 = new BranchAcademy();
        branch1.setMobile("011115555");
        branch1.setLocationMap(mapLink);
        branch1.setAddress("branch1 address");
        branch1.setLatitude("30.0632839");
        branch1.setLongitude("31.3133858");
        branch1.setAcademyRef(academiesFirestoreManager.getAcademyRef("IPWZNdGQtHcoElY2uMjl"));
        branch1.setLocationRef(locationsFirestoreManager.getLocationRefDocument("FgCfWHoiXgK9fG10xXUI"));

        BranchAcademy branch2 = new BranchAcademy();
        branch2.setMobile("011114444");
        branch2.setLocationMap(mapLink);
        branch2.setAddress("branch2 address");
        branch2.setLatitude("30.0632839");
        branch2.setLongitude("31.3133858");
        branch2.setAcademyRef(academiesFirestoreManager.getAcademyRef("IPWZNdGQtHcoElY2uMjl"));
        branch2.setLocationRef(locationsFirestoreManager.getLocationRefDocument("yJRdiU28IFgpT66OOvrF"));

        BranchAcademy branch3 = new BranchAcademy();
        branch3.setMobile("02222265454");
        branch3.setLocationMap(mapLink);
        branch3.setAddress("branch3 address");
        branch3.setLatitude("30.0632839");
        branch3.setLongitude("31.3133858");
        branch3.setAcademyRef(academiesFirestoreManager.getAcademyRef("N3suDIV4z6EkNeaMJkRT"));
        branch3.setLocationRef(locationsFirestoreManager.getLocationRefDocument("s7TZwIWQntgZ63cxsgRQ"));

        branchAcademyFirestoreManager.createDocument(branch1);
        branchAcademyFirestoreManager.createDocument(branch2);
        branchAcademyFirestoreManager.createDocument(branch3);
    }
}