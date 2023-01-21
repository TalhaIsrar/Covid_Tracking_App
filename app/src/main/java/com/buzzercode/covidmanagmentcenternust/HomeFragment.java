package com.buzzercode.covidmanagmentcenternust;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.ACTIVE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.AFFECTED;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.CMS_NAME;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.LAST_MESSAGE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.RECOVERED;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.REGISTERED;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SERIOUS;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SHARED_PREFS;

public class HomeFragment extends Fragment {

    int registered_patients = 0;
    int affected_patients = 0;
    int recovered_patients = 0;
    int active_patients = 0;
    int serious_patients = 0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String registered = sharedPreferences.getString(REGISTERED, null);
        final String affected = sharedPreferences.getString(AFFECTED, null);
        final String recovered = sharedPreferences.getString(RECOVERED, null);
        final String active = sharedPreferences.getString(ACTIVE, null);
        final String serious = sharedPreferences.getString(SERIOUS, null);
        final String cms_id = sharedPreferences.getString(CMS_NAME, null);

        final TextView registered_view = getActivity().findViewById(R.id.registered_patients);
        final TextView affected_view = getActivity().findViewById(R.id.affected_patients);
        final TextView recovered_view = getActivity().findViewById(R.id.recovered_patients);
        final TextView active_view = getActivity().findViewById(R.id.active_patients);
        final TextView serious_view = getActivity().findViewById(R.id.serious_patients);

        registered_view.setText(registered);
        affected_view.setText(affected);
        recovered_view.setText(recovered);
        active_view.setText(active);
        serious_view.setText(serious);

        registered_patients = 0;
        affected_patients = 0;
        recovered_patients = 0;
        active_patients = 0;
        serious_patients = 0;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference user = db.collection("School").document("Accounts").collection("students").document(cms_id);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String last_message = (String) doc.get("last_message");
                    editor.putString(LAST_MESSAGE, String.valueOf(last_message));
                    editor.apply();
                }
            }
        });

        db.collection("Covid")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                registered_patients++;
                                Map<String, Object> note_info = document.getData();
                                String affected_field = (String) note_info.get("affected");

                                if (affected_field.equals("1")) {
                                    affected_patients += 1;
                                }
                                String status_field = (String) note_info.get("status");
                                if (status_field.equals("1")) {
                                    active_patients += 1;
                                }
                                if (status_field.equals("2")) {
                                    recovered_patients += 1;
                                }
                                if (status_field.equals("3")) {
                                    serious_patients += 1;
                                }
                                registered_view.setText(String.valueOf(registered_patients));
                                affected_view.setText(String.valueOf(affected_patients));
                                recovered_view.setText(String.valueOf(recovered_patients));
                                active_view.setText(String.valueOf(active_patients));
                                serious_view.setText(String.valueOf(serious_patients));

                                editor.putString(REGISTERED, String.valueOf(registered_patients));
                                editor.putString(AFFECTED, String.valueOf(affected_patients));
                                editor.putString(RECOVERED, String.valueOf(recovered_patients));
                                editor.putString(ACTIVE, String.valueOf(active_patients));
                                editor.putString(SERIOUS, String.valueOf(serious_patients));
                                editor.apply();
                            }

                        }
                    }
                });
    }
}