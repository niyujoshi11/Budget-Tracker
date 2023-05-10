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

public class EditIncome extends AppCompatActivity {

    private EditText amount_e,title;
    private TextView chooseDate,chooseIncome;
    private Button submitIncome;

    private String amount_s,titel_s,iid,date;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String monthh,yearr;
    private ImageView back_img;

    String amount1,titel1,catagory1,date1;

    String curDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_income);

        iid = getIntent().getStringExtra("iid");
        date = getIntent().getStringExtra("date");

        String dev[] = date.split("/");
        for (String devi:dev){
            monthh = dev[1];
            yearr = dev[2];
        }

        back_img = findViewById(R.id.back_img);


        chooseDate = findViewById(R.id.chooseDate_e);
        chooseIncome = findViewById(R.id.chooseIncome_e);
        amount_e = findViewById(R.id.amount_income_e);
        title = findViewById(R.id.ititle_e);
        submitIncome = findViewById(R.id.addImcome_e);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        load();

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditIncome.this,ItemList.class));
            }
        });


        submitIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount_s = amount_e.getText().toString().trim();
                titel_s = title.getText().toString().trim();

//                load();


                if (TextUtils.isEmpty(amount_s)){
                    Toast.makeText(EditIncome.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(titel_s)){
                    Toast.makeText(EditIncome.this, "Enter Title", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(catagory1)){
                    Toast.makeText(EditIncome.this, "Choose Category", Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(curDate)){
                    Toast.makeText(EditIncome.this, "Choose Date", Toast.LENGTH_SHORT).show();
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



        chooseIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditIncome.this);
                builder.setTitle("Choose Catagory").setItems(Choose_Category.salary, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get category
                        catagory1= Choose_Category.salary[i];
                        //set category
                        chooseIncome.setText(catagory1);
                    }
                }).show();

            }
        });




    }

    private void load() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("  Income").child(yearr).child(monthh).child(iid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                amount1 = ""+snapshot.child("amount").getValue();
                titel1 = ""+snapshot.child("title_i").getValue();
                catagory1 = ""+snapshot.child("catagory").getValue();
                date1 = ""+snapshot.child("date").getValue();




                String dev[] = date1.split("/");
                for (String devi:dev){
                    curDate = dev[0];
                    monthh = dev[1];
                    yearr = dev[2];
                }

                amount_e.setText(amount1);
                title.setText(titel1);
                chooseIncome.setText(catagory1);
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
        hashMap.put("iid",""+iid);
        hashMap.put("amount",""+amount_s);
        hashMap.put("title_i",""+titel_s);
        hashMap.put("catagory",""+catagory1);
        hashMap.put("date",""+curDate+"/"+monthh+"/"+yearr);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Income").child(yearr).child(monthh).child(iid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //sucsses
                progressDialog.dismiss();
                Toast.makeText(EditIncome.this, "Updated..", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //fail
                progressDialog.dismiss();
                Toast.makeText(EditIncome.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void selectDate() {


        View view = LayoutInflater.from(EditIncome.this).inflate(R.layout.calender_select,null);


        AlertDialog.Builder builder = new AlertDialog.Builder(EditIncome.this);
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