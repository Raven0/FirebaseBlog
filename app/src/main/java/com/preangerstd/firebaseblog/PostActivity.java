package com.preangerstd.firebaseblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageButton btnImg;
    private EditText tbTitle, tbContent;
    private Button btnSubmit;

    private static final int GALLERY_REQ = 1;
    private StorageReference storage;
    private DatabaseReference database;
    private Uri imageUri = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference().child("Blog");
        btnImg = (ImageButton) findViewById(R.id.btnImage);
        tbTitle = (EditText) findViewById(R.id.tbTitle);
        tbContent = (EditText) findViewById(R.id.tbContent);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        progressDialog = new ProgressDialog(this);

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQ);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }

    private void startPosting() {
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        final String title_val = tbTitle.getText().toString().trim();
        final String desc_val = tbContent.getText().toString().trim();
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && imageUri != null){
            StorageReference filepath = storage.child("Image_Post").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = database.push();
                    newPost.child("title").setValue(title_val);
                    newPost.child("content").setValue(desc_val);
                    newPost.child("image").setValue(downloadUrl.toString());
                    progressDialog.dismiss();

                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQ && resultCode == RESULT_OK){
            imageUri = data.getData();
            btnImg.setImageURI(imageUri);
        }
    }
}
