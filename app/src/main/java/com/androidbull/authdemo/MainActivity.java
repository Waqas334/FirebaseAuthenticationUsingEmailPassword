package com.androidbull.authdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView mTvUserEmail;
    private Button mBtnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mTvUserEmail = findViewById(R.id.tv_email_address);
        mBtnLogout = findViewById(R.id.btn_logout);

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            //No one is logged in
            startActivity(new Intent(this,LoginActivity.class));
        }else{
            String emailAddress = currentUser.getEmail();

            mTvUserEmail.setText(emailAddress);
        }
    }
}
