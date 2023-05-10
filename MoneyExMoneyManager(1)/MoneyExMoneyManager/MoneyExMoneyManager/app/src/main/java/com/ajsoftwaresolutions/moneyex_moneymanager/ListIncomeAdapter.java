package com.ajsoftwaresolutions.moneyex_moneymanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListIncomeAdapter extends RecyclerView.Adapter<ListIncomeAdapter.HolderlistIncomeMode> {
    private Context context;
    private ArrayList<ListIncome> listIncomes;
    String date;

    public ListIncomeAdapter(Context context, ArrayList<ListIncome> listIncomes) {
        this.context = context;
        this.listIncomes = listIncomes;
    }

    @NonNull
    @Override
    public HolderlistIncomeMode onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_all_income,parent,false);
        return new HolderlistIncomeMode(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderlistIncomeMode holder, int position) {
        ListIncome listIncome = listIncomes.get(position);
        String amount_s= listIncome.getAmount();
        String titel_i= listIncome.getTitle_i();
        String catagory= listIncome.getCatagory();
        String iid= listIncome.getIid();
        date= listIncome.getDate();


        Toast.makeText(context, ""+listIncome.getTitle_i(), Toast.LENGTH_SHORT).show();
        holder.title_i.setText(titel_i);
        holder.category.setText(catagory);
        holder.price.setText(amount_s);

        holder.edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditIncome.class);
                intent.putExtra("date",date);
                intent.putExtra("iid",iid);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditIncome.class);
                intent.putExtra("date",date);
                intent.putExtra("iid",iid);
                context.startActivity(intent);
            }
        });


        holder.delet_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete").setMessage("Do You Delete This Item").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        testdate(iid);

                        //delete



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

    }

    String inid;
    private void testdate(String iid) {
        String datee,monthh = null,yearr = null;
        String dev[] = date.split("/");
        for (String devi:dev){
            datee = dev[0];
            monthh = dev[1];
            yearr = dev[2];
        }
        inid = iid;
        deletItem(inid,yearr,monthh);

    }


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    private void deletItem(String inid, String yearr, String monthh) {

//        Toast.makeText(context, ""+inid, Toast.LENGTH_SHORT).show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Income").child(yearr).child(monthh).child(inid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //product Deleted
                Toast.makeText(context, "Product Deleted ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return listIncomes.size();
    }
    class HolderlistIncomeMode extends RecyclerView.ViewHolder {

        private TextView title_i,category,price;
        private ImageView edit_item,delet_item;


        public HolderlistIncomeMode(@NonNull View itemView) {
            super(itemView);
            title_i = itemView.findViewById(R.id.title_i);
            category = itemView.findViewById(R.id.category_i);
            price = itemView.findViewById(R.id.price_i);
            edit_item = itemView.findViewById(R.id.edit_item_i);
            delet_item = itemView.findViewById(R.id.delet_item_i);

        }
    }
}
