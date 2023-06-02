package com.kanon.tamarin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kanon.tamarin.adapters.AcademyCategoryBranchResultListAdapter;
import com.kanon.tamarin.adapters.BranchesResultListAdapter;
import com.kanon.tamarin.adapters.EditProfileImagesAdapter;
import com.kanon.tamarin.firestore.AcademiesFirestoreManager;
import com.kanon.tamarin.firestore.AcademyCategoryBranchFirestoreManager;
import com.kanon.tamarin.firestore.BranchAcademyFirestoreManager;
import com.kanon.tamarin.firestore.LocationsFirestoreManager;
import com.kanon.tamarin.models.Academies;
import com.kanon.tamarin.models.AcademyCategoryBranch;
import com.kanon.tamarin.models.BranchAcademy;
import com.kanon.tamarin.models.Categories;
import com.kanon.tamarin.models.Locations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 6000;

    private AcademiesFirestoreManager academiesFirestoreManager;
    private BranchAcademyFirestoreManager branchAcademyFirestoreManager;
    private LocationsFirestoreManager locationsFirestoreManager;
    private AcademyCategoryBranchFirestoreManager academyCategoryBranchFirestoreManager;

    // Create a storage reference from our app
    StorageReference storageRef;

    private Academies currentAcademyProfile;
    private List<BranchAcademy> currentAcademyBranches;
    private List<AcademyCategoryBranch> currentBranchCategories;
    private BranchAcademy branchForEdit;

