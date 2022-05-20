package com.keremkulac.car_parts.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.keremkulac.car_parts.Activities.MainActivity;
import com.keremkulac.car_parts.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


public class CarPartAddFragment extends Fragment {
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    String selectedMainItem,selectedSubItem,pictureName,partName,partCode,partMainCategoryName,partSubCategoryName,currentDate,partPrice;
    ArrayAdapter<String> arrayAdapter ;
    Spinner selectMainCategory, selectSubCategory ;
    Button carPartSave ;
    Uri imageData;
    ImageView addCarPartSelectImage,addCarPartBack;
    EditText addCarPartName,addCarPartCode,addCarPartPrice;
    SimpleDateFormat formatter;
    Date date ;
    FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_part_add, container, false);
        Init(view);
        carPartList();
        selectedPartImage();
        registerLauncher(view);
        register();
        back();
        return view;
    }

    private void back() {
        addCarPartBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void Init(View view) {
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
        selectMainCategory = view.findViewById(R.id.addCarPartSelectMainCategory);
        selectSubCategory = view.findViewById(R.id.addCarPartSelectSubCategory);
        carPartSave = view.findViewById(R.id.carPartSave);
        addCarPartSelectImage = view.findViewById(R.id.addCarPartSelectImage);
        addCarPartCode = view.findViewById(R.id.addCarPartCode);
        addCarPartName = view.findViewById(R.id.addCarPartName);
        addCarPartPrice = view.findViewById(R.id.addCarPartPrice);
        addCarPartBack = view.findViewById(R.id.addCarPartBack);
        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        date = new Date();
    }

    public void arrayAdapterList(int id){
            arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1
                    ,getResources().getStringArray(id));
        }
    public void carPartList(){
        arrayAdapterList(R.array.carPartArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectMainCategory.setAdapter(arrayAdapter);
        selectMainCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                if(i == 0){
                    tv.setTextColor(Color.GRAY);
                } else if(i > 0 ){
                    selectedMainItem = (String)adapterView.getItemAtPosition(i);
                    carPartList2(selectedMainItem);
                    System.out.println(selectedMainItem);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(),"Lütfen bir kategori seçiniz",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void carPartList2(String item){
        switch (item){
            case "Elektrik":
                carPartListSub(R.array.carPartElectricArray);
                break;
            case "Filtre":
                carPartListSub(R.array.carPartFilterArray);
                break;
            case "Fren":
                carPartListSub(R.array.carPartBrakeArray);
                break;
            case "Kaporta":
                carPartListSub(R.array.carPartBodyworkArray);
                break;
            case "Motor":
                carPartListSub(R.array.carPartEngineArray);
                break;
        }
    }
    public void carPartListSub(int id){
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1
                ,getResources().getStringArray(id));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSubCategory.setAdapter(arrayAdapter);
        selectSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0 ){
                    selectedSubItem = (String) adapterView.getItemAtPosition(i);
                    System.out.println(selectedSubItem);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(),"Lütfen bir kategori seçiniz",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void register(){
        carPartSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                partName = addCarPartName.getText().toString().trim();
                partCode = addCarPartCode.getText().toString().trim();
                partPrice = addCarPartPrice.getText().toString().trim();
                partMainCategoryName = selectedMainItem;
                partSubCategoryName = selectedSubItem;

                currentDate = formatter.format(date);
                if(partName.equals("") || partCode.equals("") || partMainCategoryName.equals("") ||  partSubCategoryName.equals("")||partPrice.equals("")  ){
                    System.out.println("asdas7");
                    Toast.makeText(getActivity(),"Lütfen bilgileri eksiksiz giriniz",Toast.LENGTH_LONG).show();
                }else{
                    UUID uuid = UUID.randomUUID();
                    pictureName = "images/"+uuid+".jpg";
                    if(imageData != null){
                        carPartSaveToDatabase();
                    }else{
                        System.out.println("asdas6");
                        Toast.makeText(getActivity(),"Lütfen eklemek istediğiniz parçanın fotoğrafını seçiniz",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void carPartSaveToDatabase(){
        storageReference.child(pictureName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference pictureReference = firebaseStorage.getReference(pictureName);
                pictureReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String pictureUrl = uri.toString();
                        HashMap<String,Object> carParts = new HashMap<>();
                        carParts.put("carPartMainCategory",partMainCategoryName);
                        carParts.put("carPartSubCategory",partSubCategoryName);
                        carParts.put("carPartName",partName);
                        carParts.put("carPartPrice",partPrice+" "+"TRY");
                        carParts.put("carPartCode",partCode);
                        carParts.put("carPartRegisterDate", currentDate);
                        carParts.put("carPartImageUrl",pictureUrl);
                        firebaseFirestore.collection("CarParts")
                                .add(carParts)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        System.out.println("asdas");
                                        Toast.makeText(getActivity(),"Kayıt başarılı",Toast.LENGTH_LONG).show();
                                        CarPartAddFragment carPartAddFragment = new CarPartAddFragment();
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.main_frame_layout,carPartAddFragment).commit();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("asdas2");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("asdas3");
                        Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("asdas4");
                System.out.println(e.getLocalizedMessage());
                Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void selectedPartImage(){
        addCarPartSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage(view);
            }
        });
    }
    public void selectImage(View view){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Galeriye ulaşmak için izin gerekli",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }
    }
    public void registerLauncher(View view){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null){
                        intentFromResult.getData();
                        imageData = intentFromResult.getData();
                        try {
                            if(Build.VERSION.SDK_INT >= 28){
                                ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(),imageData);
                                Picasso.get().load(imageData).resize(150,120).into(addCarPartSelectImage);
                            }else{
                                Picasso.get().load(imageData).resize(150,120).into(addCarPartSelectImage);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }else{
                    Toast.makeText(getActivity(), "İzin gerekli", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}