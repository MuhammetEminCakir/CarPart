package com.keremkulac.car_parts.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.keremkulac.car_parts.Categories.OrderHistory;
import com.keremkulac.car_parts.Fragments.OrderFragment;
import com.keremkulac.car_parts.R;

import java.util.ArrayList;

public abstract class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryHolder>{
    FragmentActivity activity;
    ArrayList<OrderHistory> orderHistoryArrayList;



    public OrderHistoryAdapter(ArrayList<OrderHistory> orderHistoryArrayList, FragmentActivity activity) {
        this.orderHistoryArrayList = orderHistoryArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public OrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_order_history,parent,false);
        OrderHistoryAdapter.OrderHistoryHolder orderHistoryHolder = new OrderHistoryAdapter.OrderHistoryHolder(view);
        return  orderHistoryHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryHolder holder, int position) {
        OrderHistory orderHistory = orderHistoryArrayList.get(position);
        holder.date.setText(orderHistory.getDate());
        holder.totalPrice.setText(orderHistory.getTotal());
        holder.orderHistory = orderHistory;

    }

    @Override
    public int getItemCount() {
        return orderHistoryArrayList.size();
    }

    public abstract Context getContext();

    public class OrderHistoryHolder extends RecyclerView.ViewHolder{
        TextView date,totalPrice;
        OrderHistory orderHistory;
        CardView recyclerOrderHistoryDetail;
        public OrderHistoryHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.recyclerOrderHistoryDate);
            totalPrice = itemView.findViewById(R.id.recyclerOrderHistoryTotalPrice);
            recyclerOrderHistoryDetail = itemView.findViewById(R.id.recyclerOrderHistoryDetail);
            recyclerOrderHistoryDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    System.out.println("Deneme");
                    OrderFragment orderFragment = new OrderFragment();
                    setFragmentsBundle(orderFragment,bundle,R.id.frame_layout,totalPrice.getText().toString());
                    //setFragments(orderFragment,R.id.frame_layout);
                }
            });

        }
    }
    public void setFragmentsBundle(Fragment fragment, Bundle bundle, int layoutID, String totalPrice){
        bundle.putString("totalPrice",totalPrice);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutID,fragment).commit();
    }




}
