package com.example.menu.admin.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.admin.EditData;
import com.example.menu.models.Dish;
import com.example.menu.models.Reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.dishViewHolder> {
    ArrayList<Reservation> reservations;
    Context context;
    private FirebaseFirestore firestore;

    public ReservationAdapter(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public com.example.menu.admin.adapters.ReservationAdapter.dishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_reservation_item,null,false) ;

        com.example.menu.admin.adapters.ReservationAdapter.dishViewHolder s = new com.example.menu.admin.adapters.ReservationAdapter.dishViewHolder(v) ;
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull final com.example.menu.admin.adapters.ReservationAdapter.dishViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Reservation reservation = reservations.get(position);
        holder.name.setText((reservation.getName()));
        holder.phone.setText((reservation.getPhone()));
        holder.number.setText(reservation.getNumber());
        holder.date.setText(reservation.getDate());
        holder.time.setText(reservation.getTime());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Confirm Delete");
                alert.setMessage("Sure you want to delete this reservation?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        reservations.remove(position);
                        notifyItemRemoved(position);

                        firestore = FirebaseFirestore.getInstance();

                        DocumentReference documentReference = firestore.collection("Reservation").document(reservation.getId());
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
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    class dishViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView phone;
        TextView number;
        TextView date;
        TextView time;
        public dishViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.adminPersonName);
            phone = itemView.findViewById(R.id.adminPhoneNumber);
            number = itemView.findViewById(R.id.adminPersonNumber);
            date = itemView.findViewById(R.id.admindate);
            time = itemView.findViewById(R.id.admintime);
        }
    }
}