package com.keremkulac.car_parts.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.keremkulac.car_parts.Adapter.OrderAdapter;
import com.keremkulac.car_parts.Categories.Order;
import com.keremkulac.car_parts.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class OrderFragment extends Fragment {

    ArrayList<Order> orderArrayList;
    RecyclerView  recyclerView;
    OrderAdapter orderAdapter;
    float totalPrice =0;
    SimpleDateFormat formatter;
    Date date ;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String id,total;
    FirebaseFirestore firebaseFirestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        Init(view);
        getID();
        return view;
    }

    public void Init(View view){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerViewOrder);
        orderArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderAdapter = new OrderAdapter(orderArrayList);
        recyclerView.setAdapter(orderAdapter);
        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        date = new Date();
        total =  getArguments().getString("totalPrice");
    }
    public void getID(){
        firebaseFirestore.collection("OrderHistory")
                .whereEqualTo("orderHistoryMail",firebaseUser.getEmail())
                .whereEqualTo("orderHistoryTotalPrice",total)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              id= document.getData().get("orderHistoryID").toString();
                            }
                            getData(id);
                        }
                    }
                });
    }

    private void getData(String id) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference
                .child(firebaseUser.getUid())
                .child(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    String name = data.child("name").getValue(String.class);
                    String code = data.child("code").getValue(String.class);
                    String price = data.child("price").getValue(String.class);
                    String url = data.child("downloadUrl").getValue(String.class);
                    String date = data.child("date").getValue(String.class);
                    System.out.println(name);
                    System.out.println(code);
                    System.out.println(price);
                    System.out.println(date);
                    Order order = new Order(name,price,date,code,url);
                    orderArrayList.add(order);
                    String replacePrice = price;
                    replacePrice = replacePrice.replace(",", ".");
                    float f = Float.parseFloat(replacePrice);
                    totalPrice = totalPrice+f;
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}