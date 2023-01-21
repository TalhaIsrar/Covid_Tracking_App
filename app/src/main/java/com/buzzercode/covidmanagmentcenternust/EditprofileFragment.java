package com.buzzercode.covidmanagmentcenternust;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.CMS_NAME;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SHARED_PREFS;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.TYPE;

public class EditprofileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditprofileFragment() {
    }

    public static EditprofileFragment newInstance(String param1, String param2) {
        EditprofileFragment fragment = new EditprofileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editprofile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String cms_id = sharedPreferences.getString(CMS_NAME, null);
        final String type = sharedPreferences.getString(TYPE, null);

        final ImageView update_profile_img = getActivity().findViewById(R.id.update_profile_img);
        final ProgressBar wait = getActivity().findViewById(R.id.pic_loading);
        final TextView profile_name = getActivity().findViewById(R.id.edit_name_display);

        final EditText guardian_view = getActivity().findViewById(R.id.update_guardian);
        final EditText age_view = getActivity().findViewById(R.id.update_age);
        final EditText email_view = getActivity().findViewById(R.id.update_email);
        final EditText department_view = getActivity().findViewById(R.id.update_department);
        final EditText classes_view = getActivity().findViewById(R.id.update_class);
        final EditText phone_view = getActivity().findViewById(R.id.update_phone);
        final EditText address_view = getActivity().findViewById(R.id.update_address);

        final Button update = getActivity().findViewById(R.id.update_profile);
        final ProgressBar updating = getActivity().findViewById(R.id.updating);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (type.equals("student"))
        {
            DocumentReference user = db.collection("School").document("Accounts").collection("students").document(cms_id);
            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        String address = (String) doc.get("address");
                        String phone = (String) doc.get("phone");
                        String classes = (String) doc.get("section");
                        String department = (String) doc.get("department");
                        String email = (String) doc.get("email");
                        String age = (String) doc.get("age");
                        String guardian = (String) doc.get("parent");
                        String name = (String) doc.get("name");
                        String img = (String) doc.get("image");

                        profile_name.setText(name);
                        profile_name.setVisibility(View.VISIBLE);
                        guardian_view.setText(guardian);
                        age_view.setText(age);
                        email_view.setText(email);
                        department_view.setText(department);
                        classes_view.setText(classes);
                        phone_view.setText(phone);
                        address_view.setText(address);
                        if (img.equals("")) {
                            update_profile_img.setImageResource(R.drawable.logo);
                        } else {
                            Glide.with(getActivity().getApplicationContext())
                                    .load(img)
                                    .into(update_profile_img);
                        }
                        wait.setVisibility(View.INVISIBLE);
                        update_profile_img.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        else if (type.equals("faculty"))
        {
            classes_view.setVisibility(View.GONE);
            DocumentReference user = db.collection("School").document("Accounts").collection("faculty").document(cms_id);
            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        String address = (String) doc.get("address");
                        String phone = (String) doc.get("phone");
                        String department = (String) doc.get("department");
                        String email = (String) doc.get("email");
                        String age = (String) doc.get("age");
                        String guardian = (String) doc.get("parent");
                        String name = (String) doc.get("name");
                        String img = (String) doc.get("image");

                        profile_name.setText(name);
                        profile_name.setVisibility(View.VISIBLE);
                        guardian_view.setText(guardian);
                        age_view.setText(age);
                        email_view.setText(email);
                        department_view.setText(department);
                        phone_view.setText(phone);
                        address_view.setText(address);
                        if (img.equals("")) {
                            update_profile_img.setImageResource(R.drawable.logo);
                        } else {
                            Glide.with(getActivity().getApplicationContext())
                                    .load(img)
                                    .into(update_profile_img);
                        }
                        wait.setVisibility(View.INVISIBLE);
                        update_profile_img.setVisibility(View.VISIBLE);
                    }
                }
            });

        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.setVisibility(View.INVISIBLE);
                updating.setVisibility(View.VISIBLE);
                final String final_parent = guardian_view.getText().toString();
                final String final_age = age_view.getText().toString();
                final String final_email = email_view.getText().toString();
                final String final_section = classes_view.getText().toString();
                final String final_phone = phone_view.getText().toString();
                final String final_address = address_view.getText().toString();
                final String final_department = department_view.getText().toString();

                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                if (final_parent.isEmpty()|| final_age.isEmpty()|| final_email.isEmpty() ||
                        final_phone.isEmpty() || final_address.isEmpty() || final_department.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter All Values", Toast.LENGTH_SHORT).show();
                    update.setVisibility(View.VISIBLE);
                    updating.setVisibility(View.GONE);
                }
                else
                {
                    if (type.equals("student"))
                    {
                        DocumentReference docIdRef = db.collection("School").document("Accounts").collection("students").document(cms_id);
                        docIdRef.update("parent", final_parent);
                        docIdRef.update("age", final_age);
                        docIdRef.update("email", final_email);
                        docIdRef.update("section", final_section);
                        docIdRef.update("phone", final_phone);
                        docIdRef.update("address", final_address);
                        docIdRef.update("department", final_department)
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
                        classes_view.setText(final_section);
                        Toast.makeText(getActivity().getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        update.setVisibility(View.VISIBLE);
                        updating.setVisibility(View.GONE);
                    }
                    else if (type.equals("faculty"))
                    {
                        DocumentReference docIdRef = db.collection("School").document("Accounts").collection("faculty").document(cms_id);
                        docIdRef.update("parent", final_parent);
                        docIdRef.update("age", final_age);
                        docIdRef.update("email", final_email);
                        docIdRef.update("phone", final_phone);
                        docIdRef.update("address", final_address);
                        docIdRef.update("department", final_department)
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
                        Toast.makeText(getActivity().getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

                    }

                    guardian_view.setText(final_parent);
                    age_view.setText(final_age);
                    email_view.setText(final_email);
                    department_view.setText(final_department);
                    phone_view.setText(final_phone);
                    address_view.setText(final_address);
                    update.setVisibility(View.VISIBLE);
                    updating.setVisibility(View.GONE);
                }
            }
        });
    }
}