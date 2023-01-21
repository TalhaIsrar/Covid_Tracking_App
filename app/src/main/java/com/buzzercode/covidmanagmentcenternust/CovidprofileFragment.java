package com.buzzercode.covidmanagmentcenternust;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.scwang.wave.MultiWaveHeader;

import static android.content.Context.MODE_PRIVATE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.CMS_NAME;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SHARED_PREFS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CovidprofileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CovidprofileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String affected = "0";
    String status = "0";

    public CovidprofileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CovidprofileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CovidprofileFragment newInstance(String param1, String param2) {
        CovidprofileFragment fragment = new CovidprofileFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_covidprofile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String cms_id = sharedPreferences.getString(CMS_NAME, null);

        final MultiWaveHeader waveView;
        waveView = getActivity().findViewById(R.id.waveview);
        final TextView status_view = getActivity().findViewById(R.id.status);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("Covid").document(cms_id);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String affected = (String) doc.get("affected");
                    String status = (String) doc.get("status");

                    if (affected.equals("0")){
                        status_view.setText("UnAffected");
                        status_view.setTextColor(Color.GREEN);

                    }
                    else
                    {
                        if (status.equals("1"))
                        {
                            status_view.setText("Active");
                            status_view.setTextColor(Color.YELLOW);
                        }
                        if (status.equals("2"))
                        {
                            status_view.setText("Recovered");
                            status_view.setTextColor(Color.GREEN);

                        }
                        if (status.equals("3"))
                        {
                            status_view.setText("Serious");
                            status_view.setTextColor(Color.RED);
                        }
                    }
                }
            }
        });

        final Button update = getActivity().findViewById(R.id.update_covid_);
        final ProgressBar updating = getActivity().findViewById(R.id.updating_covid);
        RadioGroup radioGroup = (RadioGroup)getActivity().findViewById(R.id.edit_infected_check);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
            }
        });

        RadioGroup radioGroup2 = (RadioGroup)getActivity().findViewById(R.id.edit_status_check);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton2 = (RadioButton)group.findViewById(checkedId);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.setVisibility(View.INVISIBLE);
                updating.setVisibility(View.VISIBLE);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(getActivity(),"Please select a choice",Toast.LENGTH_SHORT)
                            .show();
                    update.setVisibility(View.VISIBLE);
                    updating.setVisibility(View.GONE);
                }
                else {
                    RadioButton radioButton= (RadioButton) radioGroup .findViewById(selectedId);
                    if (radioButton.getText().equals("Yes"))
                    {
                        int selectedId2 = radioGroup2.getCheckedRadioButtonId();
                        if (selectedId2 == -1) {
                            Toast.makeText(getActivity(),"Please select a choice",Toast.LENGTH_SHORT)
                                    .show();
                            update.setVisibility(View.VISIBLE);
                            updating.setVisibility(View.GONE);
                        }
                        else {
                            RadioButton radioButton2= (RadioButton) radioGroup2 .findViewById(selectedId2);
                            if (radioButton2.getText().equals("Recovered"))
                            {
                                status = "2";
                                affected = "1";
                            }
                            else if (radioButton2.getText().equals("Active"))
                            {
                                status = "1";
                                affected = "1";
                            }
                            else
                            {
                                status = "3";
                                affected = "1";
                            }
                            register_void (affected, status,cms_id);
                            update.setVisibility(View.VISIBLE);
                            updating.setVisibility(View.GONE);
                            Fragment fragment = null;
                            fragment = new HomeFragment();
                            loadFragment(fragment);
                        }
                    }
                    else
                    {
                        affected = "0";
                        status = "0";
                        register_void (affected, status ,cms_id);
                        update.setVisibility(View.VISIBLE);
                        updating.setVisibility(View.GONE);
                        Fragment fragment = null;
                        fragment = new HomeFragment();
                        loadFragment(fragment);

                    }
                }
            }
        });
    }

    void register_void (String affected, String status, String cms_id)
    {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("Covid").document(cms_id);
        docIdRef.update("affected", affected);
        docIdRef.update("status", status)
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
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).commit();
        fragmentTransaction.addToBackStack(null);
    }
}