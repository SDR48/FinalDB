package com.example.sdr.finaldb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAct extends AppCompatActivity {
Button log;
    EditText emails,passwords;
    TextView register,chpass;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emails = (EditText)findViewById(R.id.logname);
        passwords = (EditText)findViewById(R.id.logpass);
        log = (Button)findViewById(R.id.loginbut);
        register = (TextView)findViewById(R.id.gotoreg);
        progressDialog = new ProgressDialog(this);
        chpass=(TextView)findViewById(R.id.fgtpw);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            this.finish();
            startActivity(new Intent(LoginAct.this,MainActivity.class));
        }
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(emails.getText().toString(),passwords.getText().toString());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAct.this,Registration_Act.class));
            }
        });
        chpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAct.this,changepass.class));
            }
        });
    }
    private void validate(String email, String password)
    {
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    checkemail();
                    startActivity(new Intent(LoginAct.this,MainActivity.class));
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginAct.this,"Failed",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void checkemail()
    {
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();

        if(fbuser.isEmailVerified())
        {
            finish();
            startActivity(new Intent(LoginAct.this,MainActivity.class));
        }
        else
        {
            sendemail();
            firebaseAuth.signOut();
        }
    }
    private void sendemail()
    {
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fbuser!=null)
        {
            fbuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(LoginAct.this,"Successfully sent verification",Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();

                    }
                    else{
                        Toast.makeText(LoginAct.this,"Mail couldn't be sent",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
