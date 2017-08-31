package com.preangerstd.firebaseblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SinglePostActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView mSingleUser, mSingleTitle, mSingleContent;
    private ImageView mSingleImage;
    private String postKey = null;
    private Button btnDelete;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        postKey = getIntent().getExtras().getString("postId");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mAuth = FirebaseAuth.getInstance();

        mSingleUser = (TextView) findViewById(R.id.singleUser);
        mSingleTitle = (TextView) findViewById(R.id.singleTitle);
        mSingleContent = (TextView) findViewById(R.id.singleContent);
        mSingleImage = (ImageView) findViewById(R.id.singleImage);
        btnDelete = (Button) findViewById(R.id.btnDeletePost);

        mDatabase.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String postTitle = (String) dataSnapshot.child("title").getValue();
                String postContent = (String) dataSnapshot.child("content").getValue();
                String postImage = (String) dataSnapshot.child("image").getValue();
                String postUser = (String) dataSnapshot.child("username").getValue();
                String postUid = (String) dataSnapshot.child("uid").getValue();

                mSingleTitle.setText(postTitle);
                mSingleContent.setText(postContent);
                mSingleUser.setText(postUser);

                Picasso.with(SinglePostActivity.this).load(postImage).into(mSingleImage);

                if(mAuth.getCurrentUser().getUid().equals(postUid)){
                    btnDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(postKey).removeValue();
                Intent mainIntent = new Intent(SinglePostActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

    }
}
