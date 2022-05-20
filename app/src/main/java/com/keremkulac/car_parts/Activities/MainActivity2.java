package com.keremkulac.car_parts.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.keremkulac.car_parts.Fragments.AccountFragment;
import com.keremkulac.car_parts.Fragments.HomePageFragment;
import com.keremkulac.car_parts.Fragments.LoginFragment;
import com.keremkulac.car_parts.Fragments.OrderHistoryFragment;
import com.keremkulac.car_parts.Fragments.RecyclerFragment;
import com.keremkulac.car_parts.Fragments.ShoppingCartRecyclerFragment;
import com.keremkulac.car_parts.R;

public class MainActivity2 extends AppCompatActivity {
    MaterialToolbar toolbar ;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Bundle bundle;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    String currentMail,currentUserName,currentUserLastname;
    ConstraintLayout accountButton,accountLogout,accountOrder,accountShoppingCart;
    TextView currentUserNameLastname;
    RecyclerView recyclerView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        HomePageFragment homePageFragment = new HomePageFragment();
        setFragments(homePageFragment,R.id.frame_layout);
        Init();
        toolBar();
        account();
        signOut();
        order();
        basket();
        getUserNameLastname();
    }

    public void Init(){
        recyclerView = findViewById(R.id.recyclerView);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bundle = new Bundle();
        accountButton = (ConstraintLayout) navigationView.getHeaderView(0).findViewById(R.id.accountButton);
        accountLogout = (ConstraintLayout) navigationView.getHeaderView(0).findViewById(R.id.accountLogout);
        accountOrder = (ConstraintLayout) navigationView.getHeaderView(0).findViewById(R.id.accountOrder);
        accountShoppingCart = (ConstraintLayout) navigationView.getHeaderView(0).findViewById(R.id.accountShoppingCart);
        currentUserNameLastname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.currentUserNameLastname);

    }

    public void toolBar(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id){
                    case R.id.nav_fuse_box:
                        RecyclerFragment recyclerFragment1 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment1,bundle,R.id.frame_layout,"Elektrik","Sigorta Kutuları");
                        break;
                    case R.id.nav_signal:
                        RecyclerFragment recyclerFragment2 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment2,bundle,R.id.frame_layout,"Elektrik","Sinyaller");
                        break;
                    case R.id.nav_headlight:
                        RecyclerFragment recyclerFragment3 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment3,bundle,R.id.frame_layout,"Elektrik","Farlar");
                        break;
                    case R.id.nav_buffer:
                        RecyclerFragment recyclerFragment4 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment4,bundle,R.id.frame_layout,"Kaporta","Tampon");
                        break;
                    case R.id.nav_door_handle:
                        RecyclerFragment recyclerFragment5 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment5,bundle,R.id.frame_layout,"Kaporta","Kapı Kolu");
                        break;
                    case R.id.nav_bonnet:
                        RecyclerFragment recyclerFragment6 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment6,bundle,R.id.frame_layout,"Kaporta","Kaput");
                        break;
                    case R.id.nav_air_filter:
                        RecyclerFragment recyclerFragment7 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment7,bundle,R.id.frame_layout,"Filtre","Hava Filtresi");
                        break;
                    case R.id.nav_pollen_filter:
                        RecyclerFragment recyclerFragment8 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment8,bundle,R.id.frame_layout,"Filtre","Polen Filtresi");
                        break;
                    case R.id.nav_oil_filter:
                        RecyclerFragment recyclerFragment9 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment9,bundle,R.id.frame_layout,"Filtre","Yağ Filtresi");
                        break;
                    case R.id.nav_fuel_filter:
                        RecyclerFragment recyclerFragment10 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment10,bundle,R.id.frame_layout,"Filtre","Yakıt Filtresi");
                        break;
                    case R.id.nav_brake_lining:
                        RecyclerFragment recyclerFragment11 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment11,bundle,R.id.frame_layout,"Fren","Fren Balatası");
                        break;
                    case R.id.nav_brake_disc:
                        RecyclerFragment recyclerFragment12 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment12,bundle,R.id.frame_layout,"Fren","Fren Diski");
                        break;
                    case R.id.nav_abs_sensor:
                        RecyclerFragment recyclerFragment13 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment13,bundle,R.id.frame_layout,"Fren","ABS Sensörü");
                        break;
                    case R.id.nav_radiator:
                        RecyclerFragment recyclerFragment14 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment14,bundle,R.id.frame_layout,"Motor","Radyatör");
                        break;
                    case R.id.nav_timing_belt:
                        RecyclerFragment recyclerFragment15 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment15,bundle,R.id.frame_layout,"Motor","Triger Kayışı");
                        break;
                    case R.id.nav_injector:
                        RecyclerFragment recyclerFragment16 = new RecyclerFragment();
                        setFragmentsBundle(recyclerFragment16,bundle,R.id.frame_layout,"Motor","Enjektör");
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    public void getUserNameLastname(){
        currentMail = user.getEmail();
        System.out.println(currentMail);
        firebaseFirestore.collection("User")
                .whereEqualTo("userEmail",currentMail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currentUserName = document.getData().get("userName").toString();
                                currentUserLastname = document.getData().get("userLastname").toString();
                                currentUserNameLastname.setText(currentUserName+" "+currentUserLastname);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signOut(){
        accountLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment loginFragment = new LoginFragment();
                auth.signOut();
                Intent loginIntent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(loginIntent);
                finish();
                Toast.makeText(getApplicationContext(),"Oturum sonlandırıldı",Toast.LENGTH_SHORT).show();

                //setFragments(loginFragment,R.id.main_frame_layout);
            }
        });
    }

    public void account(){
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountFragment accountFragment = new AccountFragment();
                System.out.println("Account");
                setFragments(accountFragment,R.id.frame_layout);
            }
        });
    }

    public void order(){
        accountOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  OrderFragment orderFragment = new OrderFragment();
                OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
                setFragments(orderHistoryFragment,R.id.frame_layout);

            }
        });
    }

    public void basket(){
        accountShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingCartRecyclerFragment shoppingCartRecyclerFragment = new ShoppingCartRecyclerFragment();
                setFragments(shoppingCartRecyclerFragment,R.id.frame_layout);
            }
        });
    }

    public void setFragments(Fragment fragment, int layoutID){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutID,fragment).commit();
    }

    public void setFragmentsBundle(Fragment fragment, Bundle bundle, int layoutID, String selectMainCategory,String selectSubCategory){
        bundle.putString("selectMainCategory",selectMainCategory);
        bundle.putString("selectSubCategory",selectSubCategory);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutID,fragment).commit();
    }


}