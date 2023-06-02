package com.kanon.tamarin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.kanon.tamarin.firestore.AcademiesFirestoreManager;
import com.kanon.tamarin.models.Academies;
import com.kanon.tamarin.sharedpref.TamarinSharedPrefHandler;

import java.util.List;

public class LoginAcademyActivity extends AppCompatActivity {

    private EditText emailET, passwordET;
    private Button loginBTN;

    private AcademiesFirestoreManager academiesFirestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_academy);

        emailET = findViewById(R.id.academy_login_email);
        passwordET = findViewById(R.id.academy_login_password);
        loginBTN = findViewById(R.id.academy_login_submit_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailET.getText().toString().isEmpty()
                        || passwordET.getText().toString().isEmpty())
                {
                    Toast.makeText(LoginAcademyActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                academiesFirestoreManager = AcademiesFirestoreManager.academyNewInstance();

                academiesFirestoreManager.LoginAcademy(emailET.getText().toString(), passwordET.getText().toString(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot != null) {

                                if (querySnapshot.isEmpty())
                                {
                                    Toast.makeText(LoginAcademyActivity.this, "Wrong email or password please try again", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                List<Academies> currentAcademy = querySnapshot.toObjects(Academies.class);
                                new TamarinSharedPrefHandler(LoginAcademyActivity.this).InsertValue("currentAcademy", currentAcademy.get(0).getDocumentId());
                                finish();
                            }
                            else
                            {
                                Toast.makeText(LoginAcademyActivity.this, "Wrong email or password please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

    }
}