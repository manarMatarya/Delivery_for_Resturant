package com.example.menu.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.menu.R;
import com.example.menu.activities.Registeration;
import com.example.menu.adapters.MainAdapter;
import com.example.menu.adapters.VarietiesAdapter;
import com.example.menu.models.Dish;
import com.example.menu.models.varieties;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link main_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class main_fragment extends Fragment {
    private ArrayList<Dish> list2, list3;
    private RecyclerView rec1, rec2, rec3;
    private MainAdapter adapter2, adapter3;
    private FirebaseFirestore firestore;
    EditText search;
    ImageView mainmore;
    TextView tvsearch;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public main_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment main_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static main_fragment newInstance(String param1, String param2) {
        main_fragment fragment = new main_fragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_main_fragment, container, false);

        mainmore = v.findViewById(R.id.mainmore);
        mainmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profile = new Profile();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, profile);
                fragmentTransaction.commit();

            }
        });

        rec1 = v.findViewById(R.id.R1);
        rec2 = v.findViewById(R.id.R2);
        rec3 = v.findViewById(R.id.R3);
        search = v.findViewById(R.id.search);
        tvsearch = v.findViewById(R.id.tvsearch);
        tvsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List list = new List();
                Bundle bundle = new Bundle();
                bundle.putString("title", search.getText().toString()); //key and value
                list.setArguments(bundle);
                FragmentTransaction fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.main_container, list);
                fragmentManager.addToBackStack(null);
                fragmentManager.commit();
            }

        });


        final ArrayList<varieties> varieties = new ArrayList<>();

        varieties.add(new varieties("Salads", R.drawable.ic_salad));
        varieties.add(new varieties("Sandwiches", R.drawable.ic_burger));
        varieties.add(new varieties("Meals", R.drawable.ic_meals));
        varieties.add(new varieties("Juices", R.drawable.ic_cocktail));
        varieties.add(new varieties("Dessert", R.drawable.ic_dessert));
        varieties.add(new varieties("Appetizer", R.drawable.ic_soup));

        VarietiesAdapter adapter1 = new VarietiesAdapter(varieties);
        rec1.setAdapter(adapter1);
        final LinearLayoutManager horizontalLayoutManagaer1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rec1.setLayoutManager(horizontalLayoutManagaer1);
        rec1.setVisibility(View.VISIBLE);

        list2 = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Food").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Dish upload = q.toObject(Dish.class);
                    list2.add(upload);
                }
                adapter2 = new MainAdapter(list2);
                rec2.setAdapter(adapter2);
                LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                rec2.setLayoutManager(horizontalLayoutManagaer2);
                rec2.setVisibility(View.VISIBLE);
            }

        });
        list3 = new ArrayList<>();
        firestore.collection("Food").whereEqualTo("isVegetarian", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Dish upload = q.toObject(Dish.class);
                    list3.add(upload);
                }
                adapter3 = new MainAdapter(list3);
                rec3.setAdapter(adapter3);
                LinearLayoutManager horizontalLayoutManagaer3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                rec3.setLayoutManager(horizontalLayoutManagaer3);
                rec3.setVisibility(View.VISIBLE);
            }

        });

        return v;
    }

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link profile#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class profile extends Fragment {
        TextView profilename, profileemail, profileaddress;
        private FirebaseAuth fAuth;

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public profile() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profile.
         */
        // TODO: Rename and change types and number of parameters
        public static profile newInstance(String param1, String param2) {
            profile fragment = new profile();
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
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }


        @Override
        public void onStart() {
            super.onStart();
            if (fAuth.getCurrentUser() == null) {
                startActivity(new Intent(getContext(), Registeration.class));
            }
        }
    }

}