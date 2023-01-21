package com.buzzercode.covidmanagmentcenternust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.buzzercode.covidmanagmentcenternust.LoginActivity.CMS_NAME;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.COVID_REG;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SHARED_PREFS;


public class RegistercovidActivity extends AppCompatActivity {

    String affected = "0";
    String status = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registercovid);

        Button infected_check = (Button) findViewById(R.id.after_infected);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.infected_check);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            }
        });

        infected_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(RegistercovidActivity.this, "Please select a choice", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
                    if (radioButton.getText().equals("Yes")) {
                        Intent intent = new Intent(RegistercovidActivity.this, Registercovid2Activity.class);
                        RegistercovidActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    } else {
                        affected = "0";
                        status = "0";
                        register_void(affected, status);
                    }
                }
            }
        });
    }

    void register_void(String affected, String status) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String cms_id = sharedPreferences.getString(CMS_NAME, null);
        final String covid_reg = sharedPreferences.getString(COVID_REG, null);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("Covid").document(cms_id);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(getApplicationContext(), "User Id Exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Map<String, Object> newStudent = new HashMap<>();
                        newStudent.put("affected", affected);
                        newStudent.put("status", status);
                        db.collection("Covid").document(cms_id).set(newStudent);
                        editor.putString(COVID_REG, "yes");
                        editor.apply();
                        DocumentReference ref2 = db.collection("School").document("Accounts").collection("students").document(cms_id);
                        ref2
                                .update("covid", "yes")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

                        Intent intent = new Intent(RegistercovidActivity.this, MainActivity.class);
                        RegistercovidActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    }
                } else {
                }
            }
        });
    }
}
