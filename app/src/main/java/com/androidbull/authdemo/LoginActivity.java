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

public class LoginActivity extends AppCompatActivity {

    private EditText mEtEmail, mEtPassword;
    private TextView mTvLogin;
    private FirebaseAuth mAuth;

    private LinearLayout mLlSignUp;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressView = findViewById(R.id.progress_login);
        mLlSignUp = findViewById(R.id.ll_what);
        mEtEmail = findViewById(R.id.et_email_login);
        mEtPassword = findViewById(R.id.et_pass_login);
        mTvLogin = findViewById(R.id.tv_login_login);

        mAuth = FirebaseAuth.getInstance();

        mProgressView.setVisibility(View.GONE);


        mLlSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Perform login

                String emailAddress = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();

                if (!isValidEmail(emailAddress)) {
                    //Email invalid
                    Toast.makeText(LoginActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                }else if(password.length()<6){
                    //Password length short
                    Toast.makeText(LoginActivity.this, "Password must be 6 character long", Toast.LENGTH_SHORT).show();
                }else{
                    //Show the progress View
                    mProgressView.setVisibility(View.VISIBLE);
                    //Sign in user
                    mAuth.signInWithEmailAndPassword(emailAddress,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressView.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                //Sign in completed!
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();

                            }else{
                                //Something went wrong
                                new AlertDialog.Builder(LoginActivity.this)
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

    private boolean isValidEmail(String email){

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }


}
