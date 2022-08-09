package com.example.menu.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.activities.MainScreen;
import com.example.menu.activities.Refister_form;
import com.example.menu.fragments.var_info;
import com.example.menu.models.Dish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.mainViewHolder> {
    ArrayList<Dish> dishes;
    Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();



    final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public MainAdapter(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public mainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dish, null, false);

        MainAdapter.mainViewHolder s = new MainAdapter.mainViewHolder(v);
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull final mainViewHolder holder, int position) {
        final Dish d = dishes.get(position);
        holder.name.setText((d.getName()));
        holder.price.setText(d.getPrice() + "");
        Picasso.get()
                .load(d.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var_info info = new var_info();
                Bundle bundle = new Bundle();
                bundle.putString("id", d.getId()); //key and value
                info.setArguments(bundle);
                FragmentTransaction fragmentManager = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.main_container, info);
                fragmentManager.addToBackStack(null);
                fragmentManager.commit();
            }
        });
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.fav.setImageResource(R.drawable.redheart);

                Dish dish = (new Dish(d.getId(),d.getName().toString(), d.getPrice()));

                DocumentReference documentReference = firestore.collection("Favorite").document(mAuth.getCurrentUser().getUid());

                documentReference.collection("favorate").document(d.getId()).set(dish).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });



            }
        });

    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class mainViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView fav;
        ImageView image;

        public mainViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.dishname);
            price = itemView.findViewById(R.id.dishprice);
            image = itemView.findViewById(R.id.dishimg);
            fav = itemView.findViewById(R.id.dfav);
        }
    }
}
