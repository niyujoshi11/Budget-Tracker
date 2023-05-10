package com.ajsoftwaresolutions.moneyex_moneymanager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditExpence extends AppCompatActivity {


    private EditText amount,title;
    private TextView chooseDate,chooseExpense;
    private Button submitExpense;
    private ImageView eback_img;


    private String amount_s,titel_s,eid,date;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String monthh,yearr;

    String amount1,titel1,catagory1,date1;

    String curDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expence);

       
        eid = getIntent().getStringExtra("eid");
        date = getIntent().getStringExtra("date");

        String dev[] = date.split("/");
        for (String devi:dev){
            monthh = dev[1];
            yearr = dev[2];
        }



        chooseDate = findViewById(R.id.echooseDate);
        chooseExpense = findViewById(R.id.echooseExpense);
        amount = findViewById(R.id.eamount);
        title = findViewById(R.id.etitle);
        submitExpense = findViewById(R.id.esubmitExpense);
        eback_img = findViewById(R.id.eback_img);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        load();


        eback_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditExpence.this,ItemList.class));
            }
        });
        submitExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount_s = amount.getText().toString().trim();
                titel_s = title.getText().toString().trim();

//                load();


                if (TextUtils.isEmpty(amount_s)){
                    Toast.makeText(EditExpence.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(titel_s)){
                    Toast.makeText(EditExpence.this, "Enter Title", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(catagory1)){
                    Toast.makeText(EditExpence.this, "Choose Category", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(curDate)){
                    Toast.makeText(EditExpence.this, "Choose Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditExpence();

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

                AlertDialog.Builder builder = new AlertDialog.Builder(EditExpence.this);
                builder.setTitle("Choose Catagory").setItems(Choose_Category.selectE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get category
                        catagory1= Choose_Category.selectE[i];
                        //set category
                        chooseExpense.setText(catagory1);
                    }
                }).show();

            }
        });




    }

    private void load() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Expense").child(yearr).child(monthh).child(eid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                amount1 = ""+snapshot.child("amount").getValue();
                titel1 = ""+snapshot.child("titel").getValue();
                catagory1 = ""+snapshot.child("catagory").getValue();
                date1 = ""+snapshot.child("date").getValue();




                String dev[] = date1.split("/");
                for (String devi:dev){
                    curDate = dev[0];
                    monthh = dev[1];
                    yearr = dev[2];
                }

                amount.setText(amount1);
                title.setText(titel1);
                chooseExpense.setText(catagory1);
                chooseDate.setText(date1);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void EditExpence() {



        progressDialog.setMessage("Updating Expense...");
        progressDialog.show();

        final String timetamp = ""+System.currentTimeMillis();
        //img without
        HashMap<String , Object> hashMap =new HashMap<>();
        hashMap.put("eid",""+eid);
        hashMap.put("amount",""+amount_s);
        hashMap.put("titel",""+titel_s);
        hashMap.put("catagory",""+catagory1);
        hashMap.put("date",""+curDate+"/"+monthh+"/"+yearr);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Expense").child(yearr).child(monthh).child(eid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //sucsses
                progressDialog.dismiss();
                Toast.makeText(EditExpence.this, "Updated..", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //fail
                progressDialog.dismiss();
                Toast.makeText(EditExpence.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void selectDate() {


        View view = LayoutInflater.from(EditExpence.this).inflate(R.layout.calender_select,null);


        AlertDialog.Builder builder = new AlertDialog.Builder(EditExpence.this);
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
                yearr = String.valueOf(year);
                monthh = String.valueOf(month);

                chooseDate.setText(curDate+"/"+monthh+"/"+yearr);

                dialog.dismiss();
            }
        });





    }
}