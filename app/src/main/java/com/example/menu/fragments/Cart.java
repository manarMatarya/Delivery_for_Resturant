package com.example.menu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.activities.LocationPicker;
import com.example.menu.adapters.CartAdapter;
import com.example.menu.models.Dish;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cart extends Fragment {
    public static float total = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String ARG_PARAM1 = "name";
    private static String ARG_PARAM2 = "price";
    RecyclerView cartRec;
    TextView cartTotal;
    ArrayList<Dish> dishList;
    Button order, cont;
    private CartAdapter adapter;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String name = "";
    private float price = 0;

    public Cart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cart.
     */
    // TODO: Rename and change types and number of parameters
    public static Cart newInstance(String param1, String param2) {
        Cart fragment = new Cart();
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
            name = getArguments().getString(ARG_PARAM1);
            price = getArguments().getFloat(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        cartRec = v.findViewById(R.id.cartRec);
        cartTotal = v.findViewById(R.id.carttotal);
        order = v.findViewById(R.id.cartbtn);
        cont = v.findViewById(R.id.cartbtnCont);

        dishList = new ArrayList<>();
        cartRec.setHasFixedSize(true);
        cartRec.setLayoutManager(new LinearLayoutManager(getContext()));
        dishList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        total = 0;
        //عرض البيانات حسب الاسم القادم من الريسايكلر
        DocumentReference documentReference = firestore.collection("Cart").document(mAuth.getCurrentUser().getUid());

        documentReference.collection("cart").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Dish upload = q.toObject(Dish.class);
                    dishList.add(upload);
                    total += upload.getPrice();
                    cartTotal.setText(total + "");
                }
                adapter = new CartAdapter(dishList);
                cartRec.setAdapter(adapter);
                LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                cartRec.setLayoutManager(horizontalLayoutManagaer2);
                cartRec.setVisibility(View.VISIBLE);
            }

        });


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationPicker.class);
                startActivity(intent);
                //       Toast.makeText(getContext(), "Your order arrives soon.. ", Toast.LENGTH_LONG).show();
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_fragment fragment = new main_fragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container, fragment);
                ft.commit();
            }
        });
        return v;
    }
}