package com.buzzercode.covidmanagmentcenternust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterstudentActivity extends AppCompatActivity {

    private ImageView add_image, imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    StorageReference storageReference;
    Boolean image = false;

    String image_path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerstudent);

        final EditText name = findViewById(R.id.add_name);
        final EditText cms_uid = findViewById(R.id.add_cms_uid);
        final EditText parent = findViewById(R.id.add_parent);
        final EditText password = findViewById(R.id.add_password);
        final EditText age = findViewById(R.id.add_age);
        final EditText id = findViewById(R.id.add_id);
        final EditText email = findViewById(R.id.add_email);
        final EditText department = findViewById(R.id.add_department);
        final EditText section = findViewById(R.id.add_section);
        final EditText phone = findViewById(R.id.add_phone);
        final EditText address = findViewById(R.id.add_address);
        final Button add_student = findViewById(R.id.add_student);
        final ProgressBar adding_student = findViewById(R.id.adding_student);
        adding_student.getIndeterminateDrawable().setColorFilter(0xFF0175FA,
                android.graphics.PorterDuff.Mode.MULTIPLY);
        add_image = findViewById(R.id.add_image);
        imageView = findViewById(R.id.profile_image);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        RelativeLayout background = findViewById(R.id.btn_cont);

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_student.setVisibility(View.GONE);
                background.setVisibility(View.GONE);
                adding_student.setVisibility(View.VISIBLE);
                final String final_name = name.getText().toString();
                final String final_cms_uid = cms_uid.getText().toString();
                final String final_parent = parent.getText().toString();
                final String final_password = password.getText().toString();
                final String final_age = age.getText().toString();
                final String final_id = id.getText().toString();
                final String final_email = email.getText().toString();
                final String final_section = section.getText().toString();
                final String final_phone = phone.getText().toString();
                final String final_address = address.getText().toString();
                final String final_department = department.getText().toString();

                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                if (final_name.isEmpty() || final_cms_uid.isEmpty() || final_parent.isEmpty() || final_password.isEmpty() ||
                        final_age.isEmpty() || final_id.isEmpty() || final_email.isEmpty() || final_section.isEmpty() ||
                        final_phone.isEmpty() || final_address.isEmpty() || final_department.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter All Values", Toast.LENGTH_SHORT).show();
                    add_student.setVisibility(View.VISIBLE);
                    background.setVisibility(View.VISIBLE);
                    adding_student.setVisibility(View.GONE);
                } else {
                    if (final_password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password is too small", Toast.LENGTH_SHORT).show();
                        add_student.setVisibility(View.VISIBLE);
                        background.setVisibility(View.VISIBLE);
                        adding_student.setVisibility(View.GONE);
                    }
                    else if (!image)
                    {
                        Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                        add_student.setVisibility(View.VISIBLE);
                        background.setVisibility(View.VISIBLE);
                        adding_student.setVisibility(View.GONE);
                    }
                    else {
                        add_image.setVisibility(View.GONE);
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();

                        mAuth.signInAnonymously().addOnSuccessListener(RegisterstudentActivity.this, new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                if (filePath != null) {
                                    StorageReference ref
                                            = storageReference
                                            .child("profilepic/students/" + cms_uid);

                                    Bitmap bmp = null;
                                    try {
                                        bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bmp.compress(Bitmap.CompressFormat.JPEG, 7, baos);
                                    byte[] data = baos.toByteArray();
                                    UploadTask uploadTask2 = ref.putBytes(data);
                                    uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterstudentActivity.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                                        }
                                    }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }
                                            return ref.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downloadUri = task.getResult();
                                                if (downloadUri == null) {
                                                    return;
                                                }
                                                image_path = downloadUri.toString();
                                                DocumentReference docIdRef = db.collection("School").document("Accounts").collection("students").document(final_cms_uid);
                                                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                Toast.makeText(getApplicationContext(), "User Id Exists", Toast.LENGTH_SHORT).show();
                                                                add_student.setVisibility(View.VISIBLE);
                                                                background.setVisibility(View.VISIBLE);
                                                                adding_student.setVisibility(View.GONE);
                                                            } else {
                                                                Map<String, Object> newStudent = new HashMap<>();
                                                                newStudent.put("name", final_name);
                                                                newStudent.put("cms_uid", final_cms_uid);
                                                                newStudent.put("parent", final_parent);
                                                                newStudent.put("password", final_password);
                                                                newStudent.put("age", final_age);
                                                                newStudent.put("id", final_id);
                                                                newStudent.put("email", final_email);
                                                                newStudent.put("section", final_section);
                                                                newStudent.put("phone", final_phone);
                                                                newStudent.put("address", final_address);
                                                                newStudent.put("department", final_department);
                                                                newStudent.put("image", image_path);
                                                                newStudent.put("covid", "no");
                                                                newStudent.put("last_message","000000");
                                                                db.collection("School").document("Accounts").collection("students").document(final_cms_uid).set(newStudent);
                                                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(RegisterstudentActivity.this, LoginActivity.class);
                                                                RegisterstudentActivity.this.startActivity(intent);
                                                                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });

                                }
                            }
                        }).addOnFailureListener(RegisterstudentActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e("EXE", "signInAnonymously:FAILURE", exception);
                            }
                        });

                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(RegisterstudentActivity.this);
        builder.setMessage("Student has not been registered. You want to exit anyways?");
        builder.setTitle("You want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RegisterstudentActivity.this, LoginActivity.class);
                        RegisterstudentActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
        image = true;
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);

                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
