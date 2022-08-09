package com.example.menu.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.menu.R;
import com.example.menu.adapters.CartAdapter;
import com.example.menu.adapters.MainAdapter;
import com.example.menu.models.Dish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link var_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class var_info extends Fragment {
    TextView title;
    ImageView image;
    TextView desc,total,price,calory;
    Button a,b,add;
    RatingBar rating;

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String id;
    private String mParam2;

    public var_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment var_info.
     */
    // TODO: Rename and change types and number of parameters
    public static var_info newInstance(String param1, String param2) {
        var_info fragment = new var_info();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_var_info, container, false);
        title = v.findViewById(R.id.vartitle);
        image = v.findViewById(R.id.varimg);
        desc = v.findViewById(R.id.vardesc);
        rating = v.findViewById(R.id.varrating);
        calory = v.findViewById(R.id.varcalory);

        price = v.findViewById(R.id.varprice);
        total = v.findViewById(R.id.vartotal);
        a = v.findViewById(R.id.vara);
        b = v.findViewById(R.id.varb);
        add = v.findViewById(R.id.varbtn);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();//عرض البيانات حسب الاسم القادم من الريسايكلر

        firestore.collection("Food").whereEqualTo("id",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Dish upload = q.toObject(Dish.class);

                    title.setText(upload.getName());
                        Picasso.get()
                                .load(upload.getImage())
                                .placeholder(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(image);
                        price.setText(upload.getPrice()+"");
                        desc.setText(upload.getDescription());
                        rating.setRating(upload.getRating());
                        calory.setText(upload.getCalories()+"");
                }

            }

        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(total.getText().toString());
                num++;
                float p=Float.valueOf(price.getText().toString());
                price.setText((num*p)+"");
                total.setText(num+"");
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(total.getText().toString());
                if(num > 1)
                num--;
                float p=Float.valueOf(price.getText().toString());
                price.setText((num*p)+"");
                total.setText(num+"");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(total.getText().toString());
                float p=Float.valueOf(price.getText().toString());
                Dish dish = (new Dish(id,title.getText().toString(), p));

                DocumentReference documentReference = firestore.collection("Cart").document(mAuth.getCurrentUser().getUid());

                documentReference.collection("cart").document(id).set(dish).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
        return v;
    }
}