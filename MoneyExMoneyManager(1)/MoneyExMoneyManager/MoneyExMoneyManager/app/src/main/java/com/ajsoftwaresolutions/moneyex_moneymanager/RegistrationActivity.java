package com.ajsoftwaresolutions.moneyex_moneymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name,enter_log_email,enter_log_password,enter_log_copassword;
    private Button sign_up_send;
    private TextView log_link;
    private ImageView back_img;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private String fullName,emailId,password,con_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.name);
        enter_log_email = findViewById(R.id.enter_log_email);
        enter_log_password = findViewById(R.id.enter_log_password);
        enter_log_copassword = findViewById(R.id.enter_log_copassword);
        sign_up_send = findViewById(R.id.sign_up_send);
        log_link = findViewById(R.id.log_link);
        back_img = findViewById(R.id.back_img);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        log_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

        sign_up_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputData();


            }
        });




    }

    private void inputData() {

        fullName = name.getText().toString().trim();
        emailId = enter_log_email.getText().toString().trim();
        password = enter_log_password.getText().toString().trim();
        con_password = enter_log_copassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)){
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            return;
        }if (!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
            Toast.makeText(this, "Enter EmailId", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }if (!password.equals(con_password)){
            Toast.makeText(this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount();

    }
    private void createAccount() {
        progressDialog.setMessage("Creating Your Account...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(emailId,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //sucsses Account
                saveFirebase();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Fail Account
                progressDialog.dismiss();
                Toast.makeText(RegistrationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void saveFirebase() {
        progressDialog.setMessage("Please Wait....");

        final String timestamp = ""+System.currentTimeMillis();

            //save info without image

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+firebaseAuth.getUid());
            hashMap.put("email",""+emailId);
            hashMap.put("name",""+fullName);

            // save to db

        // save to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //db updated
                progressDialog.dismiss();
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed updating
                progressDialog.dismiss();
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                finish();
            }
        });


    }






}