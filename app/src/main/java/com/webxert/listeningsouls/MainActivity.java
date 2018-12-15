package com.webxert.listeningsouls;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.webxert.listeningsouls.adapters.ViewPagerAdapter;
import com.webxert.listeningsouls.common.Constants;
import com.webxert.listeningsouls.models.ChatModel;
import com.webxert.listeningsouls.models.SaverModel;
import com.webxert.listeningsouls.viewholders.MyMessageViewHolder;
import com.webxert.listeningsouls.viewholders.OtherMessageViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<SaverModel> arrayList = new ArrayList<>();

    RelativeLayout user_layout;
    LinearLayout admin_layout;
    SharedPreferences reader;
    ImageView submit_button;
    String email;
    EditText message_text;
    String layout_decider = "";
    RecyclerView user_recyclerview;
    boolean matched = false;


    FirebaseRecyclerAdapter<ChatModel, OtherMessageViewHolder> adapter;


    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
        ;

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
        ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getSharedPreferences(Constants.SH_PREFS, MODE_PRIVATE).edit().clear().apply();
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reader = getSharedPreferences(Constants.SH_PREFS, MODE_PRIVATE);
        user_layout = findViewById(R.id.user_layout);
        admin_layout = findViewById(R.id.admin_layout);
        submit_button = findViewById(R.id.submit_button);
        message_text = findViewById(R.id.message_text);
        email = reader.getString(Constants.USER_EMAIL, "null");


        layout_decider = reader.getString(Constants.AUTH_, "null");
        if (layout_decider.equals(Constants.ADMIN_AUTH)) {
            admin_layout.setVisibility(View.VISIBLE);
            user_layout.setVisibility(View.GONE);
            tabLayout = findViewById(R.id.sliding_tabs);
            viewPager = findViewById(R.id.viewpager);


            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

        } else if (layout_decider.equals(Constants.CUSTOMER_AUTH)) {

            user_layout.setVisibility(View.VISIBLE);
            admin_layout.setVisibility(View.GONE);
              user_recyclerview = findViewById(R.id.user_message_list);
            user_recyclerview.setLayoutManager(new LinearLayoutManager(this));

            displayMessages();

            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(message_text.getText().toString())) {
                        FirebaseDatabase.getInstance().getReference("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(Constants.DOMAIN_NAME).push().setValue(new ChatModel(email, "1", message_text.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                message_text.setText("");
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


        } else {
            FrameLayout placeholder = findViewById(R.id.placeholder_layout);
            placeholder.setVisibility(View.VISIBLE);
        }


    }

    private void displayMessages() {
        Log.e("InsideDisplay", "Here");


        Query query = FirebaseDatabase.getInstance().getReference("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.DOMAIN_NAME);

        DatabaseReference message_ref = FirebaseDatabase.getInstance().getReference("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.DOMAIN_NAME);

        message_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Toast.makeText(MainActivity.this, ""+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                Map<String, String> map =(Map) dataSnapshot.getValue();
                SaverModel saverModel = new SaverModel(dataSnapshot.getKey(), map);
                Log.e("size", String.valueOf(arrayList.size()));
                if(arrayList.size() == 0)
                {
                    arrayList.add(saverModel);

                }


                for (int i = 0; i < arrayList.size(); i++) {
                    SaverModel model = arrayList.get(i);
                    if(!dataSnapshot.getKey().equals(model.getId()))
                    {
                        matched = false;
                    }else
                        {
                            matched = true;
                            break;
                        }

                }
                if(!matched)
                {
                    arrayList.add(saverModel);
                }

                }





                //Log.e("message", map.get("message").toString());



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

        FirebaseRecyclerOptions<ChatModel> options = new FirebaseRecyclerOptions.Builder<ChatModel>()
                .setQuery(query, ChatModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ChatModel, OtherMessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OtherMessageViewHolder holder, int position, @NonNull ChatModel model) {
                Log.e("onBindViewHolder", position + "");
                holder.message.setText(model.getMessage());
                user_recyclerview.scrollToPosition(position);



//                Log.e(MainActivity.class.getSimpleName(), String.valueOf(position));
//                switch (holder.getItemViewType()) {
//                    case Constants.ADMIN_TYPE:
//                        OtherMessageViewHolder otherMessageViewHolder = (OtherMessageViewHolder) holder;
//                        otherMessageViewHolder.message.setText(model.getMessage());
//                        break;
//                    case Constants.CUSTOMER_TYPE:
//                        MyMessageViewHolder myMessageViewHolder = (MyMessageViewHolder) holder;
//                        myMessageViewHolder.message.setText(model.getMessage());
//                        break;
//                }

            }

            @NonNull
            @Override
            public OtherMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_message_layout, parent, false);
                return new OtherMessageViewHolder(view);

//                switch (viewType) {
//
//                    case Constants.ADMIN_TYPE:
//
//                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_message_layout, parent, false);
//                        return new OtherMessageViewHolder(view);
//                    case Constants.CUSTOMER_TYPE:
//
//                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_layout, parent, false);
//                        return new MyMessageViewHolder(view);
//
//                    default:
//                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_layout, parent, false);
//                        return new MyMessageViewHolder(view);
//
//                }

            }

//            @Override
//            public int getItemViewType(int position) {
//                Log.e("LayoutDecider", layout_decider);
//                if (layout_decider.equals(Constants.CUSTOMER_AUTH))
//                    return 1;
//                else return 0;
//            }
        };
        adapter.startListening();
        user_recyclerview.setAdapter(adapter);

    }
}
