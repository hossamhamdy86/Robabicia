package com.example.robabicia.viewmodel;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.robabicia.MyRepository;
import com.example.robabicia.checkItem;
import com.example.robabicia.model.item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ViewModelitem extends ViewModel {

    private checkItem.checkdata checkItem ;
    private LiveData<List<item>> listLiveData;
    private MyRepository repository ;

    public ViewModelitem() {
    }

    public ViewModelitem(checkItem.checkdata checkItem) {
        this.checkItem = checkItem;
    }

    public void init(){
        if (listLiveData != null){
            return;
        }
        repository = MyRepository.getInstance();
        listLiveData = repository.getData();
    }

    public LiveData<List<item>> getItems(){
        return listLiveData;
    }

    public void setData (final item item , final Uri selectedImagePath , Boolean isconnected) {
        if (isconnected == false){
            checkItem.onError("Sorry, the data cannot be saved without using the internet");
        } else if (item.getItem_description().isEmpty() || item.getItem_description().length() < 50) {
            checkItem.onError("Description must be more 50 letter");
        } else if (item.getItem_imageUrl() == null) {
            checkItem.onError("Please Put Your Item Image");
        } else if (item.getItem_name().isEmpty()) {
            checkItem.onError("Please Enter Your Item Name");
        } else if (item.getPerson_name().isEmpty()) {
            checkItem.onError("Please Enter Your Name");
        } else if (item.getPrice().isEmpty()){
            checkItem.onError("Please Enter Item Price");
        }else if (item.getPhone().isEmpty() || item.getPhone().length() != 11) {
            checkItem.onError("Please Enter Your Phone And It Must be 11 Number");
        } else if (item.getLocation().isEmpty()) {
            checkItem.onError("Please Enter Your Location");
        } else {
            final HashMap<String, Object> newItem = new HashMap<>();
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            newItem.put("item_name", item.getItem_name());
            newItem.put("person_name", item.getPerson_name());
            newItem.put("item_description", item.getItem_description());
            newItem.put("item_imageUrl", item.getItem_imageUrl());
            newItem.put("location", item.getLocation());
            newItem.put("phone", item.getPhone());
            newItem.put("price" , item.getPrice());
            newItem.put("Date",new Timestamp(new Date()));
            StorageReference currentUserStorageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference reference = currentUserStorageRef.child("Picture").child(item.getPhone());
            reference.putFile(selectedImagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    db.collection("items").add(newItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(final DocumentReference documentReference) {
                            checkItem.onSucces("Item successfully added");
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String image_uri = uri.toString();
                                    documentReference.update("item_imageUrl", image_uri);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            checkItem.onError(e.getMessage());
                        }
                    });
                }
            });
        }
    }

}
