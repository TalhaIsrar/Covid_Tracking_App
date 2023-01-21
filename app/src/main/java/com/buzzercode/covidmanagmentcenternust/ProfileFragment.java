package com.buzzercode.covidmanagmentcenternust;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.CMS_NAME;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SHARED_PREFS;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.TYPE;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String cms_id = sharedPreferences.getString(CMS_NAME, null);
        final String type = sharedPreferences.getString(TYPE, null);

        final ImageView profile_img = getActivity().findViewById(R.id.profile_img);
        final ProgressBar wait = getActivity().findViewById(R.id.pic_loading);
        final TextView profile_name = getActivity().findViewById(R.id.name_display);

        final TextView cms_uid_view = getActivity().findViewById(R.id.profile_cms_uid);
        final TextView guardian_view = getActivity().findViewById(R.id.profile_guardian);
        final TextView age_view = getActivity().findViewById(R.id.profile_age);
        final TextView id_view = getActivity().findViewById(R.id.profile_id);
        final TextView email_view = getActivity().findViewById(R.id.profile_email);
        final TextView department_view = getActivity().findViewById(R.id.profile_department);
        final TextView classes_view = getActivity().findViewById(R.id.profile_class);
        final TextView phone_view = getActivity().findViewById(R.id.profile_phone);
        final TextView address_view = getActivity().findViewById(R.id.profile_adddress);
        final TextView classes_view2 = getActivity().findViewById(R.id.profile_class_head);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (type.equals("student")) {
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
                        String id = (String) doc.get("id");
                        String age = (String) doc.get("age");
                        String guardian = (String) doc.get("parent");
                        String name = (String) doc.get("name");
                        String cms_name = (String) doc.get("cms_uid");
                        String img = (String) doc.get("image");

                        profile_name.setText(name);
                        cms_uid_view.setText(cms_name);
                        guardian_view.setText(guardian);
                        age_view.setText(age);
                        id_view.setText(id);
                        email_view.setText(email);
                        department_view.setText(department);
                        classes_view.setText(classes);
                        phone_view.setText(phone);
                        address_view.setText(address);
                        if (img.equals("")) {
                            profile_img.setImageResource(R.drawable.logo);
                        } else {
                            Glide.with(getActivity().getApplicationContext())
                                    .load(img)
                                    .into(profile_img);
                        }
                        wait.setVisibility(View.INVISIBLE);
                        profile_img.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else if (type.equals("faculty")) {
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
                        String id = (String) doc.get("id");
                        String age = (String) doc.get("age");
                        String guardian = (String) doc.get("parent");
                        String name = (String) doc.get("name");
                        String cms_name = (String) doc.get("cms_uid");
                        String img = (String) doc.get("image");

                        profile_name.setText(name);
                        cms_uid_view.setText(cms_name);
                        guardian_view.setText(guardian);
                        age_view.setText(age);
                        id_view.setText(id);
                        email_view.setText(email);
                        department_view.setText(department);
                        classes_view2.setVisibility(View.GONE);
                        classes_view.setVisibility(View.GONE);
                        phone_view.setText(phone);
                        address_view.setText(address);
                        if (img.equals("")) {
                            profile_img.setImageResource(R.drawable.logo);
                        } else {
                            Glide.with(getActivity().getApplicationContext())
                                    .load(img)
                                    .into(profile_img);
                        }
                        wait.setVisibility(View.INVISIBLE);
                        profile_img.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}