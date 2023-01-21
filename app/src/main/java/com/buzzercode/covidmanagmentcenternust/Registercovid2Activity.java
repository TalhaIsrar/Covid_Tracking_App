package com.buzzercode.covidmanagmentcenternust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.TYPE;

public class Registercovid2Activity extends AppCompatActivity {
    String affected = "0";
    String status = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registercovid2);
        TextView submit_covid = (TextView) findViewById(R.id.submit_covid);
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.status_check);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton2 = (RadioButton) group.findViewById(checkedId);
            }
        });
        submit_covid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup2.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(Registercovid2Activity.this, "Please select a choice", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    RadioButton radioButton2 = (RadioButton) radioGroup2.findViewById(selectedId);
                    if (radioButton2.getText().equals("Recovered")) {
                        status = "2";
                        affected = "1";
                    } else if (radioButton2.getText().equals("Active")) {
                        status = "1";
                        affected = "1";
                    } else {
                        status = "3";
                        affected = "1";
                    }
                    register_void(affected, status);
                }
            }
        });
    }

    void register_void(String affected, String status) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String cms_id = sharedPreferences.getString(CMS_NAME, null);
        final String covid_reg = sharedPreferences.getString(COVID_REG, null);
        final String type = sharedPreferences.getString(TYPE, null);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("Covid").document(cms_id);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                    Map<String, Object> newStudent = new HashMap<>();
                    newStudent.put("affected", affected);
                    newStudent.put("status", status);
                    db.collection("Covid").document(cms_id).set(newStudent);
                    editor.putString(COVID_REG, "yes");
                    editor.apply();
                    String value = "students";
                    if (type.equals("faculty")) {
                        value = "faculty";
                    }
                    DocumentReference ref2 = db.collection("School").document("Accounts").collection(value).document(cms_id);
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

                    Intent intent = new Intent(Registercovid2Activity.this, MainActivity.class);
                    Registercovid2Activity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
    }
}