package com.example.menu.admin.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.admin.EditData;
import com.example.menu.admin.UpdateData;
import com.example.menu.fragments.Cart;
import com.example.menu.fragments.List;
import com.example.menu.fragments.var_info;
import com.example.menu.models.Dish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminDishAdapter extends RecyclerView.Adapter<AdminDishAdapter.dishViewHolder> {
    ArrayList<Dish> dishes;
    Context context;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    public AdminDishAdapter(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public com.example.menu.admin.adapters.AdminDishAdapter.dishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_custom_list,null,false) ;

        com.example.menu.admin.adapters.AdminDishAdapter.dishViewHolder s = new com.example.menu.admin.adapters.AdminDishAdapter.dishViewHolder(v) ;
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull final com.example.menu.admin.adapters.AdminDishAdapter.dishViewHolder holder,  int position) {
        final Dish d = dishes.get(position);
        holder.name.setText((d.getName()));
        holder.desc.setText((d.getDescription()));
        holder.price.setText(d.getPrice()+"");
        Picasso.get()
                .load(d.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.img);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Confirm Delete");
                alert.setMessage("Sure you want to delete this food?");
              //  alert.setIcon(Integer.parseInt("@drawable/person"));
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dishes.remove(position);
                        notifyItemRemoved(position);

                        firestore = FirebaseFirestore.getInstance();
                        mAuth = FirebaseAuth.getInstance();


                        DocumentReference documentReference = firestore.collection("Food").document(d.getId());

                        documentReference.delete();
                            Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.setCancelable(true);

                    }
                });
                alert.show();


                return false;
            }
        });

        
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditData update = new EditData();
                Bundle bundle = new Bundle();
                bundle.putString("id", d.getId()); //key and value
                update.setArguments(bundle);
                FragmentTransaction fragmentManager = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.admin_main_container, update);
                fragmentManager.addToBackStack(null);
                fragmentManager.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    class dishViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView desc;
        TextView price;
        ImageView img;
        Button add;
        public dishViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.adminlname);
            desc = itemView.findViewById(R.id.adminldesc);
            price = itemView.findViewById(R.id.adminlprice);
            img = itemView.findViewById(R.id.adminlimg);
            add = itemView.findViewById(R.id.adminlbtnAdd);

        }
    }
}


