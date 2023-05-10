package com.ajsoftwaresolutions.moneyex_moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FlashScreen extends AppCompatActivity {

    Handler handler;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_flash_screen);





        firebaseAuth = FirebaseAuth.getInstance();
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashScreen.this,LogIn_Page.class));

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null){
                    //user login not it is null value
                    startActivity(new Intent(FlashScreen.this, MainActivity.class));
                }
                else {
                    //user aauto login
                    checkUserType();

                }


            }
        },3000);

    }



    private void checkUserType() {
        //if user
        //else seller

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){

                        startActivity(new Intent(FlashScreen.this,HomePage.class));
//                        finish();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

}