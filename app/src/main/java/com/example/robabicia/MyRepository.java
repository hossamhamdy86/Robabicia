package com.example.robabicia;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.robabicia.model.item;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyRepository {

    private static MyRepository instance;

    public static MyRepository getInstance(){
        if (instance == null){
            instance = new MyRepository();
        }
        return instance;
    }

    public MutableLiveData<List<item>> getData (){
        final MutableLiveData<List<item>> mutableLiveData = new MutableLiveData<>();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("items");
        collectionReference.orderBy("Date").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null && e == null){
                    ArrayList<item> itemArrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot query : queryDocumentSnapshots){
                        itemArrayList.add(query.toObject(item.class));
                    }
                    mutableLiveData.setValue(itemArrayList);
                }
            }
        });

        return mutableLiveData;
    }


}
