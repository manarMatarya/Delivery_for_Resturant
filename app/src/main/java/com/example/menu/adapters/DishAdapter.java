package com.example.menu.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.models.Dish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.dishViewHolder> {
    ArrayList<Dish> dishes;
    Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public DishAdapter(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public dishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list,null,false) ;

        DishAdapter.dishViewHolder s = new dishViewHolder(v) ;
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull final dishViewHolder holder, int position) {
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

        holder.a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = Integer.valueOf(holder.ltotal.getText().toString());
                total++;
                float p=Float.valueOf(holder.price.getText().toString());
                holder.ltotal.setText(total+"");
                holder.price.setText((total*p)+"");
            }
        });

        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = Integer.valueOf(holder.ltotal.getText().toString());
                if(total > 1)
                total--;
                float p=Float.valueOf(holder.price.getText().toString());
                holder.ltotal.setText(total+"");
                holder.price.setText((total*p)+"");
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                int total = Integer.valueOf(holder.ltotal.getText().toString());
                final float price = (d.getPrice()) * (total);

                Dish dish = (new Dish(d.getId(),d.getName().toString(), d.getPrice()));

                DocumentReference documentReference = firestore.collection("Cart").document(mAuth.getCurrentUser().getUid());

                documentReference.collection("cart").document(d.getId()).set(dish).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    class dishViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView desc;
        TextView price;
        ImageView img;
        Button a,b;
        TextView ltotal;
        Button add;
        public dishViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.lname);
            desc = itemView.findViewById(R.id.ldesc);
            price = itemView.findViewById(R.id.lprice);
            img = itemView.findViewById(R.id.limg);
            a = itemView.findViewById(R.id.a);
            b = itemView.findViewById(R.id.b);
            ltotal = itemView.findViewById(R.id.ltotal);
            add = itemView.findViewById(R.id.lbtnAdd);

        }
    }
    }

