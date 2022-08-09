package com.example.menu.admin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.menu.R;
import com.example.menu.adapters.DishAdapter;
import com.example.menu.admin.adapters.AdminDishAdapter;
import com.example.menu.fragments.main_fragment;
import com.example.menu.models.Dish;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminMainFragment extends Fragment {
    RecyclerView lrec;
    ArrayList<Dish> dishList;
    private AdminDishAdapter adapter;
    private FirebaseFirestore firestore;
    private FloatingActionButton floatingActionButton;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment adminMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminMainFragment newInstance(String param1, String param2) {
        AdminMainFragment fragment = new AdminMainFragment();
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
        View v =  inflater.inflate(R.layout.fragment_admin_main, container, false);

        lrec=v.findViewById(R.id.adminlrec);
        lrec.setHasFixedSize(true);
        lrec.setLayoutManager(new LinearLayoutManager(getContext()));
        dishList = new ArrayList<>();
        floatingActionButton = v.findViewById(R.id.new_food);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFood fragment = new AddFood();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.admin_main_container, fragment);
                ft.commit();
            }
        });


        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Food").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Dish upload = q.toObject(Dish.class);
                    dishList.add(upload);
                }
                adapter = new AdminDishAdapter(dishList);
                lrec.setAdapter(adapter);
                LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                lrec.setLayoutManager(horizontalLayoutManagaer);
                lrec.setVisibility(View.VISIBLE);
            }

        });


        return v;
    }
}