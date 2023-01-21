package com.buzzercode.covidmanagmentcenternust;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;

import android.text.TextUtils;
import android.widget.Toast;

import com.buzzercode.covidmanagmentcenternust.data.Message;
import com.buzzercode.covidmanagmentcenternust.data.MessageAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.CMS_NAME;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.LAST_MESSAGE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SHARED_PREFS;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.TYPE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore database;
    Query query;
    private FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder> adapter;
    private EditText input;
    private ProgressBar pgBar;
    private String userId, userName;
    String type;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
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
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        type = sharedPreferences.getString(TYPE, null);
        final String cms_id = sharedPreferences.getString(CMS_NAME, null);

        ImageView send = getActivity().findViewById(R.id.send_message);
        input = getActivity().findViewById(R.id.message_text);
        pgBar = getActivity().findViewById(R.id.loading_messages);
        final RecyclerView recyclerView = getActivity().findViewById(R.id.message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String last_message, date;
                if (type.equals(("student"))) {
                    last_message = sharedPreferences.getString(LAST_MESSAGE, null);
                    date = new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());

                    if (date.equals(last_message)) {
                        Toast.makeText(getActivity(), "You can only enter one message per day", Toast.LENGTH_LONG).show();
                        input.setText("");
                        return;
                    } else {
                        editor.putString(LAST_MESSAGE, date);
                        editor.apply();
                        final FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference ref2 = db.collection("School").document("Accounts").collection("students").document(cms_id);
                        ref2
                                .update("last_message", date)
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
                        String message = input.getText().toString();
                        if (TextUtils.isEmpty(message)) {
                            Toast.makeText(getActivity(), "Your Message is empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        database.collection("Messages").add(new Message(type, userName, message, userId));
                        input.setText("");
                    }

                } else {
                    String message = input.getText().toString();
                    if (TextUtils.isEmpty(message)) {
                        Toast.makeText(getActivity(), "Your Message is empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    database.collection("Messages").add(new Message(type, userName, message, userId));
                    input.setText("");
                }
            }
        });
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            return;
        }
        userId = user.getUid();
        userName = user.getDisplayName();
        database = FirebaseFirestore.getInstance();
        query = database.collection("Messages").orderBy("messageTime");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e == null) {
                    if (queryDocumentSnapshots != null || queryDocumentSnapshots.isEmpty()) {
                        pgBar.setVisibility(View.GONE);

                    }
                    recyclerView.scrollToPosition(queryDocumentSnapshots.size() - 1);
                }
            }
        });
        adapter = new MessageAdapter(query, userId, getActivity());
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }
}