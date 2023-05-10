package com.ajsoftwaresolutions.moneyex_moneymanager;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class HomePage extends AppCompatActivity {


    PieChart pieChart;
    TextView choose_date,balance;
    ImageView addImcome,addExpanse,signOut;
    String curDate,Year,Month;


    private FirebaseAuth firebaseAuth;

    private String totel="0";


    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        firebaseAuth = FirebaseAuth.getInstance();

        signOut = findViewById(R.id.signOut);
        pieChart = findViewById(R.id.activity_main_piechart);
        choose_date = findViewById(R.id.choose_date);

        Calendar c = Calendar.getInstance();
        Year = String.valueOf(c.get(Calendar.YEAR));
        Month = String.valueOf(c.get(Calendar.MONTH)+1);
        curDate = String.valueOf(c.get(Calendar.DATE));





        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                builder.setTitle("SignOut").setMessage("Do You Want To SignOut?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        firebaseAuth.signOut();
                        startActivity(new Intent(HomePage.this,MainActivity.class));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //dismiss
                        dialogInterface.dismiss();
                    }
                }).show();

            }
        });
        addExpanse = findViewById(R.id.addExpanse);
        addImcome = findViewById(R.id.addImcome);
        balance = findViewById(R.id.balance);


        addImcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,Addincome.class));
            }
        });
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,ItemList.class));
            }
        });
        addExpanse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,AddExpense.class));
            }
        });


        choose_date.setText(curDate+"/"+Month+"/"+Year);
        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                clearData();
                selectDate();

            }
        });

        test();
//
//        setupPieChart();
//        loadPieChartData();

    }



    float e1 = 0,e2 = 0,e3 = 0,e4 = 0,e5 = 0,e6 = 0,e7 = 0,e8 = 0;
    float totel1=0,abc=0,totlefinal;
    float abc0=0,abc1=0,abc2=0,abc3=0,abc4=0,abc5=0,abc6=0,abc7=0;
    float e9=0;
