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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.keremkulac.car_parts.R;

import java.util.HashMap;
import java.util.Random;


public class SignUpFragment extends Fragment {
    EditText signUpEmail,signUpPassword,signUpName,signUpLastname,signUpPhoneNumber;
    ImageView signUpBackButton;
    Button signUpSignUpButton;
    LoginFragment loginFragment;
    String email,password,name,lastname,phoneNumber;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        Init(view);
        back();
        signUp();
        return view;
    }

    public void Init(View view){
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        signUpEmail = view.findViewById(R.id.signUpEmail);
        signUpPassword = view.findViewById(R.id.signUpPassword);
        signUpName = view.findViewById(R.id.signUpName);
        signUpLastname = view.findViewById(R.id.signUpLastname);
        signUpPhoneNumber = view.findViewById(R.id.signUpPhoneNumber);
        signUpBackButton = view.findViewById(R.id.signUpBackButton);
        signUpSignUpButton = view.findViewById(R.id.signUpSignUpButton);
        loginFragment = new LoginFragment();
    }
    public void back(){
        signUpBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragments(loginFragment,R.id.main_frame_layout);
            }
        });
    }
    public void signUp(){
        signUpSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = signUpEmail.getText().toString().trim();
                password = signUpPassword.getText().toString().trim();
                name = signUpName.getText().toString().trim();
                lastname = signUpLastname.getText().toString().trim();
                phoneNumber = signUpPhoneNumber.getText().toString().trim();
                if(email.equals("") || password.equals("") || name.equals("") || lastname.equals("" ) || phoneNumber.equals("")){
                    Toast.makeText(getActivity(),"Lütfen bilgilerinizi giriniz",Toast.LENGTH_LONG).show();
                }else{
                    Random rand = new Random();
                    int random1 = 100+rand.nextInt(900);
                    int random2 = 100+rand.nextInt(900);
                    HashMap<String,Object> registerUser = new HashMap<>();
                    registerUser.put("userEmail",email);
                    registerUser.put("userName",name);
                    registerUser.put("userLastname",lastname);
                    registerUser.put("userPhoneNumber",phoneNumber);
                    registerUser.put("userBalance","0");
                    registerUser.put("userID",random1+ " "+random2);
                    firebaseFirestore.collection("User")
                            .document(email)
                            .set(registerUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(),"Kayıt olma başarılı",Toast.LENGTH_SHORT).show();
                                        userMailPasswordRegister();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            try {
                                throw e;
                            }
                            catch(FirebaseAuthWeakPasswordException ex) {
                                Toast.makeText(getActivity(),"Lütfen 6 karakterden uzun bir şifre giriniz",Toast.LENGTH_LONG).show();
                                signUpPassword.requestFocus();
                            }
                            catch (FirebaseAuthUserCollisionException ex) {
                                Toast.makeText(getActivity(),"Bu email ile kayıtlı bir kullanıcı bulunmaktadır",Toast.LENGTH_LONG).show();
                                signUpEmail.requestFocus();
                            }catch(Exception ex) {
                                Toast.makeText(getActivity(),"Kayıt olma işlemi başarısız",Toast.LENGTH_LONG).show();
                            }
                            //Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }

                    });
                }

            }
        });
    }
    public void setFragments(Fragment fragment, int layoutID){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutID,fragment).commit();
    }
    public void userMailPasswordRegister(){
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getActivity(),"Kayıt başarılı",Toast.LENGTH_SHORT);
                setFragments(loginFragment,R.id.main_frame_layout);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}