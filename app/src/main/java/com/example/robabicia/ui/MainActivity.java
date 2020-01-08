package com.example.robabicia.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.example.robabicia.ItemListAdapter;
import com.example.robabicia.R;
import com.example.robabicia.databinding.ActivityMainBinding;
import com.example.robabicia.model.item;
import com.example.robabicia.viewmodel.ViewModelitem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , SearchView.OnQueryTextListener {

    private ItemListAdapter adapter ;
    private ViewModelitem viewModelitem;
    ActivityMainBinding binding;
    private ArrayList<item> itemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_main);
        binding.ButtonAddItem.setOnClickListener(this);

        viewModelitem = ViewModelProviders.of(this).get(ViewModelitem.class);
        viewModelitem.init();
        viewModelitem.getItems().observe(this, new Observer<List<item>>() {
            @Override
            public void onChanged(List<item> items) {
                itemArrayList.clear();
                itemArrayList.addAll(items);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclView.setLayoutManager(layoutManager);
        adapter = new ItemListAdapter(itemArrayList , this);
        binding.recyclView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Button_add_item){
            Intent intentAddItem = new Intent(this, ItemAdd.class);
            startActivity(intentAddItem);
        }
    }

    private void prepareRecyclerView() {
        viewModelitem = ViewModelProviders.of(this).get(ViewModelitem.class);
        viewModelitem.init();
        viewModelitem.getItems().observe(this, new Observer<List<item>>() {
            @Override
            public void onChanged(List<item> items) {
               itemArrayList.clear();
               itemArrayList.addAll(items);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclView.setLayoutManager(layoutManager);
        adapter = new ItemListAdapter(itemArrayList , this);
        binding.recyclView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.minu,menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<item> newList = new ArrayList<>();

        for (item item : itemArrayList){
            if (item.getItem_name().toLowerCase().contains(userInput)){
                newList.add(item);
            }
        }
        adapter.updateList(newList);
        return true;
    }


}
