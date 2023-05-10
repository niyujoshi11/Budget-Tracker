package com.ajsoftwaresolutions.moneyex_moneymanager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Addincome extends AppCompatActivity {


    @Override
    public void onBackPressed() {

        startActivity(new Intent(Addincome.this,HomePage.class));
    }




    private EditText amount,ititle;
    private TextView chooseDate,chooseIncome;
    private Button addImcome;

    private ImageView back_img;


    private String amount_s,ititle_s;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String catagory;

    String curDate,Year,Month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addincome);

        chooseDate = findViewById(R.id.chooseDate);
        chooseIncome = findViewById(R.id.chooseIncome);
        amount = findViewById(R.id.amount_income);
        addImcome = findViewById(R.id.addImcome);
        ititle = findViewById(R.id.ititle);
        back_img = findViewById(R.id.back_img);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Addincome.this,HomePage.class));
            }
        });

        addImcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount_s = amount.getText().toString().trim();
                ititle_s = ititle.getText().toString().trim();

                if (TextUtils.isEmpty(amount_s)){
                    Toast.makeText(Addincome.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                    return;
                } if (TextUtils.isEmpty(ititle_s)){
                    Toast.makeText(Addincome.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(catagory)){
                    Toast.makeText(Addincome.this, "Choose Category", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(curDate)){
                    Toast.makeText(Addincome.this, "Choose Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                addinCome();

            }
        });


        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectDate();
            }
        });


        chooseIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Addincome.this);
                builder.setTitle("Choose Catagory").setItems(Choose_Category.salary, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get category
                        catagory= Choose_Category.salary[i];
                        //set category
                        chooseIncome.setText(catagory);
                    }
                }).show();

            }
        });




    }

    private void addinCome() {



            progressDialog.setMessage("Adding Income...");
            progressDialog.show();

            final String timetamp = ""+System.currentTimeMillis();

                //img without
                HashMap<String , Object> hashMap =new HashMap<>();

                hashMap.put("iid",""+timetamp);
                hashMap.put("amount",""+amount_s);
                hashMap.put("title_i",""+ititle_s);
                hashMap.put("catagory",""+catagory);
                hashMap.put("date",""+curDate+"/"+Month+"/"+Year);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.child(firebaseAuth.getUid()).child(" Income").child(Year).child(Month).child(timetamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //sucsses
                        progressDialog.dismiss();
                        Toast.makeText(Addincome.this, "Income Added..", Toast.LENGTH_SHORT).show();

                        clearData();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //fail
                        progressDialog.dismiss();
                        Toast.makeText(Addincome.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

    private void clearData() {

        amount.setText("");
        ititle.setText("");
        chooseIncome.setText("Choose Category");
        chooseDate.setText("Choose Date");

    }



    private void selectDate() {


        View view = LayoutInflater.from(Addincome.this).inflate(R.layout.calender_select,null);


        AlertDialog.Builder builder = new AlertDialog.Builder(Addincome.this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        CalendarView date_c= view.findViewById(R.id.get_date);
        date_c.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month++;

                curDate = String.valueOf(dayOfMonth);
                Year = String.valueOf(year);
                Month = String.valueOf(month);

                Log.e("date",Year+"/"+Month+"/"+curDate);
                chooseDate.setText(curDate+"/"+Month+"/"+Year);

                dialog.dismiss();
            }
        });





    }
}