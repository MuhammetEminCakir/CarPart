package com.keremkulac.car_parts.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.keremkulac.car_parts.R;



public class AccountFragment extends Fragment {

    Button accountAddBalance;
    EditText accountBalanceNew;
    TextView accountName,accountLastname,accountCurrentUserBalance,accountPhoneNumber,accountEmail;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Init(view);
        addBalance();
        getData();
        return view;
    }

    public void Init(View view){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        accountAddBalance = view.findViewById(R.id.accountAddBalance);
        accountBalanceNew = view.findViewById(R.id.accountBalanceNew);
        accountPhoneNumber= view.findViewById(R.id.accountPhoneNumber);
        accountEmail= view.findViewById(R.id.accountEmail);
        accountName = view.findViewById(R.id.accountName);
        accountLastname = view.findViewById(R.id.accountLastname);
        accountCurrentUserBalance = view.findViewById(R.id.accountCurrentUserBalance);

    }

    public void addBalance(){
        accountAddBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float newBalance = Float.parseFloat(accountBalanceNew.getText().toString());
                Float oldBalance = Float.parseFloat(accountCurrentUserBalance.getText().toString());
                String total = String.valueOf(newBalance+oldBalance);
                firebaseFirestore.collection("User").document(firebaseUser.getEmail())
                        .update(
                                "userBalance",total);
                getData();
            }
        });
    }
    public void getData(){
        firebaseFirestore.collection("User")
                .whereEqualTo("userEmail",firebaseUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                accountName.setText(document.getData().get("userName").toString());
                                accountLastname.setText(document.getData().get("userLastname").toString());
                                accountCurrentUserBalance.setText(document.getData().get("userBalance").toString());
                                accountPhoneNumber.setText(document.getData().get("userPhoneNumber").toString());
                                accountEmail.setText(document.getData().get("userEmail").toString());
                            }
                        }else{
                            Toast.makeText(getActivity(),task.getException().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void setFragments(Fragment fragment, int layoutID){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutID,fragment).commit();
    }
}