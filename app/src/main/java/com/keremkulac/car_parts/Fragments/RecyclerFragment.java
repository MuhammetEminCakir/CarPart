package com.keremkulac.car_parts.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.keremkulac.car_parts.Adapter.CarPartAdapter;
import com.keremkulac.car_parts.Categories.CarPart;
import com.keremkulac.car_parts.R;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    ArrayList<CarPart> carPartArrayList;
    CarPartAdapter carPartAdapter;
    RecyclerView recyclerView;
    String incomingSelectMainCategoryName,incomingSelectSubCategoryName ;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        Init(view);
        getData();
        return view;
    }
    public void Init(View view){
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        carPartArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        carPartAdapter = new CarPartAdapter(carPartArrayList);
        recyclerView.setAdapter(carPartAdapter);
        incomingSelectMainCategoryName = getArguments().getString("selectMainCategory");
        incomingSelectSubCategoryName =  getArguments().getString("selectSubCategory");
        System.out.println(incomingSelectMainCategoryName);
        System.out.println(incomingSelectSubCategoryName);
    }

    public void getData(){
        firebaseFirestore.collection("CarParts")
                .whereEqualTo("carPartMainCategory",incomingSelectMainCategoryName)
                .whereEqualTo("carPartSubCategory",incomingSelectSubCategoryName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String ,Object> data = document.getData();
                        String carPartName = (String) data.get("carPartName");
                        String carPartCode = (String)data.get("carPartCode");
                        String carPartPrice = (String)data.get("carPartPrice");
                        String downloadUrl = (String)data.get("carPartImageUrl");
                        CarPart carPart = new CarPart(carPartName,carPartCode,carPartPrice,downloadUrl);
                        carPartArrayList.add(carPart);
                    }
                    carPartAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(),task.getException().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}