package com.keremkulac.car_parts.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keremkulac.car_parts.Categories.ShoppingCart;
import com.keremkulac.car_parts.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartHolder> {

    private ArrayList<ShoppingCart> shoppingCartArrayList;
    public ShoppingCartAdapter(ArrayList<ShoppingCart> shoppingCartArrayList) {
        this.shoppingCartArrayList = shoppingCartArrayList;
    }

    @NonNull
    @Override
    public ShoppingCartAdapter.ShoppingCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_shopping_cart,parent,false);
        ShoppingCartAdapter.ShoppingCartHolder shoppingCartHolder = new ShoppingCartAdapter.ShoppingCartHolder(view);
        return  shoppingCartHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartAdapter.ShoppingCartHolder holder, int position) {
        ShoppingCart shoppingCart = shoppingCartArrayList.get(position);
        holder.name.setText(shoppingCart.getName());
        holder.code.setText(shoppingCart.getCode());
        holder.price.setText(shoppingCart.getPrice());
        Picasso.get().load(shoppingCart.getDownloadUrl()).resize(150,120).into(holder.image);
        holder.shoppingCart = shoppingCart;
    }

    @Override
    public int getItemCount() {
        return shoppingCartArrayList.size();
    }

    public class ShoppingCartHolder extends RecyclerView.ViewHolder {
        TextView name, code, price;
        ImageView image;
        ShoppingCart shoppingCart;

        public ShoppingCartHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerShoppingCartName);
            code = itemView.findViewById(R.id.recyclerShoppingCartCode);
            price = itemView.findViewById(R.id.recyclerShoppingCartPrice);
            image = itemView.findViewById(R.id.recyclerShoppingCartImage);

        }
    }

}
