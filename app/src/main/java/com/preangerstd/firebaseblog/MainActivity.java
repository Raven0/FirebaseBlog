package com.preangerstd.firebaseblog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView blogList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        blogList = (RecyclerView) findViewById(R.id.blogList);
        blogList.setHasFixedSize(true);
        blogList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("User");
        mDatabaseUser.keepSynced(true);
        checkUserExist();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*if(mAuth.getCurrentUser() != null){
        }*/

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<BlogPost, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BlogPost, PostViewHolder>(

                BlogPost.class,
                R.layout.blog_post,
                PostViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, BlogPost model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setContent(model.getContent());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };

        blogList.setAdapter(firebaseRecyclerAdapter);

    }

    private void checkUserExist() {
        if(mAuth.getCurrentUser() != null){
            final String userid = mAuth.getCurrentUser().getUid();

            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild(userid)){
                        Toast.makeText(MainActivity.this, "Please Setup your Account", Toast.LENGTH_LONG).show();
                        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public PostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title){
            TextView postTitle = (TextView) mView.findViewById(R.id.postTitle);
            postTitle.setText(title);
        }

        public void setContent(String content){
            TextView postContent = (TextView) mView.findViewById(R.id.postContent);
            postContent.setText(content);
        }

        public void setImage(Context context, String image){
            ImageView postImage = (ImageView) mView.findViewById(R.id.postImage);
            Picasso.with(context).load(image).into(postImage);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add){
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }

        if(item.getItemId() == R.id.action_logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut(); //make sure AuthStateListener is added
    }
}