//    private List<Uri> imagesList;

    private List<Locations> locationsList;
    private List<Categories> categoriesList;

    private EditText profileNameET, profileDescriptionET, profileContactNameET,profileContactMobileET,profileEmailET;

    private RecyclerView branchesListRV;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView imagesRV;
    EditProfileImagesAdapter imagesAdapter;

    private BranchesResultListAdapter resultListAdapter;

    private Dialog newBranchDialog;
    private Button addNewBranchDialogBTN,cancelNewBranchDialogBTN;
    private EditText branchAddressET, branchMobileET, branchMapLinkET;
    private Spinner branchLocationLOV;

    private Boolean isEditBranch;

    private Dialog deleteConfirmationDialog;
    private Button deleteConfirmationYesBTN, deleteConfirmationNoBTN;

    private Button btnImageChooser;

    //int updatedImages, doneUploadImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        currentAcademyProfile = getIntent().getParcelableExtra("currentProfile");
        locationsList = getIntent().getParcelableArrayListExtra("allLocations");
        categoriesList = getIntent().getParcelableArrayListExtra("allCategories");

        profileNameET = findViewById(R.id.profile_name);
        profileDescriptionET = findViewById(R.id.profile_description);
        profileContactNameET = findViewById(R.id.profile_contact_name);
        profileContactMobileET = findViewById(R.id.profile_contact_mobile);
        profileEmailET = findViewById(R.id.profile_email);

        branchesListRV = findViewById(R.id.profile_branches_list);

        layoutManager = new LinearLayoutManager(this);
        branchesListRV.setLayoutManager(layoutManager);
        branchesListRV.setItemAnimator(new DefaultItemAnimator());

        newBranchDialog = new Dialog(EditProfileActivity.this);
        newBranchDialog.setContentView(R.layout.dialog_new_academy_branch);
        newBranchDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addNewBranchDialogBTN = newBranchDialog.findViewById(R.id.dialog_branch_add_btn);
        cancelNewBranchDialogBTN = newBranchDialog.findViewById(R.id.dialog_branch_cancel_btn);
        branchAddressET = newBranchDialog.findViewById(R.id.branchAddressET);
        branchMobileET = newBranchDialog.findViewById(R.id.branchMobileET);
        branchMapLinkET = newBranchDialog.findViewById(R.id.branchMapLinkET);
        branchLocationLOV = newBranchDialog.findViewById(R.id.dialog_LOVLocation);

        deleteConfirmationDialog = new Dialog(EditProfileActivity.this);
        deleteConfirmationDialog.setContentView(R.layout.dialog_confirmation_box);
        deleteConfirmationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        deleteConfirmationYesBTN = deleteConfirmationDialog.findViewById(R.id.dialog_confirm_yes);
        deleteConfirmationNoBTN = deleteConfirmationDialog.findViewById(R.id.dialog_confirm_no);

        btnImageChooser = findViewById(R.id.profile_image_select_btn);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null) {

                        if (result.getData().getClipData() != null)
                        {
                            int countOfImages = result.getData().getClipData().getItemCount();

                            for (int i = 0; i < countOfImages; i++) {

                                if (currentAcademyProfile.getImages() == null)
                                    currentAcademyProfile.setImages(new ArrayList<>());

                                if (currentAcademyProfile.getImages().size() < 4)
                                    currentAcademyProfile.getImages().add(result.getData().getClipData().getItemAt(i).getUri().toString());
                            }
                        }
                        else
                        {
                            if (currentAcademyProfile.getImages() == null)
                                currentAcademyProfile.setImages(new ArrayList<>());

                            if (currentAcademyProfile.getImages().size() < 4)
                                currentAcademyProfile.getImages().add(result.getData().getData().toString());
                            else
                                Toast.makeText(this, "Not allowed more than 4 images", Toast.LENGTH_SHORT).show();
                        }

                        imagesAdapter.notifyDataSetChanged();

                    }
                }
        );

        imagesRV = findViewById(R.id.profile_images_select_rv);

        if (currentAcademyProfile.getImages() == null)
            currentAcademyProfile.setImages(new ArrayList<>());

        imagesAdapter = new EditProfileImagesAdapter((ArrayList<String>) currentAcademyProfile.getImages());
        imagesRV.setLayoutManager(new GridLayoutManager(this, 2));
        imagesRV.setAdapter(imagesAdapter);

        btnImageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);

                    return;
                }

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);


                launcher.launch(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.ProfileNewBranchMenuBTN:

                isEditBranch = false;

                addNewBranchDialogBTN.setText(R.string.Add);

                newBranchDialog.show();

                return true;
            case R.id.ProfileSaveMenuBTN:

                currentAcademyProfile.setAcademyDescription(profileDescriptionET.getText().toString());
                currentAcademyProfile.setContactPersonName(profileContactNameET.getText().toString());
                currentAcademyProfile.setContactPersonMobile(profileContactMobileET.getText().toString());
                currentAcademyProfile.setEmail(profileEmailET.getText().toString());

                currentAcademyProfile.setImages(imagesAdapter.GetImagesListForSave());

                for (int i = 0; i < currentAcademyProfile.getImages().size(); i++) {
                    if (!currentAcademyProfile.getImages().get(i).startsWith("https://"))
                    {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(currentAcademyProfile.getImages().get(i)));
                            Bitmap bitmap = ((BitmapDrawable) Drawable.createFromStream(inputStream, currentAcademyProfile.getImages().get(i))).getBitmap();

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.WEBP, 50, baos);
                            byte[] data = baos.toByteArray();

                            String imageChild = currentAcademyProfile.getDocumentId() + "-" + UUID.randomUUID().toString().replace("-", "").substring(6,10) + "-" + Calendar.getInstance().getTime();

                            UploadTask uploadTask = storageRef.child(imageChild).putBytes(data);
                            int finalI = i;
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    CheckAllImagesUploaded();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    storageRef.child(imageChild).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            currentAcademyProfile.getImages().set(finalI, uri.toString());

                                            CheckAllImagesUploaded();
                                        }
                                    });


                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                CheckAllImagesUploaded();

                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        academiesFirestoreManager = AcademiesFirestoreManager.academyNewInstance();
        branchAcademyFirestoreManager = BranchAcademyFirestoreManager.branchAcademyNewInstance();
        locationsFirestoreManager = LocationsFirestoreManager.locationNewInstance();
        academyCategoryBranchFirestoreManager = AcademyCategoryBranchFirestoreManager.academyCategoryBranchNewInstance();

        storageRef = FirebaseStorage.getInstance().getReference();

        profileNameET.setText(currentAcademyProfile.getAcademyName());
        profileDescriptionET.setText(currentAcademyProfile.getAcademyDescription());
        profileContactNameET.setText(currentAcademyProfile.getContactPersonName());
        profileContactMobileET.setText(currentAcademyProfile.getContactPersonMobile());
        profileEmailET.setText(currentAcademyProfile.getEmail());

        RefreshBranchesListView();

        addNewBranchDialogBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (branchAddressET.getText().toString().isEmpty()
                    && branchMobileET.getText().toString().isEmpty())
                {
                    Toast.makeText(EditProfileActivity.this, "Missing Data", Toast.LENGTH_LONG).show();
                    return;
                }

                if (isEditBranch)
                {
                    branchForEdit.setAddress(branchAddressET.getText().toString());
                    branchForEdit.setMobile(branchMobileET.getText().toString());
                    branchForEdit.setLocationMap(branchMapLinkET.getText().toString());
                    branchForEdit.setLocationRef(locationsFirestoreManager.getLocationRefDocument(locationsList.get(branchLocationLOV.getSelectedItemPosition()).getDocumentId()));
                    branchForEdit.setLocation(locationsList.get(branchLocationLOV.getSelectedItemPosition()));

                    branchAcademyFirestoreManager.updateBranch(branchForEdit);

                    UpdateBranchActivityLocation();

                    Toast.makeText(EditProfileActivity.this, "Branch Updated!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    BranchAcademy newBranch = new BranchAcademy();
                    newBranch.setAcademy(currentAcademyProfile);
                    newBranch.setAcademyRef(academiesFirestoreManager.getAcademyRef(currentAcademyProfile.getDocumentId()));
                    newBranch.setAddress(branchAddressET.getText().toString());
                    newBranch.setMobile(branchMobileET.getText().toString());
                    newBranch.setLocationMap(branchMapLinkET.getText().toString());
                    newBranch.setLocationRef(locationsFirestoreManager.getLocationRefDocument(locationsList.get(branchLocationLOV.getSelectedItemPosition()).getDocumentId()));
                    newBranch.setLocation(locationsList.get(branchLocationLOV.getSelectedItemPosition()));

                    branchAcademyFirestoreManager.createDocument(newBranch);

                    Toast.makeText(EditProfileActivity.this, "Branch Added!", Toast.LENGTH_LONG).show();
                }

                newBranchDialog.dismiss();

                RefreshBranchesListView();
            }
        });

        cancelNewBranchDialogBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBranchDialog.dismiss();
            }
        });

        ArrayList<String> locs = new ArrayList<>();

        for (Locations loc :
                locationsList) {
            locs.add(loc.getLocationName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, locs);

        branchLocationLOV.setAdapter(arrayAdapter);
    }

    private void RefreshBranchesListView()
    {
        branchAcademyFirestoreManager.getAllAcademyBranch(academiesFirestoreManager.getAcademyRef(currentAcademyProfile.getDocumentId()), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshot = task.getResult();

                    if (querySnapshot != null) {
                        currentAcademyBranches = querySnapshot.toObjects(BranchAcademy.class);

                        for (int i = 0; i < currentAcademyBranches.size(); i++)
                        {
                            currentAcademyBranches.get(i).setLocation(SearchLocationInList(locationsList, currentAcademyBranches.get(i).getLocationRef()));
                            currentAcademyBranches.get(i).setAcademy(currentAcademyProfile);
                            currentAcademyBranches.get(i).setAcademyRef(academiesFirestoreManager.getAcademyRef(currentAcademyProfile.getDocumentId()));
                        }

                        resultListAdapter = new BranchesResultListAdapter(currentAcademyBranches, locationsList, categoriesList);

                        branchesListRV.setAdapter(resultListAdapter);

                    }

                } else {
                    Log.w("Error App", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void UpdateBranchActivityLocation(){

        academyCategoryBranchFirestoreManager = AcademyCategoryBranchFirestoreManager.academyCategoryBranchNewInstance();
        academyCategoryBranchFirestoreManager.getAllCategoriesBranch(branchAcademyFirestoreManager.getBranchRefDocument(branchForEdit.getDocumentId()), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshot = task.getResult();

                    if (querySnapshot != null) {
                        currentBranchCategories = querySnapshot.toObjects(AcademyCategoryBranch.class);

                        if (!currentBranchCategories.isEmpty()) {
                            for (int i = 0; i < currentBranchCategories.size(); i++) {
                                if (currentBranchCategories.get(i).getLocationRef() != branchForEdit.getLocationRef())
                                {
                                    currentBranchCategories.get(i).setLocation(branchForEdit.getLocation());
                                    currentBranchCategories.get(i).setLocationRef(branchForEdit.getLocationRef());
                                    academyCategoryBranchFirestoreManager.updateAcademyCategoryBranch(currentBranchCategories.get(i));
                                }
                            }
                        }
                    }

                } else {
                    Log.w("Error App", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void CheckAllImagesUploaded()
    {

        for (int i = 0; i < currentAcademyProfile.getImages().size(); i++) {
            if (!currentAcademyProfile.getImages().get(i).startsWith("https://"))
            {
                return;
            }
        }

        academiesFirestoreManager.updateAcadmey(currentAcademyProfile);
        Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_LONG).show();
    }

    public void OpenEditDialogForBranch(BranchAcademy branch)
    {
        isEditBranch = true;

        addNewBranchDialogBTN.setText(R.string.Update);

        branchAddressET.setText(branch.getAddress());
        branchMobileET.setText(branch.getMobile());
        branchMapLinkET.setText(branch.getLocationMap());

        int selectedLocationPosition = 0;

        for (int i = 0 ; i < branchLocationLOV.getAdapter().getCount(); i++)
        {
            if (branch.getLocation().getLocationName() == branchLocationLOV.getAdapter().getItem(i).toString())
                selectedLocationPosition = i;
        }
        branchLocationLOV.setSelection(selectedLocationPosition);

        branchForEdit = branch;

        newBranchDialog.show();

    }

    public void OpenDeleteDialogForBranch(BranchAcademy branch)
    {
        deleteConfirmationDialog.show();

        deleteConfirmationYesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                academyCategoryBranchFirestoreManager.getAllCategoriesBranch(branchAcademyFirestoreManager.getBranchRefDocument(branch.getDocumentId()), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot != null) {

                                List<AcademyCategoryBranch> allCategoriesForBranchToDelete = querySnapshot.toObjects(AcademyCategoryBranch.class);


                                for (int i = 0; i < allCategoriesForBranchToDelete.size(); i++)
                                {
                                    academyCategoryBranchFirestoreManager.deleteAcademyCategoryBranch(allCategoriesForBranchToDelete.get(i).getDocumentId());
                                }

                                branchAcademyFirestoreManager.deleteBranch(branch.getDocumentId());

                                RefreshBranchesListView();

                                deleteConfirmationDialog.dismiss();

                            }

                        } else {
                            Log.w("Error App", "Error Deleteing: ", task.getException());
                        }
                    }
                });
            }
        });

        deleteConfirmationNoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteConfirmationDialog.dismiss();
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
}