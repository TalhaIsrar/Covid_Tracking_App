package com.buzzercode.covidmanagmentcenternust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.buzzercode.covidmanagmentcenternust.LoginActivity.ACTIVE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.AFFECTED;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.CMS_NAME;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.COVID_REG;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.IMAGE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.RECOVERED;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.REGISTERED;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SERIOUS;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SHARED_PREFS;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SIGNED_IN;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.TYPE;

public class ExtraloginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar mProgressBarExtra;
    private Button loginextra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extralogin);
        mAuth = FirebaseAuth.getInstance();

        final EditText usernamefieldextra = findViewById(R.id.username_extra);
        final EditText passwordfieldextra = findViewById(R.id.password_extra);
        mProgressBarExtra = findViewById(R.id.progress_extra);
        mProgressBarExtra.getIndeterminateDrawable().setColorFilter(0xFF0175FA,
                android.graphics.PorterDuff.Mode.MULTIPLY);
        loginextra = findViewById(R.id.login_extra);
        LinearLayout buttons = findViewById(R.id.buttons_extra);

        loginextra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBarExtra.setVisibility(View.VISIBLE);
                buttons.setVisibility(View.INVISIBLE);

                final String usernameextra = usernamefieldextra.getText().toString();
                final String passwordextra = passwordfieldextra.getText().toString();
                if (usernameextra.isEmpty() || passwordextra.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Values", Toast.LENGTH_SHORT).show();
                    buttons.setVisibility(View.VISIBLE);
                    mProgressBarExtra.setVisibility(View.GONE);
                } else {
                    if (usernameextra.toLowerCase().trim().equals("admin")) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference user = db.collection("School").document(usernameextra);
                        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    String firebasepasswordextra = (String) doc.get("admin");
                                    if (passwordextra.equals(firebasepasswordextra)) {
                                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(SIGNED_IN, true);
                                        editor.putString(CMS_NAME, usernameextra);
                                        editor.putString(TYPE, "admin");
                                        editor.apply();
                                        Intent intent = new Intent(ExtraloginActivity.this, AdminmainActivity.class);
                                        ExtraloginActivity.this.startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                                        finish();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please Enter Correct Values", Toast.LENGTH_SHORT).show();
                                        buttons.setVisibility(View.VISIBLE);
                                        mProgressBarExtra.setVisibility(View.GONE);
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                    buttons.setVisibility(View.VISIBLE);
                                    mProgressBarExtra.setVisibility(View.GONE);
                                }
                            }
                        });

                    } else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference user = db.collection("School").document("Accounts").collection("faculty").document(usernameextra);
                        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    String firebasepasswordextra = (String) doc.get("password");
                                    String image = (String) doc.get("image");
                                    String covid_reg = (String) doc.get("covid");

                                    if (passwordextra.equals(firebasepasswordextra)) {

                                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(SIGNED_IN, true);
                                        editor.putString(CMS_NAME, usernameextra);
                                        editor.putString(TYPE, "faculty");
                                        editor.putString(REGISTERED, "0");
                                        editor.putString(AFFECTED, "0");
                                        editor.putString(RECOVERED, "0");
                                        editor.putString(ACTIVE, "0");
                                        editor.putString(SERIOUS, "0");
                                        editor.putString(IMAGE, image);
                                        editor.putString(COVID_REG, covid_reg);
                                        editor.apply();

                                        String final_email = usernameextra.trim().replace(" ", "/");
                                        String email = "f/" + final_email + "@nust.edu.pk";
                                        sign_up(email, passwordextra, "faculty", usernameextra);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please Enter Correct Values", Toast.LENGTH_SHORT).show();
                                        buttons.setVisibility(View.VISIBLE);
                                        mProgressBarExtra.setVisibility(View.GONE);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                    buttons.setVisibility(View.VISIBLE);
                                    mProgressBarExtra.setVisibility(View.GONE);
                                }
                            }
                        });
                    }

                }
            }
        });


        TextView register_faculty = findViewById(R.id.register_faculty);

        register_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraloginActivity.this, RegisterfacultyActivity.class);
                ExtraloginActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
        });
    }


    private void updateUI(FirebaseUser user, String person_type) {

        if (person_type.equals("faculty")) {
            Intent intent = new Intent(ExtraloginActivity.this, FacultymainActivity.class);
            ExtraloginActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        }
    }

    public void sign_up(final String email, final String password, final String person, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdate);
                            sign_in(email, password, person);

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                sign_in(email, password, person);

                            }
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Log.e("Exception", e.getMessage());
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Log.e("Exception", e.getMessage());
                            } catch (FirebaseAuthUserCollisionException e) {
                                Log.e("Exception", e.getMessage());
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }

                    }
                });
    }

    public void sign_in(String email, String password, final String person_type) {
        mProgressBarExtra = findViewById(R.id.progress_extra);
        loginextra = findViewById(R.id.login_extra);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginextra.setVisibility(View.VISIBLE);
                            mProgressBarExtra.setVisibility(View.GONE);
                            updateUI(user, person_type);

                        } else {
                            Toast.makeText(ExtraloginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loginextra.setVisibility(View.VISIBLE);
                            mProgressBarExtra.setVisibility(View.GONE);
                            updateUI(null, "None");

                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExtraloginActivity.this, LoginActivity.class);
        ExtraloginActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

    }
}
