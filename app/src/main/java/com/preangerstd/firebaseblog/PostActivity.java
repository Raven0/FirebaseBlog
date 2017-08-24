package com.preangerstd.firebaseblog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostActivity extends AppCompatActivity {

    private ImageButton btnImg;
    private Button btnSubmit;
    private static final int GALLERY_REQ = 1;
    private Uri imageUri = null;
    private EditText tbTitle, tbContent;
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storage = FirebaseStorage.getInstance().getReference();

        btnImg = (ImageButton) findViewById(R.id.btnImage);
        tbTitle = (EditText) findViewById(R.id.tbTitle);
        tbContent = (EditText) findViewById(R.id.tbContent);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

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
                postNow();
            }
        });
    }

    private void postNow(){
        String vTitle = tbTitle.getText().toString().trim();
        String vContent = tbContent.getText().toString().trim();

        if(!TextUtils.isEmpty(vTitle) && !TextUtils.isEmpty(vContent) && imageUri != null){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQ && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            btnImg.setImageURI(imageUri);
        }
    }
}
