package com.buzzercode.covidmanagmentcenternust.data;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buzzercode.covidmanagmentcenternust.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder>{
    private final String TAG = "MessageAdaper";
    Context context;
    String userId;
    private final int MESSAGE_IN_VIEW_TYPE  = 1;
    private final int MESSAGE_OUT_VIEW_TYPE = 2;

    public MessageAdapter(Query query, String userID, Context context) {
        super(new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build());
        this.context = context;
        this.userId = userID;

    }

    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
        final TextView mText = holder.mText;
        final TextView mUsername = holder.mUsername;
        final TextView mTime = holder.mTime;
        mUsername.setText(model.getMessageUser());
        mText.setText(model.getMessageText());
        mTime.setText(DateFormat.format("dd MMM  (h:mm a)", model.getMessageTime()));
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).getMessageUserId().equals(userId)){
            return MESSAGE_OUT_VIEW_TYPE;
        }
        return MESSAGE_IN_VIEW_TYPE;
    }

    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType==MESSAGE_IN_VIEW_TYPE){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_out, parent, false);
        }
        return new MessageHolder(view);
    }
    public class MessageHolder extends RecyclerView.ViewHolder {
        TextView mText;
        TextView mUsername;
        TextView mTime;
        public MessageHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.message_text);
            mUsername = itemView.findViewById(R.id.message_user);
            mTime = itemView.findViewById(R.id.message_time);
        }
    }
}