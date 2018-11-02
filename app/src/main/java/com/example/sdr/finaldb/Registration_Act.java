package com.example.sdr.finaldb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration_Act extends AppCompatActivity {

    EditText Name, Username, Contact, Password;
    Button submit;
    TextView login;
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_);
        Name=(EditText)findViewById(R.id.Names);
        Username=(EditText)findViewById(R.id.username);
        Contact=(EditText)findViewById(R.id.Contact);
        Password=(EditText)findViewById(R.id.password);
        submit = (Button)findViewById(R.id.Reg);
        progressDialog = new ProgressDialog(this);
        login = (TextView)findViewById(R.id.gotolog);
        firebaseAuth = FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(validate())
               {

                   String email = Username.getText().toString();
                   final String pass = Password.getText().toString().trim();
                   firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           progressDialog.setMessage("Please wait....");
                           progressDialog.show();
                           if(pass.length()>=6){
                           if(task.isComplete()){
                               progressDialog.dismiss();
                               sendemail();
                       }
                       else{
                               progressDialog.dismiss();
                               Toast.makeText(Registration_Act.this,"Failed",Toast.LENGTH_SHORT).show();
                           }
                       }
                       else{
                               progressDialog.dismiss();
                               Toast.makeText(Registration_Act.this,"Password length should be atleast 6",Toast.LENGTH_LONG).show();
                           }
                       }
                   });
               }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration_Act.this,LoginAct.class));
            }
        });
    }
    public boolean validate(){
        Boolean result=false;
        String name = Name.getText().toString();
        String usname = Username.getText().toString();
        String phonenum = Contact.getText().toString();
        String pass = Password.getText().toString();
        if(name.isEmpty() || usname.isEmpty() || phonenum.isEmpty() || pass.isEmpty())
        {
            Toast.makeText(Registration_Act.this,"Please fill all details",Toast.LENGTH_SHORT).show();
        }
        else
        {
            result=true;
        }
        return result;
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
                        Toast.makeText(Registration_Act.this,"Successfully sent verification",Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Registration_Act.this,LoginAct.class));
                    }
                    else{
                        Toast.makeText(Registration_Act.this,"Mail couldn't be sent",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
