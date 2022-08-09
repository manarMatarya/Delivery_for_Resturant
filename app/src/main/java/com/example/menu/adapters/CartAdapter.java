package com.example.menu.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.models.Dish;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.cartViewHolder> {
    public static float total =0;
    ArrayList<Dish> dishes;
    Context context;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    public CartAdapter(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart,null,false) ;

        CartAdapter.cartViewHolder s = new CartAdapter.cartViewHolder(v) ;
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull cartViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Dish d = dishes.get(position);
        holder.name.setText((d.getName()));
        holder.price.setText(d.getPrice()+"");

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dishes.remove(position);
                notifyItemRemoved(position);

                firestore = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();


                DocumentReference documentReference = firestore.collection("Cart").document(mAuth.getCurrentUser().getUid());

                documentReference.collection("cart").document(d.getId()).delete();


            }
        });


    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class cartViewHolder extends RecyclerView.ViewHolder {
        TextView name,price;
        ImageView close;
       TextView totalcart;
        public cartViewHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            name=itemView.findViewById(R.id.cartname);
            price=itemView.findViewById(R.id.cartprice);
            close=itemView.findViewById(R.id.cartclose);
           totalcart = itemView.findViewById(R.id.carttotal);

        }
    }

}

