package com.keremkulac.car_parts.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.keremkulac.car_parts.Categories.CarPart;
import com.keremkulac.car_parts.Categories.ShoppingCart;
import com.keremkulac.car_parts.FirebaseOperations;
import com.keremkulac.car_parts.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CarPartAdapter extends RecyclerView.Adapter<CarPartAdapter.CarPartHolder> {
    private ArrayList<CarPart> carPartList;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    String id;
    public ArrayList<ShoppingCart> shoppingCartArrayList;
    public CarPartAdapter(ArrayList<CarPart> carPartList) {
        this.carPartList = carPartList;
    }



    @NonNull
    @Override
    public CarPartAdapter.CarPartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_car_part,parent,false);
        CarPartHolder carPartHolder = new CarPartHolder(view);
        return  carPartHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarPartAdapter.CarPartHolder holder, int position) {
     CarPart carPart = carPartList.get(position);
     holder.name.setText(carPart.getName());
     holder.code.setText(carPart.getCode());
     holder.price.setText(carPart.getPrice());
     holder.name.setText(carPart.getName());
     Picasso.get().load(carPart.getDownloadUrl()).resize(150,120).into(holder.image);
     holder.carPart = carPart;

    }

    @Override
    public int getItemCount() {
        return carPartList.size();
    }

    public class CarPartHolder extends RecyclerView.ViewHolder {
        TextView name, code, price;
        ImageView image;
        CarPart carPart;


        public CarPartHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerPartName);
            code = itemView.findViewById(R.id.recyclerPartCode);
            price = itemView.findViewById(R.id.recyclerPartPrice);
            image = itemView.findViewById(R.id.recyclerPartImage);

            itemView.findViewById(R.id.recyclerPartAddShoppingCart)
                    .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    auth = FirebaseAuth.getInstance();
                    user = auth.getCurrentUser();
                    firestore = FirebaseFirestore.getInstance();
                    firestore.collection("User")
                            .whereEqualTo("userEmail",user.getEmail())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    id= document.getData().get("userID").toString();
                                }
                                System.out.println("ıd"+id);
                            }
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            Map<String,Object> shoppingCart = new HashMap<>();
                            shoppingCart.put("OrderPartName",carPart.getName());
                            shoppingCart.put("OrderPartCode",carPart.getCode());
                            shoppingCart.put("OrderPartPrice",carPart.getPrice());
                            shoppingCart.put("OrderPartUrl",carPart.getDownloadUrl());
                            shoppingCart.put("OrderPartEmail",user.getEmail());
                            shoppingCart.put("OrderPartDate",formatter.format(date));
                            DatabaseReference tasksRef = rootRef.child(id).push();
                            tasksRef.setValue(shoppingCart);

                        }
                    });

                }
            });
        }
    }



}
