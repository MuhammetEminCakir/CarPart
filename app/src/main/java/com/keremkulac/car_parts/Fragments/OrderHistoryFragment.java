package com.keremkulac.car_parts.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.keremkulac.car_parts.Adapter.OrderHistoryAdapter;
import com.keremkulac.car_parts.Adapter.ShoppingCartAdapter;
import com.keremkulac.car_parts.Categories.CarPart;
import com.keremkulac.car_parts.Categories.OrderHistory;
import com.keremkulac.car_parts.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class OrderHistoryFragment extends Fragment {

    RecyclerView recyclerView;
    OrderHistoryAdapter orderHistoryAdapter;
    ArrayList<OrderHistory> orderHistoryArrayList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DecimalFormat df ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        Init(view);
        getData();
        return view;
    }


    private void Init(View view) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        recyclerView = view.findViewById(R.id.recyclerViewOrderHistory);
        orderHistoryArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderHistoryAdapter = new OrderHistoryAdapter(orderHistoryArrayList, getActivity()) {
            @Override
            public Context getContext() {
                return null;
            }
        };
        recyclerView.setAdapter(orderHistoryAdapter);
        df = new DecimalFormat("#.##");

    }

    private void getData() {

        firebaseFirestore.collection("OrderHistory")
                .whereEqualTo("orderHistoryMail",firebaseUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String date = document.getData().get("orderHistoryDate").toString().trim();
                                String totalPrice = document.getData().get("orderHistoryTotalPrice").toString().trim();
                                String replacePrice = totalPrice;
                                replacePrice = replacePrice.replace(",", ".");
                                float f = Float.parseFloat(replacePrice);
                                OrderHistory orderHistory = new OrderHistory(date,String.valueOf(df.format(f)));
                                orderHistoryArrayList.add(orderHistory);

                            }
                            orderHistoryAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }



}