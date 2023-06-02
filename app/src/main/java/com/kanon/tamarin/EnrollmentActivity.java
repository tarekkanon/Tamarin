package com.kanon.tamarin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kanon.tamarin.firestore.EnrollRequestFirestoreManager;
import com.kanon.tamarin.models.EnrollRequest;

public class EnrollmentActivity extends AppCompatActivity {

    private EnrollRequestFirestoreManager enrollRequestFirestoreManager;
    private EditText enrollNameET,enrollContactPersonNameET,enrollContactPersonMobileET,enrollEmailET;
    private Button enrollSubmitBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        enrollNameET = findViewById(R.id.EnrollName);
        enrollContactPersonNameET = findViewById(R.id.EnrollContactPersonName);
        enrollContactPersonMobileET = findViewById(R.id.EnrollContactPersonMobile);
        enrollEmailET = findViewById(R.id.EnrollEmail);
        enrollSubmitBTN = findViewById(R.id.EnrollSubmitBTN);

    }

    @Override
    protected void onStart() {
        super.onStart();

        enrollSubmitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!enrollNameET.getText().toString().isEmpty()
                        & !enrollContactPersonNameET.getText().toString().isEmpty()
                        & !enrollContactPersonMobileET.getText().toString().isEmpty())
                {
                    enrollRequestFirestoreManager = EnrollRequestFirestoreManager.enrollRequestNewInstance();
                    EnrollRequest request = new EnrollRequest();
                    request.setName(enrollNameET.getText().toString());
                    request.setContactPersonName(enrollContactPersonNameET.getText().toString());
                    request.setContactPersonMobile(enrollContactPersonMobileET.getText().toString());
                    request.setEmail(enrollEmailET.getText().toString());

                    enrollRequestFirestoreManager.createDocument(request);

                    Toast.makeText(EnrollmentActivity.this,"Done", Toast.LENGTH_LONG).show();

                    finish();

                }
                else
                {
                    Toast.makeText(EnrollmentActivity.this,"Missing data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}