package com.keremkulac.car_parts.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keremkulac.car_parts.Categories.Order;
import com.keremkulac.car_parts.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    ArrayList<Order> orderArrayList;

    public OrderAdapter(ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_order,parent,false);
        OrderAdapter.OrderHolder orderHolder = new OrderAdapter.OrderHolder(view);
        return  orderHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        Order order = orderArrayList.get(position);
        holder.name.setText(order.getName());
        holder.code.setText(order.getCode());
        holder.price.setText(order.getPrice());
        Picasso.get().load(order.getUrl()).resize(150,120).into(holder.image);
        holder.order = order;

    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        TextView name,code,price;
        ImageView image;
        Order order;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerOrderName);
            code = itemView.findViewById(R.id.recyclerOrderCode);
            price = itemView.findViewById(R.id.recyclerOrderPrice);
            image = itemView.findViewById(R.id.recyclerOrderImage);
        }
    }
}
