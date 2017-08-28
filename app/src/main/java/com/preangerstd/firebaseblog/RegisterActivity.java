package com.preangerstd.firebaseblog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText tbUsername,tbEmail,tbPass;
    private Button btnRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tbUsername = (EditText) findViewById(R.id.tbUname);
        tbEmail = (EditText) findViewById(R.id.tbEmail);
        tbPass = (EditText) findViewById(R.id.tbPassword);
        btnRegist = (Button) findViewById(R.id.btnRegister);
    }
}
