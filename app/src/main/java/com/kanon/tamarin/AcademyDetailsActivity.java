package com.kanon.tamarin;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.adapters.ImageSliderAdapter;
import com.kanon.tamarin.firestore.AcademiesFirestoreManager;
import com.kanon.tamarin.firestore.AnalyticsFirestoreManager;
import com.kanon.tamarin.models.AcademyCategoryBranch;
import com.kanon.tamarin.models.Analytics;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AcademyDetailsActivity extends AppCompatActivity {

    private AcademyCategoryBranch currentAcademy;

    private TextView academyNameTV, academyDescriptionTV, academyCategoryNameTV, academyRateTV, academyMobileTV, academyAddressTV, academyEmailTV;
    private ImageView academyProfilePicImage;
    private SliderView sliderView;

    private Button CallBTN, EmailBTN, MapBTN, FBBTN, YTBTN, IGBTN, TWBTN;

    private AnalyticsFirestoreManager analyticsFirestoreManager;
    private AcademiesFirestoreManager academiesFirestoreManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_details);

        currentAcademy = getIntent().getParcelableExtra("currentAcademy");

        academyNameTV = findViewById(R.id.AcademyNameTV);
        academyDescriptionTV = findViewById(R.id.AcademyDescriptionTV);
        academyCategoryNameTV = findViewById(R.id.AcademyCategoryNameTV);
        academyRateTV = findViewById(R.id.AcademyRateTV);

        academyAddressTV = findViewById(R.id.AcademyAddressTV);

        CallBTN = findViewById(R.id.AcademyCallBTN);
        EmailBTN = findViewById(R.id.AcademyEmailBTN);
        MapBTN = findViewById(R.id.AcademyMapBTN);
        FBBTN = findViewById(R.id.AcademyFBBTN);
        YTBTN = findViewById(R.id.AcademyYTBTN);
        IGBTN = findViewById(R.id.AcademyIGBTN);
        TWBTN = findViewById(R.id.AcademyTWBTN);

        academyProfilePicImage = findViewById(R.id.AcademyProfilePicImg);

        sliderView = findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new ImageSliderAdapter(this));

        CallBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!CheckPermission(new String[] {CALL_PHONE}))
                    return;

                UpdateAnalyticsCount("call");

                Intent intent = new Intent(Intent.ACTION_DIAL);
                String callSTR = new StringBuilder().append("tel:").append(currentAcademy.getBranch().getMobile().toString()).toString();

                intent.setData(Uri.parse(callSTR));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        EmailBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateAnalyticsCount("mail");

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + currentAcademy.getAcademy().getEmail()));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        MapBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (!currentAcademy.getBranch().getLatitude().isEmpty()
                        && !currentAcademy.getBranch().getLongitude().isEmpty())
                    intent.setData(Uri.parse(new StringBuilder().append("geo:").append(currentAcademy.getBranch().getLatitude()).append(",").append(currentAcademy.getBranch().getLongitude()).append("?q=").append(currentAcademy.getBranch().getLatitude()).append(",").append(currentAcademy.getBranch().getLongitude()).append("(").append(currentAcademy.getAcademy().getAcademyName()).append(")").toString()));
                else
                    intent.setData(Uri.parse(currentAcademy.getBranch().getLocationMap()));

                UpdateAnalyticsCount("map");

                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        FBBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String FBLink = "";

                PackageManager packageManager = getPackageManager();
                try {
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    FBLink = "fb://facewebmodal/f?href=" + currentAcademy.getAcademy().getFbLink();
                } catch (Exception e) {
                    FBLink = currentAcademy.getAcademy().getFbLink(); //normal web url
                }

                UpdateAnalyticsCount("fb");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(FBLink));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        TWBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String twLink = "";

                PackageManager packageManager = getPackageManager();
                try {
                    getPackageManager().getPackageInfo("com.twitter.android", 0);
                    twLink = "twitter://user?screen_name=" + currentAcademy.getAcademy().getTwitterLink();
                } catch (Exception e) {
                    twLink = "https://twitter.com/" + currentAcademy.getAcademy().getTwitterLink(); //normal web url
                }

                UpdateAnalyticsCount("tw");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(twLink));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        IGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateAnalyticsCount("ig");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentAcademy.getAcademy().getInstaLink()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        YTBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateAnalyticsCount("yt");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentAcademy.getAcademy().getYoutubeLink()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        academyNameTV.setText(currentAcademy.getAcademy().getAcademyName());
        academyDescriptionTV.setText(currentAcademy.getAcademy().getAcademyDescription());
        academyCategoryNameTV.setText(currentAcademy.getCategory().getCategoryName());

        if (currentAcademy.getRatePerSession() > 1)
            if (currentAcademy.getSessions() != null && currentAcademy.getSessions() > 0)
                academyRateTV.setText(new StringBuilder().append("EGP ").append( currentAcademy.getRatePerSession().toString()).append( " Per " ).append(currentAcademy.getSessions()).append(" Session"));
            else
                academyRateTV.setText(new StringBuilder().append("EGP ").append( currentAcademy.getRatePerSession().toString()));

        academyAddressTV.setText(currentAcademy.getBranch().getAddress());

        if (!currentAcademy.getAcademy().getProfilePic().isEmpty())
            Picasso.get().load(currentAcademy.getAcademy().getProfilePic()).into(academyProfilePicImage);
        else
            academyProfilePicImage.setImageResource(R.mipmap.ic_launcher);

        if (currentAcademy.getAcademy().getImages() != null)
        {
            ((ImageSliderAdapter)sliderView.getSliderAdapter()).renewItems(currentAcademy.getAcademy().getImages());

            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
            sliderView.startAutoCycle();
        }
    }

    private Boolean CheckPermission(String[] permssion)
    {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), permssion[0]);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        ActivityCompat.requestPermissions(this, permssion, 200);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                boolean isAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (isAccepted)
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void UpdateAnalyticsCount(String type)
    {
        analyticsFirestoreManager = AnalyticsFirestoreManager.analyticsNewInstance();
        academiesFirestoreManager = AcademiesFirestoreManager.academyNewInstance();

        analyticsFirestoreManager.getAnalyticsFor(academiesFirestoreManager.getAcademyRef(currentAcademy.getAcademy().getDocumentId()), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot querySnapshot = task.getResult();

                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        Analytics analytics = querySnapshot.toObjects(Analytics.class).get(0);

                        if (type.contentEquals("call"))
                        {
                            analytics.setCallCount(analytics.getCallCount() + 1);
                        }
                        else if (type.contentEquals("fb"))
                        {
                            analytics.setFbCount(analytics.getFbCount() + 1);
                        }
                        else if (type.contentEquals("ig"))
                        {
                            analytics.setIgCount(analytics.getIgCount() + 1);
                        }
                        else if (type.contentEquals("tw"))
                        {
                            analytics.setTwitterCount(analytics.getTwitterCount() + 1);
                        }
                        else if (type.contentEquals("yt"))
                        {
                            analytics.setYoutubeCount(analytics.getYoutubeCount() + 1);
                        }
                        else if (type.contentEquals("mail"))
                        {
                            analytics.setMailCount(analytics.getMailCount() + 1);
                        }
                        else if (type.contentEquals("map"))
                        {
                            analytics.setMapCount(analytics.getMapCount() + 1);
                        }

                        analyticsFirestoreManager.updateAnalytics(analytics);
                    }
                    else
                    {
                        Analytics analytics = new Analytics();
                        analytics.setForRef(academiesFirestoreManager.getAcademyRef(currentAcademy.getAcademy().getDocumentId()));

                        analyticsFirestoreManager.createDocument(analytics);
                    }

                }
            }
        });
    }
}