//    private void clearData() {
//        float totel1=0,abc=0,totlefinal=0;
//    }


    private void choose_Date() {

        totlefinal=0;
        totel1 =0;
        abc =0;
        totel ="0";

//        clearData();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Income").child(Year).child(Month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                totel = ""+snapshot.child("amount").getValue();
                for (DataSnapshot ds:snapshot.getChildren()){
                    totel1 = abc;

                    totel = ""+ds.child("amount").getValue();

                    abc = Float.valueOf(totel)+totel1;
                    totel = "0";

                }
                totlefinal = abc;

                setupPieChart();
                category();
                expensetext();

//                loadPieChartData();

                totlefinal = 0;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void selectDate() {


        View view = LayoutInflater.from(HomePage.this).inflate(R.layout.calender_select, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        CalendarView date_c = view.findViewById(R.id.get_date);
        date_c.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month++;

                curDate = String.valueOf(dayOfMonth);
                Year = String.valueOf(year);
                Month = String.valueOf(month);

                Log.e("date", Year + "/" + Month + "/" + curDate);
                choose_date.setText(curDate + "/" + Month + "/" + Year);
                choose_Date();

                dialog.dismiss();
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private void setupPieChart() {


        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(false);

//        pieChart.setEntryLabelTextSize(20);
//        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Balance \n Rs."+totlefinal);
//        pieChart.setBackgroundColor(R.color.finel3);
        pieChart.setCenterTextSize(20);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);

        if(totlefinal == 0){
            startActivity(new Intent(HomePage.this,Addincome.class));
        }

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
//        clearData();
    }


//    private void loadPieChartData() {
//
//
//        float e2= 22000;
//        float e3= 4500;
//        float e4= 20000;
//        float e5= 4000;
//        float e6= 6000;
//        float e7= 5400;
//        float e9= totlefinal-e1-e2-e3-e4-e5-e6-e7;
////        float totel1 = Float.valueOf(totel)-e1;
//
//
//        Toast.makeText(HomePage.this, ""+e1, Toast.LENGTH_SHORT).show();
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(e1, "Food & Dining"));
//        entries.add(new PieEntry(e2, "Medical"));
//        entries.add(new PieEntry(e3, "Entertainment"));
//        entries.add(new PieEntry(e4, "Communication"));
//        entries.add(new PieEntry(e4, "Rant & Bills"));
//        entries.add(new PieEntry(e5, "Grocary"));
//        entries.add(new PieEntry(e6, "Transport"));
//        entries.add(new PieEntry(e7, "Other"));
//        entries.add(new PieEntry(e9, "remaining Money"));
//
//        ArrayList<Integer> colors = new ArrayList<>();
//        for (int color: ColorTemplate.MATERIAL_COLORS) {
//            colors.add(color);
//        }
//
//        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
//            colors.add(color);
//        }
//
//        PieDataSet dataSet = new PieDataSet(entries, "");
//        dataSet.setColors(colors);
//
//        PieData data = new PieData(dataSet);
//        data.setDrawValues(true);
//        data.setValueFormatter(new PercentFormatter(pieChart));
//        data.setValueTextSize(12f);
//        data.setValueTextColor(Color.BLACK);
//
//        pieChart.setData(data);
//        pieChart.invalidate();
//
//        pieChart.animateY(2000, Easing.EaseInOutQuad);
//
////        clearData();
//    }

    private void category() {


        totel1 =0;
        abc =0;
        totel ="0";


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Expense").child(Year).child(Month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                e1 = 0;
                e2= 0;
                e3= 0;
                e4= 0;
                e5= 0;
                e6= 0;
                e7= 0;
                e8= 0;
//                totel = ""+snapshot.child("amount").getValue();
                for (DataSnapshot ds:snapshot.getChildren()){

                    String category = ""+ds.child("catagory").getValue();

                    if ( category.equals("Food & Dining")){

                        totel1 = abc0;

                        totel = ""+ds.child("amount").getValue();

                        abc0 = Float.valueOf(totel)+totel1;
                        totel = "0";
                    }

                    if ( category.equals("Medical")){

                        totel1 = abc1;

                        totel = ""+ds.child("amount").getValue();

                        abc1 = Float.valueOf(totel)+totel1;
                        totel = "0";
                    }
                    if ( category.equals("Entertainment")){

                        totel1 = abc2;

                        totel = ""+ds.child("amount").getValue();

                        abc2 = Float.valueOf(totel)+totel1;
                        totel = "0";
                    }
                    if ( category.equals("Communication")){

                        totel1 = abc3;

                        totel = ""+ds.child("amount").getValue();

                        abc3 = Float.valueOf(totel)+totel1;
                        totel = "0";
                    }
                    if ( category.equals("Rant & Bills")){

                        totel1 = abc4;

                        totel = ""+ds.child("amount").getValue();

                        abc4 = Float.valueOf(totel)+totel1;
                        totel = "0";
                    }
                    if ( category.equals("Grocery")){

                        totel1 = abc5;

                        totel = ""+ds.child("amount").getValue();

                        abc5 = Float.valueOf(totel)+totel1;
                        totel = "0";
                    }
                    if ( category.equals("Transport")){

                        totel1 = abc6;

                        totel = ""+ds.child("amount").getValue();

                        abc6 = Float.valueOf(totel)+totel1;
                        totel = "0";
                    }
                    if ( category.equals("Other")){

                        totel1 = abc7;

                        totel = ""+ds.child("amount").getValue();

                        abc7 = Float.valueOf(totel)+totel1;
                        totel = "0";
                    }


                }
                e1 = abc0;
                e2= abc1;
                e3= abc2;
                e4= abc3;
                e5= abc4;
                e6= abc5;
                e7= abc6;
                e8= abc7;

//                Toast.makeText(HomePage.this, ""+totlefinal, Toast.LENGTH_SHORT).show();





//        float totel1 = Float.valueOf(totel)-e1;
                ArrayList<PieEntry> entries = new ArrayList<>();

                if(e1 >= 0){
                    entries.add(new PieEntry(e1, "Food & Dining"));

                }
                if(e2 >= 0){
                    entries.add(new PieEntry(e2, "Medical"));

                }if(e3 >= 0){
                    entries.add(new PieEntry(e3, "Entertainment"));


                }if(e4 >= 0){
                    entries.add(new PieEntry(e4, "Communication"));

                }if(e5 >= 0){
                    entries.add(new PieEntry(e5, "Rant & Bills"));

                }if(e6 >= 0){
                    entries.add(new PieEntry(e6, "Grocary"));

                }if(e7 >= 0){
                    entries.add(new PieEntry(e7, "Transport"));

                }if(e8 >= 0){
                    entries.add(new PieEntry(e8, "Other"));

                }
//                totlefinal= totlefinal-e1;
//                totlefinal= totlefinal-e2;
//                totlefinal= totlefinal-e3;
//                totlefinal= totlefinal-e4;
//                totlefinal= totlefinal-e5;
//                totlefinal= totlefinal-e6;
//                totlefinal= totlefinal-e7;
//                e9= totlefinal-e8;
//
//                if(e9 != 0){
//                    entries.add(new PieEntry(e9, "remaining Money"));
//
//                }

                ArrayList<Integer> colors = new ArrayList<>();
                for (int color: ColorTemplate.MATERIAL_COLORS) {
                    colors.add(color);
                }

                for (int color: ColorTemplate.VORDIPLOM_COLORS) {
                    colors.add(color);
                }

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(colors);

                PieData data = new PieData(dataSet);
                data.setDrawValues(true);
                data.setValueFormatter(new PercentFormatter(pieChart));
                data.setValueTextSize(12f);
                data.setValueTextColor(Color.BLACK);

                pieChart.setData(data);
                pieChart.invalidate();

                pieChart.animateY(2000, Easing.EaseInOutQuad);


                e1 = 0;
                e2= 0;
                e3= 0;
                e4= 0;
                e5= 0;
                e6= 0;
                e7= 0;
                e8= 0;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });




    }

    private void test() {

//        clearData();
        totlefinal=0;
        totel1 =0;
        abc =0;
        totel ="0";


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Income").child(Year).child(Month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                totel = ""+snapshot.child("amount").getValue();
                for (DataSnapshot ds:snapshot.getChildren()){
                    totel1 = abc;

                    totel = ""+ds.child("amount").getValue();

                    abc = Float.valueOf(totel)+totel1;
                    totel = "0";

                }
                totlefinal = abc;

                if (totlefinal == 0){
                    Toast.makeText(HomePage.this, "Data Not Found...", Toast.LENGTH_SHORT).show();
                }
                setupPieChart();
                category();
                expensetext();

//                loadPieChartData();

                totlefinal = 0;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void expensetext() {

        totlefinal=0;
        totel1 =0;
        abc =0;
        totel ="0";


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Expense").child(Year).child(Month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                totel = ""+snapshot.child("amount").getValue();
                for (DataSnapshot ds:snapshot.getChildren()){
                    totel1 = abc;

                    totel = ""+ds.child("amount").getValue();

                    abc = Float.valueOf(totel)+totel1;
                    totel = "0";

                }
                totlefinal = abc;
                balance.setText("Totle Expense:"+ totlefinal);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}