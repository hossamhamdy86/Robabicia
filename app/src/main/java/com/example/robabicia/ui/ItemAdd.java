package com.example.robabicia.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robabicia.R;
import com.example.robabicia.checkItem;
import com.example.robabicia.databinding.ActivityItemDetailsBinding;
import com.example.robabicia.model.item;
import com.example.robabicia.viewmodel.ViewModelitem;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

public class ItemAdd extends AppCompatActivity implements checkItem.checkdata , View.OnClickListener {

    ActivityItemDetailsBinding binding;
    private ViewModelitem viewModelitem = new ViewModelitem(this);
    private static final int IMAGE_PICK_CODE = 101;
    private static final int PERMISSION_CODE = 100;
    private Uri selectedImagePath = null;
    item item = new item();
    private ProgressDialog progressDialog;
    boolean isConnected ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_item_details);
        binding.itemImageView.setOnClickListener(this);
        binding.buttonAdd.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading......");

        ArrayList<String> location = new ArrayList<>();
        location.add("Cairo");
        location.add("Giza");
        location.add("Alex");
        location.add("Qalyubia");
        location.add("Other");
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, location);
        adapterSpinner.setDropDownViewResource(R.layout.list_item);
        binding.location.setAdapter(adapterSpinner);
    }

    @Override
    public void onSucces(String message) {
        progressDialog.cancel();
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public void onError(String message) {
        progressDialog.cancel();
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }

    public void handleImage() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            String[] Permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(Permissions, PERMISSION_CODE);
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            ActivityCompat.startActivityForResult(this,Intent.createChooser(intent,"select image"),IMAGE_PICK_CODE,null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    handleImage();
                }else {
                    Toast.makeText(this, "Permission was denied...", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                selectedImagePath = result.getUri();
                binding.itemImageView.setImageURI(selectedImagePath);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_add){
            item.setItem_name(binding.itemTitle.getText().toString());
            item.setPerson_name(binding.name.getText().toString());
            item.setItem_description(binding.itemDescription.getText().toString());
            item.setPrice(binding.itemPrice.getText().toString());
            if (selectedImagePath == null){
                Toast.makeText(this, "Please Enter All Data and Image", Toast.LENGTH_SHORT).show();
                return;
            }else {
                item.setItem_imageUrl(selectedImagePath.toString());
            }
            item.setLocation(((TextView) binding.location.getSelectedView()).getText().toString());
            item.setPhone(binding.addphone.getText().toString());
            if (isConnected == false){
                Toast.makeText(this, "Please connect to the Internet to can save your data", Toast.LENGTH_LONG).show();
            }
            progressDialog.show();
            viewModelitem.setData(item ,selectedImagePath ,isConnected);
        }else if (v.getId() == R.id.item_imageView){
                handleImage();
        }
    }

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected == false){
                Toast.makeText(context, "Please connect to the Internet to can save your data", Toast.LENGTH_LONG).show();
                progressDialog.cancel();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }
}

