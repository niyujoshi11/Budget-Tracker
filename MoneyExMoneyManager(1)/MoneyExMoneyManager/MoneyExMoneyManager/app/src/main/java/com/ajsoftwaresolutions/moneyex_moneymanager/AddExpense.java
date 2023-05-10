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

public class AddExpense extends AppCompatActivity {

    private EditText amount,title;
    private TextView chooseDate,chooseExpense;
    private Button submitExpense;
    private ImageView back_img;


    private String amount_s,titel_s;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String catagory;

    String curDate,Year,Month;

    @Override
    public void onBackPressed() {

        startActivity(new Intent(AddExpense.this,HomePage.class));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        chooseDate = findViewById(R.id.chooseDate);
        chooseExpense = findViewById(R.id.chooseExpense);
        amount = findViewById(R.id.amount);
        title = findViewById(R.id.title);
        submitExpense = findViewById(R.id.submitExpense);
        back_img = findViewById(R.id.back_img);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);



        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddExpense.this,HomePage.class));
            }
        });
        submitExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount_s = amount.getText().toString().trim();
                titel_s = title.getText().toString().trim();

                if (TextUtils.isEmpty(amount_s)){
                    Toast.makeText(AddExpense.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(titel_s)){
                    Toast.makeText(AddExpense.this, "Enter Title", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(catagory)){
                    Toast.makeText(AddExpense.this, "Choose Category", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(curDate)){
                    Toast.makeText(AddExpense.this, "Choose Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                addexpense();

            }
        });


        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectDate();
            }
        });


        chooseExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddExpense.this);
                builder.setTitle("Choose Catagory").setItems(Choose_Category.selectE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get category
                        catagory= Choose_Category.selectE[i];
                        //set category
                        chooseExpense.setText(catagory);
                    }
                }).show();

            }
        });




    }

    private void addexpense() {



        progressDialog.setMessage("Adding Expense...");
        progressDialog.show();

        final String timetamp = ""+System.currentTimeMillis();

        //img without
        HashMap<String , Object> hashMap =new HashMap<>();

        hashMap.put("eid",""+timetamp);
        hashMap.put("amount",""+amount_s);
        hashMap.put("titel",""+titel_s);
        hashMap.put("catagory",""+catagory);
        hashMap.put("date",""+curDate+"/"+Month+"/"+Year);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Expense").child(Year).child(Month).child(timetamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //sucsses
                progressDialog.dismiss();
                Toast.makeText(AddExpense.this, "Expense Added..", Toast.LENGTH_SHORT).show();

                clearData();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //fail
                progressDialog.dismiss();
                Toast.makeText(AddExpense.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearData() {

        amount.setText("");
        title.setText("");
        chooseExpense.setText("Choose Category");
        chooseDate.setText("Choose Date");

    }



    private void selectDate() {


        View view = LayoutInflater.from(AddExpense.this).inflate(R.layout.calender_select,null);


        AlertDialog.Builder builder = new AlertDialog.Builder(AddExpense.this);
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