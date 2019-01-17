package com.webxert.listeningsouls.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.daasuu.bl.ArrowDirection;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.webxert.listeningsouls.MainActivity;
import com.webxert.listeningsouls.R;
import com.webxert.listeningsouls.adapters.ChatMessagesAdapter;
import com.webxert.listeningsouls.common.Constants;
import com.webxert.listeningsouls.models.MessageModel;
import com.webxert.listeningsouls.models.SaverModel;
import com.webxert.listeningsouls.viewholders.MessageViewHolder;

import java.util.ArrayList;
import java.util.Map;

public class ChatFragment extends Fragment {


    public static Fragment mInstance = null;
    ArrayList<SaverModel> arrayList = new ArrayList<>();
    ArrayList<MessageModel> messages = new ArrayList<>();
    boolean matched = false;


    public ChatFragment() {

    }

    public RecyclerView recyclerView;
    FirebaseRecyclerAdapter<MessageModel, MessageViewHolder> adapter;
    ImageView submit_button;
    EditText message_text;
    String id;
    String email;

    ChatMessagesAdapter chatMessagesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.user_message_list);
        submit_button = view.findViewById(R.id.submit_button);
        message_text = view.findViewById(R.id.message_text);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatMessagesAdapter = new ChatMessagesAdapter(messages, getContext());
        recyclerView.setAdapter(chatMessagesAdapter);
        id = getArguments().getString("id");
        email = getArguments().getString("email");

        // displayMessages();

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(message_text.getText().toString())) {
                    String message =message_text.getText().toString();
                    message_text.setText("");
                    FirebaseDatabase.getInstance().getReference("Messages").child(id)
                            .child(Constants.DOMAIN_NAME).push().setValue(new MessageModel(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "1", message, "1", FirebaseAuth.getInstance().getCurrentUser().getUid())).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //message_text.setText("");
                            message_text.requestFocus();
                            displayMessages();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(MainActivity.class.getSimpleName(), e.getMessage());

                        }
                    });
                }
            }
        });

        displayMessages();

        return view;

    }

    public static Fragment getmInstance() {
        if (mInstance == null)
            return mInstance = new ChatFragment();
        else return mInstance;
    }

    private void displayMessages() {
        DatabaseReference message_ref = FirebaseDatabase.getInstance().getReference("Messages").child(id).child(Constants.DOMAIN_NAME);

        message_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Toast.makeText(MainActivity.this, ""+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                Map<String, String> map = (Map) dataSnapshot.getValue();
                SaverModel saverModel = new SaverModel(dataSnapshot.getKey(), map);
                Log.e("Key", dataSnapshot.getKey());
                Log.e("size", String.valueOf(arrayList.size()));
                if (arrayList.size() == 0 && messages.size() == 0) {
                    addNewMessage(arrayList, messages, chatMessagesAdapter, saverModel);
                }

                for (int i = 0; i < arrayList.size(); i++) {
                    SaverModel m = arrayList.get(i);
                    if (!dataSnapshot.getKey().equals(m.getId())) {
                        matched = false;
                    } else {
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    addNewMessage(arrayList, messages, chatMessagesAdapter, saverModel);
                }
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
//
//        FirebaseRecyclerOptions<MessageModel> options = new FirebaseRecyclerOptions.Builder<MessageModel>()
//                .setQuery(query, MessageModel.class)
//                .build();
//
//        adapter = new FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageModel model) {
//                //arrayList.add(model);
//                messages.add(model);
//                chatMessagesAdapter.notifyDataSetChanged();
//                Log.e("Message", model.getMessage());
//                //recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
////                Log.e("onBindViewHolder", model.getId() + "");
////                if (model.getId().equals(id)) {
////                   holder.message_layout.setArrowDirection(ArrowDirection.RIGHT);
////                   // holder.frameLayout.setForegroundGravity(Gravity.END);
////                    holder.who.setText(email);
////                } else {
////                    holder.message_layout.setArrowDirection(ArrowDirection.LEFT);
////                    holder.who.setText("You");
////                }
////                holder.message.setText(model.getMessage());
////                recyclerView.scrollToPosition(adapter.getItemCount());
//                //recyclerView.getAdapter().notifyDataSetChanged();
//
////                Log.e(MainActivity.class.getSimpleName(), String.valueOf(position));
////                switch (holder.getItemViewType()) {
////                    case Constants.ADMIN_TYPE:
////                        MessageViewHolder otherMessageViewHolder = (MessageViewHolder) holder;
////                        otherMessageViewHolder.message.setText(model.getMessage());
////                        break;
////                    case Constants.CUSTOMER_TYPE:
////                        ChatViewHolder myMessageViewHolder = (ChatViewHolder) holder;
////                        myMessageViewHolder.message.setText(model.getMessage());
////                        break;
////                }
//            }
//
//            @NonNull
//            @Override
//            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view;
//                Log.e("onCreateViewHolder", "onCreateViewHolder");
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_message_layout, parent, false);
//                return new MessageViewHolder(view);
////                switch (viewType) {
////
////                    case Constants.ADMIN_TYPE:
////
////                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_message_layout, parent, false);
////                        return new MessageViewHolder(view);
////                    case Constants.CUSTOMER_TYPE:
////
////                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_layout, parent, false);
////                        return new ChatViewHolder(view);
////
////                    default:
////                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_layout, parent, false);
////                        return new ChatViewHolder(view);
////
////                }
//            }
//
//            @Override
//            public int getItemViewType(int position) {
//                Log.e("getItemViewType", "getItemViewType");
//                return super.getItemViewType(position);
//            }
//            //            @Override
////            public int getItemViewType(int position) {
////                Log.e("LayoutDecider", layout_decider);
////                if (layout_decider.equals(Constants.CUSTOMER_AUTH))
////                    return 1;
////                else return 0;
////            }
//        };
//        adapter.startListening();
//        //recyclerView.setAdapter(adapter);
////        recyclerView.scrollToPosition(adapter.getItemCount());

    }

    private void addNewMessage(ArrayList<SaverModel> arrayList, ArrayList<MessageModel> messages, ChatMessagesAdapter chatMessagesAdapter, SaverModel saverModel) {
        arrayList.add(saverModel);
        MessageModel mm = new MessageModel();
        mm.setId(saverModel.getMap().get("id"));
        mm.setEmail(saverModel.getMap().get("email"));
        mm.setIs_admin(saverModel.getMap().get("is_admin"));
        mm.setMessage(saverModel.getMap().get("message"));
        mm.setView_type(saverModel.getMap().get("view_type"));
        messages.add(mm);
        chatMessagesAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatMessagesAdapter.getItemCount()-1);
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
