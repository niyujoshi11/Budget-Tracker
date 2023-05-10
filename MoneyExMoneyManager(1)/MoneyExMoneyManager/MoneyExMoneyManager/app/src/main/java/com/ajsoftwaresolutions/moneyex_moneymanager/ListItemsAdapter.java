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

public class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.HolderlistItemModes> {
    private Context context;
    private ArrayList<ListItemMode> listItemModes;
    String date;



    public ListItemsAdapter(Context context, ArrayList<ListItemMode> listItemModes) {
        this.context = context;
        this.listItemModes = listItemModes;
    }

    @NonNull
    @Override
    public HolderlistItemModes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_all_item,parent,false);
        return new HolderlistItemModes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderlistItemModes holder, int position) {

        ListItemMode listItemMode = listItemModes.get(position);
        String amount_s= listItemMode.getAmount();
        String titel_s= listItemMode.getTitel();
        String catagory= listItemMode.getCatagory();
        String iid= listItemMode.getIid();
        date= listItemMode.getDate();
        final String eid= listItemMode.getEid();




        holder.title.setText(titel_s);
        holder.category.setText(catagory);
        holder.price.setText(amount_s);

        holder.edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditExpence.class);
                intent.putExtra("date",date);
                intent.putExtra("eid",eid);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditExpence.class);
                intent.putExtra("date",date);
                intent.putExtra("eid",eid);
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



                        testdate(eid);

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

    private void testdate(String eid) {
        String datee,monthh = null,yearr = null;
        String dev[] = date.split("/");
        for (String devi:dev){
            datee = dev[0];
            monthh = dev[1];
            yearr = dev[2];
        }
        deletItem(eid,yearr,monthh);

    }


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    private void deletItem(String eid, String yearr, String monthh) {

                Toast.makeText(context, ""+yearr, Toast.LENGTH_SHORT).show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Expense").child(yearr).child(monthh).child(eid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //product Deleted
//                Toast.makeText(context, "Product Deleted ", Toast.LENGTH_SHORT).show();
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
        return listItemModes.size();
    }

    class HolderlistItemModes extends RecyclerView.ViewHolder {

        private TextView title,category,price;
        private ImageView edit_item,delet_item;

        public HolderlistItemModes(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            edit_item = itemView.findViewById(R.id.edit_item);
            delet_item = itemView.findViewById(R.id.delet_item);
        }
    }

}
