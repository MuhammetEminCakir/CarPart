package com.keremkulac.car_parts.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.keremkulac.car_parts.Activities.MainActivity2;
import com.keremkulac.car_parts.R;


public class LoginFragment extends Fragment {
    Button loginSignUpButton,loginLoginButton;
    EditText loginEmail,loginPassword;
    SignUpFragment signUpFragment;
    FirebaseAuth auth;
    String email,password;
    FirebaseUser currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Init(view);
        login();
        signUp();
        if(currentUser != null){
            Intent intent = new Intent(getActivity(), MainActivity2.class);
            startActivity(intent);
        }
        return view;
    }
    public void Init(View view){
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        loginLoginButton = view.findViewById(R.id.loginLoginButton);
        loginSignUpButton = view.findViewById(R.id.loginSignUpButton);
        loginEmail = view.findViewById(R.id.loginEmail);
        loginPassword = view.findViewById(R.id.loginPassword);
        signUpFragment = new SignUpFragment();
    }

    public void login(){
        loginLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = loginEmail.getText().toString().trim();
                password = loginPassword.getText().toString().trim();
                if(email.equals("") || password.equals("")){
                    Toast.makeText(getActivity(),"Lütfen email ve şifrenizi giriniz",Toast.LENGTH_LONG).show();
                }else{
                    if(email.equals("admin@gmail.com" ) && password.equals("admin123")){
                        CarPartAddFragment carPartAddFragment = new CarPartAddFragment();
                        setFragments(carPartAddFragment,R.id.main_frame_layout);
                    }else{
                        auth.signInWithEmailAndPassword(email,password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Intent intent = new Intent(getActivity(), MainActivity2.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                try {
                                    throw e;
                                } catch(FirebaseAuthInvalidUserException ex) {
                                    Toast.makeText(getActivity(),"Lütfen geçerli kayıtlı bir email adresi giriniz",Toast.LENGTH_LONG).show();
                                    loginEmail.requestFocus();
                                } catch(FirebaseNetworkException ex) {
                                    Toast.makeText(getActivity(),"Internetinizi kontrol ediniz",Toast.LENGTH_LONG).show();
                                } catch(Exception ex) {
                                    Toast.makeText(getActivity(),"Giriş işlemi başarısız",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                }
            }
        });

    }
    public void signUp(){
        loginSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragments(signUpFragment,R.id.main_frame_layout);
            }
        });
    }

    public void setFragments(Fragment fragment, int layoutID){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutID,fragment).commit();
    }

}