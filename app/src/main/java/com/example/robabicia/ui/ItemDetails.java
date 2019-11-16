package com.example.robabicia.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.robabicia.R;
import com.example.robabicia.databinding.ActivityItemDetails2Binding;
import com.squareup.picasso.Picasso;

public class ItemDetails extends AppCompatActivity implements View.OnClickListener {

    ActivityItemDetails2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_item_details2);

        Intent intent_itemDetails = getIntent();
        showDetails(intent_itemDetails);

        binding.phone.setOnClickListener(this);
    }

    private void showDetails(Intent intent) {
        binding.itemTitle.setText(intent.getStringExtra("Item_name"));
        binding.itemDescription.setText(intent.getStringExtra("description"));
        binding.location.setText(intent.getStringExtra("Location"));
        binding.name.setText(intent.getStringExtra("Person_name"));
        binding.phone.setText(intent.getStringExtra("Phone"));
        binding.data.setText(intent.getStringExtra("Date"));
        binding.itemPrice.setText(intent.getStringExtra("price"));
        Picasso.get().load(intent.getStringExtra("Item_imageUrl")).into(binding.itemImageView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.phone){
            Intent DialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +binding.phone.getText().toString()));
            startActivity(Intent.createChooser(DialIntent,"Choose app to handle"));
        }
    }

}
