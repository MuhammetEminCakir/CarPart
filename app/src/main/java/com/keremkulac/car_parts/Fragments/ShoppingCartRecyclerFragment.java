package com.keremkulac.car_parts.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.keremkulac.car_parts.Adapter.ShoppingCartAdapter;
import com.keremkulac.car_parts.Categories.ShoppingCart;
import com.keremkulac.car_parts.Activities.MainActivity2;
import com.keremkulac.car_parts.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ShoppingCartRecyclerFragment extends Fragment {

    ArrayList<ShoppingCart> shoppingCartArrayList;
    ShoppingCartAdapter shoppingCartAdapter;
    RecyclerView recyclerViewShoppingCart;
    FirebaseFirestore firebaseFirestore ;
    FirebaseUser firebaseUser ;
    FirebaseAuth auth;
    TextView recyclerViewShoppingCartTotalPrice;
    Button recyclerViewShoppingCartBuy;
    float totalPrice =0;
    String currentBalance;
    DecimalFormat df ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart_recycler, container, false);
        Init(view);
        getData();
        getCurrentBalance();
        buy();
        df = new DecimalFormat("#.##");
        return view;
    }
    public void Init(View view){
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerViewShoppingCartBuy = view.findViewById(R.id.recyclerViewShoppingCartBuy);
        recyclerViewShoppingCartTotalPrice = view.findViewById(R.id.recyclerViewShoppingCartTotalPrice);
        recyclerViewShoppingCart = view.findViewById(R.id.recyclerViewShoppingCart);
        shoppingCartArrayList = new ArrayList<>();
        recyclerViewShoppingCart.setHasFixedSize(true);
        recyclerViewShoppingCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        shoppingCartAdapter = new ShoppingCartAdapter(shoppingCartArrayList);
        recyclerViewShoppingCart.setAdapter(shoppingCartAdapter);

    }

    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("OrderTemp");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    String name = data.child("OrderPartName").getValue(String.class);
                    String code = data.child("OrderPartCode").getValue(String.class);
                    String price = data.child("OrderPartPrice").getValue(String.class);
                    String url = data.child("OrderPartUrl").getValue(String.class);
                    String date = data.child("OrderPartDate").getValue(String.class);
                    String email = data.child("OrderPartEmail").getValue(String.class);
                    ShoppingCart shoppingCart = new ShoppingCart(name,code,price,url,email,date);
                    shoppingCartArrayList.add(shoppingCart);
                    String replacePrice = price;
                    replacePrice = replacePrice.replace(",", ".");
                    float f = Float.parseFloat(replacePrice);
                    totalPrice = totalPrice+f;
                    System.out.println("xxxxxx"+price);
                }
                shoppingCartAdapter.notifyDataSetChanged();
                recyclerViewShoppingCartTotalPrice.setText(df.format(totalPrice)+" TL");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void getCurrentBalance(){
        firebaseFirestore.collection("User")
                .whereEqualTo("userEmail",firebaseUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currentBalance = document.getData().get("userBalance").toString().trim();
                            }
                        }
                    }
                });


    }
    public void buy(){
        recyclerViewShoppingCartBuy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Float currentBalancee = Float.parseFloat(currentBalance);
                if(currentBalancee>totalPrice){
                    currentBalancee =currentBalancee-totalPrice;
                    firebaseFirestore.collection("User").document(firebaseUser.getEmail())
                            .update("userBalance", df.format(currentBalancee));

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference tasksRef = rootRef.child(firebaseUser.getUid()).push();
                    System.out.println("KEY"+tasksRef.getKey());
                    tasksRef.setValue(shoppingCartArrayList);

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    HashMap<String,Object> orderHistory = new HashMap<>();
                    orderHistory.put("orderHistoryDate",formatter.format(date));
                    orderHistory.put("orderHistoryMail",firebaseUser.getEmail());
                    orderHistory.put("orderHistoryTotalPrice",df.format(totalPrice));
                    orderHistory.put("orderHistoryID",tasksRef.getKey());
                    firebaseFirestore.collection("OrderHistory")
                            .add(orderHistory).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                        }
                    });

                }else{
                    Toast.makeText(getActivity(),"Bakiyeniz yetersiz",Toast.LENGTH_SHORT).show();
                }
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), MainActivity2.class);
                        startActivity(intent);
                        deleteTempDatabase();
                    }
                };
                handler.postDelayed(runnable,2000);
            }
        });

    }
    public void deleteTempDatabase(){
        DatabaseReference databaseDelete = FirebaseDatabase.getInstance().getReference();
        Query query = databaseDelete.child("OrderTemp")
                .orderByChild("OrderPartEmail")
                .equalTo(firebaseUser.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
