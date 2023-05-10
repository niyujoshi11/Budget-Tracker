package com.ajsoftwaresolutions.moneyex_moneymanager;

import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ItemList extends AppCompatActivity {

    private ListIncomeAdapter listIncomeAdapter;
    private ArrayList<ListIncome> listIncomes;
    private ListItemsAdapter listItemsAdapter;
    private ArrayList<ListItemMode> listItemMode;
    private FirebaseAuth firebaseAuth;
    RecyclerView list_all_itemrv;
    String curDate,Year,Month;
    ImageView choose_filter,backbtn;
    private String catagory;

    TextView choose_date,balance;


    @Override
    public void onBackPressed() {

        startActivity(new Intent(ItemList.this,HomePage.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        firebaseAuth = FirebaseAuth.getInstance();


        Calendar c = Calendar.getInstance();
        Year = String.valueOf(c.get(Calendar.YEAR));
        Month = String.valueOf(c.get(Calendar.MONTH)+1);
        curDate = String.valueOf(c.get(Calendar.DATE));


        backbtn = findViewById(R.id.backbtn);
        list_all_itemrv = findViewById(R.id.list_all_itemrv);
        list_all_itemrv.setVisibility(View.VISIBLE);
        balance = findViewById(R.id.balance);
        choose_filter = findViewById(R.id.choose_filter);
//        notfound = findViewById(R.id.notfound);
//        notfound.setVisibility(View.GONE);
        choose_date = findViewById(R.id.choose_date);


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ItemList.this,HomePage.class));
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
        loadItems();


        choose_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ItemList.this);
                builder.setTitle("Choose Catagory").setItems(Choose_Category.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get category
                        catagory= Choose_Category.filter[i];
                        //set category

                        if(catagory == "All"){//        clearData();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            ref.child(firebaseAuth.getUid()).child("Expense").child(Year).child(Month).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    listItemMode.clear();

//                totel = ""+snapshot.child("amount").getValue();
                                    for (DataSnapshot ds:snapshot.getChildren()){
                                        ListItemMode listItemMode1 = ds.getValue(ListItemMode.class);
                                        //add list
                                        listItemMode.add(listItemMode1);
                                    }

                                    listItemsAdapter = new ListItemsAdapter(ItemList.this,listItemMode);
                                    list_all_itemrv.setLayoutManager(new LinearLayoutManager(ItemList.this, LinearLayoutManager.VERTICAL, false));
                                    list_all_itemrv.setAdapter(listItemsAdapter);

                                    if (list_all_itemrv.equals(null)){
                                        list_all_itemrv.setVisibility(View.GONE);
//                                        notfound.setVisibility(View.VISIBLE);
//                                        notfound.setText("Data Not Found");

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {


                                }
                            });



                        }
                        else if(catagory == "Food & Dining"){

                            filterfun(catagory);

                        }
                        else if(catagory == "Medical"){

                            filterfun(catagory);

                        }else if(catagory == "Entertainment"){

                            filterfun(catagory);

                        }else if(catagory == "Communication"){

                            filterfun(catagory);

                        }else if(catagory == "Rant & Bills"){

                            filterfun(catagory);

                        }else if(catagory == "Grocery"){

                            filterfun(catagory);

                        }else if(catagory == "Transport"){

                            filterfun(catagory);

                        }else if(catagory == "Other"){

                            filterfun(catagory);

                        }else if(catagory == "Income"){

                            listIncomes = new ArrayList<>();

                            Toast.makeText(ItemList.this, ""+Year+"/"+Month+"/"+curDate, Toast.LENGTH_SHORT).show();

                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
                            ref1.child(firebaseAuth.getUid()).child("Income").child(Year).child(Month).orderByChild("catagory").equalTo(catagory).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    listIncomes.clear();


                                    for (DataSnapshot ds:snapshot.getChildren()){
                                        ListIncome listIncomes1 = ds.getValue(ListIncome.class);
                                        //add list
                                        listIncomes.add(listIncomes1);

                                    }

                                    listIncomeAdapter = new ListIncomeAdapter(ItemList.this,listIncomes);
                                    list_all_itemrv.setLayoutManager(new LinearLayoutManager(ItemList.this, LinearLayoutManager.VERTICAL, false));
                                    list_all_itemrv.setAdapter(listIncomeAdapter);




                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {


                                }
                            });



                        }else if(catagory == "Salary"){

                            listIncomes = new ArrayList<>();

                            Toast.makeText(ItemList.this, ""+Year+"/"+Month+"/"+curDate, Toast.LENGTH_SHORT).show();

                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
                            ref1.child(firebaseAuth.getUid()).child("Income").child(Year).child(Month).orderByChild("catagory").equalTo(catagory).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    listIncomes.clear();


                                    for (DataSnapshot ds:snapshot.getChildren()){
                                        ListIncome listIncomes1 = ds.getValue(ListIncome.class);
                                        //add list
                                        listIncomes.add(listIncomes1);

                                    }

                                    listIncomeAdapter = new ListIncomeAdapter(ItemList.this,listIncomes);
                                    list_all_itemrv.setLayoutManager(new LinearLayoutManager(ItemList.this, LinearLayoutManager.VERTICAL, false));
                                    list_all_itemrv.setAdapter(listIncomeAdapter);




                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {


                                }
                            });


                        }else if(catagory == "Loan"){

                            listIncomes = new ArrayList<>();

                            Toast.makeText(ItemList.this, ""+Year+"/"+Month+"/"+curDate, Toast.LENGTH_SHORT).show();

                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
                            ref1.child(firebaseAuth.getUid()).child("Income").child(Year).child(Month).orderByChild("catagory").equalTo(catagory).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    listIncomes.clear();


                                    for (DataSnapshot ds:snapshot.getChildren()){
                                        ListIncome listIncomes1 = ds.getValue(ListIncome.class);
                                        //add list
                                        listIncomes.add(listIncomes1);

                                    }

                                    listIncomeAdapter = new ListIncomeAdapter(ItemList.this,listIncomes);
                                    list_all_itemrv.setLayoutManager(new LinearLayoutManager(ItemList.this, LinearLayoutManager.VERTICAL, false));
                                    list_all_itemrv.setAdapter(listIncomeAdapter);




                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {


                                }
                            });

                        }
//                        if (catagory == "Income" || catagory == "Salary" || catagory == "Loan"){
//
//                        }

                    }
                }).show();

            }
        });


        if (list_all_itemrv.equals(null)){
            list_all_itemrv.setVisibility(View.GONE);
//            notfound.setVisibility(View.VISIBLE);
//            notfound.setText("Data Not Found");

        }

    }

    private void filterfun(String catagory) {



        listItemMode = new ArrayList<>();
//        clearData();

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
        ref1.child(firebaseAuth.getUid()).child("Expense").child(Year).child(Month).orderByChild("catagory").equalTo(catagory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItemMode.clear();

//                totel = ""+snapshot.child("amount").getValue();
                for (DataSnapshot ds:snapshot.getChildren()){
                    ListItemMode listItemMode1 = ds.getValue(ListItemMode.class);
                    //add list
                    listItemMode.add(listItemMode1);
                }

                listItemsAdapter = new ListItemsAdapter(ItemList.this,listItemMode);
                list_all_itemrv.setLayoutManager(new LinearLayoutManager(ItemList.this, LinearLayoutManager.VERTICAL, false));
                list_all_itemrv.setAdapter(listItemsAdapter);

                if (list_all_itemrv.equals(null)){
                    list_all_itemrv.setVisibility(View.GONE);
//                    notfound.setVisibility(View.VISIBLE);
//                    notfound.setText("Data Not Found");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }


    float totel1=0,abc=0,totlefinal;
    private String totel="0";

    private void selectDate() {
        totlefinal=0;
        totel1 =0;
        abc =0;
        totel ="0";


        View view = LayoutInflater.from(ItemList.this).inflate(R.layout.calender_select, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(ItemList.this);
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



    private void choose_Date() {

        String setDate = curDate+"/"+Month+"/"+Year;

//        clearData();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Expense").child(Year).child(Month).orderByChild("date").equalTo(setDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItemMode.clear();

//                totel = ""+snapshot.child("amount").getValue();
                for (DataSnapshot ds:snapshot.getChildren()){
                    ListItemMode listItemMode1 = ds.getValue(ListItemMode.class);
                    //add list
                    listItemMode.add(listItemMode1);
                }

                listItemsAdapter = new ListItemsAdapter(ItemList.this,listItemMode);
                list_all_itemrv.setLayoutManager(new LinearLayoutManager(ItemList.this, LinearLayoutManager.VERTICAL, false));
                list_all_itemrv.setAdapter(listItemsAdapter);

                if (list_all_itemrv.equals(null)){
                    list_all_itemrv.setVisibility(View.GONE);
//                    notfound.setVisibility(View.VISIBLE);
//                    notfound.setText("Data Not Found");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


    }




    private void loadItems() {
        totlefinal=0;
        totel1 =0;
        abc =0;
        totel ="0";

        listItemMode = new ArrayList<>();



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Expense").child(Year).child(Month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItemMode.clear();
                for (DataSnapshot ds1 : snapshot.getChildren()){
                    ListItemMode listItemMode1 = ds1.getValue(ListItemMode.class);
                    //add list
                    listItemMode.add(listItemMode1);
                    totel1 = abc;

                    totel = ""+ds1.child("amount").getValue();

                    abc = Float.valueOf(totel)+totel1;
                    totel = "0";

                }
                totlefinal = abc;
                balance.setText("Totle Expense:"+ totlefinal);
                totlefinal = 0;

                //set adapter

                listItemsAdapter = new ListItemsAdapter(ItemList.this,listItemMode);
                list_all_itemrv.setLayoutManager(new LinearLayoutManager(ItemList.this, LinearLayoutManager.VERTICAL, false));
                list_all_itemrv.setAdapter(listItemsAdapter);


                if (list_all_itemrv==null){
                    list_all_itemrv.setVisibility(View.GONE);
//                    notfound.setVisibility(View.VISIBLE);
//                    notfound.setText("Data Not Found");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


}