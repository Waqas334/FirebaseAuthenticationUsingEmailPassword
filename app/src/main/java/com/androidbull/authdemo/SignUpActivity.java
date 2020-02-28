package com.androidbull.authdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEtEmail, mEtPassword, mEtRepeatPassword;
    private TextView mTvSignUp;
    private FirebaseAuth mAuth;
    private LinearLayout mLlSignIn;
    private TextView mTvWhat;
    private View mBusyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        mEtEmail = findViewById(R.id.et_email_signup);
        mEtPassword = findViewById(R.id.et_pass_signup);
        mEtRepeatPassword = findViewById(R.id.et_pass_repeat_signup);
        mTvSignUp = findViewById(R.id.tv_signup_signup);
        mLlSignIn = findViewById(R.id.ll_what);
        mTvWhat = findViewById(R.id.tv_what);
        mBusyView = findViewById(R.id.progress_singup);

        mBusyView.setVisibility(View.GONE);
        mTvWhat.setText("Log In");


        mLlSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        mTvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();
                String repeatPassword = mEtRepeatPassword.getText().toString();

                if (!isValidEmail(email)) {
                    //Email is invalid
                    Toast.makeText(SignUpActivity.this, "Wrong Email format!", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    //Password length short
                    Toast.makeText(SignUpActivity.this, "Password must be 6 character long", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(repeatPassword)) {
                    //Password not matched
                    Toast.makeText(SignUpActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
                } else {
                    mBusyView.setVisibility(View.VISIBLE);
                    //Perform signup
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mBusyView.setVisibility(View.GONE);
                            //Sign up completed
                            if (task.isSuccessful()) {
                                //Signup successful
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            } else {
                                //Sign not successful
                                //Something went wrong
                                new AlertDialog.Builder(SignUpActivity.this)
                                        .setTitle("Sorry")
                                        .setMessage("Something went wrong\n\nError Message: " + task.getException().getLocalizedMessage())
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        }
                    });

                }


            }
        });


    }


    private boolean isValidEmail(String email) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }


}